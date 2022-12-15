package com.fastcampus.ch3.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * advice - 부가 기능(여기서는 로그찍는거)
 */

@Component
@Aspect
public class LoggingAdvice {
    // execution(반환타입 패키지명.클래스명.메서드명(매개변수 목록))
    //@Around("execution(* com.fastcampus.ch3.aop.MyMath.*(..))") // pointcut - 부가기능이 적용될 메서드의 패턴
    @Around("execution(* com.fastcampus.ch3.aop.MyMath.add*(..))") // add로 시작하는 메서드에만 적용
    public Object methodCallLog(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("<<[start] " + pjp.getSignature().getName() + Arrays.toString(pjp.getArgs()));
        Object result = pjp.proceed(); // target의 메서드 호출

        System.out.println("result=" + result);
        System.out.println("[end]>> " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }
}
