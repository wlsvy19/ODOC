package com.company.design.adapter;

// 어댑터패턴: 인터페이스가 달라서 중간에 adapter역할 하는 클래스를 둬서 연결
// 헤어드라이기가 110볼트 사용 하고 있다가 220볼트 사용으로 바뀌는 시나리오

public class HairDryer implements Eletronic110V{

    @Override
    public void powerOn() {
        System.out.println("헤어 드라이기 110v On...");
    }
}
