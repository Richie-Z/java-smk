/*
 * $Id: LabelBinding.java,v 1.2 2005/10/10 17:00:51 rbair Exp $
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

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.binding.DataModel;
import org.jdesktop.swingx.LinkModel;

/**
 * Class which binds an uneditable component (JLabel) to a data model field
 * of arbitrary type.  If the field is type Image, then the image will be
 * displayed as an icon in the component.  For all other types, the data model
 * value will be converted and displayed as a String.
 * @author Amy Fowler
 * @version 1.0
 */
public class LabelBinding extends AbstractBinding {
    private JLabel label;

    public LabelBinding(JLabel label,
                        DataModel model, String fieldName) {
        super(label, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
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

    public JComponent getComponent() {
        return label;
    }

    protected void setComponent(JComponent component) {
        label = (JLabel)component;
    }

    protected Object getComponentValue() {
	Class clz = metaData.getElementClass();

        if (clz == Image.class) {
            Icon icon = label.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ( (ImageIcon) icon).getImage();
                return image;
            }
        }
	if (clz == LinkModel.class) {
	    return (LinkModel)label.getClientProperty("jdnc.link.value");
	}
        return label.getText();
    }

    protected void setComponentValue(Object value) {
	Class clz = metaData.getElementClass();

        if (clz == Image.class) {
            if (value != null) {
                ImageIcon icon = new ImageIcon( (Image) value);
                label.setIcon(icon);
            }
        }
	if (clz == LinkModel.class) {
	    if (value != null) {
		label.setText("<html>" + convertFromModelType(value) + "</html>");
		label.putClientProperty("jdnc.link.value", (LinkModel)value);
	    }
	}
        else {
            label.setText(convertFromModelType(value));
        }
    }
}
