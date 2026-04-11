package com.fif.fifgg.dto.riot.summoner;

import lombok.Getter;
import lombok.Setter;

// Summoner-v4 원본 응답
@Getter
@Setter
public class SummonerResponse {
    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private Long profileIconId;
    private Long revisionDate;
    private Long summonerLevel;
}