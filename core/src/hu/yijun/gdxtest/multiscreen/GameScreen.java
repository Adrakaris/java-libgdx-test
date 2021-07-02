package hu.yijun.gdxtest.multiscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter {

	MultiGame game;

	private float circleX = 300;
	private float circleY = 150;
	private final float radius = 20;

	private float xSpeed;
	private float ySpeed;

	private float r = 0;
	private float g = 0.8f;
	private float b = 0;

	public GameScreen(MultiGame game) {
		this.game = game;
		xSpeed = MathUtils.random(3.0f, 5.0f);
		ySpeed = MathUtils.random(2.0f, 4.0f);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				int renderY = Gdx.graphics.getHeight() - screenY;
				if (Vector2.dst(circleX, circleY, screenX, renderY) < radius) {
					game.setScreen(new EndScreen(game));
				}
				return true;
			}
		});
	}

	private void update(float delta) {
		circleX += xSpeed;
		circleY += ySpeed;

		if (circleX < radius || circleX > Gdx.graphics.getWidth()-radius) {
			xSpeed = -MathUtils.random(xSpeed-1, xSpeed+1);
			randCol();
		}
		if (circleY < radius || circleY > Gdx.graphics.getHeight()-radius) {
			ySpeed = -MathUtils.random(ySpeed-1, ySpeed+1);
			randCol();
		}
	}

	@Override
	public void render(float delta) {
		update(delta);

		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		game.shapeRenderer.setColor(r, g, b, 1);
		game.shapeRenderer.circle(circleX, circleY, radius);
		game.shapeRenderer.end();
	}

	private void randCol() {
		r = MathUtils.random(0.3f, 1);
		g = MathUtils.random(0.3f, 1);
		b = MathUtils.random(0.3f, 1);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}
