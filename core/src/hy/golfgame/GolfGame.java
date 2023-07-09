package hy.golfgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import hy.golfgame.scene.AbstractGameScreen;
import hy.golfgame.scene.MainGameScreen;

public class GolfGame extends Game {

	public enum Screens {
		MAINGAME, TITLE
	}

	private SpriteBatch spriteBatch;
	private ModelBatch modelBatch;
	private AssetManager assetManager;

	private AbstractGameScreen mainGameScreen;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();
		assetManager = new AssetManager();

		mainGameScreen = new MainGameScreen(this);

		setScreen(mainGameScreen);
	}

	public void switchGameScreen(Screens screenChoice) {
		switch (screenChoice) {
			case TITLE -> throw new RuntimeException("Don't have one mate.");
			case MAINGAME -> this.screen = mainGameScreen;
		}
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		modelBatch.dispose();
		assetManager.dispose();

		mainGameScreen.dispose();

		super.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
