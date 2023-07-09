package hy.golfgame.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import hy.golfgame.GolfGame;

public class MainGameScreen extends AbstractGameScreen {

    private Texture img;

    public MainGameScreen(GolfGame mainGame) {
        super(mainGame);

    }

    @Override
    public void show() {
        assetManager.load("badlogic.jpg", Texture.class);
        assetManager.finishLoading();

        img = assetManager.get("badlogic.jpg");

        doneLoading = true;
    }

    // render seesm to be called earlier than show on a screen which leads to issues when textures aren't loaded
    @Override
    public void render(float delta) {
        if (!doneLoading) return;

        Gdx.gl.glClearColor(1f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(img, 0, 0);
        spriteBatch.end();
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
