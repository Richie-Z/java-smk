/*
 * $Id: SpinnerBinding.java,v 1.2 2005/10/10 17:00:53 rbair Exp $
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
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.binding.DataModel;

/**
 * Class which binds a component that supports setting a value within
 * a sequence of values (JSpinner) to a field in a data model.
 * Although this binding is most commonly used for spinners, it may
 * be used with any component that defines a SpinnerModel to represent
 * its current value.

 * @author Amy Fowler
 * @version 1.0
 */
public class SpinnerBinding extends AbstractBinding {
    private JComponent component;
    private SpinnerModel spinnerModel;

    public SpinnerBinding(JSpinner spinner,
                            DataModel dataModel, String fieldName) {
        super(spinner, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE);
        initModel(spinner.getModel());
    }

    public SpinnerBinding(JSpinner spinner,
                            DataModel dataModel, String fieldName,
                           int validationPolicy) {
        super(spinner, dataModel, fieldName, validationPolicy);
        initModel(spinner.getModel());
    }

    public SpinnerBinding(JComponent component, SpinnerModel spinnerModel,
                          DataModel dataModel, String fieldName,
                          int validationPolicy) {
        super(component, dataModel, fieldName, validationPolicy);
        initModel(spinnerModel);
    }

    public JComponent getComponent() {
        return component;
    }

    protected void setComponent(JComponent component) {
        this.component = component;
        configureEditability();
    }

    protected void configureEditability() {
        if (!(component instanceof JSpinner)) return;
        JSpinner spinnerComponent = (JSpinner) component;
        spinnerComponent.setEnabled(!metaData.isReadOnly());
    }
    
    protected void installMetaDataListener() {
        PropertyChangeListener l = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("readOnly".equals(evt.getPropertyName())) {
                    configureEditability();
                }
            }
        };
        metaData.addPropertyChangeListener(l); 
    }
    
    protected Object getComponentValue(){
        return spinnerModel.getValue();
    }

    protected void setComponentValue(Object value) {
        if (value != null) {
            spinnerModel.setValue(value);
        }
    }

    private void initModel(SpinnerModel spinnerModel) {
        this.spinnerModel = spinnerModel;
        spinnerModel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

}