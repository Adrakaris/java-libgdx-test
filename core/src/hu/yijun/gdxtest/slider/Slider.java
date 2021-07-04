package hu.yijun.gdxtest.slider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Slider {

	private final Texture slider;
	private final HashMap<String, Float> circle;
	private final Colours game;

	private float x;
	private float y;
	private float value;  // the value of the slider, a float between 0 and 1
	private boolean touched;  // whether or not the circle is touched

	public Slider(Colours game, float r, float g, float b, float x, float y) {
		this.x = x;
		this.y = y;

		slider = new Texture(Gdx.files.internal("slider/slider-100.png"));
		this.game = game;
		value = 0;

		// create all attributes of the circle
		circle = (HashMap<String, Float>) Stream.of(new Object[][] {
				{"r", r},
				{"g", g},
				{"b", b},
				{"rad", 7f},  // radius
				{"px", x},
				{"py", y+(slider.getHeight()/2)}  // centre of the slider
		}).collect(Collectors.toMap(data -> (String) data[0], data -> (Float) data[1]));
	}

	public void update() {
		if (Gdx.input.isTouched()) {
			// update the circle's position in the slider
			float mouseX = Gdx.input.getX();
			float mouseY = game.HEIGHT - Gdx.input.getY();

			if (Vector2.dst(circle.get("px"), circle.get("py"), mouseX, mouseY) <= circle.get("rad")) {
				touched = true;
			}
		} else {
			touched = false;
		}

		if (touched) {
			// System.out.println(MessageFormat.format(
			// 		"MouseX {0} MouseY {1} CircX {2} CircY {3}; Dist {4}",
			// 		Gdx.input.getX(), game.HEIGHT - Gdx.input.getY(), circle.get("px"), circle.get("py"), Vector2.dst(circle.get("px"), circle.get("py"), Gdx.input.getX(), game.HEIGHT - Gdx.input.getY())));
			float newX = Math.max(x, Math.min(x + slider.getWidth(), Gdx.input.getX()));  // get a new x within the bounds of the slider
			circle.replace("px", newX);
			value = translate(newX, x, x + slider.getWidth(), 0, 1);
			// System.out.println("new x " + circle.get("px") + " new value " + value);
		}
	}

	public void drawShape() {
		game.renderer.setColor(circle.get("r"), circle.get("g"), circle.get("b"), 1);
		game.renderer.circle(circle.get("px"), circle.get("py"), circle.get("rad"));
	}

	public void drawBatch() {
		game.batch.draw(slider, x, y);

		game.font.draw(game.batch, Integer.toString(Math.round(value * 255)), x + (slider.getWidth()) + 10, y+slider.getHeight()+5);
	}

	public float getValue() {
		return value;
	}

	public static float translate(float val, float beginMin, float beginMax, float endMin, float endMax) {
		float beginRange = beginMax - beginMin;
		float endRange = endMax - endMin;
		// convert initial value to a 0-1 range
		double scaledValue = (val - beginMin) / beginRange;
		// scale to final range
		return (float) (endMin + scaledValue * endRange);
	}
}
