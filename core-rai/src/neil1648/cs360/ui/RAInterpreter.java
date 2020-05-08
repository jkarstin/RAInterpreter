/***************************************
 * RAInterpreter.java
 * 
 * TODO: 	Integrate Aggregation
 * 			Integrate Join
 * 			Integrate Rename
 * 			Write to file
 * 			Symbol buttons on GUI
 * 			Integrate Cross
 * 			Integrate Difference
 * 			Integrate Union
 * 			Integrate Intersection
 * 
 * J Karstin Neill    05.07.2020
 ***************************************/

package neil1648.cs360.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import neil1648.cs360.ui.screen.RAInputScreen;
import neil1648.cs360.ui.util.Assets;
import neil1648.cs360.ui.util.Debug;
import neil1648.cs360.ui.util.Debug.DEBUG_MODE;

public class RAInterpreter extends Game {
	
	@Override
	public void create () {
		Debug.setMode(DEBUG_MODE.ON_VERBOSE);
		new Assets();
		this.setScreen(new RAInputScreen());
	}
	
	@Override
	public void render () {
		this.screen.render(Gdx.graphics.getDeltaTime());
	}
	
}
