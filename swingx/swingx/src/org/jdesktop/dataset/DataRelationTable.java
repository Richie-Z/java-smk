/*
 * $Id: DataRelationTable.java,v 1.9 2005/10/15 11:43:19 pdoubleya Exp $
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jdesktop.dataset.event.DataTableListener;
import org.jdesktop.dataset.event.RowChangeEvent;
import org.jdesktop.dataset.event.TableChangeEvent;


/**
 *
 * @author rbair
 */
public class DataRelationTable extends DataTable {
    /**
     * The Logger
     */
    private static final Logger LOG = Logger.getLogger(DataRelationTable.class.getName());
    /**
     * The relation that is used to populate this DataRelationTable
     */
    private DataRelation relation;
    /**
     * The selector in the parent table that is used to indicate what parent
     * rows to retrieve child rows for
     */
    private DataSelector parentSelector;
    /**
     * The parentTable that is used to indicate what child rows to retrieve. If
     * parentSelector is defined, then parentTable is ignored. Otherwise, all
     * of the contents of the parentTable will be used to retrieve rows in the
     * childTable.
     * <p>
     * This is of particular use when the parentTable is another DataRelationTable,
     * and therefore a subset of the original DataTable.
     */
    private DataTable parentTable;
    /**
     * A selection listener that is affixed to the parentSelector. Whenever the
     * selection changes in the parentSelector, this is notified and refreshes
     * this table
     */
    private SelectionListener listener = new SelectionListener();
    /**
     * Listens to changes on the parentTable, if there IS a parentTable
     */
    private ParentTableListener pListener = new ParentTableListener();
    /**
     * Listens to changes on the childTable, if there IS a childTable
     */
    private ChildTableListener cListener = new ChildTableListener();

    /** 
     * Creates a new instance of DataRelationTable 
     */
    public DataRelationTable(DataSet ds) {
        super(ds);
    }
    
    public DataRelationTable(DataSet ds, String name) {
        super(ds, name);
    }
    
    /**
     * Set the relation that this DataRelationTable uses
     * @param relation the DataRelation to use
     */
    public void setRelation(DataRelation relation) {
        if (this.relation != relation) {
            if (this.relation != null && this.relation.getChildColumn() != null) {
                this.relation.getChildColumn().getTable().removeDataTableListener(cListener);
            }
            //TODO affix a DataSet listener so that if this relation is removed
            //from the DataSet, I can remove my reference to it as well.
            this.relation = relation;
            addChildListener();
            if (relation != null) {
                relation.addPropertyChangeListener("childColumn", new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        addChildListener();
                    }
                });
            }
        }
    }
    
    private void addChildListener() {
        if (this.relation != null && this.relation.getChildColumn() != null) {
            this.relation.getChildColumn().getTable().removeDataTableListener(cListener);
        }
        if (this.relation != null && this.relation.getChildColumn() != null) {
            this.relation.getChildColumn().getTable().addDataTableListener(cListener);
        }
    }
    
    /**
     * @return the relation used to populate this DataRelationTable
     */
    public DataRelation getRelation() {
        return relation;
    }
    
    /**
     * Sets the selector for this DataRelationTable. This selector is used to
     * retrieve the currently selected rows from the parent table so that the
     * proper child rows can be retrieved from the child table using the
     * relation.
     *
     * @param selector The selector on the parent table. The selector must belong
     * to the same table as the relation's parentColumn
     */
    public void setParentSelector(DataSelector selector) {
        if (this.parentSelector != null) {
            this.parentSelector.removePropertyChangeListener("rowIndices", listener);
        }
        this.parentSelector = selector;
        if (this.parentSelector != null) {
            this.parentSelector.addPropertyChangeListener("rowIndices", listener);
        }
    }
    
    /**
     * @return the DataSelector used to retrieve rows from the DataRelation
     */
    public DataSelector getParentSelector() {
        return parentSelector;
    }
    
    public void setParentTable(DataTable parent) {
        if (this.parentTable != null) {
            this.parentTable.removeDataTableListener(pListener);
        }
        this.parentTable = parent;
        if (this.parentTable != null) {
            this.parentTable.addDataTableListener(pListener);
        }
    }
    
    public DataTable getParentTable() {
        return parentTable;
    }
    
    /**
     * This method makes no sense for a DataRelationTable. A cleaner refactoring
     * might remove the need to override this method. Nothing bad happens if
     * this method is called, it just doesn't make sense. A Warning is logged
     * if this method is called.
     */
    public void setDataProvider(DataProvider dataProvider) {
        LOG.warning("An attempt was made to set the DataProvider for a " +
                "DataRelationTable. This is not honored because a " +
                "DataRelationTable, by definition, gets its records " +
                "by querying a DataSelector from the Parent table, and " +
                "the DataRelation leading to the Child table.");
    }

    /**
     * This method makes no sense for a DataRelationTable. A cleaner refactoring
     * might remove the need to override this method. Nothing bad happens if
     * this method is called, it just doesn't make sense. A Warning is logged
     * if this method is called
     */
    public void save() {
        LOG.warning("An attempt was made to save a DataRelationTable. " +
                "This is not honored because a DataRelationTable, " +
                "by definition, gets its records by querying a DataSelector " +
                "from the Parent table, and the DataRelation leading to the " +
                "Child table. Therefore, to save, the child table should be " +
                "asked to save, not the DataRelationTable.");
    }

    /**
     * Appends a row to the child table, retrieved by
     * <code>relation.getChildColumn().getTable()</code>. If the DataTable
     * doesn't exist, null is returned
     *
     * @return The newly added DataRow, or null if no DataRow was added.
     */
    public DataRow appendRow() {
        //append a row to the child table, and view it here.
        //when the row is added to the child table, the child table
        //will fire an event notification indicating that a new row has
        //been added. When this happens, this child as well as all other
        //relation tables will be notified that they need to refresh themselves.
        if (relation != null && relation.getChildColumn() != null) {
            DataTable t = relation.getChildColumn().getTable();
            DataRow row = t.appendRow();
            if (parentSelector.getRowIndices().size() == 1) {
                row.setValue(relation.getChildColumn().getName(), 
                             relation.getParentColumn().getTable().getValue(
                                 parentSelector.getRowIndices().get(0), 
                                 relation.getParentColumn().getName()
                             ));
            }
            rows.add(row);
            return row;
        }
        return null;
    }

    /**
     * Removes all of the rows from this DataRelationTable, and reloads it with
     * the proper rows from the DataRelation using the parentSelector
     * DataSelector. When rows are removed from a DataRelationTable, they still
     * exist in the child DataTable, hence it is always safe to call this method
     * without losing any data.<br>
     *
     * This method attempts to reset all of its selectors to the same rows,
     * where possible. It does this by remembering all of the values for the
     * rows key fields, and looking for them again after the refresh. If the
     * row cannot be found, it is no longer selected<br>
     *
     * TODO This code for remembering the selected rows and trying to locate
     * them again after refresh is horrible and needs to be rewritten.
     */
    public void refresh() {
        //for each DataSelector, save its selection state.
        Map<DataSelector,Object[][]> selectorState = new HashMap<DataSelector,Object[][]>();
        List<DataColumn> keyColumns = new ArrayList<DataColumn>();
        for (DataColumn c : columns.values()) {
            if (c.isKeyColumn()) {
                keyColumns.add(c);
            }
        }
        
        for (DataSelector sel : selectors.values()) {
            List<Integer> indices = sel.getRowIndices();
            Object[][] values = new Object[indices.size()][keyColumns.size()];
            for (int i=0; i<indices.size(); i++) {
                for (int j=0; j<keyColumns.size(); j++) {
                    DataRow row = rows.get(indices.get(i));
                    values[i][j] = getValue(row, keyColumns.get(j));
                }
            }
            sel.setRowIndices(new int[0]);
            selectorState.put(sel, values);
        }
        
        //clear out the DataRelationTable, and reload it
        clear();
        if (relation != null && relation.getChildColumn() != null) {
            fireDataTableChanged(TableChangeEvent.newLoadStartEvent(this));
            if (parentSelector != null) {
                //use the selector and the relation to get the rows
//                assert relation.getParentColumn() != null && relation.getParentColumn().getTable() == selector.getTable();
                super.rows.addAll(relation.getRows(parentSelector.getRowIndices()));
            } else if (parentTable != null) {
                //use all of the rows in the parent table to get the children
                List<DataRow> list = parentTable.getRows();
                super.rows.addAll(relation.getRows(list.toArray(new DataRow[list.size()])));
            } else {
                //get all of the rows from the relations child table
                super.rows.addAll(relation.getChildColumn().getTable().getRows());
            }
            fireDataTableChanged(TableChangeEvent.newLoadCompleteEvent(this));
        }
        
        //try to restore the selection state, where possible
        for (DataSelector sel : selectors.values()) {
            Object[][] values = selectorState.get(sel);
            //for each selector, find its values. Look for a row who's columns
            //values match the specified set, and match it.
            //NOTE: This algorithm is a brute force algorithm, and not very
            //efficient where multiple rows are selected in a large DataTable.
            List<Integer> indices = new ArrayList<Integer>(values.length);
            for (int i=0; i<values.length; i++) {
                boolean found = true;
                for (int rowIndex=0; rowIndex<rows.size(); rowIndex++) {
                    found = true; //reset
                    for (int j=0; j<keyColumns.size(); j++) {
                        DataRow row = rows.get(rowIndex);
                        if (found && values[i][j].equals(row.getValue(keyColumns.get(j)))) {
                            //do nothing
                        } else {
                            found = false;
                        }
                    }
                    if (found) {
                        indices.add(rowIndex);
                    }
                }
            }
            
            //ok, set the selector state
            //reset the selectors
            int[] rows = new int[indices.size()];
            for (int i=0; i<rows.length; i++) {
                rows[i] = indices.get(i);
            }
            if (super.rows.size() > 0) {
                if (rows.length == 0) {
                    //reset all of the selectors to 0
                    sel.setRowIndices(new int[]{0});
                } else {
                    sel.setRowIndices(rows);
                }
            }
        }
    }
    
    /**
     * Overridden so that it will defer to the child Table first. A
     * DataRelationTable contains all of the columns in the child Table, but
     * may also contain its own set of calculated columns as well. This method
     * must therefore check the child table for a given column before checking
     * its own set of columns.
     *
     * @param colName The String name for the column to retrieve.
     * @return The DataColumn identified by the given name.
     */
    public DataColumn getColumn(String colName) {
        DataColumn col = null;
        if (relation != null && relation.getChildColumn() != null && relation.getChildColumn().getTable() != null) {
            col = relation.getChildColumn().getTable().getColumn(colName);
        }
        
        if (col == null) {
            return super.getColumn(colName);
        } else {
            return col;
        }
    }

    /**
     * @return the set of columns that comprise this DataRelationTable. This is
     * the union of the child tables columns, and any native columns to this
     * DataRelationTable
     */
    public List<DataColumn> getColumns() {
        List<DataColumn> cols = new ArrayList<DataColumn>();
        if (relation != null && relation.getChildColumn() != null && relation.getChildColumn().getTable() != null) {
            cols.addAll(relation.getChildColumn().getTable().getColumns());
        }
        
        cols.addAll(super.getColumns());
        return Collections.unmodifiableList(cols);
    }
    
    /**
     * A listener to the parentSelector. When selection change events occur in
     * the parentSelector, this DataRelationTable is automatically refreshed.
     */
    private final class SelectionListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    }
    
    private final class ParentTableListener implements DataTableListener {
        public void rowChanged(RowChangeEvent evt) {
        }

        public void tableChanged(TableChangeEvent evt) {
            refresh();
        }
        
    }

    private final class ChildTableListener implements DataTableListener {
        public void rowChanged(RowChangeEvent evt) {
        }

        public void tableChanged(TableChangeEvent evt) {
            refresh();
        }
        
    }
}