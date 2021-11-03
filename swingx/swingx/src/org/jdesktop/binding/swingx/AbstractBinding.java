/*
 * $Id: AbstractBinding.java,v 1.2 2005/10/10 17:00:50 rbair Exp $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.ValueChangeEvent;
import org.jdesktop.binding.ValueChangeListener;
import org.jdesktop.binding.metadata.ConversionException;
import org.jdesktop.binding.metadata.Converter;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.Validator;

/**
 * Abstract base class which implements a default mechanism for binding
 * user-interface components to elements in a data model.
 *
 * Note: 
 * 
 * @author Amy Fowler
 * @version 1.0
 */

public abstract class AbstractBinding implements Binding {

    protected DataModel dataModel;
    protected MetaData metaData;
    protected String fieldName;
    protected Object cachedValue;
    protected ArrayList errorList;
    protected boolean modified = false;
    protected int validState = UNVALIDATED;
    protected boolean pulling = false;
    protected boolean pushing = false;

    private PropertyChangeSupport pcs;
    private int validationPolicy;
    private static final PropertyChangeListener[] 
        EMPTY_PROPERTY_CHANGE_LISTENER_ARRAY = new PropertyChangeListener[0];

    /**
     * 
     * @param component
     * @param dataModel
     * @param fieldName
     * @param validationPolicy
     * @throws NullPointerException if component, dataModel or
     *     fieldName is null 
     */
    protected AbstractBinding(JComponent component,
                              DataModel dataModel, String fieldName,
                              int validationPolicy) {
        checkNull(component, "component must not be null");
        checkNull(dataModel, "model must not be null");
        checkNull(fieldName, "fieldName must not be null");
        installDataModel(dataModel, fieldName);
        setComponent(component);
        setValidationPolicy(validationPolicy);
    }

//----------------------- implementing Binding
    
    public DataModel getDataModel() {
        return dataModel;
    }

    public String getFieldName() {
        return fieldName;
    }

    /** 
     * 
     * @param policy
     */
    public void setValidationPolicy(int policy) {
        int oldValidationPolicy = this.validationPolicy;
        this.validationPolicy = policy;
        installInputVerifier();
        if (policy != oldValidationPolicy) {
            firePropertyChange("validationPolicy",
                                new Integer(oldValidationPolicy),
                                new Integer(policy));
        }
    }


    public int getValidationPolicy() {
        return validationPolicy;
    }

    public boolean pull() {
        pulling = true;
//        if (metaData != null) {
            cachedValue = dataModel.getValue(metaData.getName());
            setComponentValue(cachedValue);
//        }
        setModified(false);
        setValidState(UNVALIDATED);
        // JW: initial check to force visual feedback on required
        isValid();
        pulling = false;
        return true;
    }

    public boolean push() {
        if (isValid()) {
            pushing = true;
            dataModel.setValue(metaData.getName(), cachedValue);
            setModified(false);
            pushing = false;
            return true;
        }
        return false;
    }
    
    public boolean isModified() {
        return modified;
    }

    public boolean isValid() {
        if (validState != UNVALIDATED) {
            return validState == VALID;
        }
        // need to validate
        clearValidationErrors();
        Object componentValue = getComponentValue();

        // step 1: ensure a required element has non-null value
        boolean ok = checkRequired(componentValue);

        // step 2: if necessary, convert value from component to data type
        //         appropriate for model
        Object convertedValue = null;
        if (ok) {
            try {
                convertedValue = convertToModelType(componentValue);
            } catch (Exception e) {
                ok = false;
                /**@todo aim: very nerdy message */
                addError("value must be type " + metaData.getElementClass().getName());
            }
        }

        // step 3: run any registered element-level validators
        if (ok) {
            ok = executeValidators(convertedValue);
        }

        if (ok) {
            cachedValue = convertedValue;
        }
        setValidState(ok? VALID : INVALID);

        return validState == VALID;

    }

    public int getValidState() {
        return validState;
    }


    public String[] getValidationErrors() {
        if (errorList != null) {
            return (String[])errorList.toArray(new String[1]);
        }
        return new String[0];
    }

    public void clearValidationErrors() {
        if (errorList != null) {
            errorList.clear();
        }
    }


    /**
     * Adds the specified property change listener to this binding object.
     * @param pcl PropertyChangeListener object to receive events when binding
     *        properties change
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);

        }
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes the specified property change listener from this binding object.
     * @param pcl PropertyChangeListener object to receive events when binding
     *        properties change
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     *
     * @return array containing the PropertyChangeListener objects registered
     *         on this binding object
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (pcs == null) return EMPTY_PROPERTY_CHANGE_LISTENER_ARRAY;
        return pcs.getPropertyChangeListeners();
    }

    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        if (pcs == null) return;
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    
//----------------- component related access to implement by subclasses
    
    /**
     * set component and configures metaData dependent logic/constraint state.
     * @param component
     */
    protected abstract void setComponent(JComponent component);
    protected abstract Object getComponentValue();
    protected abstract void setComponentValue(Object value);

//---------------------- update internal state: conversion/validation
    
    protected boolean checkRequired(Object componentValue) {
//        if (metaData != null) {
            if (metaData.isRequired() && isEmpty(componentValue)) {
                addError("requires a value");
                return false;
            }
            return true;
//        }
//        return false;
    }

    protected boolean isEmpty(Object componentValue) {
        return (componentValue == null ||
         (componentValue instanceof String && ((String)componentValue).equals("")));
    }

    protected Object convertToModelType(Object componentValue) throws ConversionException {
        Object convertedValue = null;
        // if the element is not required and the value is null, then it's
        // okay to skip conversion
        if (isEmpty(componentValue)) {
            return convertedValue;
        }
        Class elementClass = metaData.getElementClass();
        if (componentValue instanceof String) {
            String stringValue = (String) componentValue;
            Converter converter = metaData.getConverter();
            if (converter != null) {
                convertedValue = converter.decode(stringValue,
                                                      metaData.getDecodeFormat());
            } else if (metaData.getElementClass() == String.class) {
                convertedValue = componentValue;
            }
        }
        else {
            if (!elementClass.isAssignableFrom(componentValue.getClass())) {
                throw new ConversionException("cannot assign component value");
            } else {
                convertedValue = componentValue;
            }
        }
        return convertedValue;
    }

    protected String convertFromModelType(Object modelValue) {
        if (modelValue != null) {
            try {
                Converter converter = metaData.getConverter();
                return converter.encode(modelValue, metaData.getEncodeFormat());
            }
            catch (Exception e) {
                /**@todo aim: how to handle conversion failure? */
                return modelValue.toString();
            }
        }
        return "";
    }

    protected boolean executeValidators(Object value) {
        Validator validators[] = metaData.getValidators();
        boolean isValid = true;
        for (int i = 0; i < validators.length; i++) {
            String error[] = new String[1];
            boolean passed = validators[i].validate(value, null, error);
            if (!passed) {
                String errorMessage = error[0];
                if (errorMessage != null) {
                    addError(errorMessage);
                }
                isValid = false;
            }
        }
        return isValid;
    }

    protected void addError(String error) {
        if (errorList == null) {
            errorList = new ArrayList();
        }
        errorList.add(error);
    }


    protected void setModified(boolean modified) {
        // JW: commented to fix issue #78
//        if (pulling) {
//            return;
//        }
        boolean oldModified = this.modified;
        this.modified = modified;
        if (modified) {
            cachedValue = null;
            setValidState(UNVALIDATED);
        }
        if (oldModified != modified) {
            firePropertyChange("modified",
                                Boolean.valueOf(oldModified),
                                Boolean.valueOf(modified));
        }
    }

    protected void setValidState(int validState) {
        int oldValidState = this.validState;
        this.validState = validState;
        if (oldValidState != validState &&
            validState == UNVALIDATED) {
            clearValidationErrors();
        }
        if (validState != oldValidState) {
            firePropertyChange("validState",
                               new Integer(oldValidState),
                               new Integer(validState));
        }
    }

    
    /** installs an InputVerifier depending on 
     *  validationPolicy. 
     */
    protected void installInputVerifier() {
        getComponent().setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                if (validationPolicy != AUTO_VALIDATE_NONE) {
                    boolean isValid = isValid();
                    if (!isValid && validationPolicy == AUTO_VALIDATE_STRICT) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
        });
    }

//---------------------- init models, listeners
    
    protected void checkNull(Object component, String message) {
        if (component == null) throw new NullPointerException(message);
    }

    protected void installDataModel(DataModel dataModel, String fieldName) {
        this.dataModel = dataModel;
        this.fieldName = fieldName;
        metaData = dataModel.getMetaData(fieldName);
        // JW, Noel Grandin: the idea is to fail fast on typos
        // should be moved to dataModel api
        // JW: DirectTableBinding will fail
        // JW: for now do it anyway - let DirectTableBinding work it out
        checkNull(metaData, "Field " + fieldName
                + " does not exist in metadata");
        installDataModelListener();
        installMetaDataListener();
    }

    protected void installDataModelListener() {
        dataModel.addValueChangeListener(new ValueChangeListener() {
            public void valueChanged(ValueChangeEvent e) {
                if (e.getFieldName().equals(fieldName) &&
                      !pushing) {
                    pull();
                }
            }
        });
    }


    /**
     * 
     * here: does nothing
     *
     */
    protected void installMetaDataListener() {
//        metaData.addPropertyChangeListener(new PropertyChangeListener() {
//
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (!"enabled".equals(evt.getPropertyName())) return;
//                boolean enabled = ((Boolean) evt.getNewValue()).booleanValue();
//                getComponent().setEnabled(enabled);
//                
//            }
//            
//        });
    }
    
}
