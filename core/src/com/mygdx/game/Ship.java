package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.college.College;

public class Ship {

    private String name;
    private int attack;
    private int defence;
    private int accuracy;
    private int health;
    private ShipType type;
    private int healthMax;
    private boolean isBoss = false;
    public Texture sailingTexture;

    private College college;

    public Ship(int attack, int defence, int accuracy, int health, ShipType type, College college) {
        this.attack = attack;
        this.defence = defence;
        this.accuracy = accuracy;
        this.health = health;
        this.type = type;
        this.name = college.name + " " + type.name;
        this.healthMax = defence*20;
        this.college = college;
        this.sailingTexture = new Texture(Gdx.files.internal("ship (1).png"));
    }

    public Ship(ShipType type, College college) {
        this(type.getAttack(), type.getDefence(), type.getAccuracy(), 100, type, college);

        this.attack = type.getAttack();
        this.defence = type.getDefence();
        this.accuracy = type.getAccuracy();
        this.health = type.getDefence()*20;
        this.type = type;
        this.name = college.name + " " + type.name;
        this.healthMax = type.getDefence()*20;
        this.college = college;
        this.sailingTexture = new Texture(Gdx.files.internal("ship (1).png"));
    }

    public Ship(ShipType type, String name, College college) {
        this(type, college);
        this.name = name;
    }

    public Ship(ShipType type, String name, College college, boolean isBoss) {
        this(type, college);
        this.name = name;
        this.isBoss = isBoss;
    }

    public void damage(int amt){
        health = health - amt;
        if (health <= 0) {
            sink();
        }
    }

    public void sink() {
        //TODO Sinking function.
    }

    // Getters and Setters

    public String getName() { return name; }

    public int getMaxHealth(){
        return healthMax;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getAccuracy() { return accuracy; }

    public int getHealth() {
        return health;
    }

    public String getType() {
        return type.getName();
    }

    public int getHealthMax() {
        return healthMax;
    }

    public College getCollege() {
        return college;
    }

    public boolean getIsBoss() { return this.isBoss; }

    public void setName(String name) { this.name = name; }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
        this.healthMax = defence * 20;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setType(ShipType type) { this.type = type; }

    public void setCollege(College college) {
        this.college = college;
    }
}
