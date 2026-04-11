package com.fif.fifgg.exception;

public class SummonerNotFoundException extends RuntimeException {

    public SummonerNotFoundException(String summonerName) {
        super("소환사를 찾을 수 없습니다: " + summonerName);
    }
}