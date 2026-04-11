package com.fif.fifgg.dto.response.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// 최근 전적 요약 응답
@Getter
@AllArgsConstructor
public class RecentMatchSummaryResponse {
    private int winCount;
    private int loseCount;
    private List<String> matchIds;
}