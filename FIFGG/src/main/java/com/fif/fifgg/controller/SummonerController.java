package com.fif.fifgg.controller;

import com.fif.fifgg.dto.request.RiotIdRequest;
import com.fif.fifgg.dto.response.summoner.SummonerInfoResponse;
import com.fif.fifgg.service.SummonerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 소환사 기본 정보 API
@RestController
public class SummonerController {

    private final SummonerService summonerService;

    public SummonerController(SummonerService summonerService) {
        this.summonerService = summonerService;
    }

    @GetMapping("/api/summoners")
    public ResponseEntity<SummonerInfoResponse> getSummonerInfo(
            @RequestParam String gameName,
            @RequestParam String tagLine
    ) {
        RiotIdRequest request = new RiotIdRequest();
        request.setGameName(gameName);
        request.setTagLine(tagLine);

        return ResponseEntity.ok(summonerService.getSummonerInfo(request));
    }
}