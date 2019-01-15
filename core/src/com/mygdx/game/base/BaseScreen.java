package com.mygdx.game.base;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.base.BaseGame;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected BaseGame game;

    protected Stage mainStage;

    protected Stage uiStage;

    public final int viewWidth = 800;
    public final int viewHeight = 600;

    private boolean paused;

    public BaseScreen(BaseGame g) {
        game = g;

        mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        uiStage = new Stage(new FitViewport(viewWidth, viewHeight));

        paused = false;

        InputMultiplexer im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor(im);

        create();
    }

    public abstract void create();
    public abstract void update(float dt);

    // gameloop code; update, then render
    public void render(float dt) {
        uiStage.act(dt);

        // only pause gameplay events, not UI events
        if (!isPaused()) {
            mainStage.act(dt);
            update(dt);
        }

        // render
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    // pause methods
    public boolean isPaused() { return paused; }
    public void setPaused(boolean b) { paused = b; }
    public void togglePaused() { paused = !paused; }

    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height,true);
        uiStage.getViewport().update(width, height,true);
    }

    // methods required by Screen interface
    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

    // methods required by InputProcessor interface
    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override
    public boolean scrolled(int amount) { return false; }
}
