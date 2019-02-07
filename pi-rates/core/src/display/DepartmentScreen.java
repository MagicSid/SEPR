package display;

import banks.CoordBank;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import combat.items.RoomUpgrade;
import combat.items.Weapon;
import combat.ship.Ship;
import game_manager.GameManager;
import location.Department;
import other.Resource;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static banks.RoomUpgradeSetBank.COMP_SCI_UPGRADES;
import static banks.RoomUpgradeSetBank.LMB_UPGRADES;
import static banks.WeaponSetBank.COMP_SCI_WEPS;
import static banks.WeaponSetBank.LMB_WEPS;
import static other.Constants.*;

public class DepartmentScreen extends BaseScreen {

    private Table buttonTable;
    private ArrayList<Table> weaponBuyTableList;
    private ArrayList<Table> weaponSellTableList;
    private ArrayList<Table> roomTableList;

    private Table resource1 = new Table();
    private Table resource2 = new Table();
    private Table resource3 = new Table();

    private Skin skin;
    /**
     * Constructor for DepartmentScreen requiring game to switch screen
     */
    public DepartmentScreen(GameManager game){
        super(game);

        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
        skin = game.getSkin();
        skin.addRegions(buttonAtlas);
        buttonTable = new Table();

        textButtonStyle.font = buttonFont;
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");

        setUpTextures();
        drawBuyResourceFeatures();

        weaponBuyTableList = new ArrayList<Table>();
        weaponSellTableList = new ArrayList<Table>();
        roomTableList = new ArrayList<Table>();

        for (int i = 0; i <= 3; i++){
            weaponBuyTableList.add(new Table());
            weaponSellTableList.add(new Table());
            roomTableList.add(new Table());

            buttonTable.row().uniform().bottom();
            buttonTable.add(weaponBuyTableList.get(i));
            buttonTable.add(weaponSellTableList.get(i));
            buttonTable.add(roomTableList.get(i));
        }
        buttonTable.row().bottom();
        buttonTable.add(resource1);
        buttonTable.add(resource2);
        buttonTable.add(resource3);

        stage.addActor(buttonTable);
        buttonTable.setFillParent(true);
        buttonTable.align(Align.center);
        buttonTable.setDebug(false);

        buttonToMenu();

        drawShop();

    }
//TODO Prevent shop being able to sell more than 4 items/player being able to sell all items
    private void drawShop(){
        for (int i = 0; i <= 3; i++){
            weaponBuyTableList.get(i).clearChildren();
            weaponSellTableList.get(i).clearChildren();
            roomTableList.get(i).clearChildren();
        }
        drawBuyWeaponFeatures(titleFont, bodyFont, textButtonStyle);
        drawSellWeaponFeatures(textButtonStyle);
        drawBuyRoomUpgradeFeatures(textButtonStyle);
    }

    /**
     * Sets up gameManager to retrieve values and the playerShip
     */
    private GameManager gameManager = new GameManager(null, null);
    private Ship playerShip = gameManager.getPlayerShip();

    /**
     * Used to Draw Assets on the Screen
     */
    private SpriteBatch batch = new SpriteBatch();
    private Stage stage = new Stage();

    /**
     * Used to set values to the same no. decimal places
     */
    private DecimalFormat df;

    /**
     * Used to pick a random department to display
     */
    private int randInt = pickRandom(2);
    private Department department = assignDepartment(randInt);

    /**
     * Main style used for buttons
     */
    private BitmapFont buttonFont = new BitmapFont();
    private TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    private TextureAtlas buttonAtlas;

    /**
     * Sprite for Shopbackground and Fonts for Shop Information
     */
    private Sprite shopBackground;
    private BitmapFont titleFont = new BitmapFont();
    private BitmapFont bodyFont = new BitmapFont();

    @Override
    public void update(float delta){ }

    @Override
    public void show() {
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
        skin.addRegions(buttonAtlas);

        textButtonStyle.font = buttonFont;
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");

        shopBackground = createShopBackground();
    }


    // THIS IS NEW
    private Texture background;
    private TextureAtlas roomSpriteAtlas;

    private Texture computerScienceTexture;
    private Sprite computerScienceSprite;
    private Texture lawAndManagementTexture;
    private Sprite lawAndManagementSprite;
    private TextButton toMenu;
    private Texture hpBar;
    private Texture hpBackground;
    private BitmapFont indicatorFont;

    private List<Weapon> buyWeaponList;
    private List<TextButton> buyWeaponButtonList;
    private List<Weapon> sellWeaponList;
    private List<TextButton> sellWeaponButtonList;
    private List<RoomUpgrade> roomUpgradeList;
    private List<TextButton> buyResourceButtonList;

    public void setUpTextures(){
        background = new Texture("battleBackground.png");
        roomSpriteAtlas = new TextureAtlas("roomSpriteSheet.txt");
        computerScienceTexture = new Texture("ComputerScIsland.png");
        computerScienceSprite = new Sprite(computerScienceTexture);
        lawAndManagementTexture = new Texture("LMI.png");
        lawAndManagementSprite = new Sprite(lawAndManagementTexture);
        toMenu = new TextButton("To Menu", textButtonStyle);
        hpBar = new Texture("background.png");
        hpBackground = new Texture("disabledBackground.png");
        indicatorFont = new BitmapFont();

        buyWeaponList = new ArrayList<Weapon>();
        buyWeaponButtonList = new ArrayList<TextButton>();

        sellWeaponList = new ArrayList<Weapon>();
        sellWeaponButtonList = new ArrayList<TextButton>();

        roomUpgradeList = new ArrayList<RoomUpgrade>();

        buyResourceButtonList = new ArrayList<TextButton>();

    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);

        batch.begin();

        drawBackground();
        drawFriendlyShip();
        drawDepartment(randInt);

        drawHealthBar();
        drawIndicators();

        drawShopBackground(shopBackground);

        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    /**
     * Picks a random int
     * @param max
     * @return returns random int between 0 and max - 1
     */
    public int pickRandom(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    /**
     * Draws the Shop background
     */
    public void drawBackground() {
        batch.draw(background, 0, 0);
    }

    /**
     * Draws the friendly ship from room textures and constant coordinates
     */
    public void drawFriendlyShip(){
        Sprite friendlyCrewQuaters = roomSpriteAtlas.createSprite("crewQuaters");
        friendlyCrewQuaters.setPosition(CoordBank.FRIENDLY_CREWQUATERS.getX(),CoordBank.FRIENDLY_CREWQUATERS.getY());

        Sprite friendlyEmptyRoom1 = roomSpriteAtlas.createSprite("EmptyRoom");
        friendlyEmptyRoom1.setPosition(CoordBank.FRIENDLY_EMPTYROOM1.getX(),CoordBank.FRIENDLY_EMPTYROOM1.getY());

        Sprite friendlyCrowsNest = roomSpriteAtlas.createSprite("crowsNest");
        friendlyCrowsNest.setPosition(CoordBank.FRIENDLY_CROWSNEST.getX(),CoordBank.FRIENDLY_CROWSNEST.getY());

        Sprite friendlyGunDeck = roomSpriteAtlas.createSprite("gunDeck");
        friendlyGunDeck.setPosition(CoordBank.FRIENDLY_GUNDECK.getX(),CoordBank.FRIENDLY_GUNDECK.getY());

        Sprite friendlyEmptyRoom2 = roomSpriteAtlas.createSprite("EmptyRoom");
        friendlyEmptyRoom2.setPosition(CoordBank.FRIENDLY_EMPTYROOM2.getX(),CoordBank.FRIENDLY_EMPTYROOM2.getY());

        Sprite friendlyHelm = roomSpriteAtlas.createSprite("helm");
        friendlyHelm.setPosition(CoordBank.FRIENDLY_HELM.getX(),CoordBank.FRIENDLY_HELM.getY());

        Sprite friendlyEmptyRoom3 = roomSpriteAtlas.createSprite("EmptyRoom");
        friendlyEmptyRoom3.setPosition(CoordBank.FRIENDLY_EMPTYROOM3.getX(),CoordBank.FRIENDLY_EMPTYROOM3.getY());

        Sprite friendlyEmptyRoom4 = roomSpriteAtlas.createSprite("EmptyRoom");
        friendlyEmptyRoom4.setPosition(CoordBank.FRIENDLY_EMPTYROOM4.getX(),CoordBank.FRIENDLY_EMPTYROOM4.getY());

        friendlyCrewQuaters.draw(batch);
        friendlyCrowsNest.draw(batch);
        friendlyGunDeck.draw(batch);
        friendlyHelm.draw(batch);
        friendlyEmptyRoom1.draw(batch);
        friendlyEmptyRoom2.draw(batch);
        friendlyEmptyRoom3.draw(batch);
        friendlyEmptyRoom4.draw(batch);
    }

    /**
     * Assigns a random department to be used
     * @param randInt
     * @return random department
     */
    public Department assignDepartment(int randInt) {
        switch (randInt) {
            case 0:
                return (new Department(COMP_SCI_WEPS.getWeaponList(), COMP_SCI_UPGRADES.getRoomUpgradeList(), gameManager));
            case 1:
                return (new Department(LMB_WEPS.getWeaponList(), LMB_UPGRADES.getRoomUpgradeList(), gameManager));
        }
        return null;
    }

    /**
     * Draws the Department generated above
     * @param randInt
     */
    public void drawDepartment(int randInt) {
        switch (randInt) {
            case 0:
                batch.draw(computerScienceSprite,500,256);
                break;
            case 1:
                batch.draw(lawAndManagementSprite,400,200);
                break;
        }
    }

    /**
     * Draws Hp bars for both ships
     */
    public void drawHealthBar() {
        double defaultWidth = 320;
        int width = (int)(defaultWidth * ((double)playerShip.getHullHP() / (double)playerShip.getBaseHullHP()));

        batch.draw(hpBackground,25, 970, 320, 16);
        batch.draw(hpBar,25, 970, width, 16);
    }

    /**
     * Draws resource indicators for player
     */
    public void drawIndicators(){
        indicatorFont.setColor(1,1,1,1);

        indicatorFont.draw(batch, "Score: " + gameManager.getPoints(), 25, 965);
        indicatorFont.draw(batch, "Gold: " + gameManager.getGold(), 110, 965);
        indicatorFont.draw(batch, "Food: " + gameManager.getFood(), 195, 965);
        indicatorFont.draw(batch, "Crew: " + playerShip.getCrew(), 280, 965);
    }

    /**
     * Draws the Button returning to menu, taking the style button
     */
    public void buttonToMenu(){
        toMenu.setPosition(880, 980);
        toMenu.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button Pressed");
                game.setScreen(new MenuScreen(game));
            }
        });
        stage.addActor(toMenu);
    }

    /**
     * Generates the shopBackground
     * @return return shop background sprite
     */
    public Sprite createShopBackground(){
        Texture shopBackgroundTexture = new Texture("shopBackground.png");
        return new Sprite(shopBackgroundTexture);
    }

    /**
     * Draws the shop background from given Sprite
     * @param shopBackground
     */
    public void drawShopBackground(Sprite shopBackground) {
        shopBackground.draw(batch);
        shopBackground.setScale(1.5f, 1.5f);
        shopBackground.setPosition(256, 256);
        shopBackground.setAlpha(0.85f);
    }

    /**
     * Takes button styles and Fonts, draws buttons to buy items in the shop and the item information
     * @param titleFont
     * @param bodyFont
     * @param textButtonStyle
     */
    public void drawBuyWeaponFeatures(BitmapFont titleFont, BitmapFont bodyFont, TextButton.TextButtonStyle textButtonStyle) {
        buyWeaponList.clear();
        sellWeaponList.clear();
        roomUpgradeList.clear();
        int i = 0;
        while (i <= department.getWeaponStock().size() - 1 && department.getWeaponStock().get(i) instanceof Weapon) {
            buyWeaponList.add(department.getWeaponStock().get(i));
            System.out.println(i + ": " + department.getWeaponStock().get(i).getName());
            i++;
        }

        System.out.println(buyWeaponList.size() - 1);

        int j = 0;
        while (j <= buyWeaponList.size() - 1){
            TextButton textButton = new TextButton("Buy (" + buyWeaponList.get(j).getCost() + "g)", textButtonStyle);
            buyWeaponButtonListener(textButton, buyWeaponList.get(j));
            System.out.println(buyWeaponList.get(j).getName() + " " + j);
            weaponBuyTableList.get(j).add(new Label(buyWeaponList.get(j).getName(), skin));
            weaponBuyTableList.get(j).row();
            weaponBuyTableList.get(j).add(new Label("Damage: " + df.format(buyWeaponList.get(j).getBaseDamage()), skin));
            weaponBuyTableList.get(j).row();
            weaponBuyTableList.get(j).add(new Label("Crit Chance: " + df.format(buyWeaponList.get(j).getCritChance()), skin));
            weaponBuyTableList.get(j).row();
            weaponBuyTableList.get(j).add(new Label("Hit Chance: " + df.format(buyWeaponList.get(j).getAccuracy()), skin));
            weaponBuyTableList.get(j).row();
            weaponBuyTableList.get(j).add(new Label("Cooldown: " + df.format(buyWeaponList.get(j).getCooldown()), skin));
            weaponBuyTableList.get(j).row();
            weaponBuyTableList.get(j).add(textButton);
            j++;
        }


    }

    /**
     * Adds listeners to all weapon buy buttons
     */
    public void buyWeaponButtonListener(final TextButton textButton, final Weapon weapon) {
        textButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    department.buyWeapon(weapon);
                    drawShop();
                } catch (IllegalStateException e) {
                    textButton.setText("Insufficient Gold!");
                } catch (IllegalArgumentException e) {
                    if (e.getMessage() == "Weapon does not exist") {
                        textButton.setText("Out of Stock :(");
                    } else if (e.getMessage() == "Not enough gold") {
                        textButton.setText("Insufficient Gold!");
                    }
                }
            }
        });


    }

    /**
     * Takes button styles and Fonts, draws buttons to sell items in the shop and the item information
     * @param textButtonStyle
     */
    public void drawSellWeaponFeatures(TextButton.TextButtonStyle textButtonStyle) {
        buyWeaponList.clear();
        sellWeaponList.clear();
        roomUpgradeList.clear();
        int i = 0;
        while (i <= playerShip.getWeapons().size() - 1 && playerShip.getWeapons().get(i) instanceof Weapon) {
            sellWeaponList.add(playerShip.getWeapons().get(i));
            i++;
        }

        int j = 0;
        while (j <= sellWeaponList.size() - 1){
            System.out.println(sellWeaponList.get(j).getName() + " " + j);
            TextButton textButton = new TextButton("Sell (" + df.format(sellWeaponList.get(j).getCost() * STORE_SELL_PRICE_MULTIPLIER) + "g)", textButtonStyle);
            sellButtonListener(textButton, sellWeaponList.get(j));
            weaponSellTableList.get(j).add(new Label(sellWeaponList.get(j).getName(), skin));
            weaponSellTableList.get(j).row();
            weaponSellTableList.get(j).add(new Label("Damage: " + df.format(sellWeaponList.get(j).getBaseDamage()), skin));
            weaponSellTableList.get(j).row();
            weaponSellTableList.get(j).add(new Label("Crit Chance: " + df.format(sellWeaponList.get(j).getCritChance()), skin));
            weaponSellTableList.get(j).row();
            weaponSellTableList.get(j).add(new Label("Hit Chance: " + df.format(sellWeaponList.get(j).getAccuracy()), skin));
            weaponSellTableList.get(j).row();
            weaponSellTableList.get(j).add(new Label("Cooldown: " + df.format(sellWeaponList.get(j).getCooldown()), skin));
            weaponSellTableList.get(j).row();
            weaponSellTableList.get(j).add(textButton);
            j++;
        }

    }

    /**
     * Adds listeners to all sell buttons
     */
    public void sellButtonListener(final TextButton textButton, final Weapon weapon) {
        textButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    department.sellWeapon(weapon);
                    drawShop();
                } catch (IllegalArgumentException e) {
                    textButton.setText("Empty Slot!");
                }
            }
        });
    }


    /**
     * Takes button styles and Fonts, draws buttons to buy room upgrades in the shop and the item information
     * @param textButtonStyle
     */
    public void drawBuyRoomUpgradeFeatures(TextButton.TextButtonStyle textButtonStyle) {
        buyWeaponList.clear();
        sellWeaponList.clear();
        roomUpgradeList.clear();

        int i = 0;
        while (i <= department.getUpgradeStock().size() - 1 && department.getUpgradeStock().get(i) instanceof RoomUpgrade) {
            roomUpgradeList.add(department.getUpgradeStock().get(i));
            i++;
        }

        int j = 0;
        while (j <= roomUpgradeList.size() - 1){
            System.out.println(roomUpgradeList.get(j).getName() + " " + j);
            TextButton textButton = new TextButton("Buy (" + df.format(roomUpgradeList.get(j).getCost()) + "g)", textButtonStyle);
            buyRoomUpgradeButtonListener(textButton, roomUpgradeList.get(j));

            roomTableList.get(j).add(new Label(roomUpgradeList.get(j).getName(), skin));
            roomTableList.get(j).row();
            roomTableList.get(j).add(new Label("Room: " + roomUpgradeList.get(j).getAffectsRoom(), skin));
            roomTableList.get(j).row();
            roomTableList.get(j).add(new Label("Multiplier: " + df.format(roomUpgradeList.get(j).getMultiplier()), skin));
            roomTableList.get(j).row().uniform();
            roomTableList.get(j).add(new Label("", skin));
            roomTableList.get(j).row().uniform();
            roomTableList.get(j).add(new Label("", skin));
            roomTableList.get(j).row();
            roomTableList.get(j).add(textButton);
            j++;
        }
    }

    /**
     * Adds listeners to all buy room upgrade buttons
     */
    public void buyRoomUpgradeButtonListener(final TextButton textButton, final RoomUpgrade roomUpgrade) {
        textButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                try {
                    department.buyRoomUpgrade(roomUpgrade);
                    drawShop();
                } catch (IllegalStateException e) {
                    textButton.setText("Insufficient Gold!");
                } catch (IllegalArgumentException e) {
                    if (e.getMessage() == "Room does not exist") {
                        textButton.setText("Out of Stock :(");
                    } else if (e.getMessage() == "Not enough gold") {
                        textButton.setText("Insufficient Gold!");
                    }
                }
            }
        });
    }

    /**
     * Draws buttons and information for buying resources
     */
    public void drawBuyResourceFeatures(){
//        titleFont.draw(batch, "Crew", 160, 200);
//        titleFont.draw(batch, "Food", 360, 200);
//        titleFont.draw(batch, "Repair", 560, 200);

        buyResourceButtonList.add(new TextButton("Buy (" + CREW_COST + "g)", textButtonStyle));
        resource1.add(new Label("Crew", skin));
        resource1.row();
        resource1.add(buyResourceButtonList.get(0));

        buyResourceButtonList.add(new TextButton("Buy (" + FOOD_COST + "g)", textButtonStyle));
        resource2.add(new Label("Food", skin));
        resource2.row();
        resource2.add(buyResourceButtonList.get(1));

        buyResourceButtonList.add(new TextButton("Buy (" + REPAIR_COST + "g)", textButtonStyle));
        resource3.add(new Label("Repair", skin));
        resource3.row();
        resource3.add(buyResourceButtonList.get(2));

        buyResourceButtonListener();
    }

    /**
     * Adds listeners to the buy resource buttons
     */
    public void buyResourceButtonListener(){
        buyResourceButtonList.get(0).addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        department.buyResource(Resource.CREW, SHOP_CREW_AMOUNT);
                    } catch (IllegalStateException e) {
                    } catch (IllegalArgumentException e) {
                    }
                    return true;
                }
            });
        buyResourceButtonList.get(1).addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    department.buyResource(Resource.FOOD, SHOP_FOOD_AMOUNT);
                } catch (IllegalStateException e) {
                } catch (IllegalArgumentException e) {
                }
                return true;
            }
        });
        buyResourceButtonList.get(2).addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    department.buyResource(Resource.REPAIR, SHOP_REPAIR_AMOUNT);
                } catch (IllegalStateException e) {
                } catch (IllegalArgumentException e) {
                }
                return true;
            }
        });
    }


}

