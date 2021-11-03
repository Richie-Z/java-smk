/*
 * $Id: DataRelation.java,v 1.5 2005/10/10 17:00:57 rbair Exp $
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

package org.jdesktop.dataset;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author rbair
 */
public class DataRelation {
    /**
     * The Logger
     */
    private static final Logger LOG = Logger.getLogger(DataRelation.class.getName());
    
    //protected for testing
    /** Used as a prefix for auto-generated DataRelation names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataRelation";

    /** The shared instance of the NameGenerator for DataRelations not assigned a name. */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);

    /**
     * The DataSet that created this DataRelation. This is a readonly property
     */
    private DataSet dataSet;
    /**
     * The name of the DataRelation.
     */
    private String name;
    /**
     * The DataColumn from a "parent" DataTable to use in establishing this
     * relationship. If you need multiple columns in the parent, create a
     * single calculated column in the parent and use that instead
     */
    private DataColumn parentColumn;
    /**
     * The DataColumn from a "child" DataTable to use in establishing this
     * relationship. childColumn cannot equal DataColumn, but it can come
     * from the same table.
     */
    private DataColumn childColumn;
    
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
    /**
     * Create a new DataRelation
     */
    protected DataRelation(DataSet ds) {
        assert ds != null;
        this.dataSet = ds;
        name = NAMEGEN.generateName(this);
    }
    
    protected DataRelation(DataSet ds, String name) {
        this(ds);
        if (name != null) {
            setName(name);
        }
    }
    
    /**
     * @return the DataSet that this DataRelation belongs to
     */
    public DataSet getDataSet() {
        return dataSet;
    }
    
	/**
     * Set the name of the DataRelation
	 * @param name
	 */
	public void setName(String name) {
        if (this.name != name) {
            assert DataSetUtils.isValidName(name);
            assert !dataSet.hasElement(name);
            String oldName = this.name;
            this.name = name;
            pcs.firePropertyChange("name", oldName, name);
        }
	}

    /**
     * @return The name of this DataRelation
     */
	public String getName() {
		return name;
	}

    /**
     * @return the DataColumn that is the parent in this parent/child relation
     */
    public DataColumn getParentColumn() {
        return parentColumn;
    }

    /**
     * Sets the DataColumn that is the parent in this parent/child relation. The
     * value in the parentColumn for a specified range of field indices will be
     * used to generate the list of child Rows.
     * @param parentColumn
     */
    public void setParentColumn(DataColumn parentColumn) {
        if (this.parentColumn != parentColumn) {
            assert parentColumn != this.childColumn;
            DataColumn oldValue = this.parentColumn;
            this.parentColumn = parentColumn;
            pcs.firePropertyChange("parentColumn",  oldValue, parentColumn);
        }
    }

    /**
     * @return The child DataColumn in this parent/child relation
     */
    public DataColumn getChildColumn() {
        return childColumn;
    }

    /**
     * sets the child DataColumn in this parent/child relation
     * @param childColumn
     */
    public void setChildColumn(DataColumn childColumn) {
        if (this.childColumn != childColumn) {
            assert childColumn != this.parentColumn;
            DataColumn oldValue = this.childColumn;
            this.childColumn = childColumn;
            pcs.firePropertyChange("childColumn",  oldValue, childColumn);
        }
    }
    
    /**
     * Given a DataRow from the parent DataTable, return a list of
     * related DataRows from the child DataTable.
     * @param parentRow
     */
    public List<DataRow> getRows(DataRow parentRow) {
        //return an empty list if I don't have enough info to produce any
        //child rows. Short circuits this method
        if (parentColumn == null || childColumn == null || parentRow == null) {
            return Collections.unmodifiableList(Collections.EMPTY_LIST);
        }
        
        //make sure that this DataRow is from the parent DataTable!
        assert parentRow.getTable().equals(parentColumn.getTable());
        
        DataTable childTable = childColumn.getTable();
        Object parentKey = parentColumn.getTable().getValue(parentRow, parentColumn);
        List<DataRow> rows = new ArrayList<DataRow>();
        for (DataRow childRow : childTable.getRows()) {
            Object childKey = childTable.getValue(childRow,  childColumn);
            if (parentKey != null && childKey != null && parentKey.equals(childKey)) {
                rows.add(childRow);
            }
        }
        return Collections.unmodifiableList(rows);
    }

    /**
     * Given the index of a row in the parent DataTable, produce a corrosponding
     * list of related rows from the child DataTable
     *
     * @param parentRowIndex
     */
    public List<DataRow> getRows(int parentRowIndex) {
        if (parentColumn == null || childColumn == null || parentRowIndex < 0) {
            return Collections.unmodifiableList(Collections.EMPTY_LIST);
        }
        //NOTE: No need to check the parentRowIndex for an upper out of bounds
        //condition because the table will do so in the parentTable.getRow()
        //method call.
        DataTable parentTable = parentColumn.getTable();
        return getRows(parentTable.getRow(parentRowIndex));
    }
    
    /**
     * Given an array of DataRows, produce the union of the results for each
     * DataRow from the child DataTable
     *
     * @param parentRows
     */
    public List<DataRow> getRows(DataRow[] parentRows) {
        List<DataRow> rows = new ArrayList<DataRow>();
        for (DataRow parentRow : parentRows) {
            rows.addAll(getRows(parentRow));
        }
        return Collections.unmodifiableList(rows);
    }
    
    /**
     * Given an array if parent row indices, produce the union of the results
     * for each index from the child DataTable
     * 
     * @param parentRowIndices
     */
    public List<DataRow> getRows(int[] parentRowIndices) {
        //short circuit the method if I don't have enough info to produce proper
        //results
        if (parentColumn == null || childColumn == null || parentRowIndices == null) {
            return Collections.unmodifiableList(Collections.EMPTY_LIST);
        }
        
        DataTable parentTable = parentColumn.getTable();
        DataRow[] parentRows = new DataRow[parentRowIndices.length];
        for (int i=0; i<parentRows.length; i++) {
            parentRows[i] = parentTable.getRow(parentRowIndices[i]);
        }
        return getRows(parentRows);
    }
    
    /**
     * Given a List of indices, produce the union of the results for each index
     * from the child DataTable
     *
     * @param parentRowIndices
     */
    public List<DataRow> getRows(List<Integer> parentRowIndices) {
        //short circuit this method if I don't have enough info
        if (parentColumn == null || childColumn == null || parentRowIndices == null) {
            return Collections.unmodifiableList(Collections.EMPTY_LIST);
        }
        
        DataTable parentTable = parentColumn.getTable();
        DataRow[] parentRows = new DataRow[parentRowIndices.size()];
        for (int i=0; i<parentRows.length; i++) {
            parentRows[i] = parentTable.getRow(parentRowIndices.get(i));
        }
        return getRows(parentRows);
    }

    /**
     * Adds a PropertyChangeListener to this class for any changes to bean 
     * properties.
     *
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
   /**
     * Adds a PropertyChangeListener to this class for specific property changes.
     *
     * @param property The name of the property to listen to changes for.
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,  listener);
    }
    
    /**
     * Stops notifying a specific listener of any changes to bean properties.
     *
     * @param listener The listener to stop receiving notifications.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    /**
     * Stops notifying a specific listener of changes to a specific property.
     *
     * @param propertyName The name of the property to ignore from now on.
     * @param listener The listener to stop receiving notifications.
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName,  listener);
    }
}