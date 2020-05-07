package neil1648.cs360.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import neil1648.cs360.ui.ra.RAParser;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.sql.SQLQuery;
import neil1648.cs360.ui.util.Assets;
import neil1648.cs360.ui.util.Debug;
import neil1648.cs360.ui.util.MetaData;

public class RAInputScreen extends BaseScreen {
	
	private TextButton projButton;
	private TextButton slctButton;
	private TextButton aggrButton;
	private TextButton rnamButton;
	private TextButton joinButton;
	
	private TextButton generateButton;
	
	private Label displayLabel;
	
	private void setupWidgets() {
		this.projButton = new TextButton("PROJ", Assets.skin);
		this.slctButton = new TextButton("SLCT", Assets.skin);
		this.aggrButton = new TextButton("AGGR", Assets.skin);
		this.rnamButton = new TextButton("RNAM", Assets.skin);
		this.joinButton = new TextButton("JOIN", Assets.skin);
		
		this.generateButton = new TextButton("GENERATE SQL", Assets.skin);
		
		this.displayLabel = new Label("RA Expression Display", Assets.skin);
		
		this.stage.addActor(this.projButton);
		this.stage.addActor(this.slctButton);
		this.stage.addActor(this.aggrButton);
		this.stage.addActor(this.rnamButton);
		this.stage.addActor(this.joinButton);
		
		this.stage.addActor(this.generateButton);
		
		this.stage.addActor(this.displayLabel);
	}
	
	private void configureWidgets() {
		this.projButton.setPosition(20f,  20f);
		this.slctButton.setPosition(20f, 100f);
		this.aggrButton.setPosition(20f, 180f);
		this.rnamButton.setPosition(20f, 260f);
		this.joinButton.setPosition(20f, 340f);
		
		this.generateButton.setPosition(MetaData.VIRTUAL_WIDTH-20f, 20f, Align.bottomRight);
		
		this.displayLabel.setSize(MetaData.VIRTUAL_WIDTH-200f, MetaData.VIRTUAL_HEIGHT-200f);
		this.displayLabel.setPosition(MetaData.VIRTUAL_WIDTH/2f, MetaData.VIRTUAL_HEIGHT/2f, Align.center);
		this.displayLabel.setAlignment(Align.topLeft);
		this.displayLabel.setWrap(true);
	}
	
	private void setListeners() {
		this.projButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("PROJ button clicked");
				write("PROJ");
			}
			
		});
		this.slctButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("SLCT button clicked");
				write("SLCT");
			}
			
		});
		this.aggrButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("AGGR button clicked");
				write("AGGR");
			}
			
		});
		this.rnamButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("RNAM button clicked");
				write("RNAM");
			}
			
		});
		this.joinButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("JOIN button clicked");
				write("JOIN");
			}
			
		});
		
		this.generateButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("RA Expression confirmed. Generating equivalent MySQL...");
			}
			
		});
	}
	
	private void write(String token) {
		Debug.logv("Writing to displayLabel... " + token);
		this.displayLabel.setText(displayLabel.getText() + " " + token);
	}
	
	@Override
	public void initialize() {
		Gdx.input.setInputProcessor(this.stage);
		
		this.setupWidgets();
		this.configureWidgets();
		this.setListeners();
		
		RAParser rap = new RAParser();
		RAExpression rae = rap.run();
		Debug.log(rae);
		
		SQLQuery sqlq = new SQLQuery();
		sqlq.addSelection("col0");
		sqlq.addSelection("col1");
		sqlq.setFromTarget("tbl0");
		sqlq.addCondition("cnd0");
		Debug.log(sqlq);
	}

	@Override
	public void update(float dt) { }
	
}
