package hu.yijun.gdxtest.camerathing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CameraTest extends ApplicationAdapter {

	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;

	float botLeftX = 0;
	float botLeftY = 0;
	float rectWidth;
	float rectHeight;
	float moveSpeed = 100;
	// expanded code:
	float rotateSpeed = 20;
	float zoomSpeed = 1;

	@Override
	public void create() {
		// creates a new camera with orthographic projection, that has the width and height of the
		// viewpoint - set currently as 720 * 1080;
		// camera = new OrthographicCamera(width(), height());
		// we can also use WORLD coordinates - coordinates independent of the viewport in the first place. As follows;
		camera = new OrthographicCamera(160, 90);
		// this camera has a 160:90, but the units are WORLD COORDINATE units, which are then independent of viewport coords

		// setting the position of the centre of the camera to the centre of the scene
		// camera.position.set(width()/2, height()/2, 0);  // in viewport dependent
		camera.position.set(0,0,0);  // world coordinates
		// calculate everything camera requires with what we've given it
		camera.update();

		shapeRenderer = new ShapeRenderer();
		// rectWidth = width()/2;
		// rectHeight = height()/2;
		rectWidth = 80;
		rectHeight = 45;
	}

	private void update() {
		// move camera left and right
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, moveSpeed * delta());
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -moveSpeed * delta());
		}

		// move camera up and down
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-moveSpeed * delta(), 0);
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(moveSpeed * delta(), 0);
		}

		// zoom camera
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.zoom -= zoomSpeed * delta();
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.zoom += zoomSpeed * delta();
		}

		// rotate camera
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.rotate(-rotateSpeed * delta());
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.rotate(rotateSpeed * delta());
		}
	}

	@Override
	public void render() {

		update();

		Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		// tells shapeRenderer to use the camera to draw anything.
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0.8f,0,0,1);  // [▖]
		shapeRenderer.rect(-rectWidth, -rectHeight, rectWidth, rectHeight);
		// shapeRenderer.rect(botLeftX, botLeftY, rectWidth, rectHeight);
		shapeRenderer.setColor(0,0.8f,0,1);  // [▗]
		shapeRenderer.rect(0, -rectHeight, rectWidth, rectHeight);
		// shapeRenderer.rect(botLeftX+rectWidth, botLeftY, rectWidth, rectHeight);
		shapeRenderer.setColor(0,0,0.8f,1);  // [▝]
		shapeRenderer.rect(0, 0, rectWidth, rectHeight);
		// shapeRenderer.rect(botLeftX+rectWidth,botLeftY+rectHeight, rectWidth, rectHeight);
		shapeRenderer.setColor(0.8f,0.8f,0,1);  // [▘]
		shapeRenderer.rect(-rectWidth, 0, rectWidth, rectHeight);
		// shapeRenderer.rect(botLeftX, botLeftY+rectHeight, rectWidth, rectHeight);
		shapeRenderer.end();

		// thus the camera moves around, whilst the blocks do not
		// useful if you have a scrolling thing
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	public float width() { return Gdx.graphics.getWidth(); }

	public float height() { return Gdx.graphics.getHeight(); }

	public float delta() { return Gdx.graphics.getDeltaTime(); }
}
