/*
 * $Id: BooleanBinding.java,v 1.2 2005/10/10 17:00:54 rbair Exp $
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.jdesktop.binding.DataModel;


/**
 * Class which binds a component that supports setting a boolean
 * value (JCheckBox) to a data model field which is type Boolean.
 * Although this binding is most commonly used for checkboxes, it may
 * be used with any component that defines a ButtonModel to represent
 * its selected state.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class BooleanBinding extends AbstractBinding {
    private JComponent component;
    private ButtonModel buttonModel;

    public BooleanBinding(JToggleButton toggleButton,
                           DataModel dataModel, String fieldName) {
        // checkboxes don't need to be validated because they cannot have a null
        // value and type conversion should not be necessary.
        super(toggleButton, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        initModel(toggleButton.getModel());
    }

    public BooleanBinding(JComponent component, ButtonModel buttonModel,
                           DataModel dataModel, String fieldName) {
        super(component, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        initModel(buttonModel);
    }

    public JComponent getComponent() {
        return component;
    }

    protected void setComponent(JComponent component) {
        this.component = component;
    }

    protected Object getComponentValue(){
        return Boolean.valueOf(buttonModel.isSelected());
    }

    protected void setComponentValue(Object value) {
        if (value != null) {
            buttonModel.setSelected( ( (Boolean) value).booleanValue());
        } else {
            buttonModel.setSelected(false);
        }
    }

    private void initModel(ButtonModel model) {
        buttonModel = model;
        buttonModel.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

}