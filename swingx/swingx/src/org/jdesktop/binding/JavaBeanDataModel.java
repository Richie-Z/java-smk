/*
 * $Id: JavaBeanDataModel.java,v 1.2 2005/10/10 17:01:05 rbair Exp $
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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.NumberMetaData;

/**
 * A class that creates a collection of MetaData based BeanInfo
 * PropertyDescriptors. To use this class:
 * <ol>
 *   <li>Construct the model using the Bean class you wish to model
 *   <li>use <code>setJavaBean</code> to set the current object of this class.
 *   <li>Updates made to the form will update the property values of the bean.
 * </ol>
 * <p>
 * TODO: Using JavaBeans as a data source should be generalized and not
 * constrained to FormModels.
 *
 * @author Mark Davidson
 */
public class JavaBeanDataModel extends DefaultDataModel {

    private BeanInfo info;
    // needed for error checking in setJavaBean
    private Class beanClass;
    private Object bean; // current bean instance

    private static final Map<Class, Class> primitivesToBoxed = new HashMap<Class,Class>();
    
    // temporarily public for testing!
    public PropertyChangeListener propertyChangeListener;
    static {
        primitivesToBoxed.put(Integer.TYPE, Integer.class);
        primitivesToBoxed.put(Long.TYPE, Long.class);
        primitivesToBoxed.put(Short.TYPE, Short.class);
        primitivesToBoxed.put(Byte.TYPE, Byte.class);
        primitivesToBoxed.put(Float.TYPE, Float.class);
        primitivesToBoxed.put(Double.TYPE, Double.class);
        primitivesToBoxed.put(Boolean.TYPE, Boolean.class);
    }
    
    public JavaBeanDataModel(Class beanClass) throws IntrospectionException {
        this(beanClass, null);
    }

    public JavaBeanDataModel(Object bean) throws IntrospectionException {
        this(bean.getClass(), bean);
    }
	
    /**
     * Constructs a JavaBeanDataModel by introspecting on the class and using the data from
     * the object as the current bean
     *
     * @param beanClass the class to use to introspect properties
     * @param bean the object where the current values will be retrieved and stored.
     */
    public JavaBeanDataModel(Class beanClass, Object bean) throws IntrospectionException {
        this(beanClass, bean, null);
    }

    public JavaBeanDataModel(Class beanClass, Object bean, MetaData[] metaData) throws IntrospectionException {
        this.beanClass = beanClass;
        createDefaultMetaData(beanClass, metaData);
        setJavaBean(bean);
        
    }
    private void createDefaultMetaData(Class beanClass, MetaData[] metaData) throws IntrospectionException {
        info = Introspector.getBeanInfo(beanClass);
        boolean bound = false;
        if (metaData == null) {
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (int i = 0; i < props.length; i++) {
            	PropertyDescriptor prop = props[i];
                bound = bound || prop.isBound();
                addField(createMetaData(prop));
            }
        } else {
            for (int i = 0; i < metaData.length; i++) {
                PropertyDescriptor prop = getPropertyDescriptor(metaData[i].getName());
                if (prop != null) {
                    bound = bound || prop.isBound();
                    addField(metaData[i]);
                }
            }
        }
        if (bound) {
            initPropertyChangeListener();
        }
    }

    private MetaData createMetaData(PropertyDescriptor prop) {
        
        MetaData metaData = null;
        // JW: cannot cope with indexedPropertyTypes (?)
        // they seem to return null for getPropertyType
        if (Number.class.isAssignableFrom(prop.getPropertyType())) {
            metaData = new NumberMetaData(
                    prop.getName(),
                    prop.getPropertyType(),
                    prop.getDisplayName());
        } else if (isNumberType(prop))
        {
            // convert the primitive types to their boxed types
            // this makes the binding code happier in
            // AbstractBinding#convertToModelType
             metaData = new NumberMetaData(
                            prop.getName(),
                            primitivesToBoxed.get(prop.getPropertyType()),
                            prop.getDisplayName());
            // JW: really??
            metaData.setRequired(true);
        } else {
            Class type = prop.getPropertyType();
            if (primitivesToBoxed.get(type) != null) {
                type = primitivesToBoxed.get(type);
            }
            metaData = new MetaData(prop.getName(), type, prop
                                .getDisplayName());
                
        }
        return metaData;
    }

    private void initPropertyChangeListener() {
        if (propertyChangeListener != null) return;
        try {
            beanClass.getMethod("addPropertyChangeListener", 
                    new Class[] { PropertyChangeListener.class });
            beanClass.getMethod("removePropertyChangeListener", 
                    new Class[] { PropertyChangeListener.class });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } 
        propertyChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fireValueChanged(evt.getPropertyName());
                
            }
            
        };
        
    }

    private boolean isNumberType(PropertyDescriptor prop) {
        if (prop.getPropertyType() == Boolean.TYPE) return false;
        return prop.getPropertyType()==Integer.TYPE
             || prop.getPropertyType()==Long.TYPE
             || prop.getPropertyType()==Short.TYPE
             || prop.getPropertyType()==Byte.TYPE
             || prop.getPropertyType()==Float.TYPE
             || prop.getPropertyType()==Double.TYPE;
    }

    /**
     * Set the JavaBean instance that this model will use.
     */
    public void setJavaBean(Object bean) {
        if (bean != null && bean.getClass() != beanClass) {
            throw new RuntimeException("ERROR: argument is not a " +
                                       beanClass.toString());
        }

        Object oldBean = this.bean;
        removePropertyChangeListener(oldBean);
        this.bean = bean;
        if (bean != oldBean) {
            // Should update all the values
            String[] fieldNames = getFieldNames();
            for (int i = 0; i < fieldNames.length; i++) {
                fireValueChanged(fieldNames[i]);
            }
        }
        addPropertyChangeListener(bean);
    }

    private void addPropertyChangeListener(Object bean) {
        if ((propertyChangeListener == null) || (bean == null)) return;
        try {
            Method adder = beanClass.getMethod("addPropertyChangeListener", 
                    new Class[] { PropertyChangeListener.class });
            adder.invoke(bean, new Object[] { propertyChangeListener });
       } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
    }

    private void removePropertyChangeListener(Object bean) {
        if ((propertyChangeListener == null) || (bean == null)) return;
        try {
            Method adder = beanClass.getMethod("removePropertyChangeListener", 
                    new Class[] { PropertyChangeListener.class });
            adder.invoke(bean, new Object[] { propertyChangeListener });
       } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    /**
     * Get the JavaBean instance that this model uses.
     */
    public Object getJavaBean() {
        return bean;
    }

    public Object getValue(String fieldName) {
        if (getJavaBean() == null) {
            return null;
        }
        PropertyDescriptor prop = getPropertyDescriptor(fieldName);
        Method readMethod = prop.getReadMethod();
        if (readMethod != null) {
            try {
                return readMethod.invoke(getJavaBean(), new Object[0]);
            }
            catch (Exception ex) {
                // XXX excecption for illegal access, etc..
                ex.printStackTrace();
            }
        }
        return null;
    }

    // XXX should be protected.
    protected void setValueImpl(String fieldName, Object value) {
        if (getJavaBean() == null) {
            return;
        }
        PropertyDescriptor prop = getPropertyDescriptor(fieldName);
        Method writeMethod = prop.getWriteMethod();
        if (writeMethod != null) {
            try {
                writeMethod.invoke(getJavaBean(), new Object[] {value});
            }
            catch (Exception ex) {
                // XXX exception for illegal access, etc..
                ex.printStackTrace();
            }
        }
    }

    private PropertyDescriptor getPropertyDescriptor(String name) {
        PropertyDescriptor pd = null;
        PropertyDescriptor[] desc = info.getPropertyDescriptors();
        for (int i = 0; i < desc.length; i++) {
            if (name.equals(desc[i].getName())) {
                pd = desc[i];
                break;
            }
        }
        return pd;
    }
}
