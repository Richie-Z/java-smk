/*
 * $Id $Exp
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
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.metadata.Validator;

/**
 * Container of Bindings. Responsible for doing validation/push/pull. 
 * Listens to propertyChanges of contained Bindings.
 * 
 * @author Jeanette Winzenburg
 */
public class BindingHandler {

    private List bindings;

    private boolean modified;

    private PropertyChangeSupport propertySupport;

    private PropertyChangeListener bindingListener;

    private boolean autoCommit;

    public boolean isModified() {
        return modified;
    }

    public void add(Binding binding) {
        if (binding == null)
            return;
        getBindingList().add(binding);
        binding.addPropertyChangeListener(getBindingListener());
        // todo: update modified flag

    }

    public void remove(Binding binding) {
        if (binding == null)
            return;
        getBindingList().remove(binding);
        binding.removePropertyChangeListener(getBindingListener());
        // todo: update modified flag
    }

    public void removeAll() {
        for (Iterator iter = getBindingList().iterator(); iter.hasNext();) {
            Binding element = (Binding) iter.next();
            element.removePropertyChangeListener(getBindingListener());
            iter.remove();

        }
        setModified(false);
    }

    public Binding[] getBindings() {
        if (bindings != null) {
            return (Binding[]) bindings.toArray(new Binding[bindings.size()]);
        }
        return new Binding[0];
    }

    public boolean pull() {
        boolean result = true;
        for (Iterator iter = getBindingList().iterator(); iter.hasNext();) {
            Binding element = (Binding) iter.next();
            if (!element.pull()) {
                result = false;
            }

        }
        return result;
    }

    public boolean validate() {
        boolean result = true;
        ArrayList models = new ArrayList();
        for (Iterator iter = getBindingList().iterator(); iter.hasNext();) {
            Binding element = (Binding) iter.next();
            DataModel bindingModel = element.getDataModel();
            if (!models.contains(bindingModel)) {
                models.add(bindingModel);
            }
            if (!element.isValid()) {
                result = false;
            }

        }
        if (result) {
            for (int i = 0; i < models.size(); i++) {
                DataModel model = (DataModel) models.get(i);
                Validator validators[] = model.getValidators();
                for (int j = 0; j < validators.length; j++) {
                    String error[] = new String[1];
                    /** @todo aim: where to put error? */
                    if (!validators[j].validate(model, /* getLocale() */null,
                            error)) {
                        result = false;
                    }
                }
            }

        }
        return result;
    }

    public boolean push() {
        if (!validate()) {
            return false;
        }
        boolean result = true;
        for (Iterator iter = getBindingList().iterator(); iter.hasNext();) {
            Binding binding = (Binding) iter.next();
            if (!binding.push()) {
                result = false;
            }
        }
        return result;
    }

    
    // ---------------- property change support

    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (propertySupport == null) {
            propertySupport = new PropertyChangeSupport(this);
        }
        propertySupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (propertySupport == null)
            return;
        propertySupport.removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String name, Object oldValue,
            Object newValue) {
        if (propertySupport == null)
            return;
        propertySupport.firePropertyChange(name, oldValue, newValue);
    }

    private PropertyChangeListener getBindingListener() {
        if (bindingListener == null) {
            bindingListener = createBindingListener();
        }
        return bindingListener;
    }

    protected PropertyChangeListener createBindingListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("modified".equals(evt.getPropertyName())) {
                   updateModifiedFromBinding((Binding) evt.getSource(), ((Boolean) evt.getNewValue())
                            .booleanValue());

                }

            }

        };

        return l;
    }

    protected void updateModifiedFromBinding(Binding binding, boolean modified) {
        if (isAutoCommit() && modified) {
            invokePush(binding);
            return;
        }
        if (isModified())
            return;
        setModified(isModified() || modified);
    }

    private void invokePush(final Binding binding) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // PENDING: validation errors?
                boolean result = binding.push();
                setModified(!result);

            }
        });
    }

    private List getBindingList() {
        if (bindings == null) {
            bindings = new ArrayList();
        }
        return bindings;
    }

    private void setModified(boolean modified) {
        boolean oldModified = isModified();
        this.modified = modified;
        firePropertyChange("modified", oldModified, isModified());
    }

    /**
     * if true will push the values on every modified notification.
     * 
     * @param autoCommit
     */
    public void setAutoCommit(boolean autoCommit) {
        boolean old = isAutoCommit();
        if (old == autoCommit) {
            return;
        }
        this.autoCommit = autoCommit;
        // think: before or after firing autocommit change?
        if (autoCommit && isModified()) {
            push();
        }
        firePropertyChange("autoCommit", old, isAutoCommit());
        
    }

    public boolean isAutoCommit() {
        // TODO Auto-generated method stub
        return autoCommit;
    }
    


}
