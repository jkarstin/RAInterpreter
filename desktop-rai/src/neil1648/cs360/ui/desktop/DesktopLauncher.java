package neil1648.cs360.ui.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import neil1648.cs360.ui.RAInterpreter;
import neil1648.cs360.ui.util.MetaData;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width  = (int)MetaData.VIRTUAL_WIDTH;
		config.height = (int)MetaData.VIRTUAL_HEIGHT;
		new LwjglApplication(new RAInterpreter(), config);
	}
}
