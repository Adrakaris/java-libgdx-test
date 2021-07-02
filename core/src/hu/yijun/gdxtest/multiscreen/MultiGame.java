package hu.yijun.gdxtest.multiscreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.function.Function;

public class MultiGame extends Game {

	// note: these are package-private variables - public only in the package multiscreen
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;

	float width() { return Gdx.graphics.getWidth(); }

	float height() { return Gdx.graphics.getHeight(); }

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = getFont();
		setScreen(new TitleScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}

	private BitmapFont getFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/futura-md.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		BitmapFont futura20 = generator.generateFont(parameter);
		generator.dispose();
		return futura20;
	}
}
