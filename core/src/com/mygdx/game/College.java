package com.mygdx.game;

public class College {

    public String name;
//    public College ally;
//    public College enemy;

    public College(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static College Derwent = new College("Derwent");
}
