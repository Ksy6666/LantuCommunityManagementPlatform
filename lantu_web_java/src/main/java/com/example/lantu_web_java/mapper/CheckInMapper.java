package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface CheckInMapper {

    int insert(CheckIn checkIn);

    /** 查询某用户某天是否已签到 */
    CheckIn selectByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /** 查询某用户某月的签到记录 */
    List<CheckIn> selectByUserIdAndMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    /** 查询某用户最近 N 条签到记录（用于计算连续天数） */
    List<CheckIn> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    /** 查询用户总积分（SUM 所有签到记录） */
    Integer selectTotalPoints(@Param("userId") Long userId);

    /** 查询所有用户的总积分（用于初始化 Redis 排行榜） */
    List<Map<String, Object>> selectAllUserTotalPoints();

    // ----- 管理后台仪表盘 -----

    /** 查询今日签到次数 */
    int countTodayCheckIns();

    /** 查询本月签到次数 */
    int countThisMonthCheckIns(@Param("yearMonth") String yearMonth);
}
