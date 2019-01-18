package com.mygdx.game;

import com.mygdx.game.base.BaseGame;
import com.mygdx.game.screens.SailingScreen;

public class PiratesGame  extends BaseGame {

    public SailingScreen gs;

    @Override
    public void create() {
        gs = new SailingScreen(this);
        setScreen(gs);
    }
}
