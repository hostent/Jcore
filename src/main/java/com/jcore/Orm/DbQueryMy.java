package com.jcore.Orm;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import com.jcore.Frame.*;

public class DbQueryMy<T> implements IQuery<T> ,IDbQuery<T>{

	public DbQueryMy(String connKey, Class<?> type) {
		ConnKey = connKey;
		TEntity = new Entity<T>(type);
		trackSql = trackSql.replace("{table}", "`"+type.getSimpleName()+"`");
	}
	
	public DbQueryMy(String connKey, Class<?> type, String prefix) {
		ConnKey = connKey;
		TEntity = new Entity<T>(type);

		trackSql = trackSql.replace("{table}", "`"+prefix+type.getSimpleName()+"`");
		
		
	}

	Entity<T> TEntity;

	private String ConnKey;
	


	private String whereExp;
	private String orderExp;

 
	private String order = "";

	private Integer limitForm = null;
	private Integer limitLength = null;

	private String distinct = "";

	private String trackSql = "select {column} from {table} {where} {order} {limit}";

	private List<Object> sqlArgs = new ArrayList<Object>();

	@Override
	public IDbQuery<T> Where(String exp, Object... par) {

		whereExp = exp;
		if (par != null && par.length > 0) {
			for (int i = 0; i < par.length; i++) {
				sqlArgs.add(par[i]);

			}
		}
		return this;
	}

	@Override
	public IDbQuery<T> OrderBy(String exp) {
		orderExp = exp;
		order = "asc";

		return this;
	}

	@Override
	public IDbQuery<T> OrderByDesc(String exp) {
		order = "desc";
		orderExp = exp;
		return this;
	}

	@Override
	public IDbQuery<T> Limit(int form, int length) {
		limitForm = form;
		limitLength = length;
		return this;
	}

	@Override
	public IDbQuery<T> Distinct() {
		this.distinct = "distinct";

		return this;
	}

	@Override
	public T First() {
		limitForm = 0;
		limitLength = 1;

		BuildColumns(null);
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);
		
		Connection conn = ConnetionManager.getConn(ConnKey);

		ResultSet rs = MsSqlHelp.getResultSet(conn, this.trackSql, sqlArgs.toArray());

		T resultObj = RecordMap.toEntity(TEntity.getType(), rs);
		
		ConnetionManager.close(conn);

		return resultObj;
	}

	@Override
	public List<T> ToList() {

		BuildColumns(null);
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);

		Connection conn = ConnetionManager.getConn(ConnKey);
		
		ResultSet rs = MsSqlHelp.getResultSet(conn, this.trackSql, sqlArgs.toArray());

		List<T> list = RecordMap.toList(TEntity.getType(), rs);

		ConnetionManager.close(conn);
		
		return list;
	}

	@Override
	public long Count() {
		BuildColumns(" count(0) ");
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);
		
		Connection conn = ConnetionManager.getConn(ConnKey);
		
		int count = Integer.parseInt( MsSqlHelp.ExecScalar(conn, this.trackSql, sqlArgs.toArray()).toString());
		
		ConnetionManager.close(conn);
		return count;
	}

	@Override
	public boolean Exist() {

		BuildColumns(" count(0) ");
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);

		Connection conn = ConnetionManager.getConn(ConnKey);
		
		int count = Integer.parseInt( MsSqlHelp.ExecScalar(conn, this.trackSql, sqlArgs.toArray()).toString());

		ConnetionManager.close(conn);
		return count > 0;
	}

	@Override
	public T Get(Object id) {

		String whereStr = String.format(" where %s=? ", TEntity.getKey());

		limitForm = 0;
		limitLength = 1;

		List<Object> arg = new ArrayList<Object>();
		arg.add(id);

		BuildColumns(null);
		BuildWhere(whereStr, arg.toArray());
		BuildOrder(null);
		BuildLimit(null);

		Connection conn = ConnetionManager.getConn(ConnKey);
		
		ResultSet rs = MsSqlHelp.getResultSet(conn, this.trackSql, sqlArgs.toArray());	
		
		T entity = RecordMap.toEntity(TEntity.getType(), rs);

		ConnetionManager.close(conn);
		
		return entity;
	}

	@Override
	public T GetUnique(Object unique) {

		String whereStr = String.format(" where %s=? ", TEntity.getUniqueKey());

		limitForm = 0;
		limitLength = 1;

		List<Object> arg = new ArrayList<Object>();
		arg.add(unique);

		BuildColumns(null);
		BuildWhere(whereStr, arg.toArray());
		BuildOrder(null);
		BuildLimit(null);

		Connection conn = ConnetionManager.getConn(ConnKey);
		
		ResultSet rs = MsSqlHelp.getResultSet(conn, this.trackSql, sqlArgs.toArray());		

		T entity = RecordMap.toEntity(TEntity.getType(), rs);
		
		ConnetionManager.close(conn);

		return entity;
	}

	private void BuildColumns(String cols) {
		if (distinct.isEmpty() || distinct == null) {
			trackSql = trackSql.replace("{column}", distinct + " {column}");
		}

		if (cols != null && (!cols.isEmpty())) {
			trackSql = trackSql.replace("{column}", cols);
			return;
		}

		String columnStr = "";

		Field[] fields = TEntity.getType().getDeclaredFields();
	 
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getAnnotation(Column.class)==null)
			{
				continue;
			}
			 			
			
			String colName = fields[i].getName();

			columnStr = columnStr + ",`" + colName + "`";
		}

		columnStr = columnStr.substring(1, columnStr.length());

		trackSql = trackSql.replace("{column}", columnStr);
	}

	private void BuildWhere(String where, Object[] args) {

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				sqlArgs.add(args[i]);
			}
		}
		
		if (where != "" && where != null) {
			trackSql = trackSql.replace("{where}", where);
			return;
		}
		if (whereExp == "" || whereExp == null) {
			trackSql = trackSql.replace("{where}", "");
			return;
		}

		trackSql = trackSql.replace("{where}", " where " + whereExp);

		

	}

	private void BuildOrder(String order) {
		if (order != null) {
			trackSql = trackSql.replace("{order}", order);
			return;
		}
		if (orderExp == null) {
			trackSql = trackSql.replace("{order}", " order by 1 ");
			return;
		}
		
		trackSql = trackSql.replace("{order}", " order by " + orderExp + " "  + this.order);

	}

	private void BuildLimit(String limit) {
		if (limit != null) {
			trackSql = trackSql.replace("{limit}", limit);
			return;
		}

		if (limitLength == null) {
			trackSql = trackSql.replace("{limit}", "");
			return;
		}

		if (limitForm == null) {
			limitForm = 0;
		}
		if (limitLength == null) {
			limitLength = 1;
		}
		trackSql = trackSql.replace("{limit}",
				String.format(" limit %s,%s ", limitForm, limitLength));

	}

}
