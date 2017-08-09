package com.jcore.Mongodb;

 
import java.util.*;
import java.util.Map.*;
import java.util.regex.*;

import com.mongodb.*;
 
public class ExpCal {

	// (col>?) or (xx=?)

	public static BasicDBObject Analysis(String exp, Object[] parArray) {

		Queue<Object> par = new LinkedList<Object>() ; 
		
		for (Object object : par) {
			par.offer(object);
		}
		
		BasicDBObject where = new BasicDBObject();

		// ♫ ♪
		// exp =exp.replace(") or (", "♫");
		// exp =exp.replace(")or (", "♫");
		// exp =exp.replace(") or(", "♫");
		// exp =exp.replace(")or(", "♫");

		exp = exp.replace(") and (", "♪");
		exp = exp.replace(")and (", "♪");
		exp = exp.replace(") and(", "♪");
		exp = exp.replace(")and(", "♪");

		exp = exp.replace(")", "");
		exp = exp.replace("(", "").trim();

		// col>?♫xx=?♪rrr=?

		String[] expArray = exp.split("?");

		BasicDBList values = new BasicDBList();

		for (String expItem : expArray) {

			String itemStr = expItem;

			if (expItem.startsWith("♪")) {
				itemStr = itemStr.substring(1, itemStr.length());
			}

			calOperator(par, values, expItem);

		}

		where.append("$and", values);

		return where;

	}

	private static void calOperator(Queue<Object> par, BasicDBList values, String expItem) {

		HashMap<String, String> map = getOperators();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String opkey = (String) entry.getKey();
			String opval = (String) entry.getValue();		
			
			
			if (expItem.endsWith(opkey)) {

				String key = expItem.substring(0, expItem.indexOf(">=")).trim();
				Object val = par.poll();
				values.add(new BasicDBObject(key, new BasicDBObject(opval, val)));
				return;
			}		
			 			
			
		}
		
		//like
		if (expItem.endsWith(" like")) {

			String key = expItem.substring(0, expItem.indexOf(" like")).trim();
			Object val = par.poll();
			Pattern pattern = Pattern.compile("^.*" + val.toString()+ ".*$", Pattern.CASE_INSENSITIVE); 
			
			values.add(new BasicDBObject(key, new BasicDBObject("$regex", pattern)));
			return;
		}	
		

	}

	static HashMap<String, String> _Operators;

	static HashMap<String, String> getOperators() {
		if (_Operators != null) {
			return _Operators;
		}
		_Operators = new HashMap<String, String>();
		_Operators.put(">=", "$gte");
		_Operators.put("<=", "$lte");
		_Operators.put("!=", "$ne");
		
		_Operators.put("=", "$all");
		_Operators.put(">", "$gt");
		_Operators.put("<", "$lt");
		 
		_Operators.put(" in", "$in");
		_Operators.put(" not in", "$nin");
		
		

		return _Operators;
	}

}
