package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
import com.example.lantu_web_java.service.CheckInService;
import com.example.lantu_web_java.service.RankingService;
import com.example.lantu_web_java.service.RankingService.RankingEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    private final CheckInService checkInService;
    private final RankingService rankingService;

    public CheckInController(CheckInService checkInService, RankingService rankingService) {
        this.checkInService = checkInService;
        this.rankingService = rankingService;
    }

    /**
     * POST /api/checkin — 执行签到（需登录）
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> doCheckIn(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Map<String, Object> data = checkInService.checkIn(userId);
            return ResponseEntity.ok(ApiResponse.success("签到成功", data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/checkin/status — 获取签到状态（需登录）
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Map<String, Object> data = checkInService.getStatus(userId);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/checkin/month?yearMonth=2026-06 — 获取指定月份签到记录（需登录）
     * yearMonth 格式：yyyy-MM，不传则默认当月
     */
    @GetMapping("/month")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthStatus(
            HttpServletRequest request,
            @RequestParam(required = false) String yearMonth
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (yearMonth == null || yearMonth.isBlank()) {
                yearMonth = java.time.LocalDate.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            }
            Map<String, Object> data = checkInService.getMonthStatus(userId, yearMonth);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/checkin/ranking — 获取积分排行榜（无需登录）
     */
    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<List<RankingEntry>>> getRanking(
            @RequestParam(defaultValue = "20") int topN
    ) {
        List<RankingEntry> ranking = rankingService.getTopRankings(topN);
        return ResponseEntity.ok(ApiResponse.success(ranking));
    }

    /**
     * GET /api/checkin/ranking/me — 获取我的排名（需登录）
     */
    @GetMapping("/ranking/me")
    public ResponseEntity<ApiResponse<RankingEntry>> getMyRanking(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            RankingEntry entry = rankingService.getUserRanking(userId);
            return ResponseEntity.ok(ApiResponse.success(entry));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
