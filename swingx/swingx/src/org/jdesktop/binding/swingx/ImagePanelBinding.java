/*
 * $Id: ImagePanelBinding.java,v 1.2 2005/10/10 17:00:56 rbair Exp $
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
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.swingx.JXImagePanel;

public class ImagePanelBinding extends AbstractBinding {

    private JXImagePanel imagePanel;
    private Object val;

    public ImagePanelBinding(JXImagePanel imagePanel,
			     DataModel model, String fieldName) {
        super(imagePanel, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
	return imagePanel;
    }

    public void setComponent(JComponent component) {
	this.imagePanel = (JXImagePanel)component;
    }

    protected Object getComponentValue() {
	Class klazz = metaData.getElementClass();
	if (klazz == Image.class) {
	    return imagePanel.getImage();
	} else if (klazz == String.class) {
        return val;
    }
//	else if (klazz == Icon.class) {
//	    return imagePanel.getIcon();
//	}
	// default?
	return null;
    }

    protected void setComponentValue(Object value) {
        Class klazz = metaData.getElementClass();
        val = value;
        if (klazz == Image.class) {
            imagePanel.setImage((BufferedImage)value);
//        } else if (klazz == ImageIcon.class) {
//            imagePanel.setIcon((ImageIcon)value);
        } else if (klazz == String.class) {
            try {
                ImageIcon ii = new ImageIcon(new URL((String)value));
                BufferedImage buf = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                buf.createGraphics().drawImage(ii.getImage(), 0, 0, ii.getImageObserver());
                imagePanel.setImage(buf);
            } catch (Exception e) {
                imagePanel.setImage(null);
            }
        }
    }

}
