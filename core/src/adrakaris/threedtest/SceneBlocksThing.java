package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SceneBlocksThing implements ApplicationListener {
    public PerspectiveCamera camera;
    public CameraInputController camController;

    public ModelBatch modelBatch;
    public AssetManager assetManager;
    public boolean loading;
    public Environment environment;
    public List<ModelInstance> instances = new ArrayList<>();
    public List<ModelInstance> blocks = new ArrayList<>();
    public List<ModelInstance> invaders = new ArrayList<>();
    public ModelInstance ship;
    public ModelInstance space;


    @Override
    public void create() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(.8f, .8f, 8f, -1f, -.8f, -.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 7, 10);
        camera.lookAt(0,0,0);
        camera.near = 1;
        camera.far = 300;
        camera.update();

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

        assetManager = new AssetManager();
        assetManager.load("space/ship.obj", Model.class);
        assetManager.load("space/block.obj", Model.class);
        assetManager.load("space/invader.obj", Model.class);
        assetManager.load("space/spacesphere.obj", Model.class);

        loading = true;
    }

    private void doLoading() {
        ship = new ModelInstance(assetManager.get("space/ship.obj", Model.class));
        ship.transform.setToRotation(Vector3.Y, 180).setTranslation(0,0,6);
        instances.add(ship);

        Model blockModel = assetManager.get("space/block.obj", Model.class);
        for (float x = -5; x <= 5; x += 2) {
            ModelInstance block = new ModelInstance(blockModel);
            block.transform.setToTranslation(x, 0, 3);
            instances.add(block);
            blocks.add(block);
        }
        Model invaderModel = assetManager.get("space/invader.obj", Model.class);
        for (float x = -5; x <= 5; x += 2) {
            for (float z = -8; z <= 0; z += 2) {
                ModelInstance invader = new ModelInstance(invaderModel);
                invader.transform.setToTranslation(x,0,z);
                instances.add(invader);
                invaders.add(invader);
            }
        }
        space = new ModelInstance(assetManager.get("space/spacesphere.obj", Model.class));
        // not added to instances because we do not want to have lighting on this "skybox"

        loading = false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        if (loading && assetManager.update()) {
            doLoading();
        }
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        if (space != null) modelBatch.render(space);
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
        assetManager.dispose();
    }
}
