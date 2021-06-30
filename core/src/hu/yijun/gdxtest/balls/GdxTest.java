package hu.yijun.gdxtest.balls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GdxTest extends ApplicationAdapter {
	private SpriteBatch batch;
	// Texture img;
	private BitmapFont font;
	// ShapeRenderer shapeRenderer;
	// BouncyBoi circle;
	private PlopCircle circle;
	private DraggableBall draggableBall;

	private float r = MathUtils.random();
	private float g = MathUtils.random();
	private float b = MathUtils.random();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		// font issue when shape renderer done in spritebatch space
		// shapeRenderer = new ShapeRenderer();
		// circle = new BouncyBoi(batch, font, 0f, 0.8f, 0f, 1f);
		circle = new PlopCircle(75, 0, 0.8f, 0, 1);
		draggableBall = new DraggableBall(batch, font, 0f, 0.8f, 0f, 1f);

		// input processor does things w/o the big if chain in the thing
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyTyped(char character) {  // on any key pressed
				r = MathUtils.random();
				g = MathUtils.random();
				b = MathUtils.random();
				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {  // on a click
				r = MathUtils.random();
				g = MathUtils.random();
				b = MathUtils.random();
				return true;
			}
		});
	}

	@Override
	public void render () {
		// ScreenUtils.clear(0.25f, 0.25f, 0.25f, 1);
		// Gdx.gl.glClearColor(r, g, b, 1);
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // openGl clearing colour
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// circle.render();
		draggableBall.render();

		batch.begin();
		// batch.draw(img, 0, 0);
		// oh it also seems that libgdx coords go from the BOTTOM LEFT, lke normal coordinates
		font.draw(batch, "A ball bouncing around", Gdx.graphics.getWidth()-170, Gdx.graphics.getHeight()-20);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		// img.dispose();
		circle.dispose();
		draggableBall.dispose();
	}
}
