package hu.yijun.circledrawer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		// batch.begin();
		// batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private BitmapFont getFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = new Color(0,0,0,1);
		BitmapFont f = generator.generateFont(parameter);
		generator.dispose();
		return f;
	}
}
