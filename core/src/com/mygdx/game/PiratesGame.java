package com.mygdx.game;

import com.mygdx.game.base.BaseGame;
import com.mygdx.game.sailing.SailingScreen;

public class PiratesGame  extends BaseGame {

    @Override
    public void create() {
        SailingScreen gs = new SailingScreen(this);
        setScreen(gs);
    }
}
