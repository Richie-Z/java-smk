/*
 * $Id: ComboBoxBinding.java,v 1.2 2005/10/10 17:00:52 rbair Exp $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.metadata.EnumeratedMetaData;
import org.jdesktop.binding.metadata.SelectionInListMetaData;
import org.jdesktop.binding.swingx.adapter.DataModelToComboBoxModelAdapter;

/**
 * Class which binds a component that supports setting a one-of-many
 * value (JComboBox) to a data model field which may be an arbitrary type.
 * @author Amy Fowler
 * @version 1.0
 */
public class ComboBoxBinding extends AbstractBinding {
    private JComboBox comboBox;
    /* Note: we cannot support binding to any component with a ComboBoxModel
     * because ComboBoxModel fires no event when the value changes!
     * JW: ?? 
     */
    public ComboBoxBinding(JComboBox combobox,
                           DataModel dataModel, String fieldName) {
        super(combobox, dataModel, fieldName, Binding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
        return comboBox;
    }

    protected void setComponent(JComponent component) {
        comboBox = (JComboBox) component;
        configureDropDown();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

    
    protected void configureDropDown() {
        if (metaData instanceof EnumeratedMetaData) {
            updateComboBoxModel(((EnumeratedMetaData) metaData).getEnumeration());
        } else if (metaData instanceof SelectionInListMetaData) {
            updateComboBoxModel(((SelectionInListMetaData)metaData).getSourceDataModel());
        }
    }

    protected void updateComboBoxModel(TabularDataModel listDataModel) {
        DataModelToComboBoxModelAdapter adapter = new DataModelToComboBoxModelAdapter(listDataModel,
             ((SelectionInListMetaData) metaData).getSourceFieldName());
        adapter.setSelectedItem(getComponentValue());
        comboBox.setModel(adapter);
        setValidState(UNVALIDATED);
    }

    protected void updateComboBoxModel(Object[] items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        model.setSelectedItem(getComponentValue());
        // JW: brute force
       comboBox.setModel(model);
       setValidState(UNVALIDATED);
    }

    protected void installMetaDataListener() {
        // PENDING JW: not yet listening to dynamic drop-down changes
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("enumeration".equals(evt.getPropertyName())) {
                    updateComboBoxModel((Object[]) evt.getNewValue());
                }
                
            }
            
        };
        metaData.addPropertyChangeListener(l);
    }

    protected Object getComponentValue(){
        return comboBox.getSelectedItem();
    }

    protected void setComponentValue(Object value) {
        comboBox.getModel().setSelectedItem(value);
    }

}
