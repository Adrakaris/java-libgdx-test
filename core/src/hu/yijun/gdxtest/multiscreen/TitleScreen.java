package hu.yijun.gdxtest.multiscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class TitleScreen extends ScreenAdapter {

	MultiGame game;

	public TitleScreen(MultiGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.SPACE) {
					game.setScreen(new GameScreen(game));
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, .25f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.font.draw(game.batch, "Title Screen!", 50, Gdx.graphics.getHeight()-50);
		game.font.draw(game.batch, "Click the circle to win", 50, Gdx.graphics.getHeight()-70);
		game.font.draw(game.batch, "Press space to play", 50, Gdx.graphics.getHeight()-90);
		game.batch.end();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}
