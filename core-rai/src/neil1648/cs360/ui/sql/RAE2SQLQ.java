package neil1648.cs360.ui.sql;

import neil1648.cs360.ui.ra.expr.Projection;
import neil1648.cs360.ui.ra.expr.RAExpression;
import neil1648.cs360.ui.ra.expr.RAExpression.EXPR_TYPE;
import neil1648.cs360.ui.ra.expr.Selection;
import neil1648.cs360.ui.ra.expr.SimpleExpression;

public class RAE2SQLQ {
	
	public static SQLQuery translate(RAExpression rae) {
		if (rae == null) return null;
		
		SQLQuery sqlq = new SQLQuery();
		
		/*** MAGICAL TOP SECRET CONVERSION PROCESS SHHHH ***/
		
		//TODO: [WIP]
		
		RAExpression currentExpr=rae, target;
		RAExpression.EXPR_TYPE raeType=currentExpr.getType();
		
		while (raeType != EXPR_TYPE.SMPL) {
			target = currentExpr.getTarget();
			
			switch (raeType) {
			case PROJ:
				//Handle conflict with AGGR, since the two are mutually exclusive
				//Solution(?): if Aggregation columns are present, set FROM to the wrapped value of a new PROJ-based SQLQ
				sqlq.addSelections(((Projection)currentExpr).getColumns());
				break;
			case SLCT:
				sqlq.addConditions(((Selection)currentExpr).getConditions());
				break;
			case RNAM:
				break;
			case AGGR:
				//Add filters for group columns as ORDER BY
				//Handle conflict with PROJ, since the two are mutually exclusive
				//Solution(?): if Projection columns are present, set FROM to the wrapped value of a new AGGR-based SQLQ
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
