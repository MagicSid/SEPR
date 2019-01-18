package com.mygdx.game.screens.combat;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.screens.combat.attacks.Attack;

public class AttackButton extends TextButton {
    String name;
    Skin skin;
    String desc;
    Attack attack;

    public AttackButton(Attack attack, Skin skin){
        super(attack.getName(), skin);
        this.attack = attack;
        this.name = attack.getName();
        this.skin = skin;
        this.desc = attack.getDesc();
    }

    public AttackButton(Attack attack, Skin skin, String type){
        super(attack.getName(), skin, type);
        this.attack = attack;
        this.name = attack.getName();
        this.skin = skin;
        this.desc = attack.getDesc();
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

//    public AttackButton setAttack
}
