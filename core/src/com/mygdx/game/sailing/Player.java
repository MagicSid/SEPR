package com.mygdx.game.sailing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.base.PhysicsActor;

public class Player extends PhysicsActor {


    public Player() {
        Texture playerTex = new Texture(Gdx.files.internal("ship (1).png"));
        this.storeAnimation("default", playerTex);
        this.setOriginCenter();
        this.setMaxSpeed(200);
        this.setDeceleration(20);
        this.setEllipseBoundary();
    }

    public void playerMove(float dt) {
        this.setAccelerationXY(0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) this.rotateBy(90 * dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) this.rotateBy(-90 * dt );
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) this.addAccelerationAS(this.getRotation(), 10000);
    }
}
