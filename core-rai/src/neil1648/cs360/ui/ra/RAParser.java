package neil1648.cs360.ui.ra;

import java.util.ArrayList;
import java.util.Scanner;

import neil1648.cs360.ui.ra.expr.Aggregation;
import neil1648.cs360.ui.ra.expr.Join;
import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.Rename;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;
import neil1648.cs360.ui.util.Debug;

public class RAParser {
	
	public static enum STATUS {
		DEFAULT,
		PROJ,
		SLCT,
		RNAM,
		AGGR,
		GRUP,
		JOIN,
		ASSN_Target,
		EVAL_Target,
		END
	}
	
	private ArrayList<STATUS> stateStack;
	private ArrayList<RAExpression> exprStack;

	/*** RA EXPRESSION PARSER STATE MACHINE ENCODING ***
	 * Projection:
	 * 		PROJ col0 [... coln] FROM targ
	 * Selection:
	 * 		SLCT cnd0 [... cndn] FROM targ
	 * Rename:
	 * 		RNAM name ONTO targ
	 * Aggregation:
	 * 		AGGR act0 [... actn] [GRUP grp0 [... grpn]] FROM targ
	 * Join:
	 * 		JOIN arg0 [... argn] WITH argf
	 */
	public RAExpression run() {
		RAExpression returnRae = null;
		
		resetStacks();
		
		STATUS parseState=STATUS.DEFAULT, tmpState=STATUS.DEFAULT;
		RAExpression currentExpr=null, targetExpr=null;
		String token="", arg0="";
		
		Scanner sc = new Scanner(System.in);
		
		do {
			parseState = peekState();
			currentExpr = peekExpr();
			
			Debug.logv("State Stack:\t\t" + stateStack);
			Debug.logv("Parse State:\t\t" + parseState);
			Debug.logv("Expression Stack:\t" + exprStack);
			Debug.logv("Current Expression:\t" + currentExpr);
			Debug.logv("Target Expression:\t" + targetExpr);
			
			switch (parseState) {
			
			/*** STATELESS ***/
			
			case DEFAULT:
				token = sc.next();
				tmpState = evaluateState(token);
				//Expression started
				if (tmpState != STATUS.DEFAULT && tmpState != STATUS.END) {
					arg0 = sc.next();
					currentExpr = newExpression(tmpState, arg0);
				}
				break;
			case ASSN_Target:
				token = sc.next();
				tmpState = evaluateState(token);
				//Non action target token found
				if (tmpState == STATUS.DEFAULT) {
					//Create simple expression and set as target
					targetExpr = new SimpleExpression(token);
					Debug.logv("Assign target: " + targetExpr);
					pushState(STATUS.EVAL_Target);
				}
				//Expression started
				else if (tmpState != STATUS.END) {
					Debug.log("Sub-expression found! Resolving...");
					arg0 = sc.next();
					currentExpr = newExpression(tmpState, arg0);
				}
				break;
			//Evaluate current target state data from stack and
			//		apply target to proper state type.
			case EVAL_Target:
				popState();
				popExpr();
				int assn = mostRecentIndex(STATUS.ASSN_Target);
				Debug.logv("Index of most recent ASSNTarget: " + assn);
				Debug.logv("Proceed with EVALTarget action: " + (assn > 1));
				if (assn > 1) {
					popState();
					tmpState = popState();
					switch (tmpState) {
					case PROJ:
						Debug.log("Projection target: " + targetExpr);
						Debug.logv(currentExpr);
						Debug.logv(targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.log(currentExpr);
						targetExpr = currentExpr;
						break;
					case SLCT:
						Debug.log("Selection target: " + targetExpr);
						Debug.logv(currentExpr);
						Debug.logv(targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.log(currentExpr);
						targetExpr = currentExpr;
						break;
					case RNAM:
						Debug.log("Rename target: " + targetExpr);
						Debug.logv(currentExpr);
						Debug.logv(targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.log(currentExpr);
						targetExpr = currentExpr;
						break;
					case GRUP:
						//Remove AGGRParse which is currently sitting in the stack
						popState();
					case AGGR:
						Debug.log("Aggregation target: " + targetExpr);
						Debug.logv(currentExpr);
						Debug.logv(targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.log(currentExpr);
						targetExpr = currentExpr;
						break;
					case JOIN:
						Debug.log("Join final argument: " + targetExpr);
						Debug.logv(currentExpr);
						Debug.logv(targetExpr);
						currentExpr.setTarget(targetExpr);
						Debug.log(currentExpr);
						targetExpr = currentExpr;
						break;
					default:
						Debug.logerr("Unknown target: " + targetExpr);
						break;
					};
					
					pushState(STATUS.EVAL_Target);
				}
				break;
			
			case END:
				returnRae = targetExpr;
				break;
				
			/*** JOIN ***/
			
			//TODO: JOIN needs to have all arguments as RAExpression values rather than String
				
			case JOIN:
				token = sc.next();
				switch (token) {
				case "WITH":
					pushState(STATUS.ASSN_Target);
					break;
				default:
					Debug.log("Join argument: " + token);
					((Join)currentExpr).addArgument(token);
					break;
				}
				break;
				
			/*** PROJECTION ***/
				
			case PROJ:
				token = sc.next();
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
				token = sc.next();
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
				token = sc.next();
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
				token = sc.next();
				switch (token) {
				//Value is one of aggregate actions
				//TODO: This needs to be more specialized, i.e. actions may need a parsing state
				default:
					Debug.log("Aggregation action: " + token);
					((Aggregation)currentExpr).addAction(token);
					break;
				case "GRUP":
					token = sc.next();
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
				token = sc.next();
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
		} while (parseState != STATUS.END);
		
		sc.close();
		
		return returnRae;
	}
	
	/*** Stack Utilities ***/
	
	private void resetStacks() {
		this.stateStack = new ArrayList<STATUS>();
		this.exprStack = new ArrayList<RAExpression>();
		pushState(STATUS.DEFAULT);
	}
	
	//STATUS Stack
	
	private STATUS evaluateState(String token) {
		STATUS state;
		
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
	
	private STATUS peekState() {
		return stateStack.get(stateStack.size()-1);
	}
	
	private int mostRecentIndex(STATUS state) {
		return stateStack.lastIndexOf(state);
	}
	
	//RAExpression Stack
	
	private RAExpression newExpression(STATUS state, String arg0) {
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
	
	private RAExpression peekExpr() {
		int s = exprStack.size();
		if (s > 0) return exprStack.get(exprStack.size()-1);
		return null;
	}

}
