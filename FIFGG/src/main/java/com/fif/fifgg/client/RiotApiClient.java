package com.fif.fifgg.client;

import com.fif.fifgg.config.RiotProperties;
import com.fif.fifgg.dto.riot.account.RiotAccountResponse;
import com.fif.fifgg.dto.riot.match.MatchResponse;
import com.fif.fifgg.dto.riot.summoner.SummonerResponse;
import com.fif.fifgg.exception.RiotApiException;
import com.fif.fifgg.exception.SummonerNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

// Riot API 호출 전담
@Component
public class RiotApiClient {

    private final RestClient restClient;
    private final RiotProperties riotProperties;

    public RiotApiClient(RestClient.Builder restClientBuilder, RiotProperties riotProperties) {
        this.restClient = restClientBuilder.build();
        this.riotProperties = riotProperties;
    }

    // Riot ID로 계정 정보 조회
    public RiotAccountResponse getAccountByRiotId(String gameName, String tagLine) {
        try {
            String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
            String encodedTagLine = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);

            return restClient.get()
                    .uri(riotProperties.getRegionalHost() + "/riot/account/v1/accounts/by-riot-id/"
                            + encodedGameName + "/" + encodedTagLine)
                    .header("X-Riot-Token", riotProperties.getApiKey())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(RiotAccountResponse.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SummonerNotFoundException(gameName + "#" + tagLine);
            }
            throw new RiotApiException("Riot account 조회 실패", e);
        }
    }

    // PUUID로 소환사 정보 조회
    public SummonerResponse getSummonerByPuuid(String puuid) {
        try {
            String encodedPuuid = URLEncoder.encode(puuid, StandardCharsets.UTF_8);

            return restClient.get()
                    .uri(riotProperties.getPlatformHost() + "/lol/summoner/v4/summoners/by-puuid/" + encodedPuuid)
                    .header("X-Riot-Token", riotProperties.getApiKey())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(SummonerResponse.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SummonerNotFoundException(puuid);
            }
            throw new RiotApiException("Summoner 조회 실패", e);
        }
    }

    // PUUID로 최근 매치 ID 목록 조회
    public List<String> getRecentMatchIdsByPuuid(String puuid, int count) {
        try {
            String encodedPuuid = URLEncoder.encode(puuid, StandardCharsets.UTF_8);

            String[] matchIds = restClient.get()
                    .uri(riotProperties.getRegionalHost()
                            + "/lol/match/v5/matches/by-puuid/" + encodedPuuid + "/ids?start=0&count=" + count)
                    .header("X-Riot-Token", riotProperties.getApiKey())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(String[].class);

            return matchIds == null ? List.of() : Arrays.asList(matchIds);
        } catch (RestClientResponseException e) {
            throw new RiotApiException("최근 전적 조회 실패", e);
        }
    }

    // matchId로 매치 상세 조회
    public MatchResponse getMatchByMatchId(String matchId) {
        try {
            String encodedMatchId = URLEncoder.encode(matchId, StandardCharsets.UTF_8);

            return restClient.get()
                    .uri(riotProperties.getRegionalHost() + "/lol/match/v5/matches/" + encodedMatchId)
                    .header("X-Riot-Token", riotProperties.getApiKey())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(MatchResponse.class);
        } catch (RestClientResponseException e) {
            throw new RiotApiException("Match 상세 조회 실패", e);
        }
    }
}