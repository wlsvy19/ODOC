package com.fastcampus.ch3.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class AopMain2 {
    public static void main(String[] args) {
        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context_aop.xml");
        MyMath myMath = (MyMath) ac.getBean("myMath");

        myMath.add(3, 5);
        myMath.add(1, 2, 3);
        System.out.println("myMath.multiply(3,5) = " + myMath.multiply(3, 5));
        System.out.println("myMath.subtract(10,7) = " + myMath.subtract(10, 7));
    }
}
