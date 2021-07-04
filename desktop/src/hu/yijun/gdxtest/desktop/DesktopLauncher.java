package hu.yijun.gdxtest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.yijun.gdxtest.balls.GdxTest;
import hu.yijun.gdxtest.camerathing.CameraTest;
import hu.yijun.gdxtest.multiscreen.MultiGame;
import hu.yijun.gdxtest.slider.Colours;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "TestGDX Game";
		config.useGL30 = true;
		config.height = 720;
		config.width = 1080;
		config.resizable = false;  // TODO: could consider

		System.err.println("NOTE: illegal reflective access warnings are due to a newer (i.e. 11) Java version being used.");
		System.err.println("It works on 11 but please don't use a version higher than 11.");
		new LwjglApplication(new Colours(), config);
	}
}
