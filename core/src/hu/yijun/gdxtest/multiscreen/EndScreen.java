package hu.yijun.gdxtest.multiscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class EndScreen extends ScreenAdapter {

	MultiGame game;

	public EndScreen(MultiGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.ENTER) {
					game.setScreen(new TitleScreen(game));
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, .25f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.font.draw(game.batch, "You win!", 50, game.height()-50);
		game.font.draw(game.batch, "Press Enter to restart", 50, game.height()-90);
		game.batch.end();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}
