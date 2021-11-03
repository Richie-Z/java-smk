/*
 * $Id: ListBinding.java,v 1.2 2005/10/10 17:00:50 rbair Exp $
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;

import org.jdesktop.binding.DataConstants;
import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.metadata.ListMetaData;
import org.jdesktop.binding.swingx.adapter.DataModelToListModelAdapter;
import org.jdesktop.binding.swingx.adapter.ListSelectionBinding;

public class ListBinding extends AbstractBinding {

    private JList list;

    public ListBinding(JList list, DataModel model, String fieldName) {
        super(list, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public JComponent getComponent() {
        return list;
    }

    protected void setComponent(JComponent component) {
        list = (JList) component;
        configureSelection();
    }

    private void configureSelection() {
        Object selectionMeta = metaData
                .getCustomProperty(DataConstants.SELECTION_MODEL);
        if (selectionMeta instanceof SelectionModel) {
            new ListSelectionBinding((SelectionModel) selectionMeta, list
                    .getSelectionModel());
        }

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
        ListModel model = list.getModel();
        Class klazz = metaData.getElementClass();

        if (klazz.equals(List.class)) {
            List lvalue = new ArrayList();
            for (int i = 0, size = model.getSize(); i < size; i++) {
                lvalue.add(model.getElementAt(i));
            }
            return lvalue;
        } else if (klazz.isArray()) {

            // XXX we lose the array type, use a generic Object[]
            int size = model.getSize();
            Object[] values = new Object[size];
            for (int i = 0; i < size; i++) {
                values[i] = model.getElementAt(i);
            }
            return values;
        }
        return null;
    }

    protected void setComponentValue(Object value) {
        if (metaData instanceof ListMetaData) {
            setComponentValueFromTabularDataModel((TabularDataModel) value);
            return;
        }
        Class klazz = metaData.getElementClass();
        if (klazz.equals(List.class)) {
            List lvalue = (List) value;
            if (lvalue != null) {
                list.setListData(lvalue.toArray());
            }
        } else if (klazz.isArray()) {
            Object[] arrayValue = (Object[]) value;

            if (arrayValue != null) {
                list.setListData(arrayValue);
            } else {
                // Empty the list.
                list.setModel(new DefaultListModel());
            }
        }
    }

    private void setComponentValueFromTabularDataModel(TabularDataModel model) {
        //create a custom ListModel bound to the entire TabularDataModel
        ListMetaData listMeta = (ListMetaData) metaData;
        list.setModel(new DataModelToListModelAdapter(model, listMeta.getRendererFieldName()));
        
        
    }
}
