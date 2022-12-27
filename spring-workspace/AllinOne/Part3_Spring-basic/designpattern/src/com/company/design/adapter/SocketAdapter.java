package com.company.design.adapter;

public class SocketAdapter implements Eletronic110V{
    private Eletronic220V eletronic220V;

    public SocketAdapter(Eletronic220V eletronic220V) {
        this.eletronic220V = eletronic220V;
    }

    @Override
    public void powerOn() {
        eletronic220V.connect();
    }
}
