package com.company.design.adapter;

public class Cleaner implements Eletronic220V{

    @Override
    public void connect() {
        System.out.println("청소기 220v On...");
    }
}
