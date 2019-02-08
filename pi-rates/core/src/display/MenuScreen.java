package display;

import com.badlogic.gdx.Audio;
import base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.compression.lzma.Base;
import game_manager.GameManager;

public class MenuScreen extends BaseScreen{
    private SpriteBatch batch = new SpriteBatch();

    private Texture menuBackground = new Texture("menuBackground.png");
    private BitmapFont titleFont = new BitmapFont(); //Sets titleFont to Libgdx default font
    private Color titleColor = new Color(226F/255F, 223F/255F,164F/255F, 1);

    private Image background = new Image(menuBackground);
    private Label titleText;

    //Menu buttons, their font, style and texture
    private BitmapFont buttonFont = new BitmapFont();
    private TextButton.TextButtonStyle myTextButtonStyle = new TextButton.TextButtonStyle();
    private TextureAtlas buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
    private Skin skin = new Skin();
    private TextButton runCombat;
    private TextButton runCollege;
    private TextButton runDepartment;
    private TextButton exitGame;

    private Music mainMusic;

    public MenuScreen(GameManager game){
        super(game);
    }

    @Override
    public void update(float delta){ }

    @Override
    public void show() {
        titleFont.setColor(titleColor);
        titleFont.getData().setScale(4);

        //Adds textures to the Skin, sets Skin for Button Up and Down
        skin.addRegions(buttonAtlas);
        myTextButtonStyle.font = buttonFont;
        myTextButtonStyle.up = skin.getDrawable("buttonUp");
        myTextButtonStyle.down = skin.getDrawable("buttonDown");

        //Draws Menu Title and Background
        stage.addActor(background);
        this.background.setSize(viewwidth, viewheight);

        this.titleText = new Label("SEPR GAME", new Label.LabelStyle(titleFont, titleColor));
        this.titleText.setPosition(viewwidth/2f - 160, (viewheight*900)/1024);
        stage.addActor(this.titleText);

        mainMusic = makeMusic("the-buccaneers-haul.mp3");

        playMusic(mainMusic, true);

        /**
         * Creates Text buttons for the menu, Sets them up and Adds listeners to switch to correct screen
         */
        runCombat = new TextButton("Run Combat", myTextButtonStyle);
        runCollege = new TextButton("Run College", myTextButtonStyle);
        runDepartment = new TextButton("Run Department", myTextButtonStyle);
        exitGame = new TextButton("Exit Game", myTextButtonStyle);

        stage.addActor(runCombat);
        runCombat.setPosition(viewwidth/2f - 175, (viewheight*700)/1024);
        runCombat.setTransform(true); //Allows the Button to be Scaled
        runCombat.setScale(3);
        runCombat.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                changeScreen(new CombatScreen(game,false));
                return true;
            }
        });

        stage.addActor(runCollege);
        runCollege.setPosition(viewwidth/2f - 175, (viewheight*580)/1024);
        runCollege.setTransform(true); //Allows the Button to be Scaled
        runCollege.setScale(3);
        runCollege.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                changeScreen(new CombatScreen(game,true));
                return true;
            }
        });

        stage.addActor(runDepartment);
        runDepartment.setPosition(viewwidth/2f - 175, (viewheight*460)/1024);
        runDepartment.setTransform(true); //Allows the Button to be Scaled
        runDepartment.setScale(3);
        runDepartment.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                changeScreen(new DepartmentScreen(game));
                return true;
            }
        });

        stage.addActor(exitGame);
        exitGame.setPosition(viewwidth/2f - 175, (viewheight*340)/1024);
        exitGame.setTransform(true); //Allows the Button to be Scaled
        exitGame.setScale(3);
        exitGame.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                System.exit(0);
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(stage);

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
        menuBackground.dispose();
        mainMusic.stop();
        mainMusic.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        skin.dispose();
        buttonAtlas.dispose();
        batch.dispose();
        stage.dispose();
    }
}
