package com.rear_admirals.york_pirates;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class PirateGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	int x = 0;
	int y = 0;

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		img = new Texture("GrassIsland.png");
		Gdx.graphics.setTitle("York Pirates!");
		map.create();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) { x = x - 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { x = x + 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) { y = y + 1; }
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) { y = y - 1; }

		batch.begin();
		batch.draw(img, x, y);
		batch.end();
		map.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

}
