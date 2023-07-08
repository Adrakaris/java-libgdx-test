package adrakaris.threedtest;

import adrakaris.threedtest.fings.GameObject;
import adrakaris.threedtest.fings.collisionshape.Shape;
import adrakaris.threedtest.fings.collisionshape.SphereShape;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class InteractionSpaceScene extends InputAdapter implements ApplicationListener {
    // extend inputadapter to allow input control


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

    protected Shape blockShape;
    protected Shape invaderShape;
    protected Shape shipShape;

    public Stage stage;
    public Label label;
    public BitmapFont font;
    public StringBuilder sb;

    private int visibleCount;
    private Vector3 position = new Vector3();

    private BoundingBox bounds = new BoundingBox();

    // selected: INDEX of model instance that is selected
    // selecting: INDEX of model instance that is currently being selected
    private int selected = -1, selecting = -1;
    // visual feedback selection material (orange highlight)
    private Material selectionMaterial;
    private Material originalMaterial;

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
        // input multiplexing should be a self explanatory
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));

        assetManager = new AssetManager();
        assetManager.load("space/invaders.g3db", Model.class);
        loading = true;

        selectionMaterial = new Material();
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
        originalMaterial = new Material();
    }

    private void doLoading() {
        Model model = assetManager.get("space/invaders.g3db", Model.class);

        ship = new GameObject(model, "ship", true);  // bruh moment
        ship.transform.setToRotation(Vector3.Y, 180).setTranslation(0,0,6);
        shipShape = new SphereShape(ship.calculateBoundingBox(bounds));
        ship.shape = shipShape;
        instances.add(ship);

        for (float x = -5; x <= 5; x += 2) {
            GameObject block = new GameObject(model, "block", true);
            block.transform.setToTranslation(x, 0, 3);
            if (blockShape == null) {
                blockShape = new SphereShape(block.calculateBoundingBox(bounds));
            }
            block.shape = blockShape;
            instances.add(block);
            blocks.add(block);
        }

        for (float x = -5; x <= 5; x += 2) {
            for (float z = -8; z <= 0; z += 2) {
                GameObject invader = new GameObject(model, "invader", true);
                invader.transform.setToTranslation(x,0,z);
                if (invaderShape == null) {
                    invaderShape = new SphereShape(invader.calculateBoundingBox(bounds));
                }
                invader.shape = invaderShape;
                instances.add(invader);
                invaders.add(invader);
            }
        }
        space = new GameObject(model, "space", true);
        // not added to instances because we do not want to have lighting on this "skybox"

        loading = false;
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
        visibleCount = 0;
        for (final GameObject instance : instances) {
            if (instance.isVisible(camera)) {
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
        sb.append(" Selected: ").append(selected);
        label.setText(sb);
        stage.draw();
    }

    // ===================================================================================
    // controls

    // note that because of input multiplexing when we return false on any of these overrides
    //

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selecting = getObject(screenX, screenY);
        return selecting >= 0;  // if the object being selecting is not available then we do not object it
        // and pass to cam control
    }

    private int getObject(int screenX, int screenY) {
        Ray ray = camera.getPickRay(screenX, screenY);
        int result = -1;  // current object closest to cmra
        float prevDistance = -1;  // prevDistance from that object to camera
        // need to check for each object colliding with the ray pick the closest object and do that.

        // I don't particulalry like how he does that with index tracking vs actual object reference tracking but
        /// there we go
        for (int i = 0; i < instances.size; i++) {
            final float currentDistanceSq = instances.get(i).intersects(ray);
            if (currentDistanceSq >= 0 && (prevDistance < 0 || currentDistanceSq <= prevDistance)) {
                result = i;
                prevDistance = currentDistanceSq;
            }
        }
        return result;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // return whether or not we are selecitng an object
        if (selecting < 0) {
            return false;
        }

        // move the object if selecting is selected
        if (selected == selecting) {
            Ray ray = camera.getPickRay(screenX, screenY);
            // maths is making sure this is locked to the xz plane
            final float distance = -ray.origin.y / ray.direction.y;
            position.set(ray.direction).scl(distance).add(ray.origin);
            instances.get(selected).transform.setTranslation(position);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // basically if we click down, and click up, and the object underneath our mouse is the same, then we select it
        // finalise the selection.
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY)) {
                setSelected(selecting);
            }
            selecting = -1;
            return true;
        }
        return false;
    }

    public void setSelected(int value) {
        if (selected == value) {
            return;
        }
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            mat.set(selectionMaterial);
        }
    }

    // ===================================================================================

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
