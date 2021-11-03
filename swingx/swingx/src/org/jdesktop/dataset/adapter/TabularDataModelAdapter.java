/*
 * $Id: TabularDataModelAdapter.java,v 1.4 2005/10/10 17:01:14 rbair Exp $
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

package org.jdesktop.dataset.adapter;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.TabularValueChangeEvent;
import org.jdesktop.binding.TabularValueChangeListener;
import org.jdesktop.binding.ValueChangeEvent;
import org.jdesktop.binding.ValueChangeListener;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.Validator;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.event.DataTableListener;

/**
 *
 * @author rbair
 */
public class TabularDataModelAdapter implements TabularDataModel {
    private DataTable table;
    private MetaDataProviderAdapter mdp;
    private List<Validator> validators = new ArrayList<Validator>();
    private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
    private List<TabularValueChangeListener> tabularListeners = new ArrayList<TabularValueChangeListener>();
 
    
    /** Creates a new instance of TabularDataModelAdapter */
    public TabularDataModelAdapter(DataTable table) {
        assert table != null;
        this.table = table;
        mdp = new MetaDataProviderAdapter(table);
        table.addDataTableListener(new DataTableListener() {
            public void rowChanged(org.jdesktop.dataset.event.RowChangeEvent evt) {
                fireRowChanged((DataRow) evt.getSource());
            }

            public void tableChanged(org.jdesktop.dataset.event.TableChangeEvent evt) {
                fireTableChanged();
            }
            
        });
    }

    public int getRecordCount() {
        return table.getRowCount();
    }

    public DataTable getDataTable() {
        return table;
    }
    
    public void addValidator(Validator validator) {
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    public void removeValidator(Validator validator) {
        validators.remove(validator);
    }

    public Validator[] getValidators() {
        return validators.toArray(new Validator[validators.size()]);
    }

    public void addValueChangeListener(ValueChangeListener valueChangeListener) {
        if (!listeners.contains(valueChangeListener)) {
            listeners.add(valueChangeListener);
        }
    }

    public void removeValueChangeListener(ValueChangeListener valueChangeListener) {
        listeners.remove(valueChangeListener);
    }

    public ValueChangeListener[] getValueChangeListeners() {
        return listeners.toArray(new ValueChangeListener[listeners.size()]);
    }

    
    public void addTabularValueChangeListener(TabularValueChangeListener l) {
        if (!tabularListeners.contains(l)) {
            tabularListeners.add(l);
        }

    }
    public TabularValueChangeListener[] getTabularValueChangeListeners() {
        return tabularListeners.toArray(new TabularValueChangeListener[tabularListeners.size()]);
    }
    public void removeTabularValueChangeListener(TabularValueChangeListener l) {
        tabularListeners.remove(l);

    }
    public int getFieldCount() {
        return mdp.getFieldCount();
    }

    public String[] getFieldNames() {
        return mdp.getFieldNames();
    }

    public MetaData[] getMetaData() {
        return mdp.getMetaData();
    }

    public MetaData getMetaData(String dataID) {
        return mdp.getMetaData(dataID);
    }

    public Object getValueAt(String fieldName, int index) {
        return table.getValue(index, fieldName);
    }

    public void setValueAt(String fieldName, int index, Object value) {
        table.setValue(index, fieldName,  value);
        // JW: as long as the DataTable doesn't fire anything on updates
        // we must do it manually
        fireTabularValueChanged(index, fieldName);
    }

    public Object getValue(String fieldName) {
        //null op
        return null;
    }

    public void setValue(String fieldName, Object value) {
        //null op
    }
    
	protected void fireValueChanged(String fieldName) {
		ValueChangeEvent e = new ValueChangeEvent(this, fieldName);//getCachedEvent(fieldName);
        for (ValueChangeListener listener : listeners) {
			try {
			    listener.valueChanged(e);
			} catch (Exception ex) {
                //TODO some real exception handling needs to occur
			    ex.printStackTrace();
			}
		}
	}

    protected void fireRowChanged(DataRow row) {
       // fireAllFieldsChanged();
        int rowIndex = table.getRows().indexOf(row);
        fireTabularValueChanged(rowIndex, null);
    }


    protected void fireTableChanged() {
       // fireAllFieldsChanged();
        fireTabularValueChanged(-1, null);
    }

    protected void fireTabularValueChanged(int rowIndex, String fieldName) {
        TabularValueChangeEvent e = new TabularValueChangeEvent(this, rowIndex, fieldName);
        for (TabularValueChangeListener l: tabularListeners) {
            l.tabularValueChanged(e);
        }
        
    }
    protected void fireAllFieldsChanged() {
        for (String fieldName : mdp.getFieldNames()) {
            fireValueChanged(fieldName);
        }
        // JW: why? 
        fireValueChanged("");
    }
}