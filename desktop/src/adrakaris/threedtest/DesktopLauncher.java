package adrakaris.threedtest;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import adrakaris.threedtest.ThreeDGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("3DTestGame");
		// disable vsync
//		config.useVsync(false);
//		config.setForegroundFPS(0); // Setting to 0 disables foreground fps throttling
//		config.setIdleFPS(0); // Setting to 0 disables background fps throttling
		//
		new Lwjgl3Application(new BulletTest(), config);
	}
}
