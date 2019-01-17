package com.mygdx.game.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Player;
import com.mygdx.game.Ship;
import com.mygdx.game.base.BaseGame;
import com.mygdx.game.base.BaseScreen;
import com.mygdx.game.combat.attacks.Attack;
import com.mygdx.game.combat.attacks.Flee;
import com.mygdx.game.combat.attacks.GrapeShot;
import com.mygdx.game.sailing.SailingScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class CombatScreen extends BaseScreen {

    // Screen layout variables
    float button_pad_bottom;
    float button_pad_right;

    // Labels changed throughout the scene
    public Label descriptionLabel;
    private Label playerHPLabel;
    private Label enemyHPLabel;

    // Health bars of both ships
    private ProgressBar playerHP;
    private ProgressBar enemyHP;

    // Image textures and images for the various stages
    private Texture bg_texture;
    private Texture wood_texture;
    private Image background;
    private Image background_wood;

    public Player player;
    public Ship enemy;

    public Player temp_player;
    public Ship temp_enemy;

    // Control the layout of the stage
    private Table completeAttackTable;
    private Table attackTable;
    private Table rootTable;
    private Table descriptionTable;
    private Container<Table> tableContainer;

    // Written text box
    private TextButton textBox;

    // Variables used in handling combat
    private java.util.Stack<Attack> combatStack;
    private static List<Attack> enemyAttacks;
    private Attack currentAttack;
    private BattleEvent queuedCombatEvent;

    // Variables used in text animation
    private float delayTime = 0;
    private boolean textAnimation = false;
    private int animationIndex = 0;
    private String displayText = "";

    public CombatScreen(BaseGame g, Player player, Ship enemy) {
        super(g);
        this.player = player;
        this.enemy = enemy;

        //Load the skin for this Screen
        game.skin = new Skin(Gdx.files.internal("flat-earth-ui.json"));
        Gdx.graphics.setTitle("Combat Demo - York Pirates!");

        combatStack = new Stack();

        button_pad_bottom = viewHeight/24f;
        button_pad_right = viewWidth/32f;

        // Insantiate the image textures for use within the scene as backgrounds
        bg_texture = new Texture("water_texture_sky.png");
        background = new Image(bg_texture);
        background.setSize(viewWidth, viewHeight);

        // TODO choose best wood textue, then will add it into complete background (or leave as is)
        // simply uncomment different textures to try
//        wood_texture = new Texture("wood_texture.png");
//        wood_texture = new Texture("wood_checkerboard_texture.png");
        wood_texture = new Texture("wood_vertical_board_texture.png");
        background_wood = new Image(wood_texture);
        background_wood.setSize(viewWidth, viewHeight);

        // Create a Container which takes up the whole screen (used for layout purposes)
        tableContainer = new Container<Table>();
        tableContainer.setFillParent(true);
        tableContainer.setPosition(0,0);
        tableContainer.align(Align.bottom);

        // Instantiate some different tables used throughout scene
        rootTable = new Table();
        descriptionTable = new Table();
        attackTable = new Table();

        // Instantiate both the ships for the battle
        CombatShip myShip = new CombatShip(player.getPlayerShip(),"ship1.png", viewWidth/3);
        CombatShip enemyShip = new CombatShip(enemy,"ship2.png",viewWidth/3);

        Label shipName = new Label(player.getPlayerShip().getName(), game.skin, "default_black");
        playerHP = new ProgressBar(0, player.getPlayerShip().getHealthMax(), 0.1f, false, game.skin);
        playerHPLabel = new Label(player.getPlayerShip().getHealth() + "/" + player.getPlayerShip().getHealthMax(), game.skin);

        playerHP.getStyle().background.setMinHeight(playerHP.getPrefHeight()*2); //Setting vertical size of progress slider (Class implementation is slightly weird)
        playerHP.getStyle().knobBefore.setMinHeight(playerHP.getPrefHeight());

        Label enemyName = new Label("Enemy " + enemy.getName(), game.skin,"default_black");
        enemyHP = new ProgressBar(0, enemy.getHealthMax(),0.1f,false, game.skin);
        enemyHPLabel = new Label(enemy.getHealth() + "/" + enemy.getHealthMax(), game.skin);

        playerHP.setValue(player.getPlayerShip().getHealthMax());
        enemyHP.setValue(enemy.getHealthMax());

        Table playerHPTable = new Table();
        Table enemyHPTable = new Table();

        playerHPTable.add(playerHPLabel).padRight(viewWidth/36f);
        playerHPTable.add(playerHP).width(viewWidth/5);

        enemyHPTable.add(enemyHPLabel).padRight(viewWidth/36f);
        enemyHPTable.add(enemyHP).width(viewWidth/5);

        Label screenTitle = new Label("Combat Mode", game.skin,"title_black");
        screenTitle.setAlignment(Align.center);

        textBox = new TextButton("WELCOME TO THE BATTLE", game.skin);
        textBox.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                System.out.println("Queued event: " + queuedCombatEvent.toString());
                if (textAnimation) {
                    textAnimation = false;
                    textBox.setText(displayText);
                }
                else{
                    System.out.println("Button clicked, running combat handler with event " + queuedCombatEvent.toString());
                    textBox.setText("");
                    updateHP();
                    combatHandler(queuedCombatEvent);
                }

            }
        });

        this.queuedCombatEvent = BattleEvent.NONE;
        currentAttack = null;

        // Instantiation of the combat buttons. Attack and Flee are default attacks, the rest can be modified by within player class.
        final AttackButton button1 = new AttackButton(Attack.attackMain, game.skin);
        buttonListener(button1);
        final AttackButton button2 = new AttackButton(player.attacks.get(0), game.skin);
        buttonListener(button2);
        final AttackButton button3 = new AttackButton(player.attacks.get(1), game.skin);
        buttonListener(button3);
        final AttackButton button4 = new AttackButton(player.attacks.get(2), game.skin);
        buttonListener(button4);
        final AttackButton fleeButton = new AttackButton(Flee.attackFlee, game.skin, "red");
        buttonListener(fleeButton);

        descriptionLabel = new Label("Please choose your attack option", game.skin);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        descriptionTable.center();
        descriptionTable.add(descriptionLabel).uniform()
                .pad(0, button_pad_right,0, button_pad_right)
                .size(viewWidth/2 - button_pad_right*2, viewHeight/12).top();
        descriptionTable.row();
        descriptionTable.add(fleeButton).uniform();

        attackTable.row();
        attackTable.add(button1).uniform().width(viewWidth/5).padRight(button_pad_right);
        attackTable.add(button2).uniform().width(viewWidth/5);
        attackTable.row().padTop(button_pad_bottom);
        attackTable.add(button3).uniform().width(viewWidth/5).padRight(button_pad_right);
        attackTable.add(button4).uniform().width(viewWidth/5);

        rootTable.row().width(viewWidth*0.8f);
        rootTable.add(screenTitle).colspan(2);
        rootTable.row();
        rootTable.add(shipName);
        rootTable.add(enemyName);
        rootTable.row().fillX();
        rootTable.add(myShip);
        rootTable.add(enemyShip);
        rootTable.row();
        rootTable.add(playerHPTable);
        rootTable.add(enemyHPTable);
        rootTable.row();
        rootTable.add(textBox).colspan(2).fillX().height(viewHeight/9f).pad(viewHeight/12,0,viewHeight/12,0);
        tableContainer.setActor(rootTable);

        completeAttackTable = new Table();
        completeAttackTable.setFillParent(true);
        completeAttackTable.align(Align.bottom);
        completeAttackTable.row().expandX().padBottom(viewHeight/18f);
        completeAttackTable.add(descriptionTable).width(viewWidth/2);
        completeAttackTable.add(attackTable).width(viewWidth/2);

        background_wood.setVisible(false);
        completeAttackTable.setVisible(false);
        uiStage.addActor(background_wood);
        uiStage.addActor(completeAttackTable);

        mainStage.addActor(background);
        mainStage.addActor(tableContainer);

        // Setup Enemy Attacks - may need to change this is you want to draw attacks from enemy's class
        enemyAttacks = new ArrayList<Attack>();
        enemyAttacks.add(Attack.attackMain);
        enemyAttacks.add(GrapeShot.attackGrape);
        enemyAttacks.add(Attack.attackSwivel);

        Gdx.input.setInputProcessor(mainStage);

        System.out.println(viewWidth + "," + viewHeight + " AND " + Gdx.graphics.getWidth() + "," + Gdx.graphics.getHeight());
    }

    @Override
    public void create() {

    }

    @Override
    public void update(float dt) {
        labelAnimationUpdate(dt);
    }

    public void toggleAttackStage(){
        // This method toggles the visibility of the players attack moves and changes input processor to relevant stage
        if (background_wood.isVisible()) {
            background_wood.setVisible(false);
            completeAttackTable.setVisible(false);
            Gdx.input.setInputProcessor(mainStage);
        }
        else{
            background_wood.setVisible(true);
            completeAttackTable.setVisible(true);
            Gdx.input.setInputProcessor(uiStage);
        }
    }

    // Combat Handler
    //  This function handles the ship combat
    public void combatHandler(BattleEvent status){
        //Debugging
        System.out.println("Running combatHandler with status: " + status.toString());

        if (!combatStack.empty()){
            currentAttack = combatStack.pop();
//            System.out.println("Popping "+currentAttack.getName());
        }
        switch(status) {
            case NONE:
                toggleAttackStage();
                break;
            case PLAYER_MOVE:
                toggleAttackStage();
                textBox.setStyle(game.skin.get("default", TextButton.TextButtonStyle.class));
                System.out.println("Running players move");
//                toggleAttackStage();
                if (currentAttack.isSkipMoveStatus()) {
                    System.out.println("Charging attack");
                    currentAttack.setSkipMoveStatus(false);
                    combatStack.push(currentAttack);
                    dialog("Charging attack " + currentAttack.getName(), BattleEvent.ENEMY_MOVE);
                } else if (currentAttack.getName() == "FLEE") {
                    if (currentAttack.doAttack(player.getPlayerShip(), enemy) == 1) {
                        System.out.println("Flee successful");
                        dialog("Flee successful!", BattleEvent.PLAYER_FLEES);
                    } else {
                        System.out.println("Flee Failed");
                        dialog("Flee failed.", BattleEvent.ENEMY_MOVE);
                    }
                } else {
                    int damage = currentAttack.doAttack(player.getPlayerShip(), enemy); // Calls the attack function on the player and stores damage output
                    // This selection statement returns Special Charge Attacks to normal state
                    if (currentAttack.isSkipMove()) {
                        currentAttack.setSkipMoveStatus(true);
                    }
                    if (damage == 0) {
                        System.out.println("Player "+currentAttack.getName() + " MISSED, damage dealt: " + damage + ", Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                        dialog("Attack Missed", BattleEvent.ENEMY_MOVE);
                    } else {
                        System.out.println("Player "+currentAttack.getName() + " SUCCESSFUL, damage dealt: " + damage + ", Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                        if (player.getPlayerShip().getHealth() <= 0) {
                            System.out.println("Player has died");
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!", BattleEvent.PLAYER_DIES);
                        }
                        else if (enemy.getHealth() <= 0) {
                            System.out.println("Enemy has died");
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!", BattleEvent.ENEMY_DIES);
                        }
                        else{
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!", BattleEvent.ENEMY_MOVE);
                        }
                    }

                }
                break;
            case ENEMY_MOVE:
                System.out.println("Running enemy move");
                textBox.setStyle(game.skin.get("red", TextButton.TextButtonStyle.class));
                Attack enemyAttack = enemyAttacks.get(ThreadLocalRandom.current().nextInt(0,3));
                int damage = enemyAttack.doAttack(enemy, player.getPlayerShip());
                String message;
                if (damage == 0){
                    System.out.println("Enemy " + enemyAttack.getName() + " ATTACK MISSED");
                    message = "Enemies " + enemyAttack.getName() + " missed.";
                }
                else {
                    System.out.println("ENEMY " + enemyAttack.getName() + " SUCCESSFUL, damage dealt: " + damage + ", Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                    message = "Enemy "+enemy.getName()+ " dealt " + damage + " with " + enemyAttack.getName()+ "!";
                }
                if (player.getPlayerShip().getHealth() <= 0) {
                    System.out.println("Player has died");
                    dialog("Enemies " + enemyAttack.getName() + " hit you for "+ damage, BattleEvent.PLAYER_DIES);
                }
                else if (enemy.getHealth() <= 0) {
                    System.out.println("Enemy has died");
                    dialog("Enemies " + enemyAttack.getName() + " hit you for "+ damage, BattleEvent.ENEMY_DIES);
                }
                else {
                    if (currentAttack.isSkipMove() != currentAttack.isSkipMoveStatus()){
                        System.out.println("Loading charged attack");
                        dialog(message, BattleEvent.PLAYER_MOVE);
                    }
                    else{
                        dialog(message, BattleEvent.NONE);
                    }
                }
                break;
            case PLAYER_DIES:
                textBox.setStyle(game.skin.get("red", TextButton.TextButtonStyle.class));
                dialog("YOU HAVE DIED", BattleEvent.SCENE_RETURN);
                break;
            case ENEMY_DIES:
                textBox.setStyle(game.skin.get("default", TextButton.TextButtonStyle.class));
                player.addGold(10);
                player.addPoints(10);
                dialog("Congratulations, you have defeated Enemy " + enemy.getName(), BattleEvent.SCENE_RETURN);
                break;
            case PLAYER_FLEES:
                textBox.setStyle(game.skin.get("red", TextButton.TextButtonStyle.class));
                break;
            case SCENE_RETURN:
                System.out.println("END OF COMBAT");
                toggleAttackStage();
                textBox.setText("STOP");
                break;
        }
    }

    // Button Listener Classes

    public void buttonListener(final AttackButton button){
        button.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                descriptionLabel.setText(button.getDesc());
            };
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
                descriptionLabel.setText("Please choose your attack option");
            };
            @Override
            public void clicked(InputEvent event, float x, float y) {
                combatStack.push(button.attack);
                combatHandler(BattleEvent.PLAYER_MOVE);
            }
        });
    }

    public void buttonListener(final AttackButton button, final String message){
        button.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                descriptionLabel.setText(button.getDesc());
            };
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
                descriptionLabel.setText("Choose an option");
            };
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.setText(message);
            }
        });
    }

    //TODO Remove knob when health = 0.
    public void updateHP(){
        enemyHP.setAnimateDuration(1);
        playerHP.setAnimateDuration(1);

        if (enemy.getHealth() <= 0){
            enemy.setHealth(0);
        }
        if (player.getPlayerShip().getHealth() <= 0){
            player.getPlayerShip().setHealth(0);
        }

        enemyHPLabel.setText(enemy.getHealth()+"/"+enemy.getHealthMax());

        enemyHP.setValue(enemy.getHealth());
        playerHPLabel.setText(player.getPlayerShip().getHealth()+"/"+player.getPlayerShip().getHealthMax());
        playerHP.setValue(player.getPlayerShip().getHealth());
    }

    //Updates text box
    public void dialog(String message, final BattleEvent nextEvent){
        queuedCombatEvent = nextEvent;
        if (background_wood.isVisible()){
            toggleAttackStage();
        }
//        System.out.println(message_length + message);
        displayText = message;
        animationIndex = 0;
        textAnimation = true;

    }

    public void labelAnimationUpdate(float dt){
        if (textAnimation) {
            delayTime += dt;
            if (animationIndex > displayText.length()){
                textAnimation = false;
            }
            if (delayTime >= 0.05f){
                textBox.setText(displayText.substring(0,animationIndex));
                animationIndex++;
                delayTime = 0;
            }
        }
    }
}
