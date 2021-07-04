package hu.yijun.gdxtest.slider;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.text.MessageFormat;

public class Colours extends ApplicationAdapter {

	// camera world coordinates
	float WIDTH;
	float HEIGHT;

	OrthographicCamera camera;
	BitmapFont font;
	SpriteBatch batch;
	ShapeRenderer renderer;

	Slider[] sliders;

	@Override
	public void create() {
		WIDTH = (float) Gdx.graphics.getWidth();
		HEIGHT = (float) Gdx.graphics.getHeight();

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.position.set(WIDTH/2, HEIGHT/2, 0);
		camera.update();

		font = getFont();
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		sliders = new Slider[]{
				new Slider(this, 0.8f, 0, 0, 40, HEIGHT-20-20),
				new Slider(this, 0, 0.8f, 0, 40, HEIGHT-35-40),
				new Slider(this, 0, 0, 0.8f, 40, HEIGHT-50-60),
		};
	}

	private void update() {
		for (Slider s : sliders) {
			s.update();
		}
	}

	@Override
	public void render() {
		update();

		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(sliders[0].getValue(), sliders[1].getValue(), sliders[2].getValue(), 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw box
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(0.9098039f, 0.9176471f, 0.7882353f, 0.3921569f);
		renderer.rect(20, HEIGHT-125, 190, 110);
		renderer.end();

		// draw circle stuff
		batch.begin();
		for (Slider s : sliders) {
			s.drawBatch();
		}
		batch.end();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		for (Slider s : sliders) {
			s.drawShape();
		}
		renderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
		renderer.dispose();
	}

	private BitmapFont getFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/futura-md.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 20;
		parameter.color = new Color(0,0,0,1);
		BitmapFont futura = generator.generateFont(parameter);
		generator.dispose();
		return futura;
	}
}
