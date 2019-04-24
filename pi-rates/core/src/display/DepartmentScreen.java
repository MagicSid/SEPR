package display;

import banks.CoordBank;
import base.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import combat.items.RoomUpgrade;
import combat.items.Weapon;
import combat.ship.RoomFunction;
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
import static combat.ship.RoomFunction.CREW_QUARTERS;
import static combat.ship.RoomFunction.CROWS_NEST;
import static combat.ship.RoomFunction.GUN_DECK;
import static combat.ship.RoomFunction.HELM;
import static combat.ship.RoomFunction.NON_FUNCTIONAL;
import static other.Constants.*;

public class DepartmentScreen extends BaseScreen {

    private Table buttonTable;
    private ArrayList<Table> weaponBuyTableList;
    private ArrayList<Table> weaponSellTableList;
    private ArrayList<Table> roomTableList;

    private Table resource1 = new Table();
    private Table resource2 = new Table();
    private Table resource3 = new Table();
    private Table resource4 = new Table();
    
    private Table exitbuttontable = new Table();

    /**
     * Sets up department to retrieve values
     */
    private Department department;

    /**
     * Sets up gameManager to retrieve values and the playerShip
     */
    private Ship playerShip;

    private Table playerShipTable;
    
    private Table titletable;
    
    
    /**
     * Constructor for DepartmentScreen requiring game to switch screen
     */
    public DepartmentScreen(GameManager game, Department department) {
        super(game);

        this.department = department;

        musicSetup("heroic-age.mp3", true);

        this.playerShip = game.getPlayerShip();
        
        this.playerShipTable = new Table();
        playerShipTable.padTop(70);
        playerShipTable.padLeft(60);
        
        this.titletable = new Table();
        this.titletable.padTop(150);
        this.titletable.padLeft(30);
        
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
        skin.addRegions(buttonAtlas);
        buttonTable = new Table().padTop(35);

        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");

        setUpTextures();
        drawIndicators();
        drawFriendlyShip();
        drawtitles();
        
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
        buttonTable.row();
        buttonTable.add(resource4).colspan(3);
        

        drawBackground();
        drawShopBackground(createShopBackground());
        drawBuyResourceFeatures();
        mainStage.addActor(buttonTable);

        buttonTable.setFillParent(true);
        buttonTable.align(Align.center);
        buttonTable.setDebug(false);
        
        mainStage.addActor(playerShipTable);
        
        playerShipTable.setFillParent(true);
        playerShipTable.align(Align.topLeft);
        
        mainStage.addActor(titletable);
        
        titletable.setFillParent(true);
        titletable.align(Align.top);
        
        buttonToMenu();
        drawShop();

    }
    
    
private void drawtitles() {
		Label buyLabel = new Label("Buy",skin);
		Label sellLabel = new Label("Sell",skin);
		Label buyupgradesLabel = new Label("Buy Upgrade",skin);
		
		titletable.add(buyLabel).align(Align.left).padRight(170);
		titletable.add(sellLabel).align(Align.left).padRight(130);
		titletable.add(buyupgradesLabel).align(Align.left);
	}


//TODO Prevent shop being able to sell more than 4 items/player being able to sell all items
    private void drawShop(){
        for (int i = 0; i <= 3; i++){
            weaponBuyTableList.get(i).clearChildren();
            weaponSellTableList.get(i).clearChildren();
            roomTableList.get(i).clearChildren();
        }
        drawBuyWeaponFeatures(textButtonStyle);
        drawSellWeaponFeatures(textButtonStyle);
        drawBuyRoomUpgradeFeatures(textButtonStyle);
    }

    /**
     * Used to Draw Assets on the Screen
     */
    private SpriteBatch batch = new SpriteBatch();
    		
    /**
     * Used to set values to the same no. decimal places
     */
    private DecimalFormat df;

    /**
     * Main style used for buttons
     */
    private TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    private TextureAtlas buttonAtlas;


    @Override
    public void update(float delta){
        Gdx.input.setInputProcessor(mainStage);
        
        batch.begin();
        
        updateIndicators();
        
        
        batch.end();
        
    }

    @Override
    public void show() {
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
        skin.addRegions(buttonAtlas);

//      textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");

    }
    

    // THIS IS NEW
    private Texture background;
    private TextureAtlas roomSpriteAtlas;

    private TextButton backButton;
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

        backButton = new TextButton("Back", textButtonStyle);
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
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        super.dispose();
        batch.dispose();
    }

    /**
     * Picks a random int
     * @param max
     * @return returns random int between 0 and max - 1
     */
    @Deprecated
    public int pickRandom(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    /**
     * Draws the Shop background
     */
    public void drawBackground() {
        Image backgroundImage = new Image(background);
        backgroundImage.setSize(viewwidth, viewheight);
        mainStage.addActor(backgroundImage);
    }
    
    private Label hpLabelCQ;
    private Label hpLabelCN;
    private Label hpLabelGD;
    private Label hpLabelH;
    private Label hpLabelE1;
    private Label hpLabelE2;
    private Label hpLabelE3;
    private Label hpLabelE4;
    
    private void drawFriendlyShip(){

        hpLabelCQ = labelMaker(CREW_QUARTERS);
        hpLabelCN =  labelMaker(CROWS_NEST);
        hpLabelGD = labelMaker(GUN_DECK);
        hpLabelH = labelMaker(HELM);
        hpLabelE1 = labelMaker(NON_FUNCTIONAL);
        hpLabelE2 = labelMaker(NON_FUNCTIONAL);
        hpLabelE3 = labelMaker(NON_FUNCTIONAL);
        hpLabelE4 = labelMaker(NON_FUNCTIONAL);


        Stack groupCQ = roomSetup("crewQuaters", hpLabelCQ);
        Stack groupE1 = roomSetup("EmptyRoom", hpLabelE1);
        Stack groupCN = roomSetup("crowsNest", hpLabelCN);
        Stack groupGD = roomSetup("gunDeck", hpLabelGD);
        Stack groupE2 = roomSetup("EmptyRoom", hpLabelE2);
        Stack groupH = roomSetup("helm", hpLabelH);
        Stack groupE3 = roomSetup("EmptyRoom", hpLabelE3);
        Stack groupE4 = roomSetup("EmptyRoom", hpLabelE4);

        shipTableSetup(playerShipTable, groupE1, groupE2, groupE3, groupH, groupCN, groupGD, groupCQ, groupE4);
    }

    private Label labelMaker(RoomFunction room){
        return new Label("HP:" + playerShip.getRoom(room).getHp(), skin);
    }

    private Stack roomSetup(String roomName, Label label){
        Stack group = new Stack();
        group.addActor(new Image(roomSpriteAtlas.findRegion(roomName)));
        label.setAlignment(Align.bottomLeft);
        group.addActor(label);
        return group;
    }
    
    private void shipTableSetup(Table table, Actor a1, Actor a2, Actor a3, Actor a4,
            Actor a5, Actor a6, Actor a7, Actor a8){
		addTwoActors(table, a1, a2);
		addTwoActors(table, a3, a4);
		addTwoActors(table, a5, a6);
		addTwoActors(table, a7, a8);
	
	}

	/**
	* Method reduces repeated code
	* @param table
	* @param a1
	* @param a2
	*/
	private void addTwoActors(Table table, Actor a1, Actor a2) {
		table.add(a1).right();
		table.add(a2);
		table.row();
	}

    
    /**
     * Assigns a random department to be used
     * @param randInt
     * @return random department
     */
    public Department assignDepartment(int randInt) {
        switch (randInt) {
            case 0:
                return (new Department(COMP_SCI_WEPS.getWeaponList(), COMP_SCI_UPGRADES.getRoomUpgradeList(), game));
            case 1:
                return (new Department(LMB_WEPS.getWeaponList(), LMB_UPGRADES.getRoomUpgradeList(), game));
        }
        return null;
    }
    
    private Label hpLabel;
    private Label goldLabel;
    private Label crewLabel;
    private Label autorepairLabel;
    
    
    /**
     * Draws resource indicators for player
     */
    public void drawIndicators() {
    	hpLabel = new Label("Hull Hp:", skin);
    	playerShipTable.add(hpLabel).colspan(2);
    	playerShipTable.row();
    	goldLabel = new Label("Gold:", skin);
    	playerShipTable.add(goldLabel).colspan(2);
    	playerShipTable.row();
    	crewLabel = new Label("Crew:", skin);
    	playerShipTable.add(crewLabel).colspan(2);
    	playerShipTable.row();
    	autorepairLabel = new Label("Autorepair:", skin);
    	playerShipTable.add(autorepairLabel).colspan(2);
    	playerShipTable.row();
    	
    }

    /**
     * updates labels of the resource indicators
     */
    public void updateIndicators(){
    	
    	hpLabel.setText("Hull Hp: "+playerShip.getHullHP()+"/"+playerShip.getBaseHullHP());
    	goldLabel.setText("Gold: "+game.getGold());
    	crewLabel.setText("Crew: "+game.getCrew());
    	autorepairLabel.setText("Room repair: "+df.format(playerShip.calculateRepair()));
    	
    }

    /**
     * Draws the Button returning to menu, taking the style button
     */
    public void buttonToMenu(){
    	exitbuttontable.setFillParent(true);
        exitbuttontable.align(Align.top);
        
        backButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.debug("Department DEBUG", "Button Pressed");
                changeScreen(new SailingScreen(game, false));
            }
        });
        exitbuttontable.add(backButton).fill();
        mainStage.addActor(exitbuttontable);
    }

    /**
     * Generates the shopBackground
     * @return return shop background sprite
     */
    public Image createShopBackground(){
        Texture shopBackgroundTexture = new Texture("shopBackground.png");
        return new Image(shopBackgroundTexture);
    }

    /**
     * Draws the shop background from given Sprite
     * @param shopBackground
     */
    public void drawShopBackground(Image shopBackground) {
        shopBackground.setSize(shopBackground.getPrefWidth()*1.5f, shopBackground.getPrefHeight()*1.57f);
        shopBackground.setPosition(viewwidth/2, viewheight/2, Align.center);
        shopBackground.setColor(1, 1,1,0.85f);
        mainStage.addActor(shopBackground);
    }

    /**
     * Takes button styles and Fonts, draws buttons to buy items in the shop and the item information
     * @param textButtonStyle
     */
    public void drawBuyWeaponFeatures(TextButton.TextButtonStyle textButtonStyle) {
        buyWeaponList.clear();
        sellWeaponList.clear();
        roomUpgradeList.clear();
        int i = 0;
        while (i <= department.getWeaponStock().size() - 1 && department.getWeaponStock().get(i) instanceof Weapon) {
            buyWeaponList.add(department.getWeaponStock().get(i));
            Gdx.app.log("Weapon Stock", i + ": " + department.getWeaponStock().get(i).getName());
            i++;
        }

        Gdx.app.debug("Weapon Stock", Integer.toString(buyWeaponList.size() - 1));

        int j = 0;
        while (j <= buyWeaponList.size() - 1){
            TextButton textButton = new TextButton("Buy (" + buyWeaponList.get(j).getCost() + "g)", textButtonStyle);
            buyWeaponButtonListener(textButton, buyWeaponList.get(j));
            
            Gdx.app.log("Weapon Stock", buyWeaponList.get(j).getName() + " " + j);
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
                    textButton.setText("No space");
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
            Gdx.app.log("Weapon Stock", sellWeaponList.get(j).getName() + " " + j);
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
            Gdx.app.log("Room Upgrades", roomUpgradeList.get(j).getName() + " " + j);
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
        buyResourceButtonList.add(new TextButton("Buy (" + CREW_COST + "g)", textButtonStyle));
        resource1.add(new Label("Room Repair", skin));
        resource1.row();
        resource1.add(buyResourceButtonList.get(0));

        buyResourceButtonList.add(new TextButton("Buy (" + FOOD_COST + "g)", textButtonStyle));
        resource2.add(new Label("Crew", skin));
        resource2.row();
        resource2.add(buyResourceButtonList.get(1));

        buyResourceButtonList.add(new TextButton("Buy (" + REPAIR_COST + "g)", textButtonStyle));
        resource3.add(new Label("Hull Repair", skin));
        resource3.row();
        resource3.add(buyResourceButtonList.get(2));

        buyResourceButtonList.add(new TextButton("Buy (300g)",textButtonStyle));
        resource4.add(new Label("Increase Max Health by 200",skin));
        resource4.row();
        resource4.add(buyResourceButtonList.get(3));
        
        buyResourceButtonListener();
    }

    /**
     * Adds listeners to the buy resource buttons
     */
    public void buyResourceButtonListener(){
        buyResourceButtonList.get(0).addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        department.buyResource(Resource.CREW, CREW_COST);
                    } catch (IllegalStateException e) {
                    } catch (IllegalArgumentException e) {
                    }
                    return true;
                }
            });
        buyResourceButtonList.get(1).addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    department.buyResource(Resource.FOOD, FOOD_COST);
                } catch (IllegalStateException e) {
                } catch (IllegalArgumentException e) {
                }
                return true;
            }
        });
        buyResourceButtonList.get(2).addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    department.buyResource(Resource.REPAIR, REPAIR_COST);
                } catch (IllegalStateException e) {
                } catch (IllegalArgumentException e) {
                }
                return true;
            }
        });
        
        buyResourceButtonList.get(3).addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                	if(game.payGold(300)) {
                		playerShip.setBaseHullHP(playerShip.getBaseHullHP()+200);
                		playerShip.repairHull(200);
                	}
                } catch (IllegalStateException e) {
                } catch (IllegalArgumentException e) {
                }
                return true;
            }
        });
    }
}

