package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class InteractionSpaceScene extends InputAdapter implements ApplicationListener {
    // extend inputadapter to allow input control
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

    private int visibleCount;
    private Vector3 position = new Vector3();

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
        sb.append(" Selected: ").append(selected);
        label.setText(sb);
        stage.draw();
    }

    private boolean isVisible(final Camera camera, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return camera.frustum.sphereInFrustum(position, instance.radius);
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
        float distance = -1;  // distance from that object to camera
        // need to check for each object colliding with the ray pick the closest object and do that.

        // I don't particulalry like how he does that with index tracking vs actual object reference tracking but
        /// there we go
        for (int i = 0; i < instances.size; i++) {
            final GameObject instance = instances.get(i);

            instance.transform.getTranslation(position);
            position.add(instance.center);


            // get the closest obstacle by distance to its centre
            final float len = ray.direction.dot(
                    position.x - ray.origin.x,
                    position.y - ray.origin.y,
                    position.z - ray.origin.z
            );
            if (len < 0) { continue; }

            float distSq = position.dst2(
                    ray.origin.x + ray.direction.x * len,
                    ray.origin.y + ray.direction.y * len,
                    ray.origin.z + ray.direction.z * len
            );
            if (distance >= 0 && distSq > distance) {
                continue;
            }
            if (distSq <= instance.radius * instance.radius) {
                result = i;
                distance = distSq;
            }

            //region doing the same thing using circle bounding box
//            float dist2 = ray.origin.dst2(position);  // use squared distance (since only need comparison)
//            if (distance >= 0 && dist2 > distance) {
//                continue;
//            }
//            if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) {
//                result = i;
//                distance = dist2;
//            }
            //endregion
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
