package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Enemy;
import com.mygdx.game.Player;
import com.mygdx.game.Ship;
import com.mygdx.game.base.BaseActor;
import com.mygdx.game.base.BaseGame;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.screens.combat.CombatScreen;
import com.mygdx.game.screens.departmentscreen.ChemistryScreen;
import com.mygdx.game.screens.departmentscreen.PhysicsScreen;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.mygdx.game.college.College.Derwent;
import static com.mygdx.game.college.College.James;
import static com.mygdx.game.college.College.Vanbrugh;
import static com.mygdx.game.ShipType.Brig;

public class SailingScreen extends BaseScreen {

    private Player player;
    private Enemy enemy;

    private ArrayList<BaseActor> obstacleList;
    private ArrayList<BaseActor> removeList;
    private ArrayList<BaseActor> regionList;

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
        regionList = new ArrayList<BaseActor>();

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
                solid.setName(object.getName());
                obstacleList.add(solid);
            } else {
                System.err.println("Unknown PhysicsData object.");
            }
        }

        objects = tiledMap.getLayers().get("RegionData").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle r = rectangleObject.getRectangle();

                BaseActor region = new BaseActor();
                region.setPosition(r.x, r.y);
                region.setSize(r.width, r.height);
                region.setRectangleBoundary();
                region.setName(object.getName());
                regionList.add(region);
            } else {
                System.err.println("Unknown RegionData object.");
            }
        }
    }

    @Override
    public void update(float dt) {
        removeList.clear();

        this.player.playerMove(dt);

        for (BaseActor obstacle : obstacleList) {
            if (player.overlaps(obstacle, true)) {
                switch (obstacle.getName()) {
                    case "chemistry":
                        if (Gdx.input.isKeyPressed(Input.Keys.S)) game.setScreen(new ChemistryScreen(game, this));
                        break;
                    case "physics":
                        if (Gdx.input.isKeyPressed(Input.Keys.S)) game.setScreen(new PhysicsScreen(game, this));
                        break;
                    case "derwent":
                        if (Gdx.input.isKeyPressed(Input.Keys.F) && Derwent.getIsBossDead() == false) game.setScreen(new CombatScreen(game, this.player, new Ship(Brig, "Derwent Boss" ,Derwent, true), this));
                        break;
                    case "vanbrugh":
                        break;
                    case "james":
                        break;
                    default:
                        System.out.println("Pure obstacle");
                }
            }
        }

        for (BaseActor region : regionList) {
            if (player.overlaps(region, false)) {
                int enemyChance = ThreadLocalRandom.current().nextInt(0, 10001);
                switch (region.getName()) {
                    case "derwentregion":
                        System.out.println(enemyChance);
                        System.out.println(region.getName());
                        if (Derwent.getIsBossDead() == true) region.setName("vanbrughregion");
                        if (enemyChance <= 20) {
                            game.setScreen(new CombatScreen(game, this.player, new Ship(Brig, Derwent),this));
                        }
                        break;
                    case "vanbrughregion":
                        System.out.println(enemyChance);
                        System.out.println(region.getName());
                        if (enemyChance <= 20) {
                            game.setScreen(new CombatScreen(game, this.player, new Ship(Brig, Vanbrugh),this));
                        }
                        break;
                    case "jamesregion":
                        System.out.println(enemyChance);
                        System.out.println(region.getName());
                        if (enemyChance <= 20) {
                            game.setScreen(new CombatScreen(game, this.player, new Ship(Brig, James),this));
                        }
                        break;
                    default:
                        System.out.println("Unknown region");
                }
            }
        }

        if (player.overlaps(enemy, true)) {
            game.setScreen(new CombatScreen(game, this.player, this.enemy.enemyShip, this));
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
}
