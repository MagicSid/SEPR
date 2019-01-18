package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.base.PhysicsActor;

import static com.mygdx.game.college.College.Derwent;
import static com.mygdx.game.ShipType.Brig;

public class Enemy extends PhysicsActor {
    public Ship enemyShip;

    public Enemy() {
        Texture enemyTex = new Texture(Gdx.files.internal("ship (2).png"));
        this.storeAnimation("default", enemyTex);
        this.setEllipseBoundary();

        this.enemyShip = new Ship(Brig, Derwent);
    }
}
