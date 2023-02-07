package spring.core;

import lombok.Data;


@Data
public class LombokTest {
    private String name;
    private int age;

    public static void main(String[] args) {
        LombokTest lt = new LombokTest();
        lt.setName("lombok test...");
        System.out.println("lt.getName() = " + lt.getName());
        lt.setAge(32);
        System.out.println("lt.getAge() = " + lt.getAge());
    }
}
