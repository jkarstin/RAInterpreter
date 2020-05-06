package neil1648.cs360.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public static Skin skin;
	
	public Assets() {
		Assets.skin = new Skin();
		FileHandle fileHandle = Gdx.files.internal("raiskin.json");
		FileHandle atlasFile = fileHandle.sibling("raiskin.atlas");
		if (atlasFile.exists()) Assets.skin.addRegions(new TextureAtlas(atlasFile));
		Assets.skin.load(fileHandle);
	}

}
