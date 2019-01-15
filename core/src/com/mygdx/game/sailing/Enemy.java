package com.mygdx.game.sailing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.base.PhysicsActor;

public class Enemy extends PhysicsActor {
    public Enemy() {
        Texture enemyTex = new Texture(Gdx.files.internal("ship (2).png"));
        this.storeAnimation("default", enemyTex);
        this.setEllipseBoundary();
    }
}
