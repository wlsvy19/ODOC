package com.fif.fifgg.dto.response.summoner;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 서비스가 외부에 내보내는 소환사 기본 정보
@Getter
@AllArgsConstructor
public class SummonerInfoResponse {
    private String gameName;
    private String tagLine;
    private String puuid;
    private String summonerName;
    private Long level;
    private Long profileIconId;
}