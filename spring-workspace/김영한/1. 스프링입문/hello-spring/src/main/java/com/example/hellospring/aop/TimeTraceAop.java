package com.example.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component // Bean으로 등록
public class TimeTraceAop {
    // 공통 관심사항을 어디다가 적용?
    // execution(반환타입 패키지명.클래스명.메서드명(매개변수 목록))
    //@Around("execution(* com.example.hellospring.service.*(..))") // 서비스 디렉토리 만
     @Around("execution(* com.example.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {   // pointcut - 부가기능이 적용될 메서드의 패턴
        long start = System.currentTimeMillis();
        System.out.println("START:" + joinPoint.toLongString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END:" + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
