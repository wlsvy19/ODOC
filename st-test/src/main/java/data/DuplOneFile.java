package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 1개의 텍스트 파일에서 차량번호 읽어 중복되는게 있는지 확인
 */


public class DuplOneFile {

    public static void main(String[] args) {
        String directory = "src/textfile/";
        String fileA = directory + "C.txt";

        // 파일 존재 여부 확인
        if (!checkFileExist(fileA)) {
            return; // 파일이 없으면 종료
        }

        // 파일 데이터 읽기
        List<String> dataA = readDataFromFile(fileA);

        if (dataA == null) {
            System.out.println("파일 읽기 중 오류가 발생하여 프로그램을 종료합니다.");
            return;
        }

        // 중복 데이터와 빈도수 찾기
        Map<String, Integer> duplicateCounts = findDuplicatesWithCounts(dataA);

        // 결과 출력
        System.out.println("C.txt 내부에서 중복된 데이터 및 빈도수:");
        if (duplicateCounts.isEmpty()) {
            System.out.println("중복 없음");
        } else {
            duplicateCounts.forEach((key, value) -> System.out.println(key + " (" + value + "번)"));
        }

        // 총 데이터 개수 출력
        System.out.println("\nA.txt 데이터 총 개수: " + dataA.size());
        System.out.println("A.txt 내부 중복된 데이터 개수: " + duplicateCounts.size());
    }

    /**
     * 파일의 존재 여부 확인
     */
    private static boolean checkFileExist(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("에러: " + filename + " 파일이 존재하지 않습니다.");
            System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
            return false;
        } else if (!file.isFile()) {
            System.out.println("에러: " + filename + "은(는) 파일이 아닙니다.");
            return false;
        } else if (!file.canRead()) {
            System.out.println("에러: " + filename + " 파일을 읽을 수 없습니다. 권한을 확인해주세요.");
            return false;
        }
        return true;
    }

    /**
     * 지정된 텍스트 파일에서 데이터 읽기
     */
    private static List<String> readDataFromFile(String filename) {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // 공백 제거
                if (!line.isEmpty()) {
                    data.add(line); // 데이터 추가
                }
            }
            return data;

        } catch (Exception e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }

    /**
     * 데이터 목록에서 중복 데이터와 빈도수를 찾음
     */
    private static Map<String, Integer> findDuplicatesWithCounts(List<String> data) {
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Integer> duplicates = new LinkedHashMap<>(); // 중복 데이터 유지 순서 보존

        for (String item : data) {
            counts.put(item, counts.getOrDefault(item, 0) + 1); // 현재 아이템 빈도수 증가
        }

        // 중복된 데이터를 추출
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > 1) { // 2번 이상 나온 경우만 중복 처리
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicates;
    }
}