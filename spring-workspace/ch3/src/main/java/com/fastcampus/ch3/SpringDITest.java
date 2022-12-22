
package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("engine") // Component: 재사용하기 위해 선언
class Engine {} // <bean id="engine" class="com.fastcampus.ch3.Engie"> 과 동일

@Component
class SuperEngine extends Engine {
}

@Component
class TurboEngine extends Engine {
}

@Component
class Door {
}

@Component
class Car {
    @Value("Red") // 원시타입은 @Value
    String color;

    @Value("100") // 자동으로 int 변환됨
    int oil;

    //@Qualifier("turboEngine") // 여러개일때 Qualitfier로 선택 가능
    @Autowired // 객체는 Bean으로 등록 해야 하므로 @Autowired사용 해서 주입 -> ByType으로 @Component 찾음
    // byType-타입으로 먼저 검색(여기서 3개) 후 여러개면 이름으로 검색 - engine, superEngine, turboEngine 중 engine 찾음 -> 없으면 에러
    //@Resource(name="superEngine") // ByName, superEngine 이라는 Bean을 찾아 주입
    Engine engine;

    @Autowired
    Door[] doors;

    public void setColor(String color) {
        this.color = color;
    }

    public void setOil(int oil) {
        this.oil = oil;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setDoors(Door[] doors) {
        this.doors = doors;
    }

    @Override
    public String toString() {
        return "Car{" + "color='" + color + '\'' + ", oil=" + oil + ", engine=" + engine + ", doors=" + Arrays.toString(doors) + '}';
    }
}

public class SpringDITest {
    // 자동완성: psvm
    public static void main(String[] args) {
        // config.xml에 클래스 정보 설정 -> ApplicationContext 저장소 안에 MAP형태로 객체가 만들어짐
        //ApplicationContext ac = new GenericXmlApplicationContext("config.xml");
        ApplicationContext ac = new GenericXmlApplicationContext("config1.xml");
        Car car = (Car) ac.getBean("car"); // byName
//        Car car2 = (Car) ac.getBean("car"); // byName

        //Engine engine = (Engine) ac.getBean("engine"); // byName
        //Engine engine2 = (Engine) ac.getBean(Engine.class); // byType
//        Engine engine2 = (Engine) ac.getBean("spuerEngine"); // byName
//
//        Door door = ac.getBean("door", Door.class); // 뒤에 타입 주면 형변환 생략

        // soutv
        System.out.println("*************************************************************************");
        // 싱글톤: 클래스 객체를 하나만 생성
//        System.out.println("싱글톤 car = " + car);
//        System.out.println("싱글톤 car2 = " + car2);
//        System.out.println("프로토타입 engine = " + engine);
//        System.out.println("프로타타입 engine2 = " + engine2);
//        System.out.println("door = " + door);

        // config.xml에서 component-scan 사용시 주석 제거 or @Autowired 또는 @Resource 사용
//        car.setColor("red");
//        car.setOil(31);
//        car.setEngine(engine);
//        car.setDoors(new Door[]{ac.getBean("door", Door.class), (Door)ac.getBean("door")});
        System.out.println("car = " + car);
//        System.out.println("engine2 = " + engine2);
    }
}