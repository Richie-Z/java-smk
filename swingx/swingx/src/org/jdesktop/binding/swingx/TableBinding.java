/*
 * $Id: TableBinding.java,v 1.2 2005/10/10 17:00:51 rbair Exp $
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.jdesktop.binding.DataConstants;
import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.IndexMapper;
import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.metadata.TabularMetaData;
import org.jdesktop.binding.swingx.adapter.DataModelToTableModelAdapter;
import org.jdesktop.binding.swingx.adapter.ListSelectionBinding;
import org.jdesktop.swingx.JXTable;

/**
 * Binding a JTable to a field in a DataModel. The field value must be
 * of type TabularDataModel.
 * 
 * PENDING: no buffering, no validation.
 * 
 * @author Jeanette Winzenburg
 */
public class TableBinding extends AbstractBinding implements Binding {

    private JTable table;

    protected TableBinding(JComponent component, DataModel dataModel, String fieldName) {
        super(component, dataModel, fieldName, AUTO_VALIDATE_NONE);
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }
    
    protected void setComponent(JComponent component) {
        table = (JTable) component;
        configureSelection();
    }
    
    private void configureSelection() {
        Object selectionMeta = metaData.getCustomProperty(DataConstants.SELECTION_MODEL);
        if (selectionMeta instanceof SelectionModel) {
            new ListSelectionBinding((SelectionModel) selectionMeta, 
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


    
    protected void installMetaDataListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (DataConstants.SELECTION_MODEL.equals(evt.getPropertyName())) {
                    configureSelection();
                }
                
            }
            
        };
        metaData.addPropertyChangeListener(l);
    }


    protected Object getComponentValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "must not call: TableBinding does not support getComponentValue");
    }

    protected void setComponentValue(Object value) {
        // TODO don't recreate on every pull
        if (value == table.getModel()) return;
        if (value instanceof TabularDataModel) {
            TableModel model = createTabularAdapter((TabularDataModel) value);
            table.setModel(model);
        }

    }

    private TableModel createTabularAdapter(TabularDataModel model) {
        String[] fieldNames = null;
        if (metaData instanceof TabularMetaData) {
            fieldNames = ((TabularMetaData) metaData).getFieldNames();
        }
        return new DataModelToTableModelAdapter(model, fieldNames);
    }

    public JComponent getComponent() {
        return table;
    }

}
