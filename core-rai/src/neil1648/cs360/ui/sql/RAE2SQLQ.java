package neil1648.cs360.ui.sql;

import java.util.ArrayList;

import neil1648.cs360.ui.ra.expr.Aggregation;
import neil1648.cs360.ui.ra.expr.Join;
import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.RAExpression.EXPR_TYPE;
import neil1648.cs360.ui.util.Debug;
import neil1648.cs360.ui.ra.expr.Rename;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;

public class RAE2SQLQ {
	
	private ArrayList<SQLQuery> sqlqStack;
	
	public RAE2SQLQ() {
		this.sqlqStack = null;
	}
	
	//TODO: Currently has a bug where a leading JOIN expression is completely ignored/lost in translation
	
	public SQLQuery translate(RAExpression rae) {
		if (rae == null) return null;
		sqlqStack = new ArrayList<SQLQuery>();
		
		SQLQuery sqlq = new SQLQuery();
		
		/*** MAGICAL TOP SECRET CONVERSION PROCESS SHHHH ***/
		
		RAExpression currentExpr=rae;
		RAExpression.EXPR_TYPE raeType=currentExpr.getType();
		
		while (raeType != EXPR_TYPE.SMPL) {
			switch (raeType) {
			case PROJ:
				//If groups exist, push current sqlq onto stack and start new nested sqlq
				if (sqlq.hasGroups()) {
					pushSqlq(sqlq);
					sqlq = new SQLQuery();
				}
				//Add Projection columns to current SQLQuery selections
				sqlq.addSelections(((Projection)currentExpr).getColumns());
				break;
			case SLCT:
				sqlq.addConditions(((Selection)currentExpr).getConditions());
				break;
			case RNAM:
				sqlq.setLabel(((Rename)currentExpr).getName());
				break;
			case AGGR:
				if (sqlq.hasSelections() && !sqlq.hasGroups()) {
					pushSqlq(sqlq);
					sqlq = new SQLQuery();
				}
				//Add actions to current SQLQuery selections and groups to GROUP BY groups
				sqlq.addSelections(((Aggregation)currentExpr).getActions());
				sqlq.addGroups(((Aggregation)currentExpr).getGroups());
				break;
			case JOIN:
				//Look at first argument and determine if it is SMPL RAExpression
				JoinTable jtbl=null;
				Join tmpj = (Join)currentExpr;
				
				RAExpression.EXPR_TYPE tmpType=null;
				
				for (RAExpression tmpRae : tmpj.getArguments()) {
					
					Debug.logv("Join Table: " + jtbl);
					Debug.logv("Join Argument Expression: " + tmpRae);
					
					tmpType = tmpRae.getType();
					
					Debug.logv("Join Argument Type: " + tmpType);
					
					switch (tmpType) {
					case SMPL:
						String value = ((SimpleExpression)tmpRae).getValue();
						if (jtbl == null) jtbl = new JoinTable(value);
						else jtbl.join(value);
						break;
					default:
						SQLQuery tmpSqlq = (new RAE2SQLQ()).translate(tmpRae);
						if (jtbl == null) jtbl = new JoinTable(tmpSqlq);
						else jtbl.join(tmpSqlq);
						break;
					}
				}
				
				RAExpression tmpRae = tmpj.getTarget();
				tmpType = tmpRae.getType();
				switch (tmpType) {
				case SMPL:
					String value = ((SimpleExpression)tmpRae).getValue();
					if (jtbl == null) jtbl = new JoinTable(value);
					else jtbl.join(value);
					break;
				default:
					SQLQuery tmpSqlq = (new RAE2SQLQ()).translate(tmpRae);
					if (jtbl == null) jtbl = new JoinTable(tmpSqlq);
					else jtbl.join(tmpSqlq);
					break;
				}
				
				sqlq.setFromTarget(jtbl);
				break;
			default:
				break;
			};
			
			currentExpr = currentExpr.getTarget();
			raeType = currentExpr.getType();
		}
		
		//raeType == EXPR_TYPE.SMPL
		if (!sqlq.hasFromTarget()) sqlq.setFromTarget(((SimpleExpression)currentExpr).getValue());
		
		SQLQuery tmpSqlq;
		while ((tmpSqlq = popSqlq()) != null) {
			tmpSqlq.setFromTarget(sqlq);
			sqlq = tmpSqlq;
		}
		
		return sqlq;
	}
	
	/*** Stack Utilities ***/
	
	private int pushSqlq(SQLQuery sqlq) {
		int i = this.sqlqStack.size();
		this.sqlqStack.add(sqlq);
		return i;
	}
	
	private SQLQuery popSqlq() {
		if (this.sqlqStack.size() > 0)
			return this.sqlqStack.remove(this.sqlqStack.size()-1);
		return null;
	}
	
//	private SQLQuery peekSqlq() {
//		if (this.sqlqStack.size() > 0)
//			return this.sqlqStack.get(this.sqlqStack.size()-1);
//		return null;
//	}

}
