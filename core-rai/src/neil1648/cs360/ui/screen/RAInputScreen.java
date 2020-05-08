package neil1648.cs360.ui.screen;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import neil1648.cs360.ui.ra.RAParser;
import neil1648.cs360.ui.ra.RAParser.STATUS;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.sql.RAE2SQLQ;
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
	
	private TextField valueField;
	private TextButton valueButton;
	private TextButton targetButton;
	
	private TextButton generateButton;
	private TextButton resetButton;
	
	private Label displayLabel;
	
	private RAParser rap;
	private ArrayList<RAExpression> raeStack;
	private String tokens;
	private boolean gettingArg0;
	
	private SQLQuery sqlq;
	
	private void setupParser() {
		//TODO: Integrate GUI with RAParser to allow GUI to feed RAParser one token at a time and be able to change visual state based on RAParser's current state
		
		this.rap = new RAParser();
	}
	
	private void setupWidgets() {
		this.projButton = new TextButton("PROJ", Assets.skin, "toggle");
		this.slctButton = new TextButton("SLCT", Assets.skin, "toggle");
		this.aggrButton = new TextButton("AGGR", Assets.skin, "toggle");
		this.rnamButton = new TextButton("RNAM", Assets.skin, "toggle");
		this.joinButton = new TextButton("JOIN", Assets.skin, "toggle");
		
		this.valueField = new TextField("", Assets.skin);
		this.valueField.setMessageText("value");
		this.valueButton = new TextButton("INSERT VALUE", Assets.skin);
		this.targetButton = new TextButton("TRGT", Assets.skin);
		
		this.generateButton = new TextButton("GENERATE SQL", Assets.skin);
		this.resetButton = new TextButton("RESET", Assets.skin);
		
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
		this.stage.addActor(this.resetButton);
		
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
		this.resetButton.setPosition(MetaData.VIRTUAL_WIDTH-200f, 20f, Align.bottomRight);
		
		this.displayLabel.setSize(MetaData.VIRTUAL_WIDTH-200f, MetaData.VIRTUAL_HEIGHT-200f);
		this.displayLabel.setPosition(MetaData.VIRTUAL_WIDTH/2f, MetaData.VIRTUAL_HEIGHT/2f, Align.center);
		this.displayLabel.setAlignment(Align.topLeft);
		this.displayLabel.setWrap(true);
	}
	
	private void setListeners() {
		this.projButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!projButton.isDisabled()) {
					Debug.log("PROJ button clicked");
					uncheckTargetingButtons();
					projButton.setChecked(true);
					valueField.setDisabled(false);
					valueButton.setDisabled(false);
					gettingArg0 = true;
					tokens = "PROJ ";
				}
			}
				
		});
		this.slctButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!slctButton.isDisabled()) {
					Debug.log("SLCT button clicked");
					uncheckTargetingButtons();
					slctButton.setChecked(true);
					valueField.setDisabled(false);
					valueButton.setDisabled(false);
					gettingArg0 = true;
					tokens = "SLCT ";
				}
			}
			
		});
		this.aggrButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!aggrButton.isDisabled()) {
					Debug.log("AGGR button clicked");
					uncheckTargetingButtons();
					aggrButton.setChecked(true);
					valueField.setDisabled(false);
					valueButton.setDisabled(false);
					gettingArg0 = true;
					tokens = "AGGR ";
				}
			}
			
		});
		this.rnamButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!rnamButton.isDisabled()) {
					Debug.log("RNAM button clicked");
					uncheckTargetingButtons();
					rnamButton.setChecked(true);
					valueField.setDisabled(false);
					valueButton.setDisabled(false);
					gettingArg0 = true;
					tokens = "RNAM ";
				}
			}
			
		});
		this.joinButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!joinButton.isDisabled()) {
					Debug.log("JOIN button clicked");
					if (!gettingArg0) {
						uncheckTargetingButtons();
						joinButton.setChecked(true);
						valueField.setDisabled(false);
						valueButton.setDisabled(false);
						gettingArg0 = true;
						tokens = "JOIN ";
					}
				}
			}
			
		});
		
		this.valueField.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!valueField.isDisabled()) {
					stage.setKeyboardFocus(valueField);
					valueField.setText("");
				}
			}
			
		});
		this.valueField.addListener(new InputListener() {
			
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (!valueField.isDisabled()) {
					switch (keycode) {
					case Input.Keys.ENTER:
						Debug.log("ENTER key pressed");
						gettingArg0 = false;
						tokens += valueField.getText();
						uncheckTargetingButtons();
						writeTokens();
						valueField.setText("value");
						updateTargetButton();
						return true;
					default:
						break;
					};
				}
				
				return false;
			}
			
		});
		this.valueButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!valueButton.isDisabled()) {
					Debug.log("VALUE button clicked");
					gettingArg0 = false;
					tokens += valueField.getText();
					uncheckTargetingButtons();
					writeTokens();
					valueField.setText("value");
					updateTargetButton();
				}
			}
			
		});
		this.targetButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!targetButton.isDisabled()) {
					Debug.log("TARGET button clicked");
					if (!gettingArg0) {
						tokens += targetButton.getText();
						writeTokens();
						targetButton.setText("TRGT");
						targetButton.setDisabled(true);
						enableTargetingButtons();
						generateButton.setDisabled(false);
					}
					else {
						Debug.log("Invalid use of TRGT button");
					}
				}
			}
			
		});
		
		this.generateButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!generateButton.isDisabled()) {
					Debug.log("RA Expression confirmed. Generating equivalent MySQL...");
					write("END");
					disableTargetingButtons();
					valueField.setDisabled(true);
					valueButton.setDisabled(true);
					generateButton.setDisabled(true);
					resetButton.setDisabled(false);
				}
			}
			
		});
		this.resetButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!resetButton.isDisabled()) {
					Debug.log("Resetting interpreter...");
					resetToDefaultStates();
				}
			}
			
		});
	}
	
	private void resetToDefaultStates() {
		this.raeStack = null;
		this.tokens = "";
		this.gettingArg0 = false;
		this.sqlq = null;
		this.rap.resetParser();
		this.displayLabel.setText("");
		this.enableTargetingButtons();
		this.valueField.setDisabled(true);
		this.valueButton.setDisabled(true);
		this.targetButton.setDisabled(true);
		this.generateButton.setDisabled(true);
		this.resetButton.setDisabled(true);
	}
	
	private void disableTargetingButtons() {
		this.projButton.setDisabled(true);
		this.slctButton.setDisabled(true);
		this.aggrButton.setDisabled(true);
		this.rnamButton.setDisabled(true);
		this.joinButton.setDisabled(true);
	}
	
	private void enableTargetingButtons() {
		this.projButton.setDisabled(false);
		this.slctButton.setDisabled(false);
		this.aggrButton.setDisabled(false);
		this.rnamButton.setDisabled(false);
		this.joinButton.setDisabled(false);
	}
	
	private void uncheckTargetingButtons() {
		this.projButton.setChecked(false);
		this.slctButton.setChecked(false);
		this.aggrButton.setChecked(false);
		this.rnamButton.setChecked(false);
		this.joinButton.setChecked(false);
	}
	
	private void updateTargetButton() {
		Debug.logv("Retrieving most recent expression type...");
		RAExpression.EXPR_TYPE exprType = this.rap.mostRecentExpressionType();
		
		Debug.logv("Expression type: " + exprType);
		
		if (exprType != null) {
			switch (exprType) {
			case JOIN:
				this.targetButton.setText("WITH");
				this.targetButton.setDisabled(false);
				return;
			case RNAM:
				this.targetButton.setText("ONTO");
				break;
			default:
			case PROJ:
			case SLCT:
			case AGGR:
				this.targetButton.setText("FROM");
				break;
			};
			this.targetButton.setDisabled(false);
			this.disableTargetingButtons();
		}
		else {
			this.targetButton.setText("TRGT");
			this.targetButton.setDisabled(true);
		}
	}
	
	private void writeTokens() {
		this.write(this.tokens);
		this.tokens = "";
	}
	
	private void write(String token) {
		Debug.logv("Writing to displayLabel... " + token);
		this.displayLabel.setText(displayLabel.getText() + " " + token);
		Debug.logv("Passing to RAParser... " + token);
		this.raeStack = this.rap.parse(token);
		Debug.logv("Returned Expression Stack: " + raeStack);
		
		if (this.rap.exitedWith() == STATUS.END && this.raeStack.size() > 0) {
			this.sqlq = RAE2SQLQ.translate(this.raeStack.get(0));
			Debug.log("Generated SQLQ\n: " + this.sqlq);
			this.displayLabel.setText(this.displayLabel.getText() + "\n\nGenerated SQL:\n" + this.sqlq.toString());
		}
	}
	
	@Override
	public void initialize() {
		Gdx.input.setInputProcessor(this.stage);
		
		/*/
		RAParser rap0 = new RAParser();
		Scanner sc = new Scanner(System.in);
		ArrayList<RAExpression> exprStack = new ArrayList<RAExpression>();
		do {
			exprStack = rap0.parse(sc.nextLine());
		} while (rap0.exitedWith() != STATUS.END);
		sc.close();
		/**/
		
		this.setupParser();
		this.setupWidgets();
		this.configureWidgets();
		this.setListeners();
		
		this.resetToDefaultStates();
	}

	@Override
	public void update(float dt) { }
	
}
