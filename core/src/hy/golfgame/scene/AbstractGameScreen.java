package hy.golfgame.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import hy.golfgame.GolfGame;

/**
 * Screen but with common params that every screen will need
 *
 * That is, the asset manager, sprite batch, and model batch from the main game, as well as a backreference
 * to the game itself.
 *
 * Since we can access these from the main game tho we don't need to explictily pass them
 */
public abstract class AbstractGameScreen implements Screen {

    protected final GolfGame mainGame;

    protected final AssetManager assetManager;
    protected final SpriteBatch spriteBatch;
    protected final ModelBatch modelBatch;

    protected boolean doneLoading = false;

    public AbstractGameScreen(GolfGame mainGame) {
        this.mainGame = mainGame;
        this.assetManager = mainGame.getAssetManager();
        this.spriteBatch = mainGame.getSpriteBatch();
        this.modelBatch = mainGame.getModelBatch();
    }
}
