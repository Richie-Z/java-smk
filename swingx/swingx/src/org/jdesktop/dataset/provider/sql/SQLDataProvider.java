/*
 * $Id: SQLDataProvider.java,v 1.9 2005/10/15 11:43:20 pdoubleya Exp $
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataProvider;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataTable;
import static org.jdesktop.dataset.DataRow.DataRowStatus.DELETED;
import static org.jdesktop.dataset.DataRow.DataRowStatus.INSERTED;
import static org.jdesktop.dataset.DataRow.DataRowStatus.UPDATED;
import org.jdesktop.dataset.event.TableChangeEvent;
import org.jdesktop.dataset.provider.LoadTask;
import org.jdesktop.dataset.provider.SaveTask;
import org.jdesktop.dataset.provider.LoadTask.LoadItem;

/**
 * SQL based DataProvider for a JDNC DataSet. This implementation handles
 * retrieving values from a database table, and persisting changes back
 * to the table.
 * 
 * @author rbair
 */
public class SQLDataProvider extends DataProvider {
    /** 
     * Creates a new instance of SQLDataProvider 
     */
    public SQLDataProvider() {
    }
    
    public SQLDataProvider(String tableName) {
        TableCommand tableCommand = new TableCommand(tableName);
        setCommand(tableCommand);
    }
    
    public SQLDataProvider(String tableName, String whereClause) {
        TableCommand tableCommand = new TableCommand(tableName, whereClause);
        setCommand(tableCommand);
    }
    
    /**
     * @inheritDoc
     */
    protected LoadTask createLoadTask(DataTable[] tables) {
        return new LoadTask(tables) {
            protected void readData(DataTable[] tables) throws Exception {
                JDBCDataConnection conn = (JDBCDataConnection)getConnection();
                if (conn == null) {
                    //no connection, short circuit
                    return;
                }
                if (getCommand() == null) {
                    //there isn't any command to run, so short circuit the method
                    return;
                }
                //TODO when selectCommand exists, add it to the check here
                
                //set the progess count
                setMinimum(0);
                setMaximum(tables.length);
                //construct and execute a resultset for each table in turn.
                //as each table is finished, call scheduleLoad.
                for (DataTable table : tables) {
                    try {
                        PreparedStatement stmt = ((AbstractSqlCommand)getCommand()).getSelectStatement(conn);
                        ResultSet rs = stmt.executeQuery();

                        //collect the column names from the result set so that
                        //I can retrieve the data from the result set into the
                        //column based on matching column names
                        ResultSetMetaData md = rs.getMetaData();
                        List<String> names = new ArrayList<String>();
                        List<DataColumn> columns = table.getColumns();
                        for (int i=0; i<columns.size(); i++) {
                            String name = columns.get(i).getName();
                            for (int j=0; j<md.getColumnCount(); j++) {
                                if (name.equalsIgnoreCase(md.getColumnName(j+1))) {
                                    names.add(name);
                                }
                            }
                        }
                        
                        //iterate over the result set. Every 50 items, schedule a load
                        List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>(60);
                        while (rs.next()) {
                            if (rows.size() >= 50) {
                                LoadItem item = new LoadItem<List<Map<String,Object>>>(table, rows);
                                scheduleLoad(item);
                                rows = new ArrayList<Map<String,Object>>(60);
                            }
                            //create a row
                            Map<String,Object> row = new HashMap<String,Object>();
                            for (String name : names) {
                                row.put(name, rs.getObject(name));
                            }
                            rows.add(row);
                        }
                        //close the result set
            			rs.close();
                        //load the remaining items
                        LoadItem item = new LoadItem<List<Map<String,Object>>>(table, rows);
                        scheduleLoad(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setProgress(getProgress() + 1);
                }
                setProgress(getMaximum());
            }
            
            /**
             * @inheritDoc
             */
            protected void loadData(LoadItem[] items) {
                for (LoadItem<List<Map<String,Object>>> item : items) {
                    for (Map<String,Object> row : item.data) {
                        DataRow r  = item.table.appendRowNoEvent();
                        for (String col : row.keySet()) {
                            r.setValue(col, row.get(col));
                        }
                        r.setStatus(DataRow.DataRowStatus.UNCHANGED);
                        
                        // CLEAN
                        item.table.fireDataTableChanged(TableChangeEvent.newRowAddedEvent(item.table, r));
                    }
                    item.table.fireDataTableChanged(TableChangeEvent.newLoadCompleteEvent(item.table));                    
                }
            }
        };
    }

    /**
     * @inheritDoc
     */
    protected SaveTask createSaveTask(DataTable[] tables) {
        return new SaveTask(tables) {
            protected void saveData(DataTable[] tables) throws Exception {
                JDBCDataConnection conn = (JDBCDataConnection)getConnection();
                if (conn == null) {
                    //no connection, short circuit
                    return;
                }
                if (getCommand() == null) {
                    //there isn't any command to run, so short circuit the method
                    return;
                }
                //TODO when selectCommand exists, add it to the check here

                //set the progess count
                setMinimum(0);
                setMaximum(tables.length);
                for (DataTable table : tables) {
                    //fetch the set of rows from the table
                    List<DataRow> rows = table.getRows();
                    //for each row, either insert it, update it, delete it, or
                    //ignore it, depending on the row flag
                    for (DataRow row : rows) {
                        PreparedStatement stmt = null;
                        switch (row.getStatus()) {
                            case UPDATED:
                                stmt = ((AbstractSqlCommand)getCommand()).getUpdateStatement(conn, row);
                                conn.executeUpdate(stmt);
                                row.setStatus(DataRow.DataRowStatus.UNCHANGED);
                                break;
                            case INSERTED:
                                stmt = ((AbstractSqlCommand)getCommand()).getInsertStatement(conn, row);
                                conn.executeUpdate(stmt);
                                row.setStatus(DataRow.DataRowStatus.UNCHANGED);
                                break;
                            case DELETED:
                                stmt = ((AbstractSqlCommand)getCommand()).getDeleteStatement(conn, row);
                                conn.executeUpdate(stmt);
                                table.discardRow(row);
                                break;
                            default:
                                //do nothing
                                break;
                        }
                    }
                    table.fireDataTableChanged(TableChangeEvent.newSaveCompleteEvent(table));
                    setProgress(getProgress() + 1);
                }
                setProgress(getMaximum());
                conn.commit();
            }
        };
    }    
}
