package neil1648.cs360.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import neil1648.cs360.ui.screen.RAInputScreen;
import neil1648.cs360.ui.util.Assets;

public class RAInterpreter extends Game {
	
	@Override
	public void create () {
		new Assets();
		this.setScreen(new RAInputScreen());
	}
	
	@Override
	public void render () {
		this.screen.render(Gdx.graphics.getDeltaTime());
	}
	
}
