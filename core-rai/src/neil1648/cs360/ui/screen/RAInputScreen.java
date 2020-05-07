package neil1648.cs360.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import neil1648.cs360.ui.util.Assets;
import neil1648.cs360.ui.util.Debug;
import neil1648.cs360.ui.util.MetaData;

public class RAInputScreen extends BaseScreen {
	
	private TextButton projButton;
	private TextButton slctButton;
	private TextButton aggrButton;
	private TextButton rnamButton;
	private TextButton joinButton;
	
	private TextField valueField;
	private TextButton valueButton;
	private TextButton targetButton;
	
	private TextButton generateButton;
	
	private Label displayLabel;
	
	private void setupWidgets() {
		this.projButton = new TextButton("PROJ", Assets.skin);
		this.slctButton = new TextButton("SLCT", Assets.skin);
		this.aggrButton = new TextButton("AGGR", Assets.skin);
		this.rnamButton = new TextButton("RNAM", Assets.skin);
		this.joinButton = new TextButton("JOIN", Assets.skin);
		
		this.valueField = new TextField("value", Assets.skin);
		this.valueButton = new TextButton("INSERT VALUE", Assets.skin);
		this.targetButton = new TextButton("FROM", Assets.skin);
		
		this.generateButton = new TextButton("GENERATE SQL", Assets.skin);
		
		this.displayLabel = new Label("", Assets.skin);
		
		this.stage.addActor(this.projButton);
		this.stage.addActor(this.slctButton);
		this.stage.addActor(this.aggrButton);
		this.stage.addActor(this.rnamButton);
		this.stage.addActor(this.joinButton);
		
		this.stage.addActor(this.valueField);
		this.stage.addActor(this.valueButton);
		this.stage.addActor(this.targetButton);
		
		this.stage.addActor(this.generateButton);
		
		this.stage.addActor(this.displayLabel);
	}
	
	private void configureWidgets() {
		this.projButton.setPosition(20f, MetaData.VIRTUAL_HEIGHT- 20f, Align.topLeft);
		this.slctButton.setPosition(20f, MetaData.VIRTUAL_HEIGHT-100f, Align.topLeft);
		this.aggrButton.setPosition(20f, MetaData.VIRTUAL_HEIGHT-180f, Align.topLeft);
		this.rnamButton.setPosition(20f, MetaData.VIRTUAL_HEIGHT-260f, Align.topLeft);
		this.joinButton.setPosition(20f, MetaData.VIRTUAL_HEIGHT-340f, Align.topLeft);
		
		this.valueField.setSize(200f, 60f);
		this.valueField.setPosition(100f, 20f);
		this.valueButton.setPosition(320f, 20f);
		this.targetButton.setPosition(20f, 20f);
		
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
		
		this.valueField.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.setKeyboardFocus(valueField);
			}
			
		});
		this.valueButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("VALUE button clicked");
				write(valueField.getText());
				valueField.setText("value");
			}
			
		});
		this.targetButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Debug.log("TARGET button clicked");
				write("TRGT");
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
		
		//TODO: Integrate GUI with RAParser to allow GUI to feed RAParser one token at a time and be able to change visual state based on RAParser's current state
		
		/*/
		//Testing
		RAParser rap = new RAParser();
		RAExpression rae = rap.run();
		Debug.log(rae);
		
		SQLQuery sqlq = new SQLQuery();
		sqlq.addSelection("col0");
		sqlq.addSelection("col1");
		sqlq.setFromTarget("tbl0");
		sqlq.addCondition("cnd0");
		Debug.log(sqlq);
		/**/
	}

	@Override
	public void update(float dt) { }
	
}
