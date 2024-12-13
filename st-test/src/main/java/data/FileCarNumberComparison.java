package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileCarNumberComparison {
    public static void main(String[] args) {

        String directory = "src/textfile/";
        String fileA = directory + "A.txt";
        String fileB = directory + "B.txt";

        // 파일 존재 여부 먼저 확인
        if (!checkFilesExist(fileA, fileB)) {
            return; // 파일이 없으면 프로그램 종료
        }

        // 파일 데이터 읽기
        List<String> dataA = readDataFromFile(fileA);
        List<String> dataB = readDataFromFile(fileB);

        if (dataA == null || dataB == null) {
            System.out.println("파일 읽기 중 오류가 발생하여 프로그램을 종료합니다.");
            return;
        }

        // A.txt에만 있는 데이터
        List<String> onlyInA = new ArrayList<>(dataA);
        onlyInA.removeAll(dataB);

        // B.txt에만 있는 데이터
        List<String> onlyInB = new ArrayList<>(dataB);
        onlyInB.removeAll(dataA);

        // 결과 출력
        System.out.println("A.txt에만 있는 데이터:");
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

        // 총 데이터 개수 출력 (중복 포함)
        System.out.println("\nA.txt 데이터 총 개수: " + dataA.size());
        System.out.println("B.txt 데이터 총 개수: " + dataB.size());

        // A.txt에만 있는 데이터 개수 출력
        System.out.println("A.txt에만 있는 데이터 개수: " + onlyInA.size());

        // B.txt에만 있는 데이터 개수 출력
        System.out.println("B.txt에만 있는 데이터 개수: " + onlyInB.size());
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

    private static List<String> readDataFromFile(String filename) {
        List<String> data = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (!line.isEmpty()) {
                    data.add(line); // 데이터를 그대로 추가
                }
            }
            return data;

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

