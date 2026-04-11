package com.fif.fifgg.service;

import com.fif.fifgg.client.RiotApiClient;
import com.fif.fifgg.dto.request.RiotIdRequest;
import com.fif.fifgg.dto.response.match.RecentMatchSummaryResponse;
import com.fif.fifgg.dto.riot.account.RiotAccountResponse;
import com.fif.fifgg.dto.riot.match.MatchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

// 최근 전적 서비스
@Service
public class MatchService {

    private final RiotApiClient riotApiClient;

    public MatchService(RiotApiClient riotApiClient) {
        this.riotApiClient = riotApiClient;
    }

    // 최근 10게임 승/패를 계산
    public RecentMatchSummaryResponse getRecentMatchSummary(RiotIdRequest request) {
        validate(request);

        // 입력값 정규화: 앞뒤 공백 제거 + 중간 공백 제거
        String gameName = request.getGameName().trim().replaceAll("\\s+", "");
        String tagLine = request.getTagLine().trim();

        // Riot ID로 계정 정보 조회
        RiotAccountResponse account = riotApiClient.getAccountByRiotId(gameName, tagLine);

        // 최근 매치 ID 10개 조회
        List<String> matchIds = riotApiClient.getRecentMatchIdsByPuuid(account.getPuuid(), 10);

        int winCount = 0;
        int loseCount = 0;

        // 각 매치 상세를 가져와서 내 승패 확인
        for (String matchId : matchIds) {
            MatchResponse match = riotApiClient.getMatchByMatchId(matchId);

            if (match == null || match.getInfo() == null || match.getInfo().getParticipants() == null) {
                continue;
            }

            boolean isWin = match.getInfo().getParticipants().stream()
                    .filter(participant -> account.getPuuid().equals(participant.getPuuid()))
                    .findFirst()
                    .map(MatchResponse.Participant::isWin)
                    .orElse(false);

            if (isWin) {
                winCount++;
            } else {
                loseCount++;
            }
        }

        return new RecentMatchSummaryResponse(winCount, loseCount, matchIds);
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