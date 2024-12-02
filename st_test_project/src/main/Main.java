package main;

public class Main {
    public static void main(String[] args) {
        String originalString = "1999";
        String hashedString = SecurityUtil.encryptSHA256(originalString);
        System.out.println(hashedString);
    }
}
