package com.jcore.Orm;

import java.sql.Connection;
import java.util.*;

import com.jcore.Frame.*;

public abstract class DbSetMy<T> implements ISet<T>,IDbQuery<T> {

	public DbSetMy(String connKey) {
		ConnKey = connKey;
		Class<?> type = this.getType();

		query = new DbQueryMy<T>(connKey, type);
		TEntity = new Entity<T>(type);
	}

	public DbSetMy(String connKey, String prefix) {
		ConnKey = connKey;
		Class<?> type = this.getType();

		query = new DbQueryMy<T>(connKey, type, prefix);
		TEntity = new Entity<T>(type);

		Prefix = prefix;
	}
	
	//for xml
	public DbSetMy(String connKey,Class<T> type) {
		ConnKey = connKey;
		TEntity = new Entity<T>(type);
	}


	public abstract Class<?> getType();

	private String ConnKey;

	private String Prefix = "";

	private DbQueryMy<T> query;
	private Entity<T> TEntity;

	@Override
	public Object Add(T t) {

		String sql = "insert into {table} ( {columns} ) values ( {values} );"; // select
																				// @@IDENTITY;
		String[] columns;

		Object returnId = null;

		boolean isNeedId = false;

		if (TEntity.hasIdentity()) {
			// sql = sql + " select @@IDENTITY; ";
			isNeedId = false;
		} else {
			returnId = TEntity.getIdValue(t);
			isNeedId = true;
		}

		columns = TEntity.getColumns(isNeedId);

		sql = sql.replace("{table}", Prefix + TEntity.getType().getSimpleName());

		sql = sql.replace("{columns}", "`" + String.join("`,`", columns) + "`");

		sql = sql.replace("{values}", String.join(",", TEntity.getColumnSymbol(columns)));

		Connection conn = ConnetionManager.getConn(ConnKey);

		if (!isNeedId) {
			returnId = MsSqlHelp.ExecAndScalar(conn, sql, TEntity.getColumnValues(isNeedId, t));
		} else {
			MsSqlHelp.ExecSql(conn, sql, TEntity.getColumnValues(isNeedId, t));
		}

		ConnetionManager.close(conn);

		return returnId;
	}

	@Override
	public int Delete(Object id) {

		String sql = "delete from `{table}` where `{key}`=?";

		sql = sql.replace("{table}", Prefix + TEntity.getType().getSimpleName());
		sql = sql.replace("{key}", TEntity.getKey());

		Object[] par = new Object[1];
		par[0] = id;

		Connection conn = ConnetionManager.getConn(ConnKey);

		int result = MsSqlHelp.ExecSql(conn, sql, par);

		ConnetionManager.close(conn);

		return result;
	}

	@Override
	public int Update(T t) {

		String sql = "update `{table}` set {updateStr} where {key}='{id}'";
		sql = sql.replace("{table}", Prefix + TEntity.getType().getSimpleName());

		String[] cols = TEntity.getColumns(false);
		String colStr = "";
		for (int i = 0; i < cols.length; i++) {
			colStr = colStr + "," + String.format("%s=?", cols[i]);
		}
		colStr = colStr.substring(1, colStr.length());
		sql = sql.replace("{updateStr}", colStr);
		sql = sql.replace("{key}", TEntity.getKey());

		sql = sql.replace("{id}", TEntity.getIdValue(t));

		Connection conn = ConnetionManager.getConn(ConnKey);

		int result = MsSqlHelp.ExecSql(conn, sql, TEntity.getColumnValues(false, t));

		ConnetionManager.close(conn);

		return result;
	}

	@Override
	public List<T> QueryXml(String reportName, Hashtable<String, Object> par) {

		Connection conn = ConnetionManager.getConn(ConnKey);

		List<T> list = new ComplexSqlHelp<T>(conn, getType(),"mysql").GetReportData(reportName, par);

		ConnetionManager.close(conn);

		return list;
	}
	
	
	public String reportName="";
	
	@Override
	public List<T> QueryXml( Hashtable<String, Object> par)
	{
		return QueryXml(reportName,par);
	}
	
	@Override
	public PageData<T> QueryXml( PagePars param) {
		return QueryXml(reportName,param);
	}
	

	@Override
	public PageData<T> QueryXml(String reportName, PagePars param) {

 

		Connection conn = ConnetionManager.getConn(ConnKey);

		PageData<T> result = new ComplexSqlHelp<T>(conn, getType(),"mysql").GetReportData(reportName, param.PageSize, param.PageIndex,
				param.Order, param.Pars, true);
 

		ConnetionManager.close(conn);
		return result;

	}

	@Override
	public void ExecXml(String reportName, Dictionary<String, Object> par) {

	}

	@Override
	public IDbQuery<T> Where(String exp, Object... par) {

		return query.Where(exp, par);
	}

	@Override
	public IDbQuery<T> OrderBy(String exp) {

		return query.OrderBy(exp);
	}

	@Override
	public IDbQuery<T> OrderByDesc(String exp) {

		return query.OrderByDesc(exp);
	}

	@Override
	public IDbQuery<T> Limit(int form, int length) {

		return query.Limit(form, length);
	}

	@Override
	public IDbQuery<T> Distinct() {

		return query.Distinct();
	}

	@Override
	public T First() {

		return query.First();
	}

	@Override
	public List<T> ToList() {

		return query.ToList();
	}

	@Override
	public long Count() {

		return query.Count();
	}

	@Override
	public boolean Exist() {

		return query.Exist();
	}

	@Override
	public T Get(Object id) {

		return query.Get(id);
	}

	@Override
	public T GetUnique(Object unique) {

		return query.GetUnique(unique);
	}

}
