package hu.yijun.gdxtest.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.text.MessageFormat;

public class BouncyBoi {
	protected final ShapeRenderer renderer;
	protected final BitmapFont font;
	protected final SpriteBatch batch;

	protected final float[] col;
	private int xVel;
	protected float yVel;
	protected final int gravity;
	protected int xPos;
	protected int yPos;
	protected final int rad;
	private int initHeight;

	/**
	 * Creates a bouncing circle with a specified colour, initial accelerations and positions and sizes.
	 * This bouncy ball treats the sides of the window as perfectly elastic collisions.
	 *
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param a alpha
	 * @param xVel sideways velocity, in PIXELS PER SECOND
	 * @param gravity vertical acceleration, in PIXELS PER SECOND, must be positive (or will be made positive)
	 * @param xPos initial x position, try put it inside screen bounds please
	 * @param yPos initial y position, for best results put near top of window
	 * @param radius radius of circle in pixels
	 */
	public BouncyBoi(SpriteBatch batch, BitmapFont font, float r, float g, float b, float a, int xVel, int gravity, int xPos, int yPos, int radius) {
		renderer = new ShapeRenderer();
		this.font = font;

		this.batch = batch;
		col = new float[]{r, g, b, a};
		this.xVel = xVel;
		this.gravity = Math.abs(gravity);
		this.xPos = xPos;
		this.yPos = initHeight = yPos;
		yVel = 0;
		rad = radius;
	}

	/**
	 * Creates a bouncing ball with default parameters for initial positions.
	 *
	 * xAccel 120, yAccel 60, xPos (random), yPos (random), radius 50.
	 *
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param a alpha
	 */
	public BouncyBoi(SpriteBatch batch, BitmapFont font, float r, float g, float b, float a) {
		this(batch, font, r, g, b, a, 120, 60, 0, 0, 50);
		// set random x and y positions
		xPos = (int) (Math.random() * (Gdx.graphics.getWidth() - 120)) + 50;
		yPos = initHeight = (int) (Math.random() * 100 + (Gdx.graphics.getHeight() - 220));
	}

	protected float dt() {
		return Gdx.graphics.getDeltaTime();
	}

	protected void update() {
		// update velocity and position
		// reverse both velocity and acceleration depending on which wall to hit
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (xPos >= width - rad) {  // xpos has hit the right
			// we need to reverse direction
			xVel = -Math.abs(xVel);
		} else if (xPos < rad) {  // xpos has hit the left
			// we need to reverse direction
			xVel = Math.abs(xVel);
		}
		xPos += xVel * dt();

		if (yPos >= height - rad) {  // ypos has hit the TOP, which means we need to null its velocity
			// (it shouldn't hit the top, because gravity.)
			yVel = 0;
		} else if (yPos <= rad) {  // ball has hit the bottom, y=0 (effectively), and so must have its velocity positived
			// yVel = Math.abs(yVel);
			// we need to get a perfectly elastic collision
			yVel = (float) Math.sqrt(2 * initHeight);
		}
		yVel = yPos <= rad ? yVel : (int) (yVel - gravity * dt());  // velocity is reduced by gravity if not touching the ground
		yPos += yVel;
	}

	public void render() {
		this.update();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(col[0], col[1], col[2], col[3]);
		renderer.circle(xPos, yPos, rad);
		renderer.end();

		// debug
		batch.begin();
		String details = MessageFormat.format("PosX: {0}, PosY: {1} \nVelX: {2} (1sf), VelY: {3}\nGravity: {4} px/s\nInitial y: {5}",
				xPos, yPos, Math.round(xVel * dt()), yVel, gravity, initHeight);
		font.draw(batch, details, 20, Gdx.graphics.getHeight()-20);
		batch.end();
		// System.out.println(details);
	}

	public void dispose() {
		renderer.dispose();
	}
}
