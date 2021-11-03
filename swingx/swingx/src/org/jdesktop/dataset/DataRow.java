/*
 * $Id: DataRow.java,v 1.9 2005/10/15 11:43:19 pdoubleya Exp $
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

import static org.jdesktop.dataset.event.RowChangeEvent.EventType.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.jdesktop.dataset.event.DataTableListener;
import org.jdesktop.dataset.event.RowChangeEvent;

/**
 * <p>A <CODE>DataRow</CODE> contains a set of values within a {@link DataTable}; the <CODE>DataTable</CODE> defines
 * the {@link DataColumn DataColumns} in each row, and each row always has a value for each <CODE>DataColumn</CODE>. The
 * intersection of a <CODE>DataColumn</CODE> in a <CODE>DataRow</CODE> is called a cell, though cells are not exposed
 * in the <CODE>DataRow's</CODE> API. A <CODE>DataRow</CODE> always belongs to a <CODE>DataTable</CODE>; rows are added to a table using the
 * table's {@link DataTable#appendRow()} or {@link DataTable#appendRowNoEvent()} methods and cannot be instantiated
 * directly. Values in each cell can be changed using the {@link #setValue(String colName, Object value)} or
 * {@link #setValue(DataColumn col, Object value)} methods; values are always <CODE>Objects</CODE>.
 * 
 * <p>A <CODE>DataRow</CODE> always has a status, whose state is relative to the persistence
 * mechanism for the table. Statuses are returned using the row's <CODE>DataRowStatus</CODE> enumeration,
 * through {@link getStatus()} and can be changed using {@link #setStatus(DataRowStatus)}. Statuses
 * are normally managed without developer intervention. Normally, when rows are loaded from a persistent store, they
 * have a status <CODE>UNCHANGED</CODE>, which indicates that a {@link DataProvider} does not need to synchronize
 * them on the next {@link DataTable#save()} operation. A status of <CODE>INSERTED</CODE> means the row was added to the
 * table and should be added to the persistent store on the next {@link DataTable#save()}, <CODE>UPDATED</CODE> means changes
 * to a row loaded from the persistent store must be written back to the store, and <CODE>DELETED</CODE>
 * means the row should be removed from the store. Adding a row programmatically through the table's
 * {@link DataTable#appendRow()} or {@link DataTable#appendRowNoEvent()} methods gives the row a status of <CODE>INSERTED</CODE>.
 * 
 * <p>Each cell in a row can hold a reference value and a current value. The reference value is normally
 * the value as last read from the persistent store; this value is usually not modified programatically,
 * but may be overridden by a <CODE>DataProvider</CODE> if the row is re-read from the data store. The
 * {@link #setReferenceValue(DataColumn col, Object refValue)} method changes the reference value for a cell.
 * The current value is the value which is normally changed programmatically or through a user interface. When the current
 * value is different than the reference value, we say the cell has changed. If the row had <CODE>UNCHANGED</CODE> status before
 * the change, its status will change to <CODE>UPDATED</CODE>; this happens either on both the {@link #setValue(DataColumn, Object)}
 * or {@link #setReferenceValue(DataColumn, Object)} methods. An <CODE>INSERTED</CODE> or a <CODE>DELETED</CODE> row can have its cells
 * changed without affecting the row's status.
 * 
 * <p>For performance, comparing the reference to the current value of a cell is done using the Java
 * <CODE>==</CODE> comparison by default, which of course only works for object references that are the same. Identity
 * comparisons are used on all columns if the <CODE>DataTable's</CODE> <CODE>identityComparisonEnabled</CODE> property is set to <CODE>true</CODE>.
 * If this property is false, one can assign  a <CODE>Comparator</CODE> per-column in the table, or per-class (DataColumn type).
 * If both a per-class and a per-column <CODE>Comparator</CODE> have been assigned, the per-column <CODE>Comparator</CODE> is used.
 * If the property is false, and no <CODE>Comparator</CODE> is assigned, equality comparison (<CODE>.equals()</CODE>) is used. Note
 * that if you do not assign <CODE>Comparators</CODE>, you may have rows with an <CODE>UPDATED</CODE> status when the value is
 * actually the same, if the <CODE>==</CODE> comparison fails. For accuracy, you should assign a <CODE>Comparator</CODE>, but for efficiency
 * (e.g. columns with large content sizes), you may want a <CODE>Comparator</CODE> that just skips the test altogether.
 * 
 * <p><CODE>DataRows</CODE> support both property change listeners and event listeners; note that row status is a property of
 * the row. Events broadcast {@link RowChangeEvent} messages.
 * @author Richard Bair
 * @author Patrick Wright
 */
public class DataRow {
    /**
     * Flag indicating the status of the DataRow; these are described in the class JavaDoc.
     */
    public enum DataRowStatus {
        /**
         * The row was inserted into the table.
         */
        INSERTED,
        /**
         * The row was deleted from the table.
         */
        DELETED,
        /**
         * The row has been updated, at least one cell is modified.
         */
        UPDATED,
        /**
         * The row has not been modified; no cells are modified.
         */
        UNCHANGED
    };
    
    //used for communicating changes to this JavaBean, especially necessary for
    //IDE tools, but also handy for any other component listening to this row
    /**
     * Property change support for the row.
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /**
     * The DataTable that created this DataRow. This is an immutable property
     */
    private DataTable table;
    
    /**
     * The status of this DataRow. By default, it is set to INSERTED. It is
     * possible to change the status manually, although during certain
     * lifecycle events it is automatically changed, such as after the data
     * is saved to disk, or a change is made.
     */
    private DataRowStatus status = DataRowStatus.INSERTED;
    
    /**
     * The data associated with this Row. This structure implies that when a
     * DataColumn is removed from the DataTable, then each row will have to be
     * traversed and the DataColumn removed from the Map, along with the cell
     */
    private Map<DataColumn,DataCell> cells = new HashMap<DataColumn,DataCell>();
    
    /**
     * Create a new DataRow. The table creating this row must be passed in; rows are
     * normally created by calling the DataTable's appendRow() or appendRowNoEvent methods.
     * The new row will have a cell for each column the the table, with the value being the
     * default value for that DataColumn.
     */
    protected DataRow(DataTable table) {
        assert table != null;
        this.table = table;
        
        //construct the cells based on the columns in the table
        //add a cell for each column
        for (DataColumn col : this.table.getColumns()) {
            addCell(col);
        }
    }
    
    /**
     * Sets the given refence value to the column with the given name.
     *
     * @param colName The name of the DataColumn to change the value for.
     * @param value The new column reference value; may cause the row status to change, see class
     * comments.
     */
    public void setReferenceValue(String colName, Object value) {
        setReferenceValue(table.getColumn(colName), value);
    }
    
    /**
     * Sets the given refence value to the specified DataColumn.
     *
     * @param col The DataColumn to change the value for.
     * @param value The new column reference value; may cause the row status to change, see class
     * comments.
     */
    public void setReferenceValue(DataColumn col, Object value) {
        assert col != null;
        
        // get the cell
        getCell(col).setReferenceValue(table, col, value);
        
        // determine new status
        DataRowStatus newStatus = deriveRowStatus();
        if ( newStatus != status ) {
            setStatus(newStatus);
        }
    }
    
    /**
     * Sets the given value to the column with the given name.
     *
     * @param colName The name of the DataColumn to change the value for.
     * @param value The new column value; may cause the row status to change, see class
     * comments.
     */
    public void setValue(String colName, Object value) {
        DataColumn col = table.getColumn(colName);
        setValue(col, value);
    }
    
    /**
     * Sets the given value to the specified DataColumn with the given name.
     * @param col The DataColumn for which to set the value.
     * @param value The new column value; may cause the row status to change, see class
     * comments.
     */
    public void setValue(DataColumn col, Object value) {
        assert col != null;
        
        Object oldValue = getValue(col);
        DataCell cell = getCell(col);
        boolean wasChanged = cell.changed;
        getCell(col).setValue(table, col, value);
        
        if ( wasChanged != cell.changed ) {
            fireDataRowChanged(RowChangeEvent.newCellChangedEvent(this, col, oldValue));
        }
        
        DataRowStatus oldStatus = status;
        DataRowStatus newStatus = deriveRowStatus();
        if ( newStatus != oldStatus ) {
            setStatus(newStatus);
        }
    }
    
    /**
     * Resets all columns in the row to have their current value be their reference value, effectively reverting
     * the effects of calling {@link #setValue(DataColumn, Object)} till now. After this method call, the row will once
     * again regain its normal status (INSERTED or UNCHANGED). For a discussion on reference
     * values, see the class docs.
     */
    public void resetAllToReferenceValue() {
        List<DataColumn> cols = table.getColumns();
        for ( DataColumn col : cols ) {
            resetToReferenceValue(col);
        }
    }  
    
    /**
     * Resets the named column in the row to its reference value, effectively reverting
     * the effects of calling {@link #setValue(DataColumn, Object)} till now. After this method call, the row may once
     * again regain its normal status (INSERTED or UNCHANGED) if there are no longer any
     * modified columns. For a discussion on reference values, see the class docs.
     * @param colName The column name for which to reset to the reference value.
     */
    public void resetToReferenceValue(String colName) {
        resetToReferenceValue(table.getColumn(colName));
    }
    
    /**
     * Resets the DataColumn in the row to its reference value, effectively reverting
     * the effects of calling {@link #setValue(DataColumn, Object)} till now. After this method call, the row may once
     * again regain its normal status (INSERTED or UNCHANGED) if there are no longer any
     * modified columns. For a discussion on reference values, see the class docs.
     * @param col The DataColumn for which to reset to the reference value.
     */
    public void resetToReferenceValue(DataColumn col) {
        setValue(col, getCell(col).referenceValue);
        /* CLEAN
         getCell(col).revert();
        DataRowStatus newStatus = deriveRowStatus();
        if ( newStatus != status ) {
            setStatus(newStatus);
        }
         */
    }
    
    /**
     * Returns the current reference value for the column.
     * @return the reference value at the given column name; see class documentation
     * for a discussion of reference values.
     * @param colName The column name for which to lookup the reference value.
     */
    public Object getReferenceValue(String colName) {
        return getReferenceValue(table.getColumn(colName));
    }
    
    /**
     * Returns the value for the given column.
     * @param colName The column for which to return the current value.
     * @return the value at the given column name
     */
    public Object getValue(String colName) {
        return getValue(table.getColumn(colName));
    }

    /**
     * Returns the reference value for the column.
     * @return the reference value for the given DataColumn; see class documentation
     * for a discussion of reference values.
     * @param col The DataColumn for which to return the reference value.
     */
    public Object getReferenceValue(DataColumn col) {
        assert col != null;
        String exp = col.getExpression();
        if (exp == null || exp.equals("")) {
            DataCell cell = getCell(col);
            return cell.referenceValue;
        } else {
            return col.getValueForRow(this);
        }
    }
    
    /**
     * Returns the current value for the column.
     * @return the value for the given DataColumn.
     * @param col The DataColumn for which to return the current value.
     */
    public Object getValue(DataColumn col) {
        assert col != null;
        String exp = col.getExpression();
        if (exp == null || exp.equals("")) {
            DataCell cell = getCell(col);
            return cell.value;
        } else {
            return col.getValueForRow(this);
        }
    }
    
    /**
     * Returns the DataCell for the column; if there is none, creates one
     * initialized to the column's default value.
     */
    protected DataCell getCell(DataColumn col) {
        DataCell cell = cells.get(col);
        if (cell == null && col.getTable() == table) {
            cell = addCell(col);
        }
        return cell;
    }
    
    /**
     * Returns the DataTable that owns this row.
     * @return The DataTable that owns this DataRow.
     */
    public DataTable getTable() {
        return table;
    }
    
    /**
     * Returns the current row status.
     * @return The current DataRowStatus enumerated value for this DataRow;
     * see class comments.
     */
    public DataRowStatus getStatus() {
        return status;
    }
    
    /**
     * Returns true if the current value for the column is different from its reference value.
     * @return true if the given column has been modified; see class comments.
     * @param colName The column name to check for modification.
     */
    public boolean isModified(String colName) {
        return isModified(table.getColumn(colName));
    }
    
    /**
     * Returns true if the current value for the column is different from its reference value.
     * @return true if the given column has been modified; see class comments.
     * @param col The DataColumn to check for modification.
     */
    public boolean isModified(DataColumn col) {
        return cells.get(col).changed;
    }
    
    /**
     * CLEAN: remove these once getReference... methods have been approved (they replace getOriginal...)
     * 
     * @return the original value for the column in this row. If the cell was not
     * assigned a value explicitly, this is the column's default value. Otherwise it is
     * the first value assigned to the column on this row.
     * @deprecated use getReferenceValue(String colName)
     * @param colName 
     */
    public Object getOriginalValue(String colName) {
        return getReferenceValue(colName);
    }
    
    /**
     * CLEAN: remove these once getReference... methods have been approved (they replace getOriginal...)
     * 
     * 
     * @return the original value for the column in this row. If the cell was not
     * assigned a value explicitly, this is the column's default value. Otherwise it is
     * the first value assigned to the column on this row.
     * @deprecated use getReferenceValue(DataColumn col)
     * @param col 
     */
    public Object getOriginalValue(DataColumn col) {
        return getReferenceValue(col);
    }
    
    /**
     * Changes the row's status; a status change event will be broadcast if the status is
     * different from the current status. Changing an UPDATED row's status to UNCHANGED will 
     * overwrite reference values in all cells with the current values; modifying any column 
     * subsequently will change the row status if the columns new value is different than 
     * this reference value.
     *
     * @param status The new status for the row.
     */
    public void setStatus(DataRowStatus status) {
        if (this.status != status) {
            DataRowStatus priorStatus = this.status;
            this.status = status;
            
            pcs.firePropertyChange("status", priorStatus, status);
            
            if (this.status == DataRowStatus.UNCHANGED) {
                // overwrite all reference values with current values
                List<DataColumn> cols = table.getColumns();
                for ( DataColumn col : cols ) {
                    // note: we don't send a row-cell change event here because it's
                    // the reference val, not the current val, that is changing
                    DataCell cell = getCell(col);
                    if ( cell.changed ) cell.overwriteReference();
                }
            }
        }
    }
    
    /**
     * Used internally to fire events for row changes.
     * @param evt The RowChangeEvent to fire.
     */
    public void fireDataRowChanged(RowChangeEvent evt) {
        table.fireRowChanged(evt);
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
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Row #");
        buffer.append(table.indexOfRow(this));
        buffer.append(" [ ");
        int i=0;
        for (DataCell c : cells.values()) {
            buffer.append(c.value);
            if (i < cells.size() -1) {
                buffer.append(", ");
            }
            i++;
        }
        buffer.append(" ]");
        return buffer.toString();
    }
    
    /**
     * Used internally to add a cell to the row. If a cell for this column
     * already exists, returns that cell.
     *
     * @param col The DataColumn for which to add the cell.
     */
    protected DataCell addCell(DataColumn col) {
        DataCell cell = cells.get(col);
        if ( cell == null ) {
            cell = new DataCell(col);
            cells.put(col, cell);
        }
        return cell;
    }
    
    /**
     * Used internally to determine the row's current status, called after a cell's
     * value is changed. The general rule is that UNCHANGED rows will be marked UPDATED if
     * at least one cell is changed, and UPDATED rows will be marked UNCHANGED if no cells
     * are changed. INSERTED and DELETED rows do not change status in this method.
     */
    protected DataRowStatus deriveRowStatus() {
        DataRowStatus derived;
        switch (status) {
            case INSERTED: // fall-thru
            case DELETED:
                derived = status;
                break;
            case UPDATED: // fall-thru
            case UNCHANGED:
                boolean any = false;
                for ( DataCell cell : cells.values()) {
                    if ( cell.changed ) {
                        any = true;
                        break;
                    }
                }
                derived = ( any ? DataRowStatus.UPDATED : DataRowStatus.UNCHANGED );
                break;
            default:
                throw new RuntimeException("deriveRowStatus() has no case for row status of " + status);
        }
        return derived;
    }
    
    /**
     * Class used internally to intersect a DataColumn for the row; holds the reference
     * and current values. As these are populated per-column per-row, a minimum of state
     * information is kept in each cell, and methods receive contextual information necessary
     * for the task to reduce stored state.
     */
    private static final class DataCell {
        /**
         * The reference value for the cell.
         */
        Object referenceValue;
        /**
         * The current value for the cell.
         */
        Object value;
        /**
         * True if the current value is different from the reference value.
         */
        boolean changed;
        /**
         * True is the current value was set for the first time.
         */
        boolean valueSet;
        
        /**
         * Instantiates a cell for a given column.
         * @param col The DataColumn for which to create a cell.
         */
        DataCell(DataColumn col){
            this.value = this.referenceValue = col.getDefaultValue();
        }
        
        /**
         * Sets the reference value for the cell.
         * @param table The DataTable for the row.
         * @param col The DataColumn for the cell.
         * @param newRef The new reference value for the cell.
         */
        public void setReferenceValue(DataTable table, DataColumn col, Object newRef) {
            referenceValue = newRef;
            changed = isSame(table, col, referenceValue, newRef);
        }
        
        /**
         * Sets the current value for the cell.
         * @param table The DataTable for the row.
         * @param col The DataColumn for the cell.
         * @param newValue The new current value for the cell.
         */
        public void setValue(DataTable table, DataColumn col, Object newValue) {
            // if this is the first call, set reference value for cell
            if ( !valueSet ) {
                value = referenceValue = newValue;
                changed = false;
                valueSet = true;
                return;
            }
            
            // if the new value is our current reference value, then cell is
            // no longer changed
            if ( isSame(table, col, referenceValue, newValue )) {
                value = referenceValue;
                changed = false;
                return;
            } else {
                // not the same as our reference value
                if ( ! isSame( table, col, value, newValue )) {
                    value = newValue;
                    changed = true;
                }
            }
        }
        
        /**
         * Overwrites the reference value for the cell with its current value.
         */
        public void overwriteReference() {
            referenceValue = value;
            changed = false;
        }

        /**
         * Returns true if the two values are the same, using either <CODE>==</CODE>, or a <CODE>Comparator</CODE>; see 
         * class docs on DataRow.
         * @param table The DataTable for the row.
         * @param col The DataColumn for the cell.
         * @param baseValue The base value we are comparing.
         * @param newValue The new value we are comparing with.
         * @return True if the value are the same.
         */
        private boolean isSame(DataTable table, DataColumn col, Object baseValue, Object newValue) {
            if ( table.isIdentityComparisonEnabled()) {
                return newValue == baseValue;
            } else {
                Comparator comp = null;
                if ( table.hasColumnComparator(col)) {
                    comp = table.getColumnComparator(col);
                } else {
                    // this returns a default .equals() comparator if none
                    // is assigned to the column type
                    comp = table.getClassComparator(col.getType());
                }
                return comp.compare(baseValue, newValue) == 0;
            }
        }
    }
}