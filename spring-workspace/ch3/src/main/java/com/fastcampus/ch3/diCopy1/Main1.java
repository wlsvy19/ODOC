package com.fastcampus.ch3.diCopy1;

import java.io.FileReader;
import java.util.Properties;

class Car {}
class SportsCar extends Car{}
class Truck extends  Car{}
class SUV extends  Car{}
class Engine{}

public class Main1 {
    public static void main(String[] args) throws Exception {
        //Car car = getCar();
        Car car = (Car)getObject("car");
        Engine engine = (Engine)getObject("engine");
        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
    }

//    static Car getCar() throws Exception{
//        Properties p = new Properties();
//        p.load(new FileReader("config.txt"));
//
//        Class clazz = Class.forName(p.getProperty("car"));
//        // return (Car)clazz.newInstance();
//    }

    // Spring DI(Dependency Inject:의존관계 주입)
    // Car뿐 아니라 Engine도 받을 수 있도록 Object로 확장 가능
    static Object getObject(String key) throws Exception{
        Properties p = new Properties();
        p.load(new FileReader("config1.txt"));

        Class clazz = Class.forName(p.getProperty(key));
        // return (Car)clazz.newInstance();
        return clazz.newInstance();
    }
}
