package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.base.PhysicsActor;
import com.mygdx.game.screens.combat.attacks.Attack;
import com.mygdx.game.screens.combat.attacks.GrapeShot;
import com.mygdx.game.screens.combat.attacks.Ram;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.college.College.Derwent;
import static com.mygdx.game.ShipType.Brig;

public class Player extends PhysicsActor {

    private Ship playerShip;
    private int gold;
    private int points;
    public static List<Attack> attacks = new ArrayList<Attack>();
    private Texture sailingTexture;

    public Player() {
        Texture playerTex = new Texture(Gdx.files.internal("ship (1).png"));
        this.storeAnimation("default", playerTex);
        this.setOriginCenter();
        this.setMaxSpeed(200);
        this.setDeceleration(20);
        this.setEllipseBoundary();

        this.playerShip = new Ship(Brig,"Your Ship", Derwent);

        attacks.add(Ram.attackRam);
        attacks.add(GrapeShot.attackSwivel);
        attacks.add(Attack.attackBoard);
    }

    public void playerMove(float dt) {
        this.setAccelerationXY(0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) this.rotateBy(90 * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) this.rotateBy(-90 * dt );
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) this.addAccelerationAS(this.getRotation(), 10000);
    }

    public Ship getPlayerShip() {
        return playerShip;
    }

    public int getPoints() { return points; }

    public int getGold() { return gold; }

    public List<Attack> getAttacks() { return attacks; }

    public void setPlayerShip(Ship ship) { this.playerShip = ship; }

    public void addPoints(int amount) { points += amount; }

    public void addGold(int amount) { gold = gold + amount; }

    public void setAttacks( List<Attack> attacks ) { this.attacks = attacks; }
}
