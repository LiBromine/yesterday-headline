package com.java.libingrui;

public class MyException extends Exception {
    private String info;

    MyException(String info) {
        this.info = info;
    }

    String getInfo() { return info;}
}
