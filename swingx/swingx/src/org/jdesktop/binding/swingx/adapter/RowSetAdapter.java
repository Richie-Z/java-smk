/*
 * $Id: RowSetAdapter.java,v 1.2 2005/10/10 17:01:10 rbair Exp $
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

package org.jdesktop.binding.swingx.adapter;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.RowSet;
import javax.swing.table.AbstractTableModel;

/**
 * Placeholder for future RowSet TableModel adapter to enable
 * easy connectivity to JDBC RowSet functionality. For more details see the
 * <a href="http://www.jcp.org/en/jsr/detail?id=114">JSR 114</a>.
 * Note: This class is not yet fully functional.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class RowSetAdapter extends AbstractTableModel {
    private RowSet rowset;
    private ResultSetMetaData metaData;

    /**
     * Note: This class is not yet functional, but will be completed
     * in JDNC Milestone5.
     *
     * Creates a table model adapter which binds to the specified tabular
     * data model.
     *
     * @param rowset RowSet object containing the tabular data
     * @throws NullPointerException if rowset is null
     * @throws SQLException
     */
    private RowSetAdapter(RowSet rowset) throws java.sql.SQLException{
        if (rowset == null) {
            throw new NullPointerException("rowset cannot be null");
        }
        this.rowset = rowset;
        this.metaData = rowset.getMetaData();
    }

    public Class getColumnClass(int columnIndex) {
        Class klass = null;
        try {
            klass = Class.forName(metaData.getColumnClassName(
                translateAdapterColumn(columnIndex)));
        } catch (Exception e) {

        }
        return klass;
    }

    public int getRowCount() {
        //REMIND(aim): awk! size() doesn't exist in 1.4.2....
        //return rowset.size();
        return 0;
    }

    public int getColumnCount() {
        int columnCount = 0;
        try {
            columnCount = metaData.getColumnCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        try {
            synchronized(rowset) {
                rowset.absolute(translateAdapterRow(rowIndex));
                value = rowset.getObject(translateAdapterColumn(columnIndex));
            }
        } catch (Exception e) {

        }
        return value;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //REMIND(aim): investigate isReadOnly vs isWritable vs isDefinitelyWritable
        boolean editable = false;
        try {
            editable = metaData.isWritable(translateAdapterColumn(columnIndex));
        } catch (Exception e) {

        }
        return editable;
    }

    protected int translateAdapterColumn(int columnIndex) {
        return columnIndex + 1;
    }

    protected int translateDataColumn(int dataColumnIndex) {
        return dataColumnIndex - 1;
    }

    protected int translateAdapterRow(int rowIndex) {
        return rowIndex + 1;
    }

    protected int translateDataRow(int dataRowIndex) {
        return dataRowIndex - 1;
    }

}
