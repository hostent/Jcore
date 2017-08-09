package com.jcore.Orm;

import java.sql.*;
import java.util.logging.*;

import com.jcore.Frame.Log; 
 

public class MsSqlHelp {

	 
	     
	    public static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	     
	    public static String url = "jdbc:sqlserver://localhost:1433;databaseName=J2ee;";
	     
	    public static String user = "null";
	     
	    public static String password = "123456";

	     
	    private MsSqlHelp()
	    {
	    }

	     
	    public static Connection getConnection()
	    {
	        try
	        {
	             
	            Class.forName(driver);
	        } catch (ClassNotFoundException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	        }

	        try
	        {
	            return DriverManager.getConnection(url, user, password);
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	        }
	    }
 
	    public static Statement getStatement()
	    {
	        Connection conn = getConnection();
	        if (conn == null)
	        {
	            return null;
	        }
	        try
	        {
	            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	                    ResultSet.CONCUR_UPDATABLE);
	        
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            close(conn);
	        }
	        return null;
	    }

	     
	    public static Statement getStatement(Connection conn)
	    {
	        if (conn == null)
	        {
	            return null;
	        }
	        try
	        {

	            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	         
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	        }
	    }

	     
	    public static PreparedStatement getPreparedStatement(String cmdText, Object... cmdParams)
	    {
	        Connection conn = getConnection();
	        if (conn == null)
	        {
	            return null;
	        }

	        PreparedStatement pstmt = null;
	        try
	        {
	            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	            int i = 1;
	            for (Object item : cmdParams)
	            {
	                pstmt.setObject(i, item);
	                i++;
	            }
	        } catch (SQLException e)
	        {
	        	Log.logError(e);
	            close(conn);
	        }
	        return pstmt;
	    }

	     
	    public static PreparedStatement getPreparedStatement(Connection conn, String cmdText, Object... cmdParams)
	    {
	        if (conn == null)
	        {
	            return null;
	        }

	        PreparedStatement pstmt = null;
	        try
	        {
	            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	            int i = 1;
	            for (Object item : cmdParams)
	            {
	                pstmt.setObject(i, item);
	                i++;
	            }
	        } catch (SQLException e)
	        {
	        	Log.logError(e);
	            close(pstmt);
	        }
	        return pstmt;
	    }

	     
	    public static int ExecSql(String cmdText)
	    {
	        Statement stmt = getStatement();
	        if (stmt == null)
	        {
	            return -2;
	        }
	        int i;
	        try
	        {
	            i = stmt.executeUpdate(cmdText);
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null,
	                    ex);
	            i = -1;
	        }
	        closeConnection(stmt);
	        return i;
	    }

	     
	    public static int ExecSql(Connection conn, String cmdText)
	    {
	        Statement stmt = getStatement(conn);
	        if (stmt == null)
	        {
	            return -2;
	        }
	        int i;
	        try
	        {
	            i = stmt.executeUpdate(cmdText);

	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null,
	                    ex);
	            i = -1;
	        }
	        close(stmt);
	        return i;
	    }

	     
	    public static int ExecSql(String cmdText, Object... cmdParams)
	    {
	        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
	        if (pstmt == null)
	        {
	            return -2;
	        }
	        int i;
	        try
	        {
	            i = pstmt.executeUpdate();
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null,
	                    ex);
	            i = -1;
	        }
	        closeConnection(pstmt);
	        return i;
	    }

	    /** 
	     * 执行 SQL 语句,返回结果为整型 
	     * 主要用于执行非查询语句 
	     * @param conn 数据库连接 
	     * @param cmdText 需要 ? 参数的 SQL 语句 
	     * @param cmdParams SQL 语句的参数表 
	     * @return 非负数:正常执行; -1:执行错误; -2:连接错误 
	     */
	    public static int ExecSql(Connection conn, String cmdText, Object... cmdParams)
	    {
	        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
	        if (pstmt == null)
	        {
	            return -2;
	        }
	        int i;
	        try
	        {
	            i = pstmt.executeUpdate();

	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            i = -1;
	        }
	        close(pstmt);
	        return i;
	    }

	    
	    
	    
	    public static Object ExecAndScalar(Connection conn, String cmdText, Object... cmdParams)
	    {
	    	PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
	    	
	    	 
	        if (pstmt == null)
	        {
	            return -2;
	        }
	        int i;
	        Object result=null;
	        ResultSet rs=null; ;
	        try
	        {
	            i = pstmt.executeUpdate();
	            if(i>0)
	            {
	            	rs = getResultSet(conn," select @@IDENTITY; ");           	   	 
	            	result = buildScalar(rs);
	            }

	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            i = -1;
	        }	        
	        return result;
	    }
	    
	    
	     
	    public static Object ExecScalar(String cmdText)
	    {
	        ResultSet rs = getResultSet(cmdText);
	        Object obj = buildScalar(rs);
	        closeConnection(rs);
	        return obj;
	    }

	     
	    public static Object ExecScalar(Connection conn, String cmdText)
	    {
	        ResultSet rs = getResultSet(conn, cmdText);
	        Object obj = buildScalar(rs);
	        closeEx(rs);
	        return obj;
	    }

	     
	    public static Object ExecScalar(String cmdText, Object... cmdParams)
	    {
	        ResultSet rs = getResultSet(cmdText, cmdParams);
	        Object obj = buildScalar(rs);
	        closeConnection(rs);
	        return obj;
	    }

	    /** 
	     * 返回结果集的第一行的一列的值,其他忽略 
	     * @param cmdText SQL 语句 
	     * @return 
	     */ 
	    public static Object ExecScalar(Connection conn, String cmdText, Object... cmdParams)
	    {
	        ResultSet rs = getResultSet(conn, cmdText, cmdParams);
	        Object obj = buildScalar(rs);
	        closeEx(rs);
	        return obj;
	    }

	     
	    public static ResultSet getResultSet(String cmdText)
	    {
	        Statement stmt = getStatement();
	        if (stmt == null)
	        {
	            return null;
	        }
	        try
	        {
	            return stmt.executeQuery(cmdText);
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            closeConnection(stmt);
	        }
	        return null;
	    }

	     
	    public static ResultSet getResultSet(Connection conn, String cmdText)
	    {
	        Statement stmt = getStatement(conn);
	        if (stmt == null)
	        {
	            return null;
	        }
	        try
	        {
	            return stmt.executeQuery(cmdText);
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            close(stmt);
	        }
	        return null;
	    }

	     
	    public static ResultSet getResultSet(String cmdText, Object... cmdParams)
	    {
	        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
	        if (pstmt == null)
	        {
	            return null;
	        }
	        try
	        {
	            return pstmt.executeQuery();
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            closeConnection(pstmt);
	        }
	        return null;
	    }

	    
	    public static ResultSet getResultSet(Connection conn, String cmdText, Object... cmdParams)
	    {
	        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
	        if (pstmt == null)
	        {
	            return null;
	        }
	        try
	        {
	            return pstmt.executeQuery();
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	            close(pstmt);
	        }
	        return null;
	    }

	    public static Object buildScalar(ResultSet rs)
	    {
	        if (rs == null)
	        {
	            return null;
	        }
	        Object obj = null;
	        try
	        {
	            if (rs.next())
	            {
	                obj = rs.getObject(1);
	            }
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return obj;
	    }



	    private static void close(Object obj)
	    {
	        if (obj == null)
	        {
	            return;
	        }
	        try
	        {
	            if (obj instanceof Statement)
	            {
	                ((Statement) obj).close();
	            } else if (obj instanceof PreparedStatement)
	            {
	                ((PreparedStatement) obj).close();
	            } else if (obj instanceof ResultSet)
	            {
	                ((ResultSet) obj).close();
	            } else if (obj instanceof Connection)
	            {
	                ((Connection) obj).close();
	            }
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }

	    private static void closeEx(Object obj)
	    {
	        if (obj == null)
	        {
	            return;
	        }

	        try
	        {
	            if (obj instanceof Statement)
	            {
	                ((Statement) obj).close();
	            } else if (obj instanceof PreparedStatement)
	            {
	                ((PreparedStatement) obj).close();
	            } else if (obj instanceof ResultSet)
	            {
	                ((ResultSet) obj).getStatement().close();
	            } else if (obj instanceof Connection)
	            {
	                ((Connection) obj).close();
	            }
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	        }

	    }

	    private static void closeConnection(Object obj)
	    {
	        if (obj == null)
	        {
	            return;
	        }
	        try
	        {
	            if (obj instanceof Statement)
	            {
	                ((Statement) obj).getConnection().close();
	            } else if (obj instanceof PreparedStatement)
	            {
	                ((PreparedStatement) obj).getConnection().close();
	            } else if (obj instanceof ResultSet)
	            {
	                ((ResultSet) obj).getStatement().getConnection().close();
	            } else if (obj instanceof Connection)
	            {
	                ((Connection) obj).close();
	            }
	        } catch (SQLException ex)
	        {
	            Logger.getLogger(MsSqlHelp.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }

}


