/*
 * $Id: AbstractDataModel.java,v 1.2 2005/10/10 17:01:03 rbair Exp $
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

package org.jdesktop.binding;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.Validator;

/**
 * Abstract base class for implementing concrete DataModel implementations.
 * This class provides support for managing validators and value change listeners.
 * Subclasses must implement their own mechanism to store field meta-data and
 * values.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public abstract class AbstractDataModel implements DataModel {

    protected ArrayList validators;
    private ArrayList valueChangeListeners;
    private HashMap valueChangeEvents;

    public abstract String[] getFieldNames();

    public MetaData[] getMetaData() {
        String fieldNames[] = getFieldNames();
        MetaData metaData[] = new MetaData[fieldNames.length];
        for(int i = 0; i < fieldNames.length; i++) {
            metaData[i] = getMetaData(fieldNames[i]);
        }
        return metaData;
    }

    public abstract MetaData getMetaData(String fieldName);

    public abstract Object getValue(String fieldName);

    public void setValue(String fieldName, Object value) {
        Object oldValue = getValue(fieldName);
        setValueImpl(fieldName, value);
        if ((oldValue != null && !oldValue.equals(value)) ||
            (oldValue == null && value != null)) {
            fireValueChanged(fieldName);
        }
    }

    protected abstract void setValueImpl(String fieldName, Object value);

    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }
    public Validator[] getValidators() {
        if (validators != null) {
            return (Validator[]) validators.toArray(new Validator[validators.size()]);
        }
        return new Validator[0];
    }

    public void addValueChangeListener(ValueChangeListener l) {
        if (valueChangeListeners == null) {
            valueChangeListeners = new ArrayList();
        }
        valueChangeListeners.add(l);
    }

    public void removeValueChangeListener(ValueChangeListener l) {
        if (valueChangeListeners != null){
            valueChangeListeners.remove(l);
        }
    }

    public ValueChangeListener[] getValueChangeListeners() {
        if (valueChangeListeners != null) {
            return (ValueChangeListener[])valueChangeListeners.toArray(new ValueChangeListener[1]);
        }
        return new ValueChangeListener[0];
    }

    protected void fireValueChanged(String fieldName) {
        ValueChangeListener[] formListeners = getValueChangeListeners();
        ValueChangeEvent e = getCachedEvent(fieldName);
        if (valueChangeListeners != null) {
            for (int i = 0; i < valueChangeListeners.size(); i++) {
                ValueChangeListener vcl = (ValueChangeListener)
                    valueChangeListeners.get(i);
                vcl.valueChanged(e);
            }
        }
    }

    private ValueChangeEvent getCachedEvent(String fieldName) {
        if (valueChangeEvents == null) {
            valueChangeEvents = new HashMap();
        }
        ValueChangeEvent event = (ValueChangeEvent)valueChangeEvents.get(fieldName);
        if (event == null) {
            event = new ValueChangeEvent(this, fieldName);
            valueChangeEvents.put(fieldName, event);
        }
        return event;
    }


}
