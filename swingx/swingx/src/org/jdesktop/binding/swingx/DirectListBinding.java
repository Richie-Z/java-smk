/*
 * $Id: DirectListBinding.java,v 1.2 2005/10/10 17:00:54 rbair Exp $
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
import javax.swing.JList;

import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.TabularDataModel;
import org.jdesktop.binding.swingx.adapter.DataModelToListModelAdapter;
import org.jdesktop.binding.swingx.adapter.ListSelectionBinding;

/**
 * This "Binding" happens to the given DataModel as a whole (as
 * opposed to a single field of the model).
 * 
 * @author Richard Bair
 */
public class DirectListBinding extends AbstractBinding {
    
    private JList list;


    public DirectListBinding(JList list, TabularDataModel model, String displayFieldName, SelectionModel sm) {
        super(list, model, displayFieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        //create a selection binding
        new ListSelectionBinding(sm, list.getSelectionModel());
        //create a custom ListModel bound to the entire TabularDataModel
        list.setModel(new DataModelToListModelAdapter(model, displayFieldName));
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
        list = (JList)component;
    }

    protected Object getComponentValue(){
        return null;
    }

    protected void setComponentValue(Object value) {
    }
}

