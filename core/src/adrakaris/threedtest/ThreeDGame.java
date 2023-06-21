package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;

public class ThreeDGame extends ApplicationAdapter {

	public PerspectiveCamera camera;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Environment environment;

	public Model model;
	public ModelInstance instance;

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
		ModelLoader<ObjLoader.ObjLoaderParameters> loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal("tree-1.obj"));

		instance = new ModelInstance(model);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(instance, environment);
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
		model.dispose();
	}
}
