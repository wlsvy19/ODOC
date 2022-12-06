package com.fastcampus.ch3.aop;

import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AopMain {
    public static void main(String[] args) throws Exception {
        MyAdvide myAdvide = new MyAdvide();

        Class mycClass = Class.forName("com.fastcampus.ch3.aop.MyClass");
        Object obj = mycClass.newInstance();

        for (Method m : mycClass.getDeclaredMethods()) {
            myAdvide.invoke(m, obj, null);
        }
    }
}

// 부가 기능들이 들어 있음
class MyAdvide {

    Pattern p = Pattern.compile("a.*"); // a로 시작하는 것만

    boolean matches(Method m) {
        Matcher matcher = p.matcher(m.getName());
        return matcher.matches();
    }

    void invoke(Method m, Object obj, Object... args) throws Exception {
        // @Transaction이 들어간 코드만 붙임
        if (m.getAnnotation(Transactional.class) != null)
            System.out.println("[before]{");

        m.invoke(obj, args); // aaa(), aaa2(), bbb() 호출가능

        if (m.getAnnotation(Transactional.class) != null)
            System.out.println("}[after]");
    }
}

// 핵심 기능만 들어있음
class MyClass {
    @Transactional
    void aaa() {
        System.out.println("aaa() is calssed.");
    }

    void aaa2() {
        System.out.println("aaa2() is calssed.");
    }

    void bbb() {
        System.out.println("bbb() is calssed.");
    }
}
