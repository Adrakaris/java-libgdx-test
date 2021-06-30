package hu.yijun.gdxtest.balls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.text.MessageFormat;

@SuppressWarnings("DuplicatedCode")
public class DraggableBall extends BouncyBoi {
	private boolean picked = false;

	public DraggableBall(SpriteBatch batch, BitmapFont font, float r, float g, float b, float a, int xVel, int gravity, int xPos, int yPos, int radius) {
		super(batch, font, r, g, b, a, xVel, gravity, xPos, yPos, radius);
	}

	public DraggableBall(SpriteBatch batch, BitmapFont font, float r, float g, float b, float a) {
		super(batch, font, r, g, b, a);
	}

	private boolean inBounds(int x, int y) {
		int dx = Math.abs(x-xPos);
		int dy = Math.abs(y-yPos);
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		return distance <= rad;
	}

	@Override
	protected void update() {
		// super.update();

		if (yPos <= rad) {  // ball has hit the bottom, y=0 (effectively), and so must have its velocity positived
			// yVel = Math.abs(yVel);
			// we need to get a perfectly elastic collision
			yPos = rad;
			yVel = Math.abs(yVel);
		} else if (yPos <= rad + 1) {
			yPos = rad;
		} else {
			yVel = (int) (yVel - gravity * dt());  // velocity is reduced by gravity if not touching the ground
		}
		yPos += yVel;

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
			yVel = 0;
		}
	}

	@Override
	public void render() {

		super.render();

		/*int sampleRate = 4;
		float alphaScale = 0.5f;
		float radStep = 1f;

		float tr = rad;
		float a = 1;

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		for (int i = 0; i < sampleRate; i++) {
			a *= alphaScale;
			tr += radStep;
			renderer.setColor(col[0], col[1], col[2], a);
			renderer.circle(xPos, yPos, tr);
			// System.out.printf("a %f tr %f", a, tr);
		}
		renderer.end();*/
	}
}
