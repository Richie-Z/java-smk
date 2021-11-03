/*
 * $Id $Exp
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.binding.swingx;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.IndexMapper;
import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.swingx.adapter.DataModelToTableModelAdapter;
import org.jdesktop.binding.swingx.adapter.ListSelectionBinding;
import org.jdesktop.swingx.JXTable;

/**
 * This "Binding" happens to the given DataModel as a whole (as
 * opposed to a single field of the model).
 * 
 * JW: added mapping of row indices. (TBD: unit testing of mapping).
 *  
 * @author Richard Bair
 */
public class DirectTableBinding extends AbstractBinding {
	private JTable table;
	
	/**
	 * @param component
	 * @param dataModel
	 */
	public DirectTableBinding(JTable component, TabularDataModel dataModel) {
        this(component, dataModel, null);
//		super(component, dataModel, "", DirectTableBinding.AUTO_VALIDATE_NONE);
//		//construct a TableModel for the table based on the given dataModel.
//		TableModel tm = createAdapter(dataModel, null);
//		table.setModel(tm);
	}
	
	public DirectTableBinding(JTable component, TabularDataModel dataModel, String[] fieldNames) {
//		super(component, dataModel, "", DirectTableBinding.AUTO_VALIDATE_NONE);
//		//construct a TableModel for the table based on the given dataModel.
//		TableModel tm = createAdapter(dataModel, fieldNames);
//		table.setModel(tm);
        this(component, dataModel, fieldNames, null);
	}

    public DirectTableBinding(JTable component, TabularDataModel dataModel, 
            String[] fieldNames, SelectionModel selectionModel) {
        super(component, dataModel, "", DirectTableBinding.AUTO_VALIDATE_NONE);
        //construct a TableModel for the table based on the given dataModel.
        TableModel tm = createAdapter(dataModel, fieldNames);
        table.setModel(tm);
        if (selectionModel != null) {
           new ListSelectionBinding(selectionModel, 
                   table.getSelectionModel(), createIndexMapper(table));
        }
    }

    private IndexMapper createIndexMapper(final JTable table) {

        if (!(table instanceof JXTable)) return null;
        final JXTable xTable = (JXTable) table;
        IndexMapper mapper = new IndexMapper() {

            public int viewToModel(int index) {
                 return xTable.convertRowIndexToModel(index);
            }

            public int modelToView(int index) {
                return xTable.convertRowIndexToView(index);
            }
            
        };
        return mapper;
    }

    public boolean push() {
        return true;
    }
    
    public boolean pull() {
        return true;
    }
    
    public boolean isValid() {
        return true;
    }
    
    protected TableModel createAdapter(TabularDataModel tabularDataModel, String[] fieldNames) {
        return new DataModelToTableModelAdapter(tabularDataModel, fieldNames);
    }
	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#getBoundComponent()
	 */
	protected JComponent getBoundComponent() {
		return table;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#setBoundComponent(javax.swing.JComponent)
	 */
	protected void setBoundComponent(JComponent component) {
		if (!(component instanceof JTable)) {
			throw new IllegalArgumentException("TableBindings only accept a JTable or one of its child classes");
		}
		this.table = (JTable)component;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#getComponentValue()
	 */
	protected Object getComponentValue() {
		//a table component never updates its parent data model in this way
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.binding.AbstractUIBinding#setComponentValue(java.lang.Object)
	 */
	protected void setComponentValue(Object value) {
		//means nothing to this binding
	}

    /** 
     * override super because we don't have MetaData.
     * 
     */
    protected void installDataModel(DataModel dataModel, String fieldName) {
        this.dataModel = dataModel;
        this.fieldName = fieldName;
        installDataModelListener();
    }


	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.binding.AbstractBinding#getComponent()
	 */
	public JComponent getComponent() {
		return getBoundComponent();
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.binding.AbstractBinding#setComponent(javax.swing.JComponent)
	 */
	public void setComponent(JComponent component) {
		setBoundComponent(component);
	}

}
