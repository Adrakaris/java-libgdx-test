package hu.yijun.circledrawer.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import hu.yijun.circledrawer.MainGame;

public class ToggleButton extends AbstractButton<MainGame> {

	private boolean isToggle;

	/**
	 * A rectangular button that has a toggle feature.
	 *
	 * @param game Game class
	 * @param x x loc of button
	 * @param y y loc of button
	 * @param w width of button
	 * @param h height of button
	 * @param off off colour
	 * @param on on colour
	 */
	public ToggleButton(MainGame game, float x, float y, float w, float h, Color off, Color on) {
		super(game, x, y, w, h, off, on);
		isToggle = false;
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
//			System.out.println(Gdx.input.getX() + " X/Y " + Gdx.input.getY());
			if (bounding.contains(Gdx.input.getX(), game.HEIGHT - Gdx.input.getY())) {
				isToggle = !isToggle;
			}
		}
	}

	@Override
	public void drawRender() {
		game.shapeRenderer.setColor(isToggle ? onCol : offCol);
		game.shapeRenderer.rect(x, y, w, h);
	}

	@Override
	public void drawBatch() {

	}

	@Override
	public void dispose() {

	}

	public boolean isToggle() {
		return isToggle;
	}
}
