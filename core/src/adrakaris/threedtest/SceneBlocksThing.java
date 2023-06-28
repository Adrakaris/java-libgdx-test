package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class SceneBlocksThing implements ApplicationListener {
    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;  // sphere bounding box

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject(Model model, String rootNode, boolean mergeTransform) {
            super(model, rootNode, mergeTransform);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }


    public PerspectiveCamera camera;
    public CameraInputController camController;

    public ModelBatch modelBatch;
    public AssetManager assetManager;
    public Array<GameObject> instances = new Array<>();
    public Environment environment;
    public boolean loading;

    public Array<GameObject> blocks = new Array<>();
    public Array<GameObject> invaders = new Array<>();
    public GameObject ship;
    public GameObject space;

    public Stage stage;
    public Label label;
    public BitmapFont font;
    public StringBuilder sb;


    @Override
    public void create() {
        // create UI
        stage = new Stage();
        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        sb = new StringBuilder();

        // create scene

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
        // load the entire scene
        // by converting from an exported fbx model we can simultaneously import multiple objects
        assetManager.load("space/invaders.g3db", Model.class);
        // this makes loading more efficient
        loading = true;
    }

    private void doLoading() {
        Model model = assetManager.get("space/invaders.g3db", Model.class);
        ship = new GameObject(model, "ship", true);  // bruh moment
        ship.transform.setToRotation(Vector3.Y, 180).setTranslation(0,0,6);
        instances.add(ship);

        for (float x = -5; x <= 5; x += 2) {
            GameObject block = new GameObject(model, "block", true);
            block.transform.setToTranslation(x, 0, 3);
            instances.add(block);
            blocks.add(block);
        }

        for (float x = -5; x <= 5; x += 2) {
            for (float z = -8; z <= 0; z += 2) {
                GameObject invader = new GameObject(model, "invader", true);
                invader.transform.setToTranslation(x,0,z);
                instances.add(invader);
                invaders.add(invader);
            }
        }
        space = new GameObject(model, "space", true);
        // not added to instances because we do not want to have lighting on this "skybox"

        loading = false;
    }


    private int visibleCount;

    @Override
    public void render() {
        if (loading && assetManager.update()) {
            doLoading();
        }
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        visibleCount = 0;
        for (final GameObject instance : instances) {
            if (isVisible(camera, instance)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }

        if (space != null) modelBatch.render(space);
        modelBatch.end();

        // UI
        sb.setLength(0);
        sb.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        sb.append(" Visible: ").append(visibleCount);
        label.setText(sb);
        stage.draw();
    }

    private Vector3 position = new Vector3();
    private boolean isVisible(final Camera camera, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return camera.frustum.sphereInFrustum(position, instance.radius);
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
