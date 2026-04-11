package com.fif.fifgg.service;

import com.fif.fifgg.client.RiotApiClient;
import com.fif.fifgg.dto.request.RiotIdRequest;
import com.fif.fifgg.dto.response.summoner.SummonerInfoResponse;
import com.fif.fifgg.dto.riot.account.RiotAccountResponse;
import com.fif.fifgg.dto.riot.summoner.SummonerResponse;
import org.springframework.stereotype.Service;

// 소환사 기본 정보 서비스
@Service
public class SummonerService {

    private final RiotApiClient riotApiClient;

    public SummonerService(RiotApiClient riotApiClient) {
        this.riotApiClient = riotApiClient;
    }

    // Riot ID로 소환사 기본 정보 조회
    public SummonerInfoResponse getSummonerInfo(RiotIdRequest request) {
        validate(request);

        RiotAccountResponse account = riotApiClient.getAccountByRiotId(
                request.getGameName(),
                request.getTagLine()
        );

        SummonerResponse summoner = riotApiClient.getSummonerByPuuid(account.getPuuid());

        return new SummonerInfoResponse(
                account.getGameName(),
                account.getTagLine(),
                account.getPuuid(),
                summoner.getName(),
                summoner.getSummonerLevel(),
                summoner.getProfileIconId()
        );
    }

    // 입력값 검증
    private void validate(RiotIdRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("요청값이 비어있습니다.");
        }
        if (request.getGameName() == null || request.getGameName().isBlank()) {
            throw new IllegalArgumentException("gameName은 필수입니다.");
        }
        if (request.getTagLine() == null || request.getTagLine().isBlank()) {
            throw new IllegalArgumentException("tagLine은 필수입니다.");
        }
    }
}