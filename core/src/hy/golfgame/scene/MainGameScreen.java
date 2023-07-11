package hy.golfgame.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import hy.golfgame.GolfGame;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public class MainGameScreen extends AbstractGameScreen {

    private final Camera camera;
    private final CameraInputController camController;

    private final SceneManager sceneManager;  // scene3d manager because gltf seems only to work with scene3d
    private final Scene scene;

    public MainGameScreen(GolfGame mainGame) {
        super(mainGame);

        // asset queuing
        // TODO

        // scene3d work
        // TODO

        // camera work
        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camController = new CameraInputController(camera);

        camera.position.set(0, 5, 10);
        camera.lookAt(0, 0, 0);
        camera.near = 1;
        camera.far = 350;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(camController);


        assetManager.finishLoading();

        doneLoading = true;
    }

    public void update(float delta) {
        camController.update();
    }

    @Override
    public void render(float delta) {
        if (!doneLoading) return;

        update(delta);

        Gdx.gl.glClearColor(.4f, .4f, .4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        assetManager.clear();
    }

    @Override
    public void dispose() {
    }
}
