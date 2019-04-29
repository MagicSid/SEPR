package display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import base.BaseScreen;
import game_manager.GameManager;

public class GameOverScreen extends BaseScreen {
	/**
	 * CODE CHANGE BELOW Assessment4
	 * New screen added for assessment 4. Used when player is defeated in combat - saving disabled and return to menu button present.
	 */
	private Texture endBackground = new Texture("battleBackground.png");
	private Image background = new Image(endBackground);
	
	private Table uitable;
	
	private TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    private TextureAtlas buttonAtlas;
    private BitmapFont buttonFont = new BitmapFont();
    
    Label loseLabel;
    TextButton closeButton;
    TextButton menuButton;
	
	public GameOverScreen(final GameManager pirateGame) {
		super(pirateGame);
		
		this.saveButton.setVisible(false);
		this.mainMenuButton.setVisible(false);
		
		this.uitable = new Table();
		buttonAtlas = new TextureAtlas("buttonSpriteSheet.txt");
        skin.addRegions(buttonAtlas);
        
        mainStage.addActor(background);
      	this.background.setSize(viewwidth, viewheight);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = buttonFont;
        textButtonStyle.up = skin.getDrawable("buttonUp");
        textButtonStyle.down = skin.getDrawable("buttonDown");
        
        uitable.setFillParent(true);
		uitable.align(Align.center);
		
		addbuttons();
		
		mainStage.addActor(uitable);
		
		
	}

	@Override
	public void update(float delta) {
		Gdx.input.setInputProcessor(mainStage);
	}
	
	private void addbuttons() {
		loseLabel = new Label("Unfortunately your ship has sunk", skin);
		uitable.add(loseLabel);
		uitable.row();
		
		closeButton = new TextButton("Exit game", textButtonStyle);
		
		closeButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
            	Gdx.app.exit();
            }
        });
		
		
		uitable.add(closeButton);
		uitable.row();
		
		// not currently working. Removed for later editing
//		menuButton = new TextButton("Back to Menu",textButtonStyle);
//		
//		menuButton.addListener(new ClickListener(){
//            public void clicked(InputEvent event, float x, float y) {
//            	
//            	changeScreen(new MenuScreen(game));
//            }
//        });
//		
//		uitable.add(menuButton);
	}

	@Override
	public void show() {
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
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() {
		super.dispose();
		endBackground.dispose();
		skin.dispose();
	}
	
}
