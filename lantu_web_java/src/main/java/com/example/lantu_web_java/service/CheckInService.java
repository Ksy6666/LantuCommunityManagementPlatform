package com.example.lantu_web_java.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.lantu_web_java.entity.CheckIn;
import com.example.lantu_web_java.mapper.CheckInMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckInService {

    private static final Logger log = LoggerFactory.getLogger(CheckInService.class);
    private static final int BASE_POINTS = 1;
    private static final int STREAK_7_BONUS = 2;
    private static final int STREAK_30_BONUS = 10;

    private final CheckInMapper checkInMapper;
    private final RankingService rankingService;

    public CheckInService(CheckInMapper checkInMapper, RankingService rankingService) {
        this.checkInMapper = checkInMapper;
        this.rankingService = rankingService;
    }

    /**
     * 执行签到
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> checkIn(Long userId) {
        LocalDate today = LocalDate.now();

        // 检查是否已签到
        CheckIn existing = checkInMapper.selectOne(Wrappers.<CheckIn>lambdaQuery()
                .eq(CheckIn::getUserId, userId)
                .eq(CheckIn::getCheckInDate, today)
                .last("LIMIT 1"));
        if (existing != null) {
            throw new RuntimeException("今日已签到，请明天再来");
        }

        // 计算本次积分
        int points = BASE_POINTS;

        // 查询昨天的签到记录，判断连续天数
        int consecutiveDays = getConsecutiveDays(userId, today);

        // 如果是连续的，加上连续天数对应的额外奖励
        int streakBonus = 0;
        if (consecutiveDays > 0) {
            points += streakBonus;
            // 连续 7 天额外奖励
            if (consecutiveDays % 7 == 0) {
                streakBonus += STREAK_7_BONUS;
                points += STREAK_7_BONUS;
            }
            // 连续 30 天额外奖励
            if (consecutiveDays % 30 == 0) {
                streakBonus += STREAK_30_BONUS;
                points += STREAK_30_BONUS;
            }
        }

        // 创建签到记录（原 XML insert 用 NOW() 写 created_at，改为继承 insert 前在 Java 侧设置）
        CheckIn checkIn = new CheckIn(userId, today, points);
        checkIn.setCreatedAt(LocalDateTime.now());
        checkInMapper.insert(checkIn);

        // 同步积分到 Redis 排行榜 ZSET（失败不影响签到）
        try {
            rankingService.syncPoints(userId);
        } catch (Exception e) {
            log.warn("Redis 排行榜同步失败，签到已成功完成：{}", e.getMessage());
        }

        // 从 check_ins 表 SUM 计算总积分
        Integer totalPoints = selectTotalPoints(userId);
        int newConsecutiveDays = consecutiveDays + 1;

        Map<String, Object> result = new HashMap<>();
        result.put("points", points);
        result.put("totalPoints", totalPoints != null ? totalPoints : 0);
        result.put("consecutiveDays", newConsecutiveDays);
        result.put("streakBonus", streakBonus);
        result.put("checkInDate", today.toString());

        return result;
    }

    /**
     * 获取签到状态（当月签到日期列表 + 总积分 + 连续天数）
     */
    public Map<String, Object> getStatus(Long userId) {
        LocalDate today = LocalDate.now();
        String yearMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 当月签到记录（等价旧 SQL：DATE_FORMAT(check_in_date,'%Y-%m')=#{yearMonth} ORDER BY check_in_date ASC）
        List<CheckIn> monthlyRecords = checkInMapper.selectList(Wrappers.<CheckIn>lambdaQuery()
                .eq(CheckIn::getUserId, userId)
                .apply("DATE_FORMAT(check_in_date, '%Y-%m') = {0}", yearMonth)
                .orderByAsc(CheckIn::getCheckInDate));
        List<String> checkInDates = monthlyRecords.stream()
                .map(c -> c.getCheckInDate().toString())
                .collect(Collectors.toList());

        // 总积分（从 check_ins 表 SUM 计算）
        Integer totalPoints = selectTotalPoints(userId);

        // 连续签到天数
        int consecutiveDays = getConsecutiveDays(userId, today);
        boolean checkedInToday = checkInMapper.selectOne(Wrappers.<CheckIn>lambdaQuery()
                .eq(CheckIn::getUserId, userId)
                .eq(CheckIn::getCheckInDate, today)
                .last("LIMIT 1")) != null;
        if (checkedInToday) {
            consecutiveDays++;
        }

        // 当月签到天数
        int monthlyCount = monthlyRecords.size();

        Map<String, Object> result = new HashMap<>();
        result.put("totalPoints", totalPoints != null ? totalPoints : 0);
        result.put("consecutiveDays", consecutiveDays);
        result.put("monthlyCount", monthlyCount);
        result.put("checkedInToday", checkedInToday);
        result.put("checkInDates", checkInDates);
        result.put("yearMonth", yearMonth);

        return result;
    }

    /**
     * 获取当月每天的签到状态（用于日历显示）
     */
    public Map<String, Object> getMonthStatus(Long userId, String yearMonth) {
        List<CheckIn> records = checkInMapper.selectList(Wrappers.<CheckIn>lambdaQuery()
                .eq(CheckIn::getUserId, userId)
                .apply("DATE_FORMAT(check_in_date, '%Y-%m') = {0}", yearMonth)
                .orderByAsc(CheckIn::getCheckInDate));
        List<String> checkInDates = records.stream()
                .map(c -> c.getCheckInDate().toString())
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("checkInDates", checkInDates);
        result.put("yearMonth", yearMonth);
        result.put("count", records.size());

        return result;
    }

    /**
     * 计算连续签到天数（不含今天）
     */
    private int getConsecutiveDays(Long userId, LocalDate today) {
        // 取最近 60 条记录以足够计算连续天数（等价旧 SQL：ORDER BY check_in_date DESC LIMIT #{limit}）
        List<CheckIn> recentList = checkInMapper.selectList(Wrappers.<CheckIn>lambdaQuery()
                .eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckInDate)
                .last("LIMIT 60"));

        if (recentList.isEmpty()) {
            return 0;
        }

        // 转为 Set 方便快速查找
        Set<LocalDate> dateSet = recentList.stream()
                .map(CheckIn::getCheckInDate)
                .collect(Collectors.toSet());

        int count = 0;
        LocalDate cursor = today.minusDays(1); // 从昨天开始往前数

        while (dateSet.contains(cursor)) {
            count++;
            cursor = cursor.minusDays(1);
        }

        return count;
    }

    /**
     * 计算用户总积分（等价旧 SQL：SELECT COALESCE(SUM(points), 0) FROM check_ins WHERE user_id = ?）
     */
    private Integer selectTotalPoints(Long userId) {
        List<Object> objs = checkInMapper.selectObjs(new QueryWrapper<CheckIn>()
                .select("COALESCE(SUM(points), 0)")
                .eq("user_id", userId));
        if (objs == null || objs.isEmpty() || objs.get(0) == null) {
            return 0;
        }
        return ((Number) objs.get(0)).intValue();
    }
}
