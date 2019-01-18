package com.mygdx.game.screens.combat.attacks;

import com.mygdx.game.Ship;

public class Ram extends Attack {

    protected Ram(String name, String desc, int dmgMultiplier, double accMultiplier, boolean skipMove, int accPercent) {
        super(name, desc, dmgMultiplier, accMultiplier, skipMove, accPercent);
    }

    @Override
    public int doAttack(Ship attacker, Ship defender) {
        if ( doesHit(attacker.getAccuracy(), (int)(10*accMultiplier), 200) ) {
            damage = attacker.getAttack()*dmgMultiplier;
            defender.damage(damage);
            attacker.damage(damage/2);
            return damage;
        }
        return 0;
    }

    public static Attack attackRam = new Ram("Ram","Ram your ship into your enemy, causing damage to both of you.", 4, 1, false, 85);
}
