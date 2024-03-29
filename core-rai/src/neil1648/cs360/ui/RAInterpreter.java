/***************************************
 * RAInterpreter.java
 * 
 * TODO: 	Write to file
 * 
 * J Karstin Neill    05.07.2020
 ***************************************/

package neil1648.cs360.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import neil1648.cs360.ui.screen.RAInputScreen;
import neil1648.cs360.ui.util.Assets;

public class RAInterpreter extends Game {
	
	@Override
	public void create () {
//		Debug.setMode(DEBUG_MODE.ON);
//		Debug.setMode(DEBUG_MODE.ON_VERBOSE);
		new Assets();
		this.setScreen(new RAInputScreen());
	}
	
	@Override
	public void render () {
		this.screen.render(Gdx.graphics.getDeltaTime());
	}
	
}
