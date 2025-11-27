package data;

import java.io.*;
import java.util.*;

/**
 * 여러개의 텍스트 파일에서 숫자를 읽어 중복되는게 있는지 확인
 */

public class DuplicationHandSno {
    public static void main(String[] args) {
        String directoryPath = "src/textfile/dupl/";

        // 디렉토리 내의 모든 txt 파일 읽기
        List<File> txtFiles = getTxtFilesFromDirectory(directoryPath);

        if (txtFiles.isEmpty()) {
            System.out.println("에러: 디렉토리에 .txt 파일이 없습니다.");
            return;
        }

        try {
            // 모든 파일의 데이터를 Map에 저장 (값 -> 파일 목록)
            Map<String, Set<String>> dataToFileMap = new HashMap<>();

            for (File file : txtFiles) {
                readFileDataAndMap(file, dataToFileMap); // 파일 읽어서 데이터 매핑
            }

            System.out.println("중복된 데이터가 발견되었습니다:");
            boolean hasDuplicate = false;
            int totalDuplicateCount = 0; // 중복된 데이터의 총 개수 카운트

            for (Map.Entry<String, Set<String>> entry : dataToFileMap.entrySet()) {
                if (entry.getValue().size() > 1) { // 파일이 여러 개 겹치는 경우
                    hasDuplicate = true;
                    totalDuplicateCount++; // 중복된 데이터 개수 증가

                    // 파일 이름 정렬 (오름차순, 숫자 기준)
                    List<String> sortedFileNames = new ArrayList<>(entry.getValue());
                    sortedFileNames.sort((file1, file2) -> {
                        int num1 = extractNumberFromFileName(file1);
                        int num2 = extractNumberFromFileName(file2);
                        return Integer.compare(num1, num2); // 숫자 기준 오름차순 정렬
                    });
                    
                    System.out.println("값 " + entry.getKey() + " : " + String.join(", ", sortedFileNames));
                }
            }

            if (!hasDuplicate) {
                System.out.println("중복된 데이터가 없습니다.");
            } else {
                System.out.println("총 중복된 데이터 개수: " + totalDuplicateCount);
            }

        } catch (IOException e) {
            System.out.println("파일 처리 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 디렉토리에서 모든 .txt 파일을 찾는 메소드
     */
    private static List<File> getTxtFilesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        List<File> txtFiles = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("에러: 디렉토리가 존재하지 않거나 유효하지 않습니다: " + directoryPath);
            return txtFiles;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt")); // .txt 파일 필터
        if (files != null) {
            txtFiles.addAll(Arrays.asList(files));
        }

        return txtFiles;
    }

    /**
     * 파일 데이터를 읽고, 데이터와 파일 이름을 매핑하는 메소드
     */
    private static void readFileDataAndMap(File file, Map<String, Set<String>> dataToFileMap) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    // 읽은 데이터를 있는 그대로 Map에 저장
                    dataToFileMap
                            .computeIfAbsent(line, k -> new HashSet<>()) // 값이 없으면 새로운 Set 생성
                            .add(file.getName()); // 해당 값에 파일 이름 추가
                }
            }
        }
    }


    private static int extractNumberFromFileName(String fileName) {
        try {
            // 파일 이름에서 숫자만 추출
            return Integer.parseInt(fileName.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE; // 숫자를 추출할 수 없는 경우, 최댓값 반환
        }
    }
}