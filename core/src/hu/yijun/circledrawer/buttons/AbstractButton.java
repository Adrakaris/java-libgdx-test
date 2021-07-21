package hu.yijun.circledrawer.buttons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/**
 * An abstract button class that can be extended and used in libGDX projects
 *
 * @param <Game> The main ApplicationAdapter game class in the project - please <i>specify</i> this when extending so
 *              your class isn't genericised. For example: <code>public class Button extends AbstractButton&lt;GDXGame&gt;</code>,
 *              where your main game class is <code>GDXGame</code>
 */
public abstract class AbstractButton<Game extends ApplicationAdapter> {

	protected Game game;

	protected float x;
	protected float y;
	protected float w;
	protected float h;
	protected Color offCol;
	protected Color onCol;
	protected Rectangle bounding;

	/**
	 * An abstract base class for a rectangular button.
	 *
	 * @param game The game that the button belongs to
	 * @param x x position
	 * @param y y position
	 * @param w width
	 * @param h height
	 * @param off off colour
	 * @param on on colour
	 */
	public AbstractButton(Game game, float x, float y, float w, float h, Color off, Color on) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.offCol = off;
		this.onCol = on;
		this.bounding = new Rectangle(x, y, w, h);
	}

	public abstract void update();

	public abstract void drawRender();

	public abstract void drawBatch();

	public abstract void dispose();
}
