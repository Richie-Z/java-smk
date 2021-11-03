/*
 * $Id: MetaData.java,v 1.3 2005/10/10 17:01:09 rbair Exp $
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

package org.jdesktop.binding.metadata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Class for representing the meta-data for an field in a data model.
 * A &quot;field&quot; may map to a column on a RowSet, a property on a JavaBean,
 * or some other discrete data element on a data model.
 * The meta-data describes aspects of a data field such as it's name,
 * type, and edit constraints.  This class may be used when such information
 * isn't encapsulated in the data model object itself and thus must be represented
 * in an external object.  Meta-data is intended only for holding state about
 * a data model element and is not intended for implementing application semantics.</p>
 * <p>
 * A meta-data object should be initialized at a minimum with a name, class,
 * and label.  Additional meta-data properties can be set as necessary.
 * For example:<br>
 * <pre><code>
 *     MetaData metaData = new MetaData(&quot;firstname&quot;, String.class,
 *                                      &quot;First Name&quot;);
 *     metaData.setRequired(true);
 * </code></pre>
 * If the associated data model field requires application-specific validation
 * logic to verify the correctness of a potential value, one or more Validator
 * instances can be added to the meta-data object.  Example:<br>
 * <pre><code>
 *     MetaData metaData = new MetaData(&quot;creditcard&quot;, String.class,
 *                                      &quot;Credit Card Number&quot;
 *     metaData.setRequired(true);
 *     metaData.addValidator(new MyCreditCardValidator());
 * </code></pre>
 * </p>
 *
 * @author Amy Fowler
 * @author Rich Bair
 * @version 1.0
 */

public class MetaData {
    protected String name;
    protected Class klass = String.class;
    protected String label;
    protected Converter converter = null;
    protected Object decodeFormat = null;
    protected Object encodeFormat = null;
    protected boolean readOnly = false;
    protected int minValueCount = 0; // null value okay
    protected int maxValueCount = 1; // only one value allowed
    protected int displayWidth = 24;
    protected ArrayList validators = null;

    protected Map customProps = new HashMap();
    protected PropertyChangeSupport pcs;

    /**
     * Instantiates a meta-data object with a default name &quot;value&quot; and
     * a default field class equal to <code>java.lang.String</code>.
     * This provides the no-argument constructor required for JavaBeans.
     * It is recommended that the program explicitly set a meaningful
     * &quot;name&quot; property.
     */
    public MetaData() {
        this("value");
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * a default field class equal to <code>java.lang.String</code>.
     * @param name String containing the name of the data field
     */
    public MetaData(String name) {
        this.name = name;
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * field class.
     * @param name String containing the name of the data field
     * @param klass Class indicating type of data field
     */
    public MetaData(String name, Class klass) {
        this(name);
        this.klass = klass;
    }

    /**
     * Instantiates a meta-data object with the specified name,
     * field class, and label.
     * @param name String containing the name of the data field
     * @param klass Class indicating type of data field
     * @param label String containing the user-displayable label for the
     *        data field
     */
    public MetaData(String name, Class klass, String label) {
        this(name, klass);
        this.label = label;
    }

    /**
     * Gets the meta-data &quot;name&quot; property which indicates
     * the logical name of the associated data field.
     * @see #setName
     * @return String containing the name of the data field.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the meta-data &quot;name&quot; property.
     * @see #getName
     * @param name String containing the name of the data field
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange("name", oldName, name);
    }

    /**
     * Gets the meta-data's &quot;elementClass&quot; property which
     * indicates the type of the associated data field.
     * The default field class is <code>java.lang.String</code>.
     * @see #setElementClass
     * @return Class indicating type of data field
     */
    public Class getElementClass() {
        return klass;
    }

    /**
     * Sets the meta-data's &quot;elementClass&quot; property.
     * This <code>set</code> method is provided for meta-data bean initialization
     * only; the field class is not intended to be modified after initialization,
     * since other aspects of a meta-data object may depend on this type setting.
     * @see #getElementClass
     * @param klass Class indicating type of data field
     */
    public void setElementClass(Class klass) {
        Class oldClass = this.klass;
        this.klass = klass;
        firePropertyChange("elementClass", oldClass, klass);
    }

    /**
     * Gets the meta-data's &quot;label&quot; property, which provides
     * a label for the associated data field.  The label is intended
     * for display to the end-user and may be localized.
     * If no label has been explicitly set, then the meta-data's name is
     * returned.
     * @see #setLabel
     * @return String containing the user-displayable label for the
     *         data field
     */
    public String getLabel() {
        return label == null? name : label;
    }

    /**
     * Sets the meta-data's &quot;label&quot; property.
     * @todo Rename to setTitle
     *
     * @see #getLabel
     * @param label String containing the user-displable label for the
     *        data field
     */
    public void setLabel(String label) {
        String oldLabel = this.label;
        this.label = label;
        firePropertyChange("label", oldLabel, label);
    }

    /**
     * Gets the meta-data's converter, which performs conversions between
     * string values and objects of the associated data field's type.
     * If no converter was explicitly set on this meta-data object,
     * this method will retrieve a default converter for this data
     * field's type from the converter registry.  If no default converter
     * is registered, this method will return <code>null</code>.
     * @see Converters#get
     * @see #setConverter
     * @see #getElementClass
     * @return Converter used to perform conversions between string values
     *         and objects of this field's type
     */
    public Converter getConverter() {
        if (converter == null) {
            return Converters.get(klass);
        }
        return converter;
    }

    /**
     * Sets the meta-data's converter.
     *
     * @see #getConverter
     * @param converter Converter used to perform conversions between string values
     *         and objects of this field's type
     */
    public void setConverter(Converter converter) {
        Converter oldConverter = this.converter;
        this.converter = converter;
        firePropertyChange("converter", oldConverter, converter);
    }

    /**
     * Gets the meta-data's decode format which is used when converting
     * values from a string representation.  This property must be used when
     * conversion requires format information on how the string representation
     * is structured.  For example, a decode format should be used when decoding
     * date values.  The default decode format is <code>null</code>.
     * @see #setDecodeFormat
     * @return format object used to describe format for string-to-object conversion
     */
    public Object getDecodeFormat() {
        return decodeFormat;
    }

    /**
     * Sets the meta-data's decode format which is used when converting
     * values from a string representation.
     * @see #getDecodeFormat
     * @see java.text.DateFormat
     * @param format object used to describe format for string-to-object conversion
     */
    public void setDecodeFormat(Object format) {
        Object oldDecodeFormat = this.decodeFormat;
        this.decodeFormat = format;
        firePropertyChange("decodeFormat", oldDecodeFormat, format);
    }

    /**
     * Gets the meta-data's encode format which is used when converting
     * values to a string representation.  This property must be used when
     * conversion requires format information on how the string representation
     * should be generated.  For example, an encode format should be used when
     * encoding date values.  The default encode format is <code>null</code>.
     * @see #setEncodeFormat
     * @return format object used to describe format for object-to-string conversion
     */
    public Object getEncodeFormat() {
        return encodeFormat;
    }

    /**
     * Sets the meta-data's encode format which is used when converting
     * values to a string representation.
     * @see #getEncodeFormat
     * @see java.text.DateFormat
     * @param format object used to describe format for object-to-string conversion
     */
    public void setEncodeFormat(Object format) {
        Object oldEncodeFormat = this.encodeFormat;
        this.encodeFormat = format;
        firePropertyChange("encodeFormat", oldEncodeFormat, format);
    }

    /**
     * @return integer containing the number of characters required to
     *        display the value
     */
    public int getDisplayWidth() {
        return displayWidth;
    }

    /**
     * Sets the meta-data's &quot;displayWidth&quot; property which provides
     * a hint as to the number of characters typically required to display the value.
     * The default is 24.
     * @see #getDisplayWidth
     * @param displayWidth integer containing the number of characters required to
     *        display the value
     * @throws IllegalArgumentException if displayWidth < 0
     */
    public void setDisplayWidth(int displayWidth) {
        if (displayWidth < 0) {
            throw new IllegalArgumentException("displayWidth must be >= 0");
        }
        int oldDisplayWidth = this.displayWidth;
        this.displayWidth = displayWidth;
        firePropertyChange("displayWidth", oldDisplayWidth, displayWidth);
    }


    /**
     * Gets the meta-data's &quot;readOnly&quot; property which indicates
     * whether or not the associated data field's value cannot be modified.
     * The default is <code>false</code>.
     * @see #setReadOnly
     * @return boolean indicating whether the data field is read-only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the meta-data's &quot;readOnly&quot; property.
     * @see #isReadOnly
     * @param readOnly boolean indicating whether the data field is read-only
     */
    public void setReadOnly(boolean readOnly) {
        boolean oldReadOnly = this.readOnly;
        this.readOnly = readOnly;
        firePropertyChange("readOnly", oldReadOnly, readOnly);
    }

    /**
     * Gets the meta-data's &quot;minValueCount&quot; property, which indicates
     * the minimum number of values required for the data field.  The default
     * is 0, which means a null value is permitted.  This property should be set
     * to 1 if the field requires a non-null value.
     * @see #setMinValueCount
     * @return integer indicating the minimum number of values required for
     *         the data field
     */
    public int getMinValueCount() {
        return minValueCount;
    }

   /**
    * Sets the meta-data's &quot;minValueCount&quot; property.
    * @param minValueCount integer indicating the minimum number of values required for
    *         the data field
    */
   public void setMinValueCount(int minValueCount) {
       int oldMinValueCount = this.minValueCount;
       this.minValueCount = minValueCount;
       firePropertyChange("minValueCount", oldMinValueCount, minValueCount);
   }

   /**
    * Convenience method for calculating whether the &quot;minValueCount&quot;
    * property is greater than 0.
    * @return boolean indicating whether at least one non-null value must
    *         be set for the data field
    */
   public boolean isRequired() {
       return getMinValueCount() > 0;
   }

   public void setRequired(boolean required) {
       if (required) {
           if (getMinValueCount() <= 0) {
               setMinValueCount(1);
           }
       }
       else { /* not required */
           if (getMinValueCount() > 0) {
               setMinValueCount(0);
           }
       }
   }

   /**
    * Gets the meta-data's &quot;maxValueCount&quot; property, which indicates
    * the maximum number of values permitted for the data field.  The default
    * is 1, which means a single value is permitted.  If this property is set
    * to a value greater than 1, then the values will be contained in a
    * <code>List</code> collection.
    * @see java.util.List
    * @see #setMaxValueCount
    * @return integer indicating the maximum number of values permitted for
    *         the data field
    */
   public int getMaxValueCount() {
       return maxValueCount;
   }

   /**
    * Sets the meta-data's &quot;maxValueCount&quot; property.
    * @param maxValueCount integer indicating the maximum number of values permitted for
    *         the data field
    */
   public void setMaxValueCount(int maxValueCount) {
       int oldMaxValueCount = this.maxValueCount;
       this.maxValueCount = maxValueCount;
       firePropertyChange("maxValueCount", oldMaxValueCount, maxValueCount);
   }

   /**
    * Places a custom property into this meta-data's custom properties map.
    *
    * @param propertyName   A non-null string of the form
    *                       com.mydomain.packagename.PropertyName
    * @param value The value for the named property
    */
   public void setCustomProperty(String propertyName, Object value) {
       if (propertyName == null) {
           throw new NullPointerException("The propertyName for a custom property " +
                                              "on MetaData cannot be null");
       }
       Object oldValue = customProps.get(propertyName);
       customProps.put(propertyName, value);
       firePropertyChange(propertyName, oldValue, value);
   }

   /**
    * @param propertyName A non-null string of the form
    *            com.mydomain.packagename.PropertyName
    * @return The value for the given propertyName in the custom properties map.
    */
   public Object getCustomProperty(String propertyName) {
       if (propertyName == null) {
           throw new NullPointerException("The propertyName for a custom property " +
                                              "on MetaData cannot be null");
       }
       return customProps.get(propertyName);
   }

   /**
    * @param propertyName A non-null string of the form com.mydomain.packagename.PropertyName
    * @param defaultValue The default value to return if the custom properties map
    *            does not contain they specified propertyName
    * @return The value at the given propertyName in the customProps map.
    */
   public Object getCustomProperty(String propertyName, Object defaultValue) {
       if (propertyName == null) {
           throw new NullPointerException("The propertyName for a custom property " +
                                              "on MetaData cannot be null");
       }
       return customProps.containsKey(propertyName) ?
           customProps.get(propertyName) : defaultValue;
   }

   /**
    * Removes the custom property from the custom properties map.
    * @param propertyName A non-null string of the form com.mydomain.packagename.PropertyName
    */
   public void removeCustomProperty(String propertyName) {
       if (propertyName == null) {
           throw new NullPointerException("The propertyName for a custom property " +
                                              "on MetaData cannot be null");
       }
       Object oldValue = customProps.get(propertyName);
       customProps.remove(propertyName);
       firePropertyChange(propertyName, oldValue, null);
   }

   /**
    *
    * @return array containing the existing propertyNames in the custom properties map.
    */
   public String[] getCustomPropertyKeys() {
       Object keys[] = customProps.keySet().toArray();
       String propertyNames[] = new String[keys.length];
       System.arraycopy(keys, 0, propertyNames, 0, keys.length);
       return propertyNames;
   }

    /**
     * Adds the specified validator for this meta-data.  A validator object is
     * used to determine whether a particular object is a valid value for
     * the associated data field.  A data field may have 0 or more validators.
     * @see #removeValidator
     * @see #getValidators
     * @param validator Validator object which performs validation checks on
     *        values being set on the associated data field
     */
    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);
    }

    /**
     * Removes the specified validator for this meta-data.
     * @see #addValidator
     * @param validator Validator object which performs validation checks on
     *        values being set on the associated data field
     */
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
            if (validators.size() == 0) {
                validators = null;
            }
        }
    }

    /**
     * @see #addValidator
     * @return array containing 0 or more validators set on this meta-data
     */
    public Validator[] getValidators() {
        if (validators != null) {
            return (Validator[])validators.toArray(new Validator[1]);
        }
        return new Validator[0];
    }

    /**
     * Adds the specified property change listener to this meta-data object.
     * @param pcl PropertyChangeListener object to receive events when meta-data
     *        properties change
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes the specified property change listener from this meta-data object.
     * @param pcl PropertyChangeListener object to receive events when meta-data
     *        properties change
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        if (pcs != null) {
            pcs.removePropertyChangeListener(pcl);
        }
    }

    /**
     *
     * @return array containing the PropertyChangeListener objects registered
     *         on this meta-data object
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (pcs != null) {
            return pcs.getPropertyChangeListeners();
        }
        return new PropertyChangeListener[0];
    }

    protected void firePropertyChange(String propertyName,
                                      int oldValue, int newValue) {
        if (newValue != oldValue) {
            firePropertyChange(propertyName,
                               new Integer(oldValue), new Integer(newValue));
        }
    }

    protected void firePropertyChange(String propertyName,
                                      boolean oldValue, boolean newValue) {
        if (newValue != oldValue) {
            firePropertyChange(propertyName,
                               Boolean.valueOf(oldValue),
                               Boolean.valueOf(newValue));
        }
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        if (pcs != null) {
            pcs.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}
