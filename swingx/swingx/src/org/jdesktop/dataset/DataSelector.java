/*
 * $Id: DataSelector.java,v 1.7 2005/10/10 17:00:58 rbair Exp $
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
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author rbair
 */
public class DataSelector {
    //protected for testing
    /** Used as a prefix for auto-generated DataColumn names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataSelector";

    /** The shared instance of the NameGenerator for DataSelectors not assigned a name. */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);

    private DataTable table;
    private String name;
	/**
	 * This variable tracks which records are selected. If the bit at position
	 * <i>n</i> is 1, then the <i>nth</i> record is selected.
	 */
    private BitSet indices = new BitSet(0);
    private int[] helper = new int[1];
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	    
    /** Creates a new instance of DataSelector */
    public DataSelector(DataTable table) {
        assert table != null;
        this.table = table;
        name = NAMEGEN.generateName(this);
        if (table != null && table.getRowCount() > 0) {
            setRowIndices(new int[]{0});
        }
    }
    
    /**
     * @deprecated Use getTable() instead
     */
    public DataTable getDataTable() {
        return table;
    }
    
    public DataTable getTable() {
        return table;
    }
	/**
	 * @param name
	 */
	public void setName(String name) {
        if (this.name != name) {
            assert name != null && !name.trim().equals("");
            String oldName = this.name;
            this.name = name;
            pcs.firePropertyChange("name", oldName, name);
        }
	}

	public String getName() {
		return name;
	}

    public List<Integer> getRowIndices() {
        List<Integer> results = new ArrayList<Integer>(indices.cardinality());
        int n=-1;
        for(int i=0; i<indices.cardinality(); i++) {
            n = indices.nextSetBit(n+1);
            results.add(n);
        }
        return Collections.unmodifiableList(results);
    }
    
    /**
     * @return either the first selected index, or -1 if nothing is selected
     */
    public int getFirstRowIndex() {
        return indices.cardinality() > 0 ? indices.nextSetBit(0) : -1;
    }
    
    public void setRowIndices(int[] rowIndices) {
        // JW: to honour the bean spec
        List oldIndices = getRowIndices();
    	indices.clear();
    	for (int index : rowIndices) {
    		indices.set(index);
    	}
//    	fireSelectionModelChanged(new SelectionModelEvent(this, 0, selectedRecords.length()-1));
        //TODO I'm abusing the property change listener here to get out the selection
        //change. I need a real event, I think.
        // JW: until then we can honour the bean spec with the following line:
    	pcs.firePropertyChange("rowIndices", oldIndices, getRowIndices());
    }

    /**
     * Convenience method for setting a single row as the selected row.
     * @param index must be less than the row count of the DataTable. Also,
     * if it is greater than 0, then the row index will be set, however if
     * it is less than zero, then the selection will be cleared.
     */
    public void setRowIndex(int index) {
        assert index < table.getRowCount();
        if (index >= 0) {
            helper[0] = index;
            setRowIndices(helper);
        } else {
            setRowIndices(new int[0]);
        }
    }
    
    public void setRowIndex(DataRow row) {
        assert row != null;
//        assert row.getTable() == table;
        setRowIndices(new int[]{table.indexOfRow(row)});
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
