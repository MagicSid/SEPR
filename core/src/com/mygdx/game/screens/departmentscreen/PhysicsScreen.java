package com.mygdx.game.screens.departmentscreen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.base.BaseGame;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.screens.SailingScreen;

public class PhysicsScreen extends BaseScreen {

    private SailingScreen sailingScreen;

    public PhysicsScreen(BaseGame g, SailingScreen sailingScreen) {
        super(g);
        this.sailingScreen = sailingScreen;
    }

    @Override
    public void create() {
        BitmapFont font = new BitmapFont();
        String text = "Welcome to the Physics Department. Press B to buy upgrade, ESC to exit";
        Label.LabelStyle style = new Label.LabelStyle(font, Color.YELLOW);
        Label instructions = new Label(text, style);
        instructions.setFontScale(3);
        instructions.setPosition(200, 900);
        // Repeating color pulse effect
        instructions.addAction(Actions.forever(
                Actions.sequence(
                        Actions.color(new Color(1,1,0,1 ),0.5f),
                        Actions.delay(0.5f),
                        Actions.color(new Color(0.5f,0.5f,0,1),0.5f)
                )
        ));
        uiStage.addActor(instructions);
    }

    @Override
    public void update(float dt) {

    }

    // InputProcessor methods for handling discrete input
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) game.setScreen(this.sailingScreen);

        return false;
    }
}
