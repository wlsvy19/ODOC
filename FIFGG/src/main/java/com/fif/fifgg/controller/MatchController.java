package com.fif.fifgg.controller;

import com.fif.fifgg.dto.request.RiotIdRequest;

import com.fif.fifgg.dto.response.match.RecentMatchSummaryResponse;
import com.fif.fifgg.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 최근 전적 API
@RestController
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/api/matches/recent")
    public ResponseEntity<RecentMatchSummaryResponse> getRecentMatches(
            @RequestParam String gameName,
            @RequestParam String tagLine
    ) {
        RiotIdRequest request = new RiotIdRequest();
        request.setGameName(gameName);
        request.setTagLine(tagLine);

        return ResponseEntity.ok(matchService.getRecentMatchSummary(request));
    }
}