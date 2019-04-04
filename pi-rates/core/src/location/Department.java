package location;

import com.badlogic.gdx.Gdx;

import combat.actors.CombatPlayer;
import combat.items.RoomUpgrade;
import combat.items.Weapon;
import game_manager.GameManager;
import other.Constants;
import other.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static other.Constants.*;

/**
 * The department acts as a shop in our game.
 * Had to implement Serializable due to encoding needed for saving game data
 */
public class Department implements java.io.Serializable {
    /**
     * The weapons that can be bought at the shop
     */
    private List<Weapon> weaponStock;
    /**
     * The upgrades that can be bought at the shop
     */
    private List<RoomUpgrade> upgradeStock;
    /**
     * The resources that can be bought at the shop and their prices per unit
     */
    private Map<Resource, Integer> resourceStock;
    /**
     * The GameManager of the current game so that gold can be deducted
     */
    private GameManager gameManager;

    public Department(List<Weapon> weaponStock, List<RoomUpgrade> upgradeStock, GameManager gameManager) {
        this.weaponStock = weaponStock;
        this.upgradeStock = upgradeStock;
        this.gameManager = gameManager;
        this.resourceStock = new HashMap<Resource, Integer>();
        resourceStock.put(Resource.REPAIR, REPAIR_COST);
        resourceStock.put(Resource.CREW, CREW_COST);
        resourceStock.put(Resource.FOOD, FOOD_COST);
    }

    public List<Weapon> getWeaponStock() {
        return weaponStock;
    }

    public List<RoomUpgrade> getUpgradeStock() {
        return upgradeStock;
    }

    public Map<Resource, Integer> getResourceStock() {
        return resourceStock;
    }

    /**
     * Buys the given weapon
     * @param weapon The weapon that you want to buy
     */
    public void buyWeapon(Weapon weapon) {
        if (gameManager.getGold() < weapon.getCost()) {
            throw new IllegalArgumentException("Not enough gold");
        } else if (!weaponStock.contains(weapon)) {
            throw new IllegalArgumentException("Weapon does not exist");
        } else {
            gameManager.getPlayerShip().addWeapon(weapon);
            weaponStock.remove(weapon);
            gameManager.payGold(weapon.getCost());
        }
    }

    /**
     * Buys the given upgrade
     * @param upgrade The upgrade that you want to buy
     */
    public void buyRoomUpgrade(RoomUpgrade upgrade) {
        if (gameManager.getGold() < upgrade.getCost()) {
            throw new IllegalArgumentException("Not enough gold");
        } else if (!upgradeStock.contains(upgrade)) {
            throw new IllegalArgumentException("Upgrade does not exist");
        } else {
            gameManager.getPlayerShip().addUpgrade(upgrade);
            upgradeStock.remove(upgrade);
            gameManager.payGold(upgrade.getCost());
        }
    }

    /**
     * Buys the given resource
     * @param resource The resource that you want to buy
     * @param amount The amount of resources you want to buy
     */
    public void buyResource(Resource resource, int amount) {
        int price;
        if (!resourceStock.keySet().contains(resource)) {
            throw new IllegalArgumentException("Resource not valid");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Not allowed");
        } else {
            price = resourceStock.get(resource);
        }
        if (resource == Resource.FOOD) {
            if (price * amount > gameManager.getGold()) {
                throw new IllegalStateException("Not enough gold");
            } else {
                gameManager.addCrew(amount);
                gameManager.payGold(price * amount);
            }
        }
        if (resource == Resource.CREW) {
            if (price * amount > gameManager.getGold()) {
                throw new IllegalStateException("Not enough gold");
            } else {
                gameManager.getPlayerShip().addCrew(amount);
                gameManager.payGold(price * amount);
            }
        }
        if (resource == Resource.REPAIR) {
            if (price > gameManager.getGold()) {
                throw new IllegalStateException("Not enough gold");
            } else {
                gameManager.getPlayerShip().repairHull(gameManager.getPlayerShip().getBaseHullHP());
                gameManager.setCombatPlayer(new CombatPlayer(gameManager.getPlayerShip()));
                gameManager.payGold(amount);
            }
        }
    }

    /**
     * Sells a weapon back to the shop for slightly less than its worth
     * @param weapon The weapon that you want to sell
     */
    public void sellWeapon(Weapon weapon) {
        if (!gameManager.getPlayerShip().getWeapons().contains(weapon)) {
            throw new IllegalArgumentException("You do not own this weapon");
        } else if (weaponStock.size() == 4) {
            Gdx.app.log("Department", "Sale refused as shop is full");
        } else {
            gameManager.getPlayerShip().getWeapons().remove(weapon);
            weaponStock.add(weapon);
            gameManager.addGold((int) (weapon.getCost() * Constants.STORE_SELL_PRICE_MULTIPLIER));
        }

    }

    /**
     * Sells an upgrade to the shop for slightly less than its worth
     * @param upgrade The upgrade that you want to sell
     */
    public void sellUpgrade(RoomUpgrade upgrade) {
        if (!gameManager.getPlayerShip().hasUpgrade(upgrade)) {
            throw new IllegalArgumentException("You do not own this upgrade");
        } else {
            gameManager.getPlayerShip().delUpgrade(upgrade);
            upgradeStock.add(upgrade);
            gameManager.addGold((int) (upgrade.getCost() * Constants.STORE_SELL_PRICE_MULTIPLIER));
        }
    }
}
