package game_manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import combat.actors.CombatEnemy;
import combat.actors.CombatPlayer;
import combat.manager.CombatManager;
import combat.ship.Ship;
import display.MenuScreen;
import location.Department;
import other.Difficulty;

import static banks.RoomUpgradeSetBank.*;
import static banks.ShipBank.COLLEGE_SHIP;
import static banks.ShipBank.STARTER_SHIP;
import static banks.WeaponSetBank.*;
import static com.badlogic.gdx.Application.LOG_NONE;
import static other.Constants.STARTING_FOOD;
import static other.Constants.STARTING_GOLD;

/**
 * Controls the overall process of the game, handing control to various sub-managers (eg combat manager) as necessary.
 * It also stores the information about the game which will be needed in lots of places, eg the amount of gold the
 * player has or the points.
 * Had to implement Serializable due to encoding needed for saving game data
 */
public class GameManager extends Game implements java.io.Serializable {
    /**
     * Currency of the game
     */
    private int gold;


    /**
     * Changed.
     * A resource tied to boarding. Food was originally not used at all so this has been replaced by crew for boarding.
     * Old crew will be changed to auto-repair.
     */
    private int crew;
    /**
     * Points are accumulated by killing ships etc. They go toward recording the high scores.
     */
    private int points;
    /**
     * The name of the current player for saves and hi-scores
     */
    private String playerName;

    /**
     * The difficulty that the player is playing on.
     */
    private Difficulty difficulty;
    

    public void setPlayerShip(Ship playerShip) {
        this.playerShip = playerShip;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setCrew(int crew) {
        this.crew = crew;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Creates Instances of enemyShip, playerShip and their Actors to be used in the game
     */
    private Ship playerShip = STARTER_SHIP.getShip();

    public void setEnemyShip(Ship enemyShip) {
        this.enemyShip = enemyShip;
        this.combatEnemy = new CombatEnemy(enemyShip);
    }

    public void setCollegeShip(Ship collegeShip) {
        this.collegeShip = collegeShip;
        this.combatCollege= new CombatEnemy(collegeShip);
    }
    
    private CombatPlayer combatPlayer = new CombatPlayer(playerShip);

    private Ship enemyShip = STARTER_SHIP.getShip();
    private CombatEnemy combatEnemy = new CombatEnemy(enemyShip);

    private Ship collegeShip = COLLEGE_SHIP.getShip();

    private CombatEnemy combatCollege = new CombatEnemy(collegeShip);

    private CombatManager combatManager = new CombatManager(combatPlayer, combatEnemy);

    /**
     * Variables to manage the global volume of sounds/music in the game
     */
    private float masterVolume;
    private float soundVolume;
    private float musicVolume;

    /**
     * Different departments in the game
     */
    public static Department ComputerScience;
    public static Department LawAndManagement;
    public static Department Physics;

    /**
     * Keep track of the coordinates and angle of the player on the map
     */
    private float sailingShipX;
    private float sailingShipY;
    private float sailingShipRotation;

    public float getSailingShipX() { return this.sailingShipX; }
    public float getSailingShipY() { return this.sailingShipY; }
    public float getSailingShipRotation() { return this.sailingShipRotation; }

    public void setSailingShipX(float sailingShipX) { this.sailingShipX = sailingShipX; }
    public void setSailingShipY(float sailingShipY) { this.sailingShipY = sailingShipY; }
    public void setSailingShipRotation(float sailingShipRotation) { this.sailingShipRotation = sailingShipRotation; }

    public CombatManager getCombatManager() { return combatManager; }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    @Deprecated // This function is deprecated. Use payGold instead where possible.
    public void deductGold(int amount) {
        this.gold -= amount;
        if (gold < 0) {
            gold = 0;
        }
    }

    public void setCombatPlayer(CombatPlayer combatPlayer) {
        this.combatPlayer = combatPlayer;
    }

    public void setCombatEnemy(CombatEnemy combatEnemy) {
        this.combatEnemy = combatEnemy;
    }

    public void setCombatCollege(CombatEnemy combatCollege) {
        this.combatCollege = combatCollege;
    }

    public void setCombatManager(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    /**
     * Checks if the player can afford to pay the amount given. Charges them and returns true if they can, just returns false if they cannot.
     * @param amount
     * @return
     */
    public boolean payGold(int amount) {
        if (gold < amount) { return false; }
        gold = gold - amount;
        return true;
    }

    public int getCrew() {
        return crew;
    }

	
	public void addCrew(int amount) { 
		this.crew += amount; 
	}
	 

    public void deductCrew(int amount) {
        crew -= amount;
        if (crew < 0) {
            crew = 0;
        }
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int amount) {
        this.points += amount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Ship getPlayerShip() {
        return playerShip;
    }

    public Ship getEnemyShip() {
        return enemyShip;
    }

    public Ship getCollegeShip() { return collegeShip; }

    public CombatPlayer getCombatPlayer(){return combatPlayer; }

    public CombatEnemy getCombatEnemy() {return combatEnemy; }

    public CombatEnemy getCombatCollege() { return combatCollege; }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setMasterVolume(float volume) {
        if (volume < 0) { volume = 0; }
        else if (volume > 1) {volume = 1;}
        masterVolume = volume;
    }

    public void setSoundVolume(float volume) {
        if (volume < 0) { volume = 0; }
        else if (volume > 1) {volume = 1;}
        soundVolume = volume;
    }

    public void setMusicVolume(float volume) {
        if (volume < 0) { volume = 0; }
        else if (volume > 1) {volume = 1;}
        musicVolume = volume;
    }

    public float getMasterValue() { return masterVolume; }

    public float getMusicVolume() { return musicVolume * masterVolume; }

    public float getSoundVolume() { return soundVolume * masterVolume; }

    public float getMusicValue() { return musicVolume; }

    public float getSoundValue() { return soundVolume; }

    public GameManager(String playerName, Difficulty difficulty) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.gold = STARTING_GOLD;
        this.crew = STARTING_FOOD;
        this.points = 0;
        this.masterVolume = 0.1f;
        this.soundVolume = 0.5f;
        this.musicVolume = 0.5f;

        this.ComputerScience = new Department(COMP_SCI_WEPS.getWeaponList(), COMP_SCI_UPGRADES.getRoomUpgradeList(), this);
        this.LawAndManagement = new Department(LMB_WEPS.getWeaponList(), LMB_UPGRADES.getRoomUpgradeList(), this);
        this.Physics = new Department(PHYS_WEPS.getWeaponList(), PHYS_UPGRADES.getRoomUpgradeList(), this);
    }
    
    public void resetgamestate() {
    	/**
    	 * CODE CHANGE BELOW Assessment4
    	 * New method added to reset the game state if the player creates a new game after having played in the session already.
    	 */
    	this.gold = STARTING_GOLD;
    	this.crew = STARTING_FOOD;
    	this.points = 0;
    	this.masterVolume = 0.1f;
        this.soundVolume = 0.5f;
        this.musicVolume = 0.5f;
        
        this.ComputerScience = new Department(COMP_SCI_WEPS.getWeaponList(), COMP_SCI_UPGRADES.getRoomUpgradeList(), this);
        this.LawAndManagement = new Department(LMB_WEPS.getWeaponList(), LMB_UPGRADES.getRoomUpgradeList(), this);
        this.Physics = new Department(PHYS_WEPS.getWeaponList(), PHYS_UPGRADES.getRoomUpgradeList(), this);
        
        this.playerShip = STARTER_SHIP.getShip();
        this.playerShip.setBaseHullHP(700);
        this.playerShip.repairHull(700);
        this.combatPlayer = new CombatPlayer(playerShip);
    }



    /**
     * Creates an Instance of Screen and all the different Screens used
     */
    @Override
    public void create() { //Called when the application is
        Gdx.app.setLogLevel(LOG_NONE); // Sets level of logs to display. LOG_DEBUG when programming. LOG_NONE to mute all.
        Gdx.app.debug("Game DEBUG","Initialising Application");
        MenuScreen menuScreen =  new MenuScreen(this);
        this.setScreen(menuScreen);

    }



    @Override
    public void render() { //Called when the Application should render, Called continuously
        super.render();
    }

    @Override
    public void dispose() { //Called when the application is destroyed, resources must be disposed of from Memory
    }
}
