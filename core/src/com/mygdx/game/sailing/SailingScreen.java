package com.mygdx.game.sailing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.base.BaseActor;
import com.mygdx.game.base.BaseGame;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.base.PhysicsActor;
import com.mygdx.game.combat.CombatScreen;

import java.util.ArrayList;

public class SailingScreen extends BaseScreen {

    private Player player;

    private Enemy enemy;

    private ArrayList<BaseActor> obstacleList;
    private ArrayList<BaseActor> removeList;

    private int tileSize = 64;
    private int tileCountWidth = 80;
    private int tileCountHeight = 80;

    //calculate game world dimensions
    final int mapWidth = tileSize * tileCountWidth;
    final int mapHeight = tileSize * tileCountHeight;

    private TiledMap tiledMap;

    private OrthographicCamera tiledCamera;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private int[] backgroundLayers = {0, 1, 2};
    private int[] foregroundLayers = {3};

    public SailingScreen(BaseGame g) { super(g); }

    @Override
    public void create() {
        player = new Player();
        mainStage.addActor(player);

        enemy = new Enemy();
        mainStage.addActor(enemy);

        obstacleList = new ArrayList<BaseActor>();
        removeList = new ArrayList<BaseActor>();

        // set up tile map, renderer and camera
        tiledMap = new TmxMapLoader().load("game_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tiledCamera = new OrthographicCamera();
        tiledCamera.setToOrtho(false, viewWidth, viewHeight);
        tiledCamera.update();

        MapObjects objects = tiledMap.getLayers().get("ObjectData").getObjects();
        for (MapObject object : objects) {
            String name = object.getName();

            // all object data assumed to be stored as rectangles
            RectangleMapObject rectangleObject = (RectangleMapObject)object;
            Rectangle r = rectangleObject.getRectangle();

            switch (name) {
                case "player":
                    player.setPosition(r.x, r.y);
                    break;
                case "enemy":
                    enemy.setPosition(r.x, r.y);
                    break;
                default:
                    System.err.println("Unknown tilemap object: " + name);
            }
        }

        objects = tiledMap.getLayers().get("PhysicsData").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle r = rectangleObject.getRectangle();

                BaseActor solid = new BaseActor();
                solid.setPosition(r.x, r.y);
                solid.setSize(r.width, r.height);
                solid.setRectangleBoundary();
                obstacleList.add(solid);
            } else {
                System.err.println("Unknown PhysicsData object.");
            }
        }
    }

    @Override
    public void update(float dt) {
        removeList.clear();

        this.player.playerMove(dt);

        for (BaseActor obstacle : obstacleList) {
            player.overlaps(obstacle, true);
        }

        if (player.overlaps(enemy, true)) {
            game.setScreen(new CombatScreen(game));
        }

        for (BaseActor ba : removeList) {
            ba.destroy();
        }

        // camera adjustment
        Camera mainCamera = mainStage.getCamera();

        // center camera on player
        mainCamera.position.x = player.getX() + player.getOriginX();
        mainCamera.position.y = player.getY() + player.getOriginY();

        // bound camera to layout
        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x, viewWidth/2, mapWidth-viewWidth/2);
        mainCamera.position.y = MathUtils.clamp(mainCamera.position.y, viewHeight/2, mapHeight-viewHeight/2);
        mainCamera.update();

        // adjust tilemap camera to stay in sync with main camera
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera);
    }

    // override the render method to interleave tilemap rendering
    public void render(float dt) {
        uiStage.act(dt);

        // pause only gameplay events, not UI events
        if (!isPaused()) {
            mainStage.act(dt);
            update(dt);
        }

        // render
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render(backgroundLayers);
        mainStage.draw();
        tiledMapRenderer.render(foregroundLayers);
        uiStage.draw();
    }

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) togglePaused();

        if (keycode == Input.Keys.R) game.setScreen(new SailingScreen(game));

        return false;
    }
}
