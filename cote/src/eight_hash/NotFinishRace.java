package eight_hash;

import java.util.HashMap;
import java.util.HashSet;

/**
 * [완주하지 못한 선수]
 * 수많은 마라톤 선수들이 마라톤에 참여하였습니다. 단 한 명의 선수를 제외하고는 모든 선수가 마라톤을 완주하였습니다.
 * 마라톤에 참여한 선수들의 이름이 담긴 배열 participant와 완주한 선수들의 이름이 담긴 배열 completion이 주어질 때,
 * 완주하지 못한 선수의 이름을 return 하도록 solution 함수를 작성해주세요.
 * <p>
 * 제한사항
 * 마라톤 경기에 참여한 선수의 수는 1명 이상 100,000명 이하입니다.
 * completion의 길이는 participant의 길이보다 1 작습니다.
 * 참가자의 이름은 1개 이상 20개 이하의 알파벳 소문자로 이루어져 있습니다.
 * 참가자 중에는 동명이인이 있을 수 있습니다.
 * <p>
 * participant                                                    completion                                             return
 * ["leo", "kiki", "eden"]                                      ["eden", "kiki"]                                        "leo"
 * ["marina", "josipa", "nikola", "vinko", "filipa"]            ["josipa", "filipa", "marina", "nikola"]               "vinko"
 * ["mislav", "stanko", "mislav", "ana"]                        ["stanko", "ana", "mislav"]                            "mislav"
 */
public class NotFinishRace {
    public static void main(String[] args) {
        String[] participant = {"leo", "kiki", "eden"};
        String[] completion = {"eden", "kiki"};
    }

    private static String solution(String[] participant, String[] completion) {
        // 1. 해시맵 생성
        HashMap<String, Integer> hashMap = new HashMap<>();

        // 2. 완주한 선수들의 이름을 해시맵에 저장
        for (String s : completion) {
            hashMap.put(s, hashMap.getOrDefault(s, 0) + 1);
        }

        // 3. 참가한 선수들의 이름을 키로 하는 값을 1씩 감소
        for (String s : participant) {
            if (hashMap.getOrDefault(s, 0) == 0) {
                return s;
            }
            hashMap.put(s, hashMap.get(s) - 1);
        }

        return null;
    }
}
