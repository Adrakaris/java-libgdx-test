package hu.yijun.gdxtest.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlopCircle {
	private final ShapeRenderer renderer;

	private int xPos;
	private int yPos;
	private final int rad;
	private final float[] col;
	private boolean picked = false;

	public PlopCircle(int rad, float r, float g, float b, float a) {
		renderer = new ShapeRenderer();
		this.rad = rad;
		col = new float[]{r, g, b, a};
		xPos = Gdx.graphics.getWidth() / 2;
		yPos = Gdx.graphics.getHeight() / 2;
	}

	/**
	 * Use a pythagorean theorem to check if the given coordinates are incident with the circle
	 * @param x given x coordinate
	 * @param y given y coordinate
	 * @return whether or not it is in the circle
	 */
	private boolean inBounds(int x, int y) {
		int dx = Math.abs(x-xPos);
		int dy = Math.abs(y-yPos);
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distance <= rad;
	}

	private void update() {
		// detecting user input
		// and changing circle position if so
		if (Gdx.input.isTouched() && inBounds(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
			picked = true;
		}
		if (!Gdx.input.isTouched()) {
			picked = false;
		}

		if (picked) {
			xPos = Gdx.input.getX();
			// because the y coords gotten from mouse input are reversed to openGL rendering
			yPos = Gdx.graphics.getHeight() - Gdx.input.getY();
		}
	}

	public void render() {
		this.update();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(col[0], col[1], col[2], col[3]);
		renderer.circle(xPos, yPos, rad);
		renderer.end();
	}

	public void dispose() {
		renderer.dispose();
	}
}
