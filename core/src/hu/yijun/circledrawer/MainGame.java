package hu.yijun.circledrawer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import hu.yijun.circledrawer.buttons.ToggleButton;

public class MainGame extends ApplicationAdapter {

	public SpriteBatch batch;
	public BitmapFont futura20;
	public ShapeRenderer shapeRenderer;

	public final float WIDTH = 960;
	public final float HEIGHT = 540;

	private ToggleButton buttons;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		futura20 = getFont("fonts/futuramd.ttf", 20);
		shapeRenderer = new ShapeRenderer();
	}

	private void update() {

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
		futura20.dispose();
		shapeRenderer.dispose();
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
