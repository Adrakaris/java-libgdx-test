package hu.yijun.circledrawer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.yijun.circledrawer.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Circle Drawer";
		config.useGL30 = true;
		config.height = 540;
		config.width = 960;
		config.resizable = false;
		config.samples = 4;
		new LwjglApplication(new MainGame(), config);
	}
}
