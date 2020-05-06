package neil1648.cs360.ui.parser;

import java.util.ArrayList;
import java.util.Scanner;

import neil1648.cs360.ui.util.MetaData;

public class RAParser {
	
	public static enum STATUS {
		END,
		DEFAULT,
		PROJ,
		SLCT,
		RNAM,
		AGGR,
		GRUP,
		JOIN,
		ASSN_Target,
		EVAL_Target,
	}
	
	private ArrayList<STATUS> stateStack;

	/*** RA EXPRESSION PARSER STATE MACHINE ENCODING ***
	 * Projection:
	 * 		PROJ col1 [... coln] TRGT targ
	 * Selection:
	 * 		SLCT cnd1 [... cndn] TRGT targ
	 * Rename:
	 * 		RNAM nam1 [... namn] TRGT targ
	 * Aggregation:
	 * 		AGGR act1 [... actn] [GRUP grp1 [... grpn]] TRGT targ
	 * Join:
	 * 		JOIN arg1 [... argn] TRGT argf
	 */
	public void run() {
		
		Scanner sc = new Scanner(System.in);
		
		STATUS parseState, tmpState;
		this.stateStack = new ArrayList<STATUS>();
		pushState(STATUS.DEFAULT);
		String token="", target="";
		
		do {
			parseState = peekState();
			
			if (MetaData.DEBUG) System.out.println("State Stack: " + stateStack);
			
			switch (parseState) {
			
			/*** STATELESS ***/
			
			case DEFAULT:
				token = sc.next();
				evaluateState(token);
				break;
			case ASSN_Target:
				token = sc.next();
				tmpState = evaluateState(token);
				if (tmpState == STATUS.DEFAULT) {
					target = token;
					if (MetaData.DEBUG && MetaData.VERBOSE) System.out.println("Assign target: " + target);
					pushState(STATUS.EVAL_Target);
				}
				break;
			//Evaluate current target state data from stack and
			//		apply target to proper state type.
			case EVAL_Target:
				popState();
				int assn = stateStack.lastIndexOf(STATUS.ASSN_Target);
				if (MetaData.DEBUG && MetaData.VERBOSE) {
					System.out.println("Index of last ASSNTarget: " + assn);
					System.out.println("Validity of EVALTarget action: " + (assn-1>0));
				}
				if (assn-1>0) {
					popState();
					tmpState = popState();
					switch (tmpState) {
					case PROJ:
						System.out.println("Projection target: " + target);
						target = "Projection result";
						break;
					case SLCT:
						System.out.println("Selection target: " + target);
						target = "Selection result";
						break;
					case RNAM:
						System.out.println("Rename target: " + target);
						target = "Rename result";
						break;
					case GRUP:
						//Remove AGGRParse which is currently sitting in the stack
						popState();
					case AGGR:
						System.out.println("Aggregation target: " + target);
						target = "Aggregation result";
						break;
					case JOIN:
						System.out.println("Join final argument: " + target);
						target = "Join result";
						break;
					default:
						if (MetaData.DEBUG) System.out.println("Unknown target: " + target);
						target = "Unknown";
						break;
					};
					pushState(STATUS.EVAL_Target);
				}
				break;
			
			case END:
				break;
				
			/*** JOIN ***/
			
			case JOIN:
				token = sc.next();
				switch (token) {
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				default:
					System.out.println("Join argument: " + token);
					break;
				}
				break;
				
			/*** PROJECTION ***/
				
			case PROJ:
				token = sc.next();
				switch (token) {
				//Projection target is next
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of projection columns
				default:
					System.out.println("Projection column: " + token);
					break;
				};
				break;
				
			/*** SELECTION ***/
				
			case SLCT:
				token = sc.next();
				switch (token) {
				//Selection target is next
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of selection conditions
				default:
					System.out.println("Selection condition: " + token);
					break;
				};
				break;
				
			/*** RENAME ***/
				
			case RNAM:
				token = sc.next();
				switch (token) {
				//Rename target is next
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				//Value is one of rename new names
				default:
					System.out.println("Rename as name: " + token);
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
					System.out.println("Aggregation action: " + token);
					break;
				case "GRUP":
					pushState(STATUS.GRUP);
					break;
				//Aggregation target is next
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				};
				break;
			case GRUP:
				token = sc.next();
				switch (token) {
				default:
					System.out.println("Aggregation group: " + token);
					break;
				case "TRGT":
					pushState(STATUS.ASSN_Target);
					break;
				};
				break;
				
			/*** UNDEFINED ***/
				
			default:
				if (MetaData.DEBUG && MetaData.VERBOSE) System.out.println("Catch all reached: Undefined behavior");
				break;
			};
		} while (parseState != STATUS.END);
		
		sc.close();
	}
	
	private STATUS evaluateState(String token) {
		STATUS state;
		switch (token) {
		default:
			return STATUS.DEFAULT;
		case "PROJ":
			state = STATUS.PROJ;
			System.out.println("Projection action");
			break;
		case "SLCT":
			state = STATUS.SLCT;
			System.out.println("Selection action");
			break;
		case "RNAM":
			state = STATUS.RNAM;
			System.out.println("Rename action");
			break;
		case "AGGR":
			state = STATUS.AGGR;
			System.out.println("Aggregation action");
			break;
		case "JOIN":
			state = STATUS.JOIN;
			System.out.println("Join action");
			break;
		case "END":
			state = STATUS.END;
			System.out.println("Ending action");
			break;
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

}
