package com.fif.fifgg.dto.riot.account;

import lombok.Getter;
import lombok.Setter;

// Riot Account API 원본 응답
@Getter
@Setter
public class RiotAccountResponse {
    private String puuid;
    private String gameName;
    private String tagLine;
}