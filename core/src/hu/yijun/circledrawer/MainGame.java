package hu.yijun.circledrawer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import hu.yijun.circledrawer.buttons.ToggleButton;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

public class MainGame extends ApplicationAdapter {

	public SpriteBatch batch;
	public BitmapFont futura20;
	public ShapeRenderer shapeRenderer;

	public final float WIDTH = 960;
	public final float HEIGHT = 540;

	private BitmapFont debugFont;

	private OrthographicCamera camera;
	private Viewport viewport;
	private ToggleButton[] buttons;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		futura20 = getFont("fonts/futuramd.ttf", 20);
		shapeRenderer = new ShapeRenderer();

		debugFont = new BitmapFont();
		debugFont.setColor(new Color(0,0,0,1));

		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.position.set(WIDTH/2, HEIGHT/2, 0);
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);

		camera.update();

		// buttons are 120x60
		buttons = new ToggleButton[4];
		for (int i = 0; i < 4; i++) {
			ToggleButton t = new ToggleButton(this, 40+i*160, 40, 120, 60, new Color(0.831f, 0.098f, 0.126f, 1), new Color(0.051f, 0.737f, 0.243f, 1));
			buttons[i] = t;
		}


	}

	private void update() {
		for (ToggleButton b : buttons) {
			b.update();
		}
	}

	@Override
	public void render () {
		update();

		ScreenUtils.clear(1, 1, 1, 1);
		shapeRenderer.setProjectionMatrix(camera.combined);

		// RENDER
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (ToggleButton b : buttons) {
			b.drawRender();
		}
		shapeRenderer.end();

		// BATCH
		batch.begin();

		Boolean[] values = new Boolean[4];
		for (int i = 0; i < 4; i++) {
			values [i] = buttons[i].isToggle();
		}
		debugFont.setColor(0,0,0,1);
		debugFont.draw(batch, MessageFormat.format("Button values: {0} {1} {2} {3}", values), 20, HEIGHT-20);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		futura20.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	private BitmapFont getFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = new Color(1,1,1,1);
		BitmapFont f = generator.generateFont(parameter);
		generator.dispose();
		return f;
	}
}
