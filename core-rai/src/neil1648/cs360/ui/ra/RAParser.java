package neil1648.cs360.ui.ra;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;

import neil1648.cs360.ui.ra.expr.Aggregation;
import neil1648.cs360.ui.ra.expr.Join;
import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.RAExpression.EXPR_TYPE;
import neil1648.cs360.ui.ra.expr.Rename;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;
import neil1648.cs360.ui.util.Debug;
import neil1648.cs360.ui.util.ILoggable;

public class RAParser {
	
	public static enum STATUS implements ILoggable {
		DEFAULT,
		PROJ,
		SLCT,
		RNAM,
		AGGR,
		GRUP,
		JOIN,
		JOIN_Argi,
		JOIN_Arg0,
		ASSN_Target,
		EVAL_Target,
		HOLD,
		END
	}
	
	private ArrayList<STATUS> stateStack;
	private ArrayList<RAExpression> exprStack;
	private STATUS exitState;
	
	private StringTokenizer stoker;
	
	public RAParser() {
		resetParser();
	}
	
	/*** RA EXPRESSION PARSER STATE MACHINE ENCODING ***
	 * Projection:
	 * 		PROJ col0 [... coln] FROM targ
	 * Selection:
	 * 		SLCT cnd0 [... cndn] FROM targ
	 * Rename:
	 * 		RNAM name ONTO targ
	 * Aggregation:
	 * 		AGGR act0 [... actn] [GRUP grp0 [... grpn]] FROM targ
	 * Join: (Supports very limited nested JOIN expressions. Avoid nesting JOIN expressions for now.)
	 * 		JOIN arg0 [... argn] WITH argf
	 */
	public ArrayList<RAExpression> parse(String racode) {
		initStoker(racode);
		
		STATUS parseState=STATUS.DEFAULT, tmpState=STATUS.DEFAULT;
		RAExpression currentExpr=null, targetExpr=null, tmpExpr=null;
		String token="", arg0="";
		
		do {
			parseState = peekState();
			currentExpr = peekExpr();
			
			Debug.logv("------------------------------------");
			Debug.logv("State Stack:\t\t" + stateStack);
//			Debug.logv("Parse State:\t\t" + parseState);
			Debug.logv("Expression Stack:\t" + exprStack);
//			Debug.logv("Current Expression:\t" + currentExpr);
			Debug.logv("Target Expression:\t" + targetExpr);
			
			switch (parseState) {
			
			/*** STATELESS ***/
			
			//"Outer-most" parsing state, any expression started here is "root" expression and will have an entry in the exprStack
			case DEFAULT:
				token = popToken();
				tmpState = evaluateState(token);
				//Expression started
				switch (tmpState) {
				case DEFAULT:
				case END:
				case HOLD:
					break;
				case JOIN:
					//Determine if arg0 qualifies as SimpleExpression or requires state change
					arg0 = popToken();
					tmpState = evaluateState(arg0);
					switch (tmpState) {
					case DEFAULT:
						newExpression(STATUS.JOIN, arg0);
						break;
					case END:
					case HOLD:
						break;
					default:
						popState();
						pushState(STATUS.JOIN_Arg0);
						pushState(tmpState);
						arg0 = popToken();
						newExpression(tmpState, arg0);
						break;
					};
					break;
				default:
					arg0 = popToken();
					newExpression(tmpState, arg0);
					break;
				};
				break;
			case ASSN_Target:
				token = popToken();
				tmpState = evaluateState(token);
				switch (tmpState) {
				case DEFAULT:
					//Create simple expression and set as target
					targetExpr = new SimpleExpression(token);
					Debug.logv("Assign target: " + targetExpr);
					pushState(STATUS.EVAL_Target);
					break;
				case JOIN:
					//Determine if arg0 qualifies as SimpleExpression or requires state change
					arg0 = popToken();
					tmpState = evaluateState(arg0);
					switch (tmpState) {
					case DEFAULT:
						newExpression(STATUS.JOIN, arg0);
						break;
					case END:
					case HOLD:
						break;
					default:
						popState();
						pushState(STATUS.JOIN_Arg0);
						pushState(tmpState);
						arg0 = popToken();
						newExpression(tmpState, arg0);
						break;
					};
					break;
				default:
					Debug.log("Sub-expression found! Resolving...");
					arg0 = popToken();
					newExpression(tmpState, arg0);
					break;
				case END:
				case HOLD:
					break;
				};
				break;
			//Evaluate current target state data from stack and
			//		apply target to proper state type.
			case EVAL_Target:
				popState();
				int assn = mostRecentIndex(STATUS.ASSN_Target);
				int join = mostRecentIndex(STATUS.JOIN);
				boolean evaluate = (assn > 1 && assn > join);
				Debug.logv("Index of most recent ASSNTarget: " + assn);
				Debug.logv("Proceed with EVALTarget action: " + evaluate);
				if (evaluate) {
					popState();
					tmpState = popState();
					switch (tmpState) {
					case PROJ:
						Debug.log("Projection target: " + targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.logv(currentExpr);
						targetExpr = currentExpr;
						popExpr();
						break;
					case SLCT:
						Debug.log("Selection target: " + targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.logv(currentExpr);
						targetExpr = currentExpr;
						popExpr();
						break;
					case RNAM:
						Debug.log("Rename target: " + targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.logv(currentExpr);
						targetExpr = currentExpr;
						popExpr();
						break;
					case GRUP:
						//Remove AGGRParse which is currently sitting in the stack
						popState();
					case AGGR:
						Debug.log("Aggregation target: " + targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.logv(currentExpr);
						targetExpr = currentExpr;
						popExpr();
						break;
					case JOIN:
						Debug.log("Join final argument: " + targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.logv(currentExpr);
						targetExpr = currentExpr;
						popExpr();
						break;
					default:
						Debug.logerr("Unknown target: " + targetExpr);
						break;
					};
					
					pushState(STATUS.EVAL_Target);
				}
				else pushExpr(targetExpr);
				if (assn < join && peekState() != STATUS.JOIN_Arg0) pushState(STATUS.JOIN_Argi);
				break;
			
			case END:
				this.exitState = popState();
				resetStateStack();
				break;
			case HOLD:
				this.exitState = popState();
				break;
				
			/*** JOIN ***/
				
			case JOIN:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				case "WITH":
					pushState(STATUS.ASSN_Target);
					break;
				default:
					tmpState = evaluateState(token);
					switch (tmpState) {
					case DEFAULT:
						((Join)currentExpr).addArgument(token);
						Debug.log("Join argument: " + token);
						break;
					case END:
					case HOLD:
						break;
					default:
						arg0 = popToken();
						newExpression(tmpState, arg0);
						break;
					};
					break;
				}
				break;
			case JOIN_Arg0:
				popState();
				targetExpr = popExpr();
				tmpExpr = new Join(targetExpr);
				Debug.log("Join first expression argument: " + targetExpr);
				pushExpr(tmpExpr);
				break;
			case JOIN_Argi:
				popState();
				targetExpr = popExpr();
				tmpExpr = peekExpr();
				((Join)tmpExpr).addArgument(targetExpr);
				Debug.log("Join expression argument: " + targetExpr);
				break;
				
			/*** PROJECTION ***/
				
			case PROJ:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				//Projection target is next
				case "FROM":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of projection columns
				default:
					Debug.log("Projection column: " + token);
					((Projection)currentExpr).addColumn(token);
					break;
				};
				break;
				
			/*** SELECTION ***/
				
			case SLCT:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				//Selection target is next
				case "FROM":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of selection conditions
				default:
					Debug.log("Selection condition: " + token);
					((Selection)currentExpr).addCondition(token);
					break;
				};
				break;
				
			/*** RENAME ***/
				
			case RNAM:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				//Rename target is next
				case "ONTO":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of rename new names
				default:
					Debug.log("Rename as name: " + token);
					((Rename)currentExpr).setName(token);
					break;
				};
				break;
				
			/*** AGGREGATION ***/
				
			case AGGR:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				//Value is one of aggregate actions
				//TODO: This needs to be more specialized, i.e. actions may need a parsing state
				default:
					Debug.log("Aggregation action: " + token);
					((Aggregation)currentExpr).addAction(token);
					break;
				case "GRUP":
					token = popToken();
					Debug.log("Aggregation first group: " + token);
					((Aggregation)currentExpr).addGroup(token);
					pushState(STATUS.GRUP);
					break;
				case "FROM":
					pushState(STATUS.ASSN_Target);
					break;
				};
				break;
			case GRUP:
				token = popToken();
				if (token == null) {
					pushState(STATUS.HOLD);
					break;
				}
				switch (token) {
				default:
					Debug.log("Aggregation group: " + token);
					((Aggregation)currentExpr).addGroup(token);
					break;
				case "FROM":
					pushState(STATUS.ASSN_Target);
					break;
				};
				break;
				
			/*** UNDEFINED ***/
				
			default:
				Debug.logv("Catch all reached: Undefined behavior");
				break;
			};
		} while (parseState != STATUS.END && parseState != STATUS.HOLD);
		
		return this.exprStack;
	}
	
	/*** Stack Utilities ***/
	
	public void resetParser() {
		this.resetStateStack();
		this.resetExprStack();
		this.exitState = STATUS.DEFAULT;
		this.stoker = null;
	}
	
	private void resetStateStack() {
		this.stateStack = new ArrayList<STATUS>();
		pushState(STATUS.DEFAULT);
	}
	
	private void resetExprStack() {
		this.exprStack = new ArrayList<RAExpression>();
	}
	
	//STATUS Stack
	
	private STATUS evaluateState(String token) {
		Debug.logv("Evaluate State from Token: " + token);
		STATUS state;
		
		if (token == null) state = STATUS.HOLD;
		else {
			switch (token) {
			case "PROJ":
				state = STATUS.PROJ;
				Debug.log("Projection action");
				break;
			case "SLCT":
				state = STATUS.SLCT;
				Debug.log("Selection action");
				break;
			case "RNAM":
				state = STATUS.RNAM;
				Debug.log("Rename action");
				break;
			case "AGGR":
				state = STATUS.AGGR;
				Debug.log("Aggregation action");
				break;
			case "JOIN":
				state = STATUS.JOIN;
				Debug.log("Join action");
				break;
			case "END":
				state = STATUS.END;
				Debug.log("Ending action");
				break;
			default:
				return STATUS.DEFAULT;
			};
		}
		pushState(state);
		
		return state;
	}
	
	private int pushState(STATUS state) {
		int i = stateStack.size();
		stateStack.add(state);
		return i;
	}
	
	private STATUS popState() {
		return stateStack.remove(stateStack.size()-1);
	}
	
	public STATUS peekState() {
		return stateStack.get(stateStack.size()-1);
	}
	
	public int mostRecentIndex(STATUS state) {
		return stateStack.lastIndexOf(state);
	}
	
	//RAExpression Stack
	
	private RAExpression newExpression(STATUS state, String arg0) {
		if (arg0 == null) {
			Debug.logerr("Cannot make new expression with null arg0! " + state);
			Gdx.app.exit();
			return null;
		}
		
		RAExpression rae = null;
		
		switch (state) {
		case PROJ:
			rae = new Projection(arg0);
			Debug.log("Projection first column: " + arg0);
			break;
		case SLCT:
			rae = new Selection(arg0);
			Debug.log("Selection first condition: " + arg0);
			break;
		case RNAM:
			rae = new Rename(arg0);
			Debug.log("Rename new name: " + arg0);
			break;
		case AGGR:
			rae = new Aggregation(arg0);
			Debug.log("Aggregation first action: " + arg0);
			break;
		case JOIN:
			rae = new Join(arg0);
			Debug.log("Join first argument: " + arg0);
			break;
		default:
			break;			
		};
		
		if (rae != null) pushExpr(rae);
		
		return rae;
	}
	
	private int pushExpr(RAExpression rae) {
		int i = exprStack.size();
		exprStack.add(rae);
		return i;
	}
	
	private RAExpression popExpr() {
		int s = exprStack.size();
		if (s > 0) return exprStack.remove(s-1);
		return null;
	}
	
	public RAExpression peekExpr() {
		int s = exprStack.size();
		if (s > 0) return exprStack.get(exprStack.size()-1);
		return null;
	}
	
	/*** Input Utilities ***/
	
	public STATUS exitedWith() {
		return this.exitState;
	}
	
	private void initStoker(String input) {
		this.stoker = new StringTokenizer(input);
	}
	
	private String popToken() {
		if (this.stoker.hasMoreTokens())
			return this.stoker.nextToken();
		else return null;
	}

	public EXPR_TYPE mostRecentExpressionType() {
		STATUS state;
		
		Debug.logv(this.stateStack);
		
		for (int s=this.stateStack.size()-1; s > 0; s--) {
			state = this.stateStack.get(s);
			
			Debug.logv(state);
			
			switch (state) {
			case PROJ:
				return EXPR_TYPE.PROJ;
			case SLCT:
				return EXPR_TYPE.SLCT;
			case AGGR:
				return EXPR_TYPE.AGGR;
			case RNAM:
				return EXPR_TYPE.RNAM;
			case JOIN:
				return EXPR_TYPE.JOIN;
			default:
				break;
			};
		}
		
		return null;
	}

}
