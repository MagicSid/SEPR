package com.mygdx.game.screens.combat.attacks;

import com.mygdx.game.Ship;

public class GrapeShot extends Attack {
    public GrapeShot(String name, String desc, int dmgMultiplier, double accMultiplier, boolean skipMove, int accPercent) {
        super(name, desc, dmgMultiplier, accMultiplier, skipMove, accPercent);
    }

    @Override
    public int doAttack(Ship attacker, Ship defender) {
        damage = 0;
        for (int i = 0; i < 4; i++) { // Fires 4 shots.
            if (doesHit(attacker.getAccuracy(), (int) (10 * accMultiplier), 200)) {
                damage += attacker.getAttack() * dmgMultiplier; // Landed shots do half as much damage as a swivel shot.
                System.out.println("GRAPE HIT");
            }
            else{
                System.out.println("GRAPE MISSED");
            }
        }
        defender.damage(damage);
        return damage;
    }

    public static Attack attackGrape = new GrapeShot("Grape Shot","Fire a bundle of smaller cannonballs at the enemy.",1,1,false, 25);
}
