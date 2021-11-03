/*
 * $Id: DataTable.java,v 1.16 2005/10/15 11:43:20 pdoubleya Exp $
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

import org.jdesktop.dataset.event.DataTableListener;
import org.jdesktop.dataset.event.RowChangeEvent;
import org.jdesktop.dataset.event.TableChangeEvent;
import static org.jdesktop.dataset.event.TableChangeEvent.EventType.*;



/**
 * A <CODE>DataTable</CODE> represents a two-dimensional non-visual data structure composed of {@link DataRow DataRows}, where each row has
 * one or more {@link DataColumn DataColumns}; the set of columns is specified in the table, and applies to all rows. A
 * <CODE>DataTable</CODE> always belongs to a {@link DataSet}, and in fact must be created using the {@link DataSet#createTable()}
 * method, as a table cannot exist outside of a <CODE>DataSet</CODE>. For an overview of the DataSet API, see
 * {@link https://jdnc.dev.java.net/documentation/dataset}.
 *
 * Table data is loaded either programmatically (by your code, one row and one value at a time) or by a {@link DataProvider}. A
 * table can have a single <CODE>DataProvider</CODE>, which is responsible for loading the table from, and storing table changes back
 * to, some data source. See documentation for <CODE>DataProvider</CODE> and its subclasses to see what sources are supported.
 *
 * A table has a name property, which is unique within the
 * <CODE>DataSet</CODE> that owns it. Names can be specified when the table is created, or will be assigned automatically.
 *
 * A table must have columns assigned to it in order to store data in any row. The intersection of a column and row is called a
 * cell. Columns are instances of <CODE>DataColumn</CODE>, and each
 * has a name unique within the table. Columns add added using {@link #createColumn()}, {@link #createColumn(String)} or
 * {@link #createColumns(String...)} and are attached to the table from which they are created. As a rule, any methods that
 * are column-specific (like {@link #setValue(DataColumn, Object)} can take either the <CODE>DataColumn</CODE> instance or the name
 * of the <CODE>DataColumn</CODE> as a parameter. Columns can be removed from the table using {@link #dropColumn(String)}. Adding
 * a column to a table with rows in it will give the cells for the new column the column's {@link DataColumn#getDefaultValue() default value}.
 * Dropping a column from a table will cause the data in the cells of that column to be lost.
 *
 * A table has 0...n rows at any time. Blank rows are added using {@link #appendRow()} and {@link #appendRowNoEvent()} and remain
 * in the table in the order they were added to it, so can be retrieved by a 0-based index using {@link getRow(int)}. New rows start with
 * the current set of columns in the table, and adding or dropping columns from a table will have those automatically added or dropped
 * from all rows in the table. To remove a row, use {@link #deleteRow(int)} or {@link #deleteRow(DataRow)} if you want the table's
 * DataProvider to remove the row as well from the table's data source. Deleted rows are maked with a row status of DELETED and
 * remain in memory until the DataProvider removes them. To drop a row completely, use {@link #discardRow(int)} or {@link #discardRow(DataRow)}.
 * Discarding rows causes them to be dropped immediately from the table, and the deletion will not be recorded by the table's DataProvider.
 *
 * Once you have rows in the table, you can change values in the row using {@link #setValue(int, String, Object)}, or through the
 * equivalent methods on the {@link DataRow} itself. You can retrieve row values using {@link #getValue(int, String)} or equivalent
 * methods on the row. Rows have a certain status within the table, as described in the documentation for the {@link DataRow} class. To list
 * rows in the table, use {@link getRows()} or {@link getRow(int)}.
 * @author Richard Bair
 * @author Patrick Wright
 */
public class DataTable {
    /**
     * The Logger
     */
    private static final Logger LOG = Logger.getLogger(DataTable.class.getName());
    
    //protected for testing
    /** Used as a prefix for auto-generated names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataTable";
    
    /** The shared instance of the NameGenerator for DataTables not assigned a name. */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);
    
    //used for communicating changes to this JavaBean, especially necessary for
    //IDE tools, but also handy for any other component listening to this table
    /**
     * Used for communicating changes to this JavaBean, especially necessary for IDE tools, but also handy for any other component listening to this table.
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /**
     * A reference to the DataSet to which this DataTable belongs
     */
    private DataSet dataSet;
    
    /**
     * The DataProvider that is used to manage this DataTables data. It is
     * technically possible for a DataProvider to populate a DataTable to which
     * it is not associated, but the results are undefined
     */
    private DataProvider dataProvider;
    
    /**
     * The name of this DataTable
     */
    private String name;
    
    /**
     * Mapping of DataColumn.name to DataColumn. Extremely useful for
     * discovering the column object associated with a name. The resolver code
     * responsible for locating a particular data cell will first convert the
     * column name to a DataColumn, and then use the DataColumn in the hash for
     * locating the data. This is done so that:
     * <ol>
     * <li>The lookup is constant time (or close to it)</li>
     * <li>Avoid looping through a columns list looking for the column</li>
     * <li>More efficient handling if the column name changes in a large table,
     * which would not be possible if the column name was used in the hash</li>
     * </ol>
     */
    protected Map<String,DataColumn> columns = new LinkedHashMap<String,DataColumn>();
    
    /**
     * A map of Comparators by Class type; only one Comparator is allowed per class.
     * This is used by DataRows to compare a new value for a cell with the
     * reference value, to determine if the cell is "changed" or not.
     */
    protected Map<Class,Comparator> classComparators = new HashMap<Class,Comparator>();
    
    /**
     * A map of Comparators by DataColumn; only one Comparator is allowed per column.
     * This is used by DataRows to compare a new value for a cell with the
     * reference value, to determine if the cell is "changed" or not.
     */
    protected Map<DataColumn,Comparator> columnComparators = new HashMap<DataColumn,Comparator>();
    
    /**
     * A comparator for checking for changes between reference and current values for a column
     * in a row, used when identityComparisonEnabled is false, and no column or class comparators
     * are assigned.
     */
    private final static Comparator EQUALS_COMPARATOR = new Comparator() {
        public boolean equals(Object obj) {
            return obj == this;
        }
        
        public int compare(Object o1, Object o2) {
            return ( o1.equals(o2) ? 0 : -1 );
        }
    };
    
    /**
     * The list of DataRows in this DataTable. The DataRow actually contains the
     * data at the various cells.
     */
    protected List<DataRow> rows = new ArrayList<DataRow>();
    
    /**
     * Every DataTable contains a list of selectors, which manage tracking
     * "selected" state in a DataTable. This is necessary for proper handling
     * of master/detail relationships, also known as parent/child. This data
     * structure maps the data selectors name to the selector itself.
     */
    protected Map<String,DataSelector> selectors = new HashMap<String,DataSelector>();
    
    /**
     * Indicates whether deleting rows is supported. Attempts to
     * delete a row when row deletion is not supported will be ignored
     */
    private boolean deleteRowSupported = true;
    
    /**
     * Indicates whether appending rows is supported. Attempts to append a
     * row when row appending is not supported will be ignored.
     */
    private boolean appendRowSupported = true;
    
    /**
     * Indicates whether new values applied to any row are compared with the
     * cell's reference value using == (instance identity comparison). If true,
     * identity comparison is used; if false, defaults to using .equals() on
     * the two values, unless a Comparator is assigned to the column class or
     * the column. See class docs for {@link DataRow} and {@link DataTable} for
     * more details on value comparison and row status. Defaults to true for new
     * tables.
     */
    private boolean identityComparisonEnabled;
    
    /**
     * A list of DataTableListeners to notify of various events
     */
    private List<DataTableListener> listeners = new ArrayList<DataTableListener>();
    
    /**
     * A PropertyChangeListener for listening to name property change events
     * on DataSelectors and DataColumns. The listener makes sure that the name
     * used as the hash key in the selectors and columns maps is always
     * accurate.
     */
    private final PropertyChangeListener nameChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof DataSelector) {
                DataSelector sel = selectors.remove(evt.getOldValue());
                if (sel != null) {
                    //name changed
                    selectors.put((String)evt.getNewValue(), sel);
                }
            } else if (evt.getSource() instanceof DataColumn) {
                DataColumn c = columns.remove(evt.getOldValue());
                if (c != null) {
                    //name changed
                    columns.put((String)evt.getNewValue(), c);
                }
            }
        }
    };
    
    /**
     * Create a new DataTable for a specified DataSet, with an
     * automatically-generated name unique to the DataSet.
     *
     * @param ds The DataSet to which the table will be added.
     */
    protected DataTable(DataSet ds) {
        assert ds != null;
        this.dataSet = ds;
        this.name = NAMEGEN.generateName(this);
        this.identityComparisonEnabled = true;
    }
    
    /**
     * Create a new DataTable for a specified DataSet, with the
     * specified name.
     *
     * @param ds The DataSet to which the table will be added.
     * @param name The name of the table.
     */
    protected DataTable(DataSet ds, String name) {
        this(ds);
        if (name != null) {
            setName(name);
        }
    }
    
    /**
     * creates a new DataColumn, and adds it to this DataTable. A name will
     * be automatically generated for the DataColumn.
     *
     * @return the DataColumn that was created
     */
    public DataColumn createColumn() {
        return createColumn(null);
    }
    
    /**
     * Creates a new DataColumn with the given name, and adds it to this
     * DataTable.
     *
     * @param colName the name to give the DataColumn. If the name is invalid
     * or has already been used in the DataSet, an assertion will be raised
     *
     * @return the new DataColumn
     */
    public DataColumn createColumn(String colName) {
        DataColumn col = new DataColumn(this);
        if (colName != null) {
            col.setName(colName);
        }
        columns.put(col.getName(),  col);
        col.addPropertyChangeListener("name", nameChangeListener);
        
        fireDataTableChanged(TableChangeEvent.newColumnAddedEvent(this, col));
        return col;
    }
    
    /**
     * Adds a column to the table for each name in the supplied array of Strings.
     * @param colNames Array of column names to add to the table.
     */
    public void createColumns(String... colNames) {
        for (String name : colNames) {
            createColumn(name);
        }
    }
    
    /**
     * Drops the column with the given name. If there is no column by the
     * specified name, then nothing is done.
     * @param colName Name of the column to drop.
     */
    public void dropColumn(String colName) {
        DataColumn col = columns.remove(colName);
        if (col != null) {
            col.removePropertyChangeListener("name",  nameChangeListener);
            //remove any remaining references to this column in the DataRelations
            for (DataRelation r : dataSet.getRelations()) {
                DataColumn c = r.getChildColumn();
                if (c == col) {
                    r.setChildColumn(null);
                }
                c = r.getParentColumn();
                if (c == col) {
                    r.setParentColumn(null);
                }
            }
            columnComparators.remove(col);
            
            fireDataTableChanged(TableChangeEvent.newColumnRemovedEvent(this, col));
        }
    }
    
    /**
     * @return a List of DataColumns representing all of the columns in this
     * DataTable.
     */
    public List<DataColumn> getColumns() {
        return Collections.unmodifiableList(new ArrayList<DataColumn>(columns.values()));
    }
    
    /**
     * @param colName the name of the column that you want to retrieve
     * @return the DataColumn with the given name. If the given name does not
     * map to a DataColumn in this DataTable, then null is returned.
     */
    public DataColumn getColumn(String colName) {
        return columns.get(colName);
    }
    
    /**
     * @return an unmodifiable list of all of the rows in this DataTable. The
     * individual DataRow elements are modifiable, but the List is not.
     * This includes all rows, regardless of status--inserted, updated, deleted
     * and unchanged.
     */
    public List<DataRow> getRows() {
        return Collections.unmodifiableList(rows);
    }
    
    /**
     * @param index the Index in this table associated with the DataRow to be
     * retrieved. This must be &gt;0, and &lt;rows.size()
     * @return the DataRow at the given 0 based index. The index must be valid
     */
    public DataRow getRow(int index) {
        assert index >= 0 && index < rows.size();
        return rows.get(index);
    }
    
    /**
     * @return the number of DataRows in this DataTable; this includes all rows, regardless
     * of status--inserted, updated, deleted and unchanged.
     */
    public int getRowCount() {
        return rows.size();
    }
    
    /**
     * Creates and returns a selector. The DataSelector will have a generated
     * name by default.
     * @return a selector on the table.
     */
    public DataSelector createSelector() {
        return createSelector(null);
    }
    
    /**
     * Creates and returns a selector with the given name. If the name is
     * invalid, an assertion will be raised
     * @param name the name for the new DataSelector
     */
    public DataSelector createSelector(String name) {
        DataSelector sel = new DataSelector(this);
        if (name != null) {
            sel.setName(name);
        }
        if (rows.size() > 0) {
            sel.setRowIndex(0);
        }
        selectors.put(sel.getName(),  sel);
        sel.addPropertyChangeListener("name", nameChangeListener);
        return sel;
    }
    
    /**
     * @return a List of DataSelectors associated with this DataTable.
     */
    public List<DataSelector> getSelectors() {
        return Collections.unmodifiableList(new ArrayList<DataSelector>(selectors.values()));
    }
    
    /**
     * @param name the name of the selector to create or return
     * @return the DataSelector with the given name. If no such DataSelector
     * exists, then a new DataSelector will be created and added to this
     * DataTable by the given name.
     */
    public DataSelector getSelector(String name) {
        //if the given selector doesn't exist, create it implicitly
        if (!selectors.containsKey(name)) {
            return createSelector(name);
        } else {
            return selectors.get(name);
        }
    }
    
    /**
     * Drop the given selector
     * @param selector the selector to drop
     */
    public void dropSelector(DataSelector selector) {
        dropSelector(selector.getName());
    }
    
    /**
     * drops the given named selector. If a selector by that name does not
     * exist, then do nothing
     * @param selectorName the name of the selector to drop
     */
    public void dropSelector(String selectorName) {
        DataSelector sel = selectors.remove(selectorName);
        if (sel != null) {
            sel.removePropertyChangeListener("name",  nameChangeListener);
        }
    }
    
    /**
     * Sets the given name to be the name of this DataTable
     * @param name The new name of the table; should be unique within the table's DataSet.
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
     * Returns the name of this <CODE>DataTable</CODE>.
     * @return the name of this DataTable
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns true if rows in this table compare reference with current values
     * using the Java identity comparison (==); see class
     * JavaDocs, and docs for {@link DataRow}.
     * @return true if rows in this table compare reference with current values
     * using the Java identity comparison (==); see class
     * JavaDocs, and docs for {@link DataRow}.
     */
    public boolean isIdentityComparisonEnabled() {
        return identityComparisonEnabled;
    }
    
    /**
     * Sets whether rows in this table compare reference with current values
     * using the Java identity comparison (==); see class
     * JavaDocs, and docs for {@link DataRow}.
     * @param identityComparisonEnabled if true, table will use identity (<CODE>==</CODE>) comparison in checking for row changes.
     */
    public void setIdentityComparisonEnabled(boolean identityComparisonEnabled) {
        this.identityComparisonEnabled = identityComparisonEnabled;
    }
    
    /**
     * Assigns a Comparator to use in comparing current and reference values
     * in DataRow cells, according to a DataColumn's type/class; see class
     * JavaDocs, and docs for {@link DataRow}. Only one Comparator can be assigned
     * per class; calling this multiple times on the same class will overwrite
     * the Comparator assignment.
     *
     * @param klass The Class to bind the Comparator with.
     * @param comp The Comparator used.
     */
    public void setClassComparator(Class klass, Comparator comp) {
        assert klass != null;
        assert comp != null;
        classComparators.put(klass, comp);
    }
    
    /**
     * Removes assignment of a Comparator for a DataColumn type/class; see class
     * JavaDocs, and docs for {@link DataRow}.
     *
     * @param klass The class for which the Comparator will be removed. Fails silently
     * if none is assigned.
     */
    public void removeClassComparator(Class klass) {
        assert klass != null;
        classComparators.remove(klass);
    }
    
    /**
     * @return true if the given Class has a Comparator assigned; see class
     * JavaDocs, and docs for {@link DataRow}.
     */
    public boolean hasClassComparator(Class klass) {
        return classComparators.get(klass) != null;
    }
    
    /**
     * Returns a Comparator bound to a given Class for comparison of
     * current and reference values in a DataRow. Will return a
     * default Comparator that uses .equals() comparison if none
     * was explicitly assigned.
     *
     * @param klass The class for which to retrieve a Comparator
     * @return The Comparator bound to the Class, or a default Comparator.
     */
    public Comparator getClassComparator(Class klass) {
        Comparator comp = classComparators.get(klass);
        if ( comp == null ) {
            comp = EQUALS_COMPARATOR;
        }
        return comp;
    }
    
    /**
     * Assigns a specific Comparator to a DataColumn, for use in comparing
     * changes to DataRow cells; see class
     * JavaDocs, and docs for {@link DataRow}.
     *
     * @param col The DataColumn to which the Comparator is bound.
     * @param comp The Comparator instance.
     */
    public void setColumnComparator(DataColumn col, Comparator comp) {
        assert col != null;
        assert comp != null;
        columnComparators.put(col, comp);
    }
    
    /**
     * Removes the specific Comparator assigned to the column, if any; fails
     * silently if none assigned; see class
     * JavaDocs, and docs for {@link DataRow}.
     *
     * @param col The DataColumn for which to remove the bound Comparator.
     */
    public void removeColumnComparator(DataColumn col) {
        assert col != null;
        columnComparators.remove(col);
    }
    
    /**
     * Returns true if the column has a specific comparator assigned to it;
     * with {@link setColumnComparator(DataColumn, Comparator)}; see class
     * JavaDocs, and docs for {@link DataRow}.
     *
     * @param col The DataColumn to find a Comparator for.
     * @return true if a Comparator has been bound to this DataColumn.
     */
    public boolean hasColumnComparator(DataColumn col) {
        return columnComparators.get(col) != null;
    }
    
    /**
     * Returns a Comparator bound to a given Column. Will return the comparator
     * for the column's class if no specific comparator was assigned to the column; see class
     * JavaDocs, and docs for {@link DataRow}.
     *
     * @param col The DataColumn for which to find the Comparator.
     * @return The Comparator bound to this column, or the Comparator bound to the
     * column's class; see {@link #getClassComparator(Class klass)}
     */
    public Comparator getColumnComparator(DataColumn col) {
        assert col != null;
        Comparator comp = columnComparators.get(col);
        if ( comp == null ) {
            comp = getClassComparator(col.getType());
        }
        return comp;
    }
    
    /**
     * Returns true if deletion of rows is supported.
     * @return true if deletion of rows is supported
     */
    public boolean isDeleteRowSupported() {
        return deleteRowSupported;
    }
    
    /**
     * Sets the deleteRowSupported flag
     * @param deleteRowSupported the new value for deleteRowSupported
     */
    public void setDeleteRowSupported(boolean deleteRowSupported) {
        if (this.deleteRowSupported != deleteRowSupported) {
            boolean oldValue = this.deleteRowSupported;
            this.deleteRowSupported = deleteRowSupported;
            pcs.firePropertyChange("deleteRowSupported", oldValue, deleteRowSupported);
        }
    }
    
    /**
     * Returns true if appending rows is supported.
     * @return true if appending rows is supported
     */
    public boolean isAppendRowSupported() {
        return appendRowSupported;
    }
    
    /**
     * Sets whether appending rows is supported
     * @param appendRowSupported the new value for appendRowSupported
     */
    public void setAppendRowSupported(boolean appendRowSupported) {
        if (this.appendRowSupported != appendRowSupported) {
            boolean oldValue = this.appendRowSupported;
            this.appendRowSupported = appendRowSupported;
            pcs.firePropertyChange("appendRowSupported", oldValue, appendRowSupported);
        }
    }
    
    /**
     * Returns the {@link DataProvider} for this <CODE>DataTable</CODE>. May be null.
     * @return the DataProvider for this DataTable. May be null.
     */
    public DataProvider getDataProvider() {
        return dataProvider;
    }
    
    /**
     * Sets the DataProvider for this DataTable.
     * @param dataProvider the DataProvider for this DataTable. This may be null.
     */
    public void setDataProvider(DataProvider dataProvider) {
        if (this.dataProvider != dataProvider) {
            DataProvider oldValue = this.dataProvider;
            this.dataProvider = dataProvider;
            pcs.firePropertyChange("dataProvider", oldValue, dataProvider);
        }
    }
    
    /**
     * Returns the {@link DataSet} that is associated with this <CODE>DataTable</CODE>.
     * @return the DataSet that is associated with this DataTable
     */
    public DataSet getDataSet() {
        return dataSet;
    }
    
    /**
     * Append a new DataRow to this DataTable, and return the newly appended
     * Row. If appendRowSupported is false, then this method returns null
     * @return null if !appendRowSupported, else the newly created row
     */
    public DataRow appendRow() {
        final DataRow row = appendRowNoEvent();
        if (row != null) {
            fireDataTableChanged(TableChangeEvent.newRowAddedEvent(this, row));
            
            //I added this, don't know if I really want it here
            if (selectors.get("current") == null) {
                createSelector("current");
            }
            // ASK: why the PCL here?
            row.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    fireDataTableChanged(TableChangeEvent.newRowAddedEvent(DataTable.this, row));
                }
            });
            selectors.get("current").setRowIndices(new int[]{indexOfRow(row)});
        }
        return row;
    }
    
    /**
     * Appends a new DataRow to this DataTable, and returns the newly appended
     * row. This method differs from {@link #appendRow()} in that it does not fire
     * any event. This is useful to the DataProvider, which will be adding many
     * rows and may not want many event notifications
     * @return The row added to the table.
     */
    public DataRow appendRowNoEvent() {
        if (appendRowSupported) {
            DataRow row = new DataRow(this);
            int oldSize = rows.size();
            rows.add(row);
            if (oldSize == 0 && rows.size() > 0) {
                //check the selectors. Any selectors that are not
                //set, set.
                for (DataSelector s : selectors.values()) {
                    s.setRowIndices(new int[]{0});
                }
            }
            return row;
        } else {
            return null;
        }
    }
    
    /**
     * Doesn't actually remove the row, just marks it for deletion. If
     * deletion of rows is not supported, nothing happens.
     * @param rowIndex the index of the row to delete. If the index is invalid,
     * an assertion will be raised
     */
    public void deleteRow(int rowIndex) {
        assert rowIndex >= 0 && rowIndex < rows.size();
        deleteRow(rows.get(rowIndex));
    }
    
    /**
     * Doesn't actually remove the row, just marks it for deletion. If
     * deletion of rows is not supported, nothing happens.
     * @param row the row to delete. The row must belong to this DataTable
     */
    public void deleteRow(DataRow row) {
        assert row.getTable() == this;
        if (deleteRowSupported) {
            row.setStatus(DataRow.DataRowStatus.DELETED);
            fireDataTableChanged(TableChangeEvent.newRowDeletedEvent(this, row));
        }
    }
    
    /**
     * Actually removes the row from the DataTable. Unlike the #deleteRow method,
     * this does not later remove the record from the data store. Rather, it simply
     * discards the row entirely
     * @param rowIndex Index of the row to discard.
     */
    public void discardRow(int rowIndex) {
        assert rowIndex > 0 && rowIndex < rows.size();
        DataRow row = rows.remove(rowIndex);
        fireDataTableChanged(TableChangeEvent.newRowDiscardedEvent(this, row));
    }
    
    /**
     * Actually removes the row from the DataTable. Unlike the {@link #deleteRow(DataRow)} method,
     * this does not later remove the record from the data store. Rather, it simply
     * discards the row entirely.
     * @param row The row to discard.
     */
    public void discardRow(DataRow row) {
        discardRow(indexOfRow(row));
    }
    
    /**
     * Loads this DataTable using this tables DataProvider. If DataProvider is
     * null, then nothing is loaded. This method <b>does not</b> clear out the
     * DataTable prior to loading. Calling load() <i>n</i> times will cause the
     * DataTable to contain <i>rowCount * n</i> rows to be added.
     */
    public void load() {
        if (dataProvider != null) {
            fireDataTableChanged(TableChangeEvent.newLoadStartEvent(this));
            dataProvider.load(this);
        }
    }
    
    /**
     * Loads this DataTable <b>synchronously</b> using this table&amp;s
     * DataProvider. That is, this method blocks until the load is completed.
     * If DataProvider
     * is null, then nothing is loaded. This method <b>does not</b> clear out
     * the DataTable prior to loading. Calling load() <i>n</i> times will cause
     * the DataTable to contain <i>rowCount * n</i> rows to be added
     */
    public void loadAndWait() {
        if (dataProvider != null) {
            fireDataTableChanged(TableChangeEvent.newLoadStartEvent(this));
            dataProvider.loadAndWait(this);
        }
    }
    
    /**
     * Saves this DataTable to the underlying DataStore. This calls the
     * DataProviders save method. If no DataProvider is specified, then nothing
     * is done.
     */
    public void save() {
        if (dataProvider != null) {
            fireDataTableChanged(TableChangeEvent.newSaveStartEvent(this));
            dataProvider.save(this);
        }
    }
    
    /**
     * Saves this DataTable to the underyling DataStore <b>synchronously</b>.
     * That is, this method blocks until the save is complete. This calls the
     * DataProvider&amp;s save method. If no DataProvider is specified, then
     * nothing is done.
     */
    public void saveAndWait() {
        if (dataProvider != null) {
            fireDataTableChanged(TableChangeEvent.newSaveStartEvent(this));
            dataProvider.saveAndWait(this);
        }
    }
    
    /**
     * Clears all of the rows from this DataTable. <em>If any rows were altered,
     * these changes are lost!</em>. Call #save to save the changes before
     * clearing. An {@linkplain TableChangeEvent event} is posted indicating that the table was cleared.
     */
    public void clear() {
        rows.clear();
        //clear out all of the DataSelectors
        for (DataSelector sel : selectors.values()) {
            sel.setRowIndices(new int[0]);
        }
        fireDataTableChanged(TableChangeEvent.newTableClearedEvent(this));
    }
    
    /**
     * Refreshes the DataSet. This is symantically the same as:
     * <code>
     * clear();
     * load();
     * </code>
     */
    public void refresh() {
        clear();
        load();
    }
    
    /**
     * Refreshes the DataSet <b>synchronously</b>. That is, this method blocks
     * until the refresh is completed. This is symantically the same as:
     * <code>
     * clear();
     * loadAndWait();
     * </code>
     */
    public void refreshAndWait() {
        clear();
        loadAndWait();
    }
    
    /**
     * Returns the current value for the row at the given row index, for the given column.
     * @param index the row index of the row that you want to retrieve a value
     * for
     * @param columnName the name of the column who's value you want retrieved
     * @return the value at the given rowIndex, for the given columnName. If
     * either the index is invalid or the columnName, assertions will be raised.
     */
    public Object getValue(int index, String columnName) {
        assert index >= 0 && index < rows.size();
        assert columns.containsKey(columnName);
        return rows.get(index).getValue(columnName);
    }
    
    /**
     * Sets the field value at the given row idnex and column to the given
     * value. If either the index is invalid or the columnName, assertions will
     * be raised.
     * @param index The row index at which to set the value
     * @param columnName Name of the column to change
     * @param value The new value for the cell
     */
    public void setValue(int index, String columnName, Object value) {
        assert index >= 0 && index < rows.size();
        assert columns.containsKey(columnName);
        rows.get(index).setValue(columnName, value);
    }
    
    /**
     * Retrieves the current value for the given row and column.
     * @param row The DataRow to retrieve the value from. This row must be
     * a member of this table, or an assertion will be raised. If null, a
     * NullPointerException will be thrown.
     * @param col The DataColumn to retrieve the value from. This col must
     * be a member of this table, or an assertion will be raised. If null, a
     * NullPointerException will be thrown.
     * @return  the current value for the given row and column.
     */
    public Object getValue(DataRow row, DataColumn col) {
        assert row.getTable() == this;
        assert col.getTable() == this;
        return row.getValue(col);
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
     * Adds a {@link DataTableListener} to the table for event propagation. If the DTL is also a {@link PropertyChangeListener},
     * like {@link DataTableEventAdapter}, then this is automatically added as a property change listener on the table as well.
     * @param listener The listener to add for event notification.
     */
    public void addDataTableListener(DataTableListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        if ( listener instanceof PropertyChangeListener ) {
            addPropertyChangeListener((PropertyChangeListener)listener);
        }
    }
    
    // CHANGED
    /**
     * Removes an event listener from the table; if the listener is also a {@link PropertyChangeListener}, then
     * it is also removed as a property change listener on the table as well.
     * @param listener The event listener to remove from the table.
     */
    public void removeDataTableListener(DataTableListener listener) {
        listeners.remove(listener);
        if ( listener instanceof PropertyChangeListener ) {
            removePropertyChangeListener((PropertyChangeListener)listener);
        }
    }
    
    /**
     * Broadcasts to listeners on the table that the table has changed, using a {@link TableChangeEvent}.
     * @param evt The {@link TableChangeEvent} recording the event.
     */
    public void fireDataTableChanged(TableChangeEvent evt) {
        for (DataTableListener listener : new ArrayList<DataTableListener>(listeners)) {
            listener.tableChanged(evt);
        }
    }
    
    /**
     * Broadcasts to listeners on the table that a row has changed using a {@link RowChangeEvent}.
     * @param evt The {@link RowChangeEvent} capturing the event on a row.
     */
    public void fireRowChanged(RowChangeEvent evt) {
        for (DataTableListener listener : new ArrayList<DataTableListener>(listeners)) {
            listener.rowChanged(evt);
        }
    }
    
    /**
     * Internal method that returns the int index of the given DataRow. This is
     * currently only used for constructing toString on DataRow, and testing.
     */
    protected int indexOfRow(DataRow row) {
        return rows.indexOf(row);
    }
}