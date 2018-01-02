package com.jcore.Orm;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.dom4j.Element;

import com.jcore.Frame.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComplexSqlHelp<T> {

	public ComplexSqlHelp(Connection conn,Class<?> type,String dbType) {
		Conn = conn;
		TEntity = new Entity<T>(type);
		_dbType=dbType;
	}

	Entity<T> TEntity;
	
	private String _dbType="";

	private Connection Conn;

	private static List<ReportWhereEntity> FillReportWhereEntity(Element xmlNode) 
    {
		List<ReportWhereEntity> list = new ArrayList<ReportWhereEntity>();

		List nl = xmlNode.elements();
		
		
		for (Iterator it = nl.iterator(); it.hasNext();) {
		      Element elm = (Element) it.next();
		      
		      ReportWhereEntity result = new ComplexSqlHelp.ReportWhereEntity();
				result.Data = elm.getStringValue();
				result.Prepend = elm.attributeValue("prepend");
				result.Type = elm.attributeValue("type");
				result.Property = elm.attributeValue("property");
				list.add(result);
		}

		return list;

	}

	private static String getDynamicStr(List<ReportWhereEntity> list, Hashtable<String, Object> condition,
			Hashtable<String, Object> tag) {
		StringBuilder sb = new StringBuilder();

		tag = new Hashtable<String, Object>();
		Hashtable<String, Object> conditionDict = new Hashtable<String, Object>();

		Enumeration<String> keys =condition.keys();
		
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			conditionDict.put(key.toLowerCase(), condition.get(key));
		}
		boolean isSet = false;
		for (ReportWhereEntity item : list) {

			item.Property = item.Property.toLowerCase();
			item.Data = item.Data.toLowerCase();
			if (!conditionDict.containsKey(item.Property)) {
				continue;
			}
			if (tag.containsKey(item.Property)) {
				continue;
			}
			if (conditionDict.get(item.Property) == null || (conditionDict.get(item.Property).getClass() == String.class
					&& (conditionDict.get(item.Property).toString()).isEmpty())) {
				continue;
			}
			if (item.Type.trim().isEmpty()) {
				if (sb.length() != 0) {
					sb.append(item.Prepend);
				}
				if (item.Prepend.trim().equals(",")) {
					isSet = true;
					sb.append(item.Data);
				} else {
					sb.append(" ( ").append(item.Data).append(" ) ");
				}
				tag.put(item.Property, conditionDict.get(item.Property));
			} else if (item.Type.trim().toLowerCase().equals("in") ) {
				String value = conditionDict.get(item.Property).toString();
				if (value.startsWith(",")) {
					value = value.substring(1, value.length() - 1);
				}
				if (value.endsWith(",")) {
					value = value.substring(0, value.length() - 2);
				}

				if (value == null || value.isEmpty()) {
					continue;
				}
				String str = " (";
				for (int i = 0; i < value.split(",").length; i++) {
					String keyStr = item.Property + "__" + i;
					str = str + "?,";
					tag.put(keyStr, value.split(",")[i]);
				}
				if (str.endsWith(",")) {
					str = str.substring(0, str.length() - 1);
				}
				str = str + ") ";
				if (sb.length() != 0) {
					sb.append(item.Prepend);
				}
				sb.append(" ( ").append(item.Data.replace("@" + item.Property, str)).append(" ) ");
			} else if (item.Type.trim().toLowerCase().equals("like")) {
				if (sb.length() != 0) {
					sb.append(item.Prepend);
				}
				sb.append(" ( ").append(item.Data).append(" ) ");
				tag.put(item.Property, "%" + conditionDict.get(item.Property) + "%");
			} else if (item.Type.trim().toLowerCase().equals("leftlike")) {
				if (sb.length() != 0) {
					sb.append(item.Prepend);
				}
				sb.append(" ( ").append(item.Data).append(" ) ");
				tag.put(item.Property, conditionDict.get(item.Property) + "%");
			}
		}

		if (!isSet && sb.length() == 0) {
			sb.append("(1=1)");
		}
		String result = sb.toString();
		return result;

	}

 

	public List<T> GetReportData(String reportName, int pageSize, int pageIndex, String order,
			Hashtable<String, Object> where, boolean isPage, long totalCount) {

		try {
			Element root = XmlConfigManager.getRepsConfig();
			
			Element rep=null;

			List  nl = root.elements("Rep");
			for (Iterator it = nl.iterator(); it.hasNext();) {
			      Element elm = (Element) it.next();

			      if (elm.attribute("key").getStringValue().equals(reportName) ) {
						rep = elm;
					}		
			} 			
			 

			if (rep == null) {
				throw new Exception("can not find the xml node.");
			}

			Element reportsqlNode = rep.element("ReportSql");
					
 
			String reportsql = "";
			if (reportsqlNode != null) {
				reportsql = reportsqlNode.getStringValue();
			}

			Element countSqlNode = rep.element("TotalSql");
			String countSql = "";
			if (countSqlNode != null) {
				countSql = countSqlNode.getStringValue();
			}

			Hashtable<String, Object> dataRes = new Hashtable<String, Object>();
			Hashtable<String, Object> data = new Hashtable<String, Object>();

			List dynamicNodes = rep.elements("Dynamic");
			
			for (Iterator it = dynamicNodes.iterator(); it.hasNext();) {
			      Element elm = (Element) it.next();			      

					List<ReportWhereEntity> list = FillReportWhereEntity(elm);
					String dynamicStr = getDynamicStr(list, where, dataRes);
					String dyProperty = elm.attribute("property").getStringValue();
					reportsql = reportsql.replace(dyProperty, dynamicStr);

					if (isPage) {
						countSql = countSql.replace(dyProperty, dynamicStr);
					}
					if (data != null) {
						while (dataRes.keys().hasMoreElements()) {
							String key = dataRes.keys().nextElement();

							if (data.containsKey(key)) {
								continue;
							}
							data.put(key, dataRes.get(key));
						}
					}
			       
			} 	

 

			if (isPage) {
				if (order == null || order.isEmpty()) {
					throw new Exception("order must not be null!");
				}

				int start = (pageIndex - 1) * pageSize;

				if(_dbType.equals("mysql"))
				{
					reportsql = reportsql + " order by %s limit %s,%s ";
				}
				else
				{
					reportsql = reportsql + "  order by %s OFFSET %s ROWS FETCH NEXT %s ROWS ONLY";
				}
				

				reportsql = String.format(reportsql, order, start, pageSize);
			}

			if (where != null) {

				Enumeration<String> keys =where.keys();
				
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (!data.containsKey(key.toLowerCase())) {
						Object obj =where.get(key);
						int i=0;
						if(obj  instanceof java.util.List)
						{
							for (Object objItem : (List)obj) {
								data.put(key+"_"+i, objItem);
								i++;
							}
						}
						else
						{
							data.put(key, obj);
						}
						
					}
				}

			}

			HashMap<String,Object> map = sort(reportsql,data);
			
			ResultSet rs = MsSqlHelp.getResultSet(Conn, (String)map.get("sql"), (Object[])map.get("data"));

			List<T> list = RecordMap.toList(TEntity.getType(), rs);

			if (isPage) {
				HashMap<String,Object> mapPage = sort(countSql,data);
				totalCount = (long) MsSqlHelp.ExecScalar(Conn, (String)mapPage.get("sql"), (Object[])mapPage.get("data"));
			} else {
				totalCount = 0;
			}

			return list;
		} catch (Exception e) {
			totalCount = 0;

			return null;
		} finally {
			try {
				if (!Conn.isClosed()) {
					Conn.close();
				}
			} catch (SQLException e) {
				 
				Log.logError(e);
			}
		}

	}

	

	public HashMap<String,Object> sort(String sql, Hashtable<String, Object> par) {
		// 组装
		HashMap<String, Integer> tagMap = new HashMap<String, Integer>();

		par.forEach(new BiConsumer<String, Object>() {
			@Override
			public void accept(String t, Object u) {

				int index = sql.indexOf("@" + t.toLowerCase());

				tagMap.put(t, index);
				
			}
		});

		// 排序

		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(tagMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			// 升序排序
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}

		});
		
		

		// 输出结构
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		Object[] resultArray = new Object[tagMap.size()];
		String sqlTag =sql;
		
		for (int i = 0; i < list.size(); i++) {

			String key =list.get(i).getKey();
			resultArray[i] = par.get(key);
			
			sqlTag = sqlTag.replace("@"+key.toLowerCase(), "?");
			
		}
		 
		result.put("data", resultArray);
		result.put("sql", sqlTag);

		return result;

	}

	
	
	public static class ReportWhereEntity {
		public String Property;
		public String Prepend;
		public String Type;
		public String Data;
	}
}
