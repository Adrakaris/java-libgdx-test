package hu.yijun.circledrawer;

import java.awt.*;

public class TextBox {

	private final MainGame game;

	private final float x;
	private final float y;
	private final float w;
	private final float h;
	private final float fontOffset;
	private final Color deselect;
	private final Color select;
	private final Color fontCol;

	private String text;
	private boolean active;

	/**
	 * Constructs a text input box given the following parameters. This doesn't need a stage or anything to run
	 *
	 * // TODO: spearate out needed parts from the game in params to make it more modularable
	 *
	 * @param game Main game class - should have a SpriteBatch and BitmapFont already
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param initText
	 * @param fontOffset
	 * @param deselect
	 * @param select
	 * @param fontCol
	 */
	public TextBox(MainGame game, float x, float y, float w, float h,
				   String initText, float fontOffset, Color deselect, Color select, Color fontCol) {
		this.game = game;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.fontOffset = fontOffset;
		this.deselect = deselect;
		this.select = select;
		this.fontCol = fontCol;
		this.text = initText;

		active = false;
	}

	public TextBox(MainGame game, float x, float y, float w, float h) {
		this(game, x, y, w, h,
				"", 3,
				new Color(0.92f, 0.92f, 0.92f, 1f),
				new Color(0.84f, 0.84f, 0.84f, 1),
				new Color(0,0,0,1));
	}
}
