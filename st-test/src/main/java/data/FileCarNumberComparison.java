package data;

import java.io.*;
import java.util.*;

/**
 * 2개의 텍스트 파일에서 숫자를 읽어 중복되는게 있는지 확인
 */

public class FileCarNumberComparison {
    public static void main(String[] args) {

        String directory = "src/textfile/";
        String fileA = directory + "A.txt";
        String fileB = directory + "B.txt";

        // 파일 존재 여부 확인
        if (!checkFilesExist(fileA, fileB)) {
            return; // 파일이 없으면 종료
        }

        // 파일 데이터 읽기
        List<String> dataA = readDataFromFile(fileA);
        List<String> dataB = readDataFromFile(fileB);

        if (dataA == null || dataB == null) {
            System.out.println("파일 읽기 중 오류가 발생하여 프로그램을 종료합니다.");
            return;
        }

        // A.txt 내부 중복 데이터
        List<String> duplicatesInA = findInternalDuplicates(dataA);

        // B.txt 내부 중복 데이터
        List<String> duplicatesInB = findInternalDuplicates(dataB);

        // 파일 간 데이터 비교
        List<String> onlyInA = new ArrayList<>(dataA);
        onlyInA.removeAll(dataB);

        List<String> onlyInB = new ArrayList<>(dataB);
        onlyInB.removeAll(dataA);

        List<String> duplicatesBetweenFiles = findDuplicates(dataA, dataB);

        // 결과 출력
        System.out.println("A.txt 내부에서 중복된 데이터:");
        if (duplicatesInA.isEmpty()) {
            System.out.println("중복 없음");
        } else {
            duplicatesInA.forEach(System.out::println);
        }

        System.out.println("\nB.txt 내부에서 중복된 데이터:");
        if (duplicatesInB.isEmpty()) {
            System.out.println("중복 없음");
        } else {
            duplicatesInB.forEach(System.out::println);
        }

        //System.out.println("\nA.txt와 B.txt 사이의 중복 데이터:");
        if (duplicatesBetweenFiles.isEmpty()) {
            System.out.println("없음");
        } else {
            duplicatesBetweenFiles.forEach(System.out::println);
        }

        System.out.println("\nA.txt에만 있는 데이터:");
        if (onlyInA.isEmpty()) {
            System.out.println("없음");
        } else {
            onlyInA.forEach(System.out::println);
        }

        System.out.println("\nB.txt에만 있는 데이터:");
        if (onlyInB.isEmpty()) {
            System.out.println("없음");
        } else {
            onlyInB.forEach(System.out::println);
        }

        // 총 데이터 개수 출력
        System.out.println("\nA.txt 데이터 총 개수: " + dataA.size());
        System.out.println("B.txt 데이터 총 개수: " + dataB.size());

        System.out.println("A.txt에만 있는 데이터 개수: " + onlyInA.size());
        System.out.println("B.txt에만 있는 데이터 개수: " + onlyInB.size());
        System.out.println("A.txt 내부 중복된 데이터 개수: " + duplicatesInA.size());
        System.out.println("B.txt 내부 중복된 데이터 개수: " + duplicatesInB.size());
    }

    /**
     * 파일의 존재 여부 확인
     */
    private static boolean checkFilesExist(String... filenames) {
        boolean allFilesExist = true;
        for (String filename : filenames) {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("에러: " + filename + " 파일이 존재하지 않습니다.");
                System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
                allFilesExist = false;
            } else if (!file.isFile()) {
                System.out.println("에러: " + filename + "은(는) 파일이 아닙니다.");
                allFilesExist = false;
            } else if (!file.canRead()) {
                System.out.println("에러: " + filename + " 파일을 읽을 수 없습니다. 권한을 확인해주세요.");
                allFilesExist = false;
            }
        }
        return allFilesExist;
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
     * 하나의 데이터 목록에서 중복 데이터를 찾음 (내부 중복)
     */
    private static List<String> findInternalDuplicates(List<String> data) {
        Set<String> unique = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (String item : data) {
            if (!unique.add(item)) { // 이미 존재한다면 중복으로 추가
                duplicates.add(item);
            }
        }
        return new ArrayList<>(duplicates);
    }

    /**
     * 두 데이터 목록 사이에서 중복 데이터를 찾음 (교집합)
     */
    private static List<String> findDuplicates(List<String> dataA, List<String> dataB) {
        Set<String> setA = new HashSet<>(dataA);
        Set<String> setB = new HashSet<>(dataB);

        setA.retainAll(setB); // 두 세트의 교집합 구하기
        return new ArrayList<>(setA);
    }
}
