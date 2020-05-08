package neil1648.cs360.ui.sql;

import java.util.ArrayList;

import neil1648.cs360.ui.ra.expr.Aggregation;
import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.RAExpression.EXPR_TYPE;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;

public class RAE2SQLQ {
	
	private ArrayList<SQLQuery> sqlqStack;
	
	public RAE2SQLQ() {
		this.sqlqStack = null;
	}
	
	public SQLQuery translate(RAExpression rae) {
		if (rae == null) return null;
		sqlqStack = new ArrayList<SQLQuery>();
		
		SQLQuery sqlq = new SQLQuery();
		
		/*** MAGICAL TOP SECRET CONVERSION PROCESS SHHHH ***/
		
		//TODO: [WIP]
		
		RAExpression currentExpr=rae, target;
		RAExpression.EXPR_TYPE raeType=currentExpr.getType();
		
		while (raeType != EXPR_TYPE.SMPL) {
			target = currentExpr.getTarget();
			
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
				break;
			default:
				break;
			};
			
			currentExpr = target;
			raeType = currentExpr.getType();
		}
		
		//raeType == EXPR_TYPE.SMPL
		sqlq.setFromTarget(((SimpleExpression)currentExpr).getValue());
		
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
