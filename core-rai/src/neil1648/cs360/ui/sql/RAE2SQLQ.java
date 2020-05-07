package neil1648.cs360.ui.sql;

import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.RAExpression.EXPR_TYPE;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;

public class RAE2SQLQ {
	
	public static SQLQuery translate(RAExpression rae) {
		SQLQuery sqlq = new SQLQuery();
		
		/*** MAGICAL TOP SECRET CONVERSION PROCESS SHHHH ***/
		
		//TODO: [WIP]
		
		RAExpression currentExpr=rae, target;
		RAExpression.EXPR_TYPE raeType=currentExpr.getType();
		
		while (raeType != EXPR_TYPE.SMPL) {
			target = currentExpr.getTarget();
			
			switch (raeType) {
			case PROJ:
				sqlq.addSelections(((Projection)currentExpr).getColumns());
				break;
			case SLCT:
				sqlq.addConditions(((Selection)currentExpr).getConditions());
				break;
			case RNAM:
				break;
			case AGGR:
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
		
		return sqlq;
	}

}
