package com.jcore.Orm;

import java.sql.*;
import java.util.*;


import com.jcore.Frame.*;

public abstract class DbSet<T> implements ISet<T> ,IDbQuery<T>{

	public DbSet(Connection conn) {
		Conn = conn;
		Class<?> type =this.getType();
		
		query = new DbQuery<T>(conn,type);
		TEntity = new Entity<T>(type);
	}
	
	public abstract Class<?> getType();

	private Connection Conn;

	private DbQuery<T> query;
	private Entity<T> TEntity;

	@Override
	public Object Add(T t) {

		String sql = "insert into {table} ( {columns} ) values ( {values} );"; // select
																				// @@IDENTITY;
		String[] columns;

		Object returnId = null;

		boolean isNeedId = false;

		if (TEntity.hasIdentity()) {
			sql = sql + " select @@IDENTITY; ";
			isNeedId = false;
		} else {
			// TODO
			isNeedId = true;
		}

		columns = TEntity.getColumns(isNeedId);

		sql = sql.replace("{table}", TEntity.getType().getName());

		sql = sql.replace("{columns}", "[" + String.join("],[", columns) + "]");

		sql = sql.replace("{values}", "@" + String.join(",@", columns));

		Object obj = MsSqlHelp.ExecScalar(Conn, sql, TEntity.getColumnValues(isNeedId, t));

		if (returnId == null) {
			returnId = obj;
		}

		return returnId;
	}

	@Override
	public int Delete(Object id) {
		
		String sql = "delete from [{table}] where [{key}]=@key";

		sql = sql.replace("{table}", TEntity.getType().getName());
		sql = sql.replace("{key}", TEntity.getKey());

		Object[] par = new Object[1];
		par[0] = id;

		int result = MsSqlHelp.ExecSql(Conn, sql, par);
		
		return result;
	}

	@Override
	public int Update(T t) {
		
		String sql = "update [{table}] set {updateStr} where {key}=@{key}";
		sql = sql.replace("{table}", TEntity.getType().getName());
		
		String[] cols = TEntity.getColumns(false);
		String colStr ="";
		for(int i=0;i<=cols.length;i++)
		{
			colStr = colStr + ","+ String.format("%s=@%s", cols[i]);
		}
		colStr = colStr.substring(1, colStr.length()-1);
		sql = sql.replace("{updateStr}", colStr);
		sql = sql.replace("{key}", TEntity.getKey());
		 
		int result = MsSqlHelp.ExecSql(Conn, sql, TEntity.getColumnValues(false, t));
		
		return result;
	}

	@Override
	public List<T> QueryXml(String reportName, Hashtable<String, Object> par) {

		return	new ComplexSqlHelp<T>(Conn,getType()).GetReportData(reportName, 0, 0, "", par, false, 0);
		 
	}

	@Override
	public PageData<T> QueryXml(String reportName, PagePars param) {
		 
		 PageData<T> result = new PageData<T>();
         int totalCount = 0;
         result.rows =new ComplexSqlHelp<T>(Conn,getType()).GetReportData(  reportName, param.PageSize, param.PageIndex, param.Order, param.Pars, true, totalCount);
         result.total = totalCount;
         result.current = param.PageIndex;
         result.rowCount = param.PageSize;
         
		return result;
		
	}

	@Override
	public void ExecXml(String reportName, Dictionary<String, Object> par) {
		 

	}

	@Override
	public IDbQuery<T> Where(String exp, Object... par) {

		return query.Where(exp,par);
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
