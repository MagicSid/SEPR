package com.mygdx.game.college;

public class College {

    public String name;
//    public College ally;
//    public College enemy;
    private boolean isBossDead = false;

    public College(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsBossDead(boolean isBossDead) { this.isBossDead = isBossDead; }

    public boolean getIsBossDead() { return this.isBossDead; }

    public static College Derwent = new College("Derwent");
    public static College Vanbrugh = new College("Vanbrugh");
    public static College James = new College("James");
}
