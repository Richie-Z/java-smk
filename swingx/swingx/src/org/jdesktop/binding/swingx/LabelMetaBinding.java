/*
 * $Id: LabelMetaBinding.java,v 1.2 2005/10/10 17:00:51 rbair Exp $
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
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.binding.DataModel;
import org.jdesktop.swingx.JXHyperlink;

/**
 * Class which binds an uneditable component (JLabel) to the metaData label
 * property of a dataModel's field, adding a colon.
 * @author Amy Fowler
 * @author Jeanette Winzenburg
 */
public class LabelMetaBinding extends AbstractBinding {
    private JLabel label;
    private Icon requiredIcon;

    public LabelMetaBinding(JLabel label,
                        DataModel model, String fieldName) {
        super(label, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        // auto-pull - we are accessing metaData only
        pull();
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public boolean push() {
        // do nothing, value was not edited
        return true;
    }

    public boolean pull() {
        if (metaData != null) {
            setComponentValue(metaData.getLabel());
            updateRequiredFeedBack(metaData.isRequired());
        }
        return true;
    }

    public JComponent getComponent() {
        return label;
    }

    protected void setComponent(JComponent component) {
        label = (JLabel)component;
    }

    protected void installDataModelListener() {
        // do nothing - the label is bound to the metaData.label property
    }
    
    protected void installMetaDataListener() {
        metaData.addPropertyChangeListener(new PropertyChangeListener() {
            // PENDING: listen to enabled
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (!"label".equals(evt.getPropertyName())) return;
                        pull();
                        
                    }
                    
                });
    }
    protected Object getComponentValue() {
        return label.getText();
    }

    protected void setComponentValue(Object value) {
      label.setText(createColonedText(value));

    }

    protected void updateRequiredFeedBack(boolean required) {
        if (required) {
           label.setIcon(getRequiredIcon()); 
        } else {
            label.setIcon(null);
        }
        
    }

    protected Icon getRequiredIcon() {
        if (requiredIcon == null) {
            URL url = JXHyperlink.class.getResource("resources/asterisk.8x8.png");
            requiredIcon = new ImageIcon(url);

        }
        return requiredIcon;
    }

    protected String createColonedText(Object value) {
        if (isEmpty(value)) {
            return "";
        }
        String text = value.toString();
        return getComponent().getComponentOrientation().isLeftToRight() ?
                text + getColon() : getColon() + text;
    }

    protected String getColon() {
        return ":";
    }
}
