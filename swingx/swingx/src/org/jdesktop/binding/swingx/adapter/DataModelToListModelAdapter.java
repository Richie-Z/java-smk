/*
 * $Id $Exp
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

import javax.swing.AbstractListModel;

import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.TabularValueChangeEvent;
import org.jdesktop.binding.TabularValueChangeListener;



public class DataModelToListModelAdapter extends AbstractListModel {
    TabularDataModel tabModel = null;
    String fieldName;

    public DataModelToListModelAdapter(TabularDataModel model, String fieldName) {
        this.tabModel = model;
        this.fieldName = fieldName;
        installDataModelListener();
    }
    
	protected void installDataModelListener() {
		TabularValueChangeListener l = new TabularValueChangeListener() {

            public void tabularValueChanged(TabularValueChangeEvent e) {
                fireContentsChanged(DataModelToListModelAdapter.this, 0, tabModel.getRecordCount() - 1);
                
            }
		};
        tabModel.addTabularValueChangeListener(l);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return tabModel.getRecordCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return tabModel.getValueAt(fieldName, index);
	}
	
}