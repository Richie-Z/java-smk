/*
 * $Id: RadioBinding.java,v 1.2 2005/10/10 17:00:54 rbair Exp $
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

import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.metadata.EnumeratedMetaData;
import org.jdesktop.swingx.JXRadioGroup;


/**
 * Class which binds a component that supports setting a one-of-many
 * value (JXRadioGroup) to a data model field which is may be an arbitrary type.
 * @author Amy Fowler
 * @version 1.0
 */
public class RadioBinding extends AbstractBinding {
    private JXRadioGroup radioGroup;

    public RadioBinding(JXRadioGroup radioGroup,
                           DataModel dataModel, String fieldName) {
        super(radioGroup, dataModel, fieldName, Binding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
        return radioGroup;
    }

    protected void setComponent(JComponent component) {
        radioGroup = (JXRadioGroup) component;
        configureRadioButtons();
        radioGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }


    protected Object getComponentValue(){
        return radioGroup.getSelectedValue();
    }

    protected void setComponentValue(Object value) {
        radioGroup.setSelectedValue(value);
    }
    
    private void configureRadioButtons() {
        if (metaData instanceof EnumeratedMetaData) {
            radioGroup.setValues(((EnumeratedMetaData) metaData).getEnumeration());
        }
        
    }
    
    
}
