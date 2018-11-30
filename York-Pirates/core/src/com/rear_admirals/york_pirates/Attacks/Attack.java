package com.rear_admirals.york_pirates.Attacks;

import com.rear_admirals.york_pirates.Ship;

public class Attack {
	protected String name;
	protected String desc;

	protected Attack() {
		name = "Broadside";
		desc = "Fire a broadside at your enemy.";
	}

	protected boolean doesHit( int accuracy, int mult, int bound) {
		if ( accuracy * mult > Math.random() * bound ) { return true; }
		else { return false; }
	}

	public boolean doAttack(Ship attacker, Ship defender) {
		if ( doesHit(attacker.getAccuracy(), 10, 100) ) {
			defender.damage(attacker.getAttack());
			return true;
		}
		return false;
	}

	public String getName() { return name;	}
	public String getDesc() { return desc; }

	public static final Attack attackMain = new Attack();

}