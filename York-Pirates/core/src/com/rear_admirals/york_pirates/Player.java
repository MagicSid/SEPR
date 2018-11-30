package com.rear_admirals.york_pirates;


import com.rear_admirals.york_pirates.Attacks.*;

import java.util.ArrayList;
import java.util.List;

import static com.rear_admirals.york_pirates.ShipType.Brig;

public class Player {
    public Ship playerShip;
    private int gold;
    private int points;
    public static List<Attack> attacks = new ArrayList<Attack>();

    public Player() {
	    playerShip = new Ship(Brig);
        gold = 0;
        points = 0;

        attacks.add(Attacks.list.attackMain);
        attacks.add(Attacks.list.attackGrape);
        attacks.add(Attacks.list.attackRam);
        attacks.add(Attacks.list.attackSwivel);
        attacks.add(Attacks.list.attackFlee);
    }

    public Player(Ship ship) {
        playerShip = ship;
        gold = 0;
        points = 0;

	    attacks.add(Attacks.list.attackMain);
	    attacks.add(Attacks.list.attackGrape);
	    attacks.add(Attacks.list.attackRam);
	    attacks.add(Attacks.list.attackSwivel);
	    attacks.add(Attacks.list.attackFlee);
    }

    public int getPoints() { return points; }

	public int getGold() { return gold; }

	public List<Attack> getAttacks() { return attacks; }

    public void addPoints(int amount) { points += amount; }

    public void addGold(int amount) { gold = gold + amount; }

    public void setAttacks( List<Attack> attacks ) { this.attacks = attacks; }

}