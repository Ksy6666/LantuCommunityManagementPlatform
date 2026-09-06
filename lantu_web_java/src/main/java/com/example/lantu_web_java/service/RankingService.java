package com.example.lantu_web_java.service;

import com.example.lantu_web_java.entity.User;
import com.example.lantu_web_java.entity.UserAvatar;
import com.example.lantu_web_java.mapper.CheckInMapper;
import com.example.lantu_web_java.mapper.UserAvatarMapper;
import com.example.lantu_web_java.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);
    private static final String RANKING_KEY = "checkin:ranking";
    private static final int DEFAULT_TOP_N = 20;

    private final ObjectProvider<StringRedisTemplate> redisProvider;
    private final UserMapper userMapper;
    private final UserAvatarMapper userAvatarMapper;
    private final CheckInMapper checkInMapper;

    public RankingService(ObjectProvider<StringRedisTemplate> redisProvider,
                          UserMapper userMapper,
                          UserAvatarMapper userAvatarMapper,
                          CheckInMapper checkInMapper) {
        this.redisProvider = redisProvider;
        this.userMapper = userMapper;
        this.userAvatarMapper = userAvatarMapper;
        this.checkInMapper = checkInMapper;
    }

    /**
     * 安全获取 StringRedisTemplate，Redis 不可用时返回 null
     */
    private StringRedisTemplate getRedis() {
        try {
            return redisProvider.getIfAvailable();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 应用启动时，将 MySQL 中的签到数据同步到 Redis ZSET
     */
    @PostConstruct
    public void initRanking() {
        StringRedisTemplate redis = getRedis();
        if (redis == null) return;

        try {
            Long size = redis.opsForZSet().zCard(RANKING_KEY);
            if (size != null && size > 0) {
                log.info("Redis 排行榜已有 {} 条记录，跳过初始化", size);
                return;
            }
        } catch (Exception e) {
            log.warn("Redis 排行榜初始化检查失败：{}", e.getMessage());
        }

        try {
            List<Map<String, Object>> allPoints = checkInMapper.selectAllUserTotalPoints();
            if (allPoints.isEmpty()) {
                log.info("数据库无签到记录，跳过 Redis 排行榜初始化");
                return;
            }
            for (Map<String, Object> row : allPoints) {
                Long userId = ((Number) row.get("userId")).longValue();
                int totalPoints = ((Number) row.get("totalPoints")).intValue();
                redis.opsForZSet().add(RANKING_KEY, String.valueOf(userId), totalPoints);
            }
            log.info("Redis 排行榜初始化完成，共 {} 条记录", allPoints.size());
        } catch (Exception e) {
            log.warn("Redis 排行榜初始化失败：{}", e.getMessage());
        }
    }

    /**
     * 从 MySQL 加载排行榜数据（Redis 为空时的备用方案）
     */
    private List<RankingEntry> getRankingFromDb(int topN) {
        List<Map<String, Object>> allPoints = checkInMapper.selectAllUserTotalPoints();
        if (allPoints.isEmpty()) return Collections.emptyList();

        // 取前 topN 条
        int limit = Math.min(topN, allPoints.size());
        List<Map<String, Object>> topList = allPoints.subList(0, limit);

        List<Long> userIds = topList.stream()
                .map(row -> ((Number) row.get("userId")).longValue())
                .collect(Collectors.toList());

        List<User> users = userMapper.selectByIds(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        List<UserAvatar> avatars = userAvatarMapper.selectByUserIds(userIds);
        Map<Long, String> avatarMap = avatars.stream()
                .collect(Collectors.toMap(UserAvatar::getUserId, UserAvatar::getFilePath));

        List<RankingEntry> result = new ArrayList<>();
        int rank = 1;
        for (Map<String, Object> row : topList) {
            Long userId = ((Number) row.get("userId")).longValue();
            int totalPoints = ((Number) row.get("totalPoints")).intValue();
            User user = userMap.get(userId);
            RankingEntry entry = new RankingEntry(
                    rank++,
                    userId,
                    user != null ? user.getNickname() : "未知用户",
                    avatarMap.get(userId),
                    totalPoints
            );
            entry.setRoleName(user != null ? user.getRoleName() : null);
            result.add(entry);
        }
        return result;
    }

    /**
     * 同步用户总积分到排行榜 ZSET
     */
    public void syncPoints(Long userId) {
        StringRedisTemplate redis = getRedis();
        if (redis == null) return;

        Integer totalPoints = checkInMapper.selectTotalPoints(userId);
        int points = totalPoints != null ? totalPoints : 0;
        try {
            redis.opsForZSet().add(RANKING_KEY, String.valueOf(userId), points);
        } catch (Exception e) {
            log.warn("Redis 同步积分失败：{}", e.getMessage());
        }
    }

    /**
     * 获取排行榜前 N 名
     */
    public List<RankingEntry> getTopRankings(int topN) {
        if (topN <= 0) topN = DEFAULT_TOP_N;

        // 优先从 Redis 读取
        StringRedisTemplate redis = getRedis();
        if (redis != null) {
            try {
                Set<String> members = redis.opsForZSet().reverseRange(RANKING_KEY, 0, topN - 1);
                if (members != null && !members.isEmpty()) {
                    return buildRankingFromRedis(redis, members);
                }
            } catch (Exception e) {
                log.warn("Redis 读取排行榜失败，回退到 MySQL：{}", e.getMessage());
            }
        }

        // Redis 无数据或不可用时，从 MySQL 查询
        return getRankingFromDb(topN);
    }

    /**
     * 从 Redis ZSET 的 member 集合构建排行榜
     */
    private List<RankingEntry> buildRankingFromRedis(StringRedisTemplate redis, Set<String> members) {
        List<Long> userIds = members.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<User> users = userMapper.selectByIds(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        List<UserAvatar> avatars = userAvatarMapper.selectByUserIds(userIds);
        Map<Long, String> avatarMap = avatars.stream()
                .collect(Collectors.toMap(UserAvatar::getUserId, UserAvatar::getFilePath));

        List<RankingEntry> result = new ArrayList<>();
        int rank = 1;
        for (String member : members) {
            Long userId = Long.valueOf(member);
            User user = userMap.get(userId);
            // 跳过已删除用户（数据库不存在的用户不应出现在排行榜中）
            if (user == null) {
                redis.opsForZSet().remove(RANKING_KEY, member);
                continue;
            }
            Double score = redis.opsForZSet().score(RANKING_KEY, member);
            RankingEntry entry = new RankingEntry(
                    rank++,
                    userId,
                    user.getNickname(),
                    avatarMap.get(userId),
                    score != null ? score.intValue() : 0
            );
            entry.setRoleName(user.getRoleName());
            result.add(entry);
        }
        return result;
    }

    /**
     * 获取用户自己的排名与积分（Redis 不可用时返回 -1）
     */
    public RankingEntry getUserRanking(Long userId) {
        StringRedisTemplate redis = getRedis();
        Long rank = null;
        Double score = null;

        if (redis != null) {
            try {
                rank = redis.opsForZSet().reverseRank(RANKING_KEY, String.valueOf(userId));
                score = redis.opsForZSet().score(RANKING_KEY, String.valueOf(userId));
            } catch (Exception e) {
                log.warn("Redis 获取个人排名失败：{}", e.getMessage());
            }
        }

        User user = userMapper.selectById(userId);
        String nickname = user != null ? user.getNickname() : "未知用户";

        UserAvatar avatar = userAvatarMapper.selectByUserId(userId);
        String avatarUrl = avatar != null ? avatar.getFilePath() : null;

        return new RankingEntry(
                rank != null ? rank.intValue() + 1 : -1,
                userId,
                nickname,
                avatarUrl,
                score != null ? score.intValue() : 0
        );
    }

    /**
     * 排行榜条目
     */
    public static class RankingEntry {
        private int rank;
        private Long userId;
        private String nickname;
        private String avatarUrl;
        private int totalPoints;
        private String roleName;

        public RankingEntry() {}

        public RankingEntry(int rank, Long userId, String nickname, String avatarUrl, int totalPoints) {
            this.rank = rank;
            this.userId = userId;
            this.nickname = nickname;
            this.avatarUrl = avatarUrl;
            this.totalPoints = totalPoints;
        }

        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
        public int getTotalPoints() { return totalPoints; }
        public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
    }
}
