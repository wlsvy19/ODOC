package main;

import java.io.*;
import java.util.*;

/**
 * 
 * @author leejp
 * 두개의 테이블에서 처리일련번호 등 키값으로 데이터 비교해서 어느 테이블에 이상한 데이터가 들어가는지 확인하는 프로그램
 *
 */
public class FileNumberComparison {
    public static void main(String[] args) {
        // 프로젝트 구조에 맞는 파일 경로 설정
        String directory = "src/textfile/";
        String fileA = directory + "A.txt";
        String fileB = directory + "B.txt";

        // 파일 존재 여부 먼저 확인
        if (!checkFilesExist(fileA, fileB)) {
            return; // 파일이 없으면 프로그램 종료
        }

        Set<Integer> numbersFromA = readNumbersFromFile(fileA);
        Set<Integer> numbersFromB = readNumbersFromFile(fileB);

        // 파일 읽기가 실패한 경우 체크
        if (numbersFromA == null || numbersFromB == null) {
            System.out.println("파일 읽기 실패로 프로그램을 종료합니다.");
            return;
        }

        Set<Integer> matches = new HashSet<>(numbersFromA);
        matches.retainAll(numbersFromB);

        Set<Integer> uniqueToA = new HashSet<>(numbersFromA);
        uniqueToA.removeAll(numbersFromB);

        Set<Integer> uniqueToB = new HashSet<>(numbersFromB);
        uniqueToB.removeAll(numbersFromA);

        List<Integer> sortedMatches = new ArrayList<>(matches);
        List<Integer> sortedUniqueToA = new ArrayList<>(uniqueToA);
        List<Integer> sortedUniqueToB = new ArrayList<>(uniqueToB);
        Collections.sort(sortedMatches);
        Collections.sort(sortedUniqueToA);
        Collections.sort(sortedUniqueToB);

        System.out.println("\n=== 분석 결과 ===");
        System.out.println("파일 A의 숫자 개수: " + numbersFromA.size());
        System.out.println("파일 B의 숫자 개수: " + numbersFromB.size());

        System.out.println("\n매칭되는 숫자 개수: " + matches.size());
        if (!matches.isEmpty()) {
            System.out.println("매칭되는 숫자들: " + sortedMatches);
        } else {
            System.out.println("매칭되는 숫자가 없습니다.");
        }

        System.out.println("\n파일 A에만 있는 숫자 개수: " + uniqueToA.size());
        if (!uniqueToA.isEmpty()) {
            System.out.println("파일 A에만 있는 숫자들: " + sortedUniqueToA);
        } else {
            System.out.println("파일 A에만 있는 숫자가 없습니다.");
        }

        System.out.println("\n파일 B에만 있는 숫자 개수: " + uniqueToB.size());
        if (!uniqueToB.isEmpty()) {
            System.out.println("파일 B에만 있는 숫자들: " + sortedUniqueToB);
        } else {
            System.out.println("파일 B에만 있는 숫자가 없습니다.");
        }
    }

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

    private static Set<Integer> readNumbersFromFile(String filename) {
        Set<Integer> numbers = new HashSet<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        numbers.add(Integer.parseInt(line));
                    } catch (NumberFormatException e) {
                        System.out.println("경고: " + filename + "의 " + lineNumber + "번째 줄에 잘못된 숫자 형식이 있습니다: " + line);
                    }
                }
            }
            return numbers;

        } catch (FileNotFoundException e) {
            System.out.println("에러: " + filename + " 파일을 찾을 수 없습니다.");
            System.out.println("전체 경로: " + new File(filename).getAbsolutePath());
            return null;
        } catch (IOException e) {
            System.out.println("에러: " + filename + " 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("예상치 못한 에러가 발생했습니다: " + e.getMessage());
            return null;
        }
    }
}