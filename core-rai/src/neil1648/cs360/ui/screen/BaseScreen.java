package neil1648.cs360.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen implements Screen {
	
	protected Stage stage;
	
	public abstract void initialize();
	public abstract void update(float dt);
	
	public BaseScreen() {
		this.stage = new Stage();
		this.initialize();
	}

	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		this.stage.act();
		this.update(delta);
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() { }
	
//	@Override
//	public boolean keyDown(int keycode) {
//		return false;
//	}
//	@Override
//	public boolean keyUp(int keycode) {
//		return false;
//	}
//	@Override
//	public boolean keyTyped(char character) {
//		return false;
//	}
//	@Override
//	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		return false;
//	}
//	@Override
//	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//		return false;
//	}
//	@Override
//	public boolean touchDragged(int screenX, int screenY, int pointer) {
//		return false;
//	}
//	@Override
//	public boolean mouseMoved(int screenX, int screenY) {
//		return false;
//	}
//	@Override
//	public boolean scrolled(int amount) {
//		return false;
//	}

}
