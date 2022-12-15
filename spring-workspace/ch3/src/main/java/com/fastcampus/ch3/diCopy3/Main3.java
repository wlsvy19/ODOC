package com.fastcampus.ch3.diCopy3;

import com.google.common.reflect.ClassPath;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
class Car {
}

@Component
class SportsCar extends Car {
}

@Component
class Truck extends Car {
}

@Component
class SUV extends Car {
}

@Component
class Engine {
}

class AppContext {
    Map map; // 객체 저장소

    AppContext() {
        map = new HashMap();
        // @Component 붙은 객체들 Mapdp 저장
        doComponentScan();
    }

    private void doComponentScan() {
        try {
            // 1. 패키지내의 클래스 목록을 가져온다.
            // 2. 반복문으로 클래스를 하나씩 읽어와 @Component 붙어있는지 확인
            // 3. @Component 붙어있으면 객체를 생성해서 Mapdp 저장
            ClassLoader classLoader = AppContext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.fastcampus.ch3.diCopy3"); // 패키지이름 적기
            for (ClassPath.ClassInfo classInfo : set) {
                Class clazz = classInfo.load();
                Component component = (Component) clazz.getAnnotation(Component.class);
                if (component != null) {
                    String id = StringUtils.uncapitalize(classInfo.getSimpleName()); // 클래스 이름만 있음
                    map.put(id, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // key에 해당하는 객체 반환
    // byName
    Object getBean(String key) {
        return map.get(key);
    }

    // byType
    Object getBean(Class clazz) {
        for (Object obj : map.values()) {
            if(clazz.isInstance(obj))
                return obj;
        }
        return null;
    }
}

// 자동 객체 등록하기 - Component Scanning
public class Main3 {
    public static void main(String[] args) throws Exception {
        AppContext ac = new AppContext();
        Car car = (Car) ac.getBean("car"); // byName으로 객체 검색
        Car car2 = (Car) ac.getBean(Car.class); // byType으로 객체 검색
        Engine engine = (Engine) ac.getBean("engine");
        System.out.println("car2 = " + car2);
        System.out.println("engine = " + engine);
    }

}
