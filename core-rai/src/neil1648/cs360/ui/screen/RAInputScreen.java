package neil1648.cs360.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import neil1648.cs360.ui.util.Assets;

public class RAInputScreen extends BaseScreen {
	
	private TextButton projButton;
	private TextButton slctButton;
	private TextButton aggrButton;
	private TextButton rnamButton;
	private TextButton joinButton;
	
	@Override
	public void initialize() {
		this.projButton = new TextButton("PROJ", Assets.skin);
		this.slctButton = new TextButton("SLCT", Assets.skin);
		this.aggrButton = new TextButton("AGGR", Assets.skin);
		this.rnamButton = new TextButton("RNAM", Assets.skin);
		this.joinButton = new TextButton("JOIN", Assets.skin);
		
		this.projButton.setPosition(20f,  20f);
		this.slctButton.setPosition(20f, 100f);
		this.aggrButton.setPosition(20f, 180f);
		this.rnamButton.setPosition(20f, 260f);
		this.joinButton.setPosition(20f, 340f);
		
		this.projButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("PROJ button clicked");
			}
			
		});
		this.slctButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("SLCT button clicked");
			}
			
		});
		this.aggrButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("AGGR button clicked");
			}
			
		});
		this.rnamButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("RNAM button clicked");
			}
			
		});
		this.joinButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("JOIN button clicked");
			}
			
		});
		
		this.stage.addActor(this.projButton);
		this.stage.addActor(this.slctButton);
		this.stage.addActor(this.aggrButton);
		this.stage.addActor(this.rnamButton);
		this.stage.addActor(this.joinButton);
		
		Gdx.input.setInputProcessor(this.stage);
		
		//RAParser rap = new RAParser();
		//rap.run();
	}

	@Override
	public void update(float dt) {
		
	}
	
}
