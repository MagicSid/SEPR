package com.mygdx.game.screens.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.game.Ship;
import com.mygdx.game.base.BaseActor;

public class CombatShip extends BaseActor {

    float ship_size;
    Texture texture;
    Ship ship;

    public CombatShip(Ship ship, String shipFile, float ship_size){
        this.ship = ship;
        this.texture = new Texture(Gdx.files.internal(shipFile));
        this.ship_size = ship_size;
        this.setBounds(getX(), getY(), ship_size,ship_size);
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.setColor(1,1,1, alpha);
        batch.draw(texture, getX(), getY(), ship_size,ship_size);
    }
}
