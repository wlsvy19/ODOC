package com.fif.fifgg.dto.riot.match;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Match-v5 원본 응답
@Getter
@Setter
public class MatchResponse {

    private Metadata metadata;
    private Info info;

    @Getter
    @Setter
    public static class Metadata {
        private String matchId;
        private List<String> participants;
    }

    @Getter
    @Setter
    public static class Info {
        private List<Participant> participants;
    }

    @Getter
    @Setter
    public static class Participant {
        private String puuid;
        private boolean win;
        private String championName;
        private int kills;
        private int deaths;
        private int assists;
    }
}