package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreeDGame extends ApplicationAdapter {

	public Random random = new Random(42);

	public PerspectiveCamera camera;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Environment environment;

	public AssetManager assets;
	public List<ModelInstance> instances = new ArrayList<>();
	public boolean loading;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		// evn
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1));
		environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1, -.8f, -.2f));

		// setup camera
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(5, 5, 5);
		camera.lookAt(0,0,0);
		camera.near = 1;
		camera.far = 300;
		camera.update();

		camController = new CameraInputController(camera);
		Gdx.input.setInputProcessor(camController);

		// custom obj (aint fully support)
		assets = new AssetManager();
		assets.load("tree-1.g3db", Model.class);
		loading = true;
	}

	private void doLoading() {
		Model tree = assets.get("tree-1.g3db", Model.class);
		for (int i = 0; i < 20; i++) {
			ModelInstance treeInstance = new ModelInstance(tree);
			Vector3 newPos = new Vector3(random.nextFloat(-10, 10), 0, random.nextFloat(-10, 10));
			treeInstance.transform
					.setToRotation(0, 1, 0, random.nextFloat(-180, 180))
					.setTranslation(newPos);
			instances.add(treeInstance);
		}
		loading = false;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		if (loading && assets.update()) {
			doLoading();
		}

		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}
}
