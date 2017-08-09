package com.jcore.Orm;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import com.jcore.Frame.*;

public class DbQuery<T> implements IQuery<T> ,IDbQuery<T> {

	public DbQuery(Connection conn, Class<?> type) {
		Conn = conn;
		TEntity = new Entity<T>(type);
		trackSql = trackSql.replace("{table}", "["+type.getSimpleName()+"]");
	}

	Entity<T> TEntity;

	private Connection Conn;

	private String whereExp;
	private String orderExp;

	@SuppressWarnings("unused")
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

		ResultSet rs = MsSqlHelp.getResultSet(Conn, this.trackSql, sqlArgs.toArray());

		T resultObj = RecordMap.toEntity(TEntity.getType(), rs);

		return resultObj;
	}

	@Override
	public List<T> ToList() {

		BuildColumns(null);
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);

		ResultSet rs = MsSqlHelp.getResultSet(Conn, this.trackSql, sqlArgs.toArray());

		List<T> list = RecordMap.toList(TEntity.getType(), rs);

		return list;
	}

	@Override
	public long Count() {
		BuildColumns(" count(0) ");
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);

		int count = (int) MsSqlHelp.ExecScalar(Conn, this.trackSql, sqlArgs.toArray());
		return count;
	}

	@Override
	public boolean Exist() {

		BuildColumns(" count(0) ");
		BuildWhere(null, null);
		BuildOrder(null);
		BuildLimit(null);

		int count = (int) MsSqlHelp.ExecScalar(Conn, this.trackSql, sqlArgs.toArray());

		return count > 0;
	}

	@Override
	public T Get(Object id) {

		String whereStr = String.format(" where %s=@id ", TEntity.getKey());

		limitForm = 0;
		limitLength = 1;

		List<Object> arg = new ArrayList<Object>();
		arg.add(id);

		BuildColumns(null);
		BuildWhere(whereStr, arg.toArray());
		BuildOrder(null);
		BuildLimit(null);

		ResultSet rs = MsSqlHelp.getResultSet(Conn, this.trackSql, sqlArgs.toArray());

		T entity = RecordMap.toEntity(TEntity.getType(), rs);

		return entity;
	}

	@Override
	public T GetUnique(Object unique) {

		String whereStr = String.format(" where %s=@unique ", TEntity.getUniqueKey());

		limitForm = 0;
		limitLength = 1;

		List<Object> arg = new ArrayList<Object>();
		arg.add(unique);

		BuildColumns(null);
		BuildWhere(whereStr, arg.toArray());
		BuildOrder(null);
		BuildLimit(null);

		ResultSet rs = MsSqlHelp.getResultSet(Conn, this.trackSql, sqlArgs.toArray());

		T entity = RecordMap.toEntity(TEntity.getType(), rs);

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
			String colName = fields[i].getName();

			columnStr = columnStr + ",[" + colName + "]";
		}

		columnStr = columnStr.substring(1, columnStr.length());

		trackSql = trackSql.replace("{column}", columnStr);
	}

	private void BuildWhere(String where, Object[] args) {

		if (where != "" && where != null) {
			trackSql = trackSql.replace("{where}", "");
			return;
		}
		if (whereExp == "" || whereExp == null) {
			trackSql = trackSql.replace("{where}", "");
			return;
		}

		trackSql = trackSql.replace("{where}", " where " + whereExp);

		if (args != null && args.length > 0) {
			for (int i = 0; i <= args.length; i++) {
				sqlArgs.add(args[i]);
			}
		}

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
				String.format(" offset %s rows fetch next %s rows only ", limitForm, limitLength));

	}

}
