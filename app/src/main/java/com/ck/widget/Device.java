package com.ck.widget;


public class Device {
    public Device(String name, int state) {
        this.name = name;
        this.state = state;
    }

    private String name;
    private int state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
