/*
 * $Id: TableCommand.java,v 1.5 2005/10/10 17:01:16 rbair Exp $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.dataset.provider.sql;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataRow;

/**
 * A simplified DataCommand for use with an SQLDataProvider, which acts against
 * all of the columns in a given RDBMS table. No joins are used.
 * Because this is a simple table-based DataCommand, it can infer the INSERT,
 * UPDATE, and DELETE SQL statements to use when working with the table.
 * <p>
 *
 * @author rbair
 */
public class TableCommand extends AbstractSqlCommand {
    /**
     * The name of the table from which to get results. If this value is
     * null, then the TableCommand is in an uninitialized state
     */
    private String tableName;
    /**
     * The where clause for this query. This is never null, but may be empty
     */
    private String whereClause = "";
    /**
     * The order by clause for this query. This is never null, but may be empty
     */
    private String orderByClause = "";
    /**
     * The having clause for this query. This is never null, but may be empty
     */
    private String havingClause = "";
    
	/**
     * Helper used for notifying of bean property changes. In particular, this
     * is used to notify of changes in the tableName or various clauses
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /** 
     * Creates a new instance of TableCommand 
     */
    public TableCommand() {
        this(null, null);
    }
    
    public TableCommand(String tableName) {
        this(tableName, null);
    }
    
    public TableCommand(String tableName, String whereClause) {
        setTableName(tableName);
        setWhereClause(whereClause);
    }
    
    /**
     * Sets the name of the table in the Database from which to load/save
     * data
     */
    public void setTableName(String tableName) {
        if (this.tableName != tableName) {
            String oldValue = this.tableName;
            this.tableName = tableName;
            pcs.firePropertyChange("tableName", oldValue, tableName);
        }
    }
    
    /**
     * Sets the where clause to use in the query. This clause *must* include
     * the &quot;where&quot; keyword
     */
    public void setWhereClause(String clause) {
        if (whereClause != clause) {
            String oldValue = this.whereClause;
            whereClause = clause == null ? "" : clause;
            pcs.firePropertyChange("whereClause", oldValue, whereClause);
        }
    }
    
    public void setOrderByClause(String clause) {
        if (orderByClause != clause) {
            String oldValue = this.orderByClause;
            orderByClause = clause == null ? "" : clause;
            pcs.firePropertyChange("orderByClause", oldValue, orderByClause);
        }
    }
    
    public void setHavingClause(String clause) {
        if (havingClause != clause) {
            String oldValue = this.havingClause;
            havingClause = clause == null ? "" : clause;
            pcs.firePropertyChange("havingClause", oldValue, havingClause);
        }
    }
    
//
//    public void executeSaveQuery(DataSet ds) {
//    	if (ds == null) {
//    		return;
//    	}
//    	
//        if (!(ds instanceof RowSetDataSet)) {
//            throw new IllegalArgumentException("The SimpleJDBCQueryTask " +
//                    "cannot save data sources that it did not generate");
//        }
//        
//        try {
//            CachedRowSet crs = ((RowSetDataSet)ds).getRowSet();
//            /*
//             * HACK! This next line (setTransactionIsolation) is a total hack,
//             * needed because the RI for CachedRowSetWriter tries to set the
//             * transaction isolation level to the CachedRowSets level.
//             * Unfortunately, the default level is unacceptable for HSQL.
//             * The RI probably needs to be hacked so that the CachedRowSetImpl
//             * will have its transaction isolation set to a level acceptable
//             * by the Database.
//             */
//            crs.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            crs.acceptChanges(dsc.getConnection());
//            dsc.getConnection().commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
    /**
     * 
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * 
     * @param propertyName
     * @param listener
     */
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     *
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    	pcs.removePropertyChangeListener(propertyName, listener);
    }

    public String[] getParameterNames() {
		return super.getParameterNames(new String[]{whereClause, orderByClause, havingClause});
    }

    private PreparedStatement createPreparedStatement(String parameterizedSql, JDBCDataConnection conn) throws Exception {
        //replace all of the named parameters in the sql with their
        //corrosponding values. This is done by first converting the sql
        //to proper JDBC sql by inserting '?' for each and every param.
        //As this is done, a record is kept of which parameters go with
        //which indexes. Then, the parameter values are applied.
        //map containing the indexes for each named param
        Map<String,List<Integer>> indexes = new HashMap<String,List<Integer>>();
        String sql = constructSql(parameterizedSql, indexes);

        PreparedStatement ps = conn.prepareStatement(sql);

        //now, apply the given set of parameters
        for (String paramName : getParameterNames()) {
            List<Integer> list = indexes.get(paramName);
            if (list != null) {
                for (int index : list) {
                    ps.setObject(index + 1, getParameter(paramName));
                }
            }
        }
        return ps;
    }
    
    protected PreparedStatement getSelectStatement(JDBCDataConnection conn) throws Exception {
        if (tableName == null) {
            //this TableCommand has not been configured, throw an exception
            throw new Exception("TableCommand not configured with a table name");
        }

        try {
            //construct the select sql by combining the tableName portion and
            //the various clause portions
            StringBuilder buffer = new StringBuilder();
            buffer.append("select * from ");
            buffer.append(tableName);
            buffer.append(" ");
            buffer.append(whereClause);
            buffer.append(" ");
            buffer.append(orderByClause);
            buffer.append(" ");
            buffer.append(havingClause);
            String sql = buffer.toString().trim();
            return createPreparedStatement(sql, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected PreparedStatement getUpdateStatement(JDBCDataConnection conn, DataRow row) throws Exception {
        if (tableName == null) {
            //this TableCommand has not been configured, throw an exception
            throw new Exception("TableCommand not configured with a table name");
        }

        try {
            Map<String,Object> values = new HashMap<String,Object>();
            //construct the select sql by combining the tableName portion and
            //the various clause portions
            StringBuilder buffer = new StringBuilder();
            buffer.append("update ");
            buffer.append(tableName);
            buffer.append(" set ");
            //iterate over all of the columns in the row. Each cell that has been
            //modified needs to be included in this update statement
            List<DataColumn> columns = row.getTable().getColumns();
            int modCount = 0;
            for (int i=0; i<columns.size(); i++) {
                DataColumn col = columns.get(i);
                if (row.isModified(col)) {
                    buffer.append(col.getName());
                    buffer.append(" = :" + col.getName() + ", ");
                    values.put(col.getName(), row.getValue(col));
                    modCount++;
                }
            }
            //if nothing was modified, skip this row
            if (modCount == 0) {
                return null;
            }
            //remove the trailing comma
            buffer.delete(buffer.length()-2, buffer.length());
            
            //do the where clause
            buffer.append(" where ");
            int keyColCount = 0;
            for (int i=0; i<columns.size(); i++) {
                DataColumn col = columns.get(i);
                if (col.isKeyColumn()) {
                    buffer.append(col.getName());
                    buffer.append(" = :orig_" + col.getName() + " and ");
                    values.put("orig_" + col.getName(), row.getOriginalValue(col));
                    keyColCount++;
                }
            }
            if (keyColCount == 0) {
                System.err.println("WARNING!!! No key columns were specified, the entire table '" + tableName + "' will be updated!!");
                //remove the where clause
                buffer.delete(buffer.length() - 7, buffer.length());
            } else {
                buffer.delete(buffer.length() - 4, buffer.length());
            }
            
            String sql = buffer.toString().trim();
            return super.prepareStatement(sql, values, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected PreparedStatement getInsertStatement(JDBCDataConnection conn, DataRow row) throws Exception {
        if (tableName == null) {
            //this TableCommand has not been configured, throw an exception
            throw new Exception("TableCommand not configured with a table name");
        }

        try {
            Map<String,Object> values = new HashMap<String,Object>();
            StringBuilder buffer = new StringBuilder();
            buffer.append("insert into ");
            buffer.append(tableName);
            buffer.append("(");
            for (DataColumn col : row.getTable().getColumns()) {
                buffer.append(col.getName());
                buffer.append(", ");
            }
            buffer.replace(buffer.length()-2, buffer.length(), ")");
            buffer.append(" values(");
            for (DataColumn col : row.getTable().getColumns()) {
                buffer.append(":" + col.getName() + ", ");
                values.put(col.getName(), row.getValue(col));
            }
            buffer.replace(buffer.length()-2, buffer.length(), ")");
            String sql = buffer.toString().trim();
            return super.prepareStatement(sql, values, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected PreparedStatement getDeleteStatement(JDBCDataConnection conn, DataRow row) throws Exception {
        if (tableName == null) {
            //this TableCommand has not been configured, throw an exception
            throw new Exception("TableCommand not configured with a table name");
        }

        try {
            Map<String,Object> values = new HashMap<String,Object>();
            StringBuilder buffer = new StringBuilder();
            buffer.append("delete from ");
            buffer.append(tableName);
            buffer.append(" where ");
            int keyColCount = 0;
            List<DataColumn> columns = row.getTable().getColumns();
            for (int i=0; i<columns.size(); i++) {
                DataColumn col = columns.get(i);
                if (col.isKeyColumn()) {
                    buffer.append(col.getName());
                    buffer.append(" = :orig_" + col.getName() + " and ");
                    values.put("orig_" + col.getName(), row.getOriginalValue(col));
                    keyColCount++;
                }
            }
            if (keyColCount == 0) {
                System.err.println("WARNING!!! No key columns were specified, the entire table '" + tableName + "' will be deleted!!");
                //remove the where clause
                buffer.delete(buffer.length() - 7, buffer.length());
            } else {
                buffer.delete(buffer.length() - 4, buffer.length());
            }
            
            String sql = buffer.toString().trim();
            return super.prepareStatement(sql, values, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}