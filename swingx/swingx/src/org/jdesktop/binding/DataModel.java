/*
 * $Id: DataModel.java,v 1.3 2005/10/10 17:01:03 rbair Exp $
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

import org.jdesktop.binding.metadata.MetaDataProvider;
import org.jdesktop.binding.metadata.Validator;

/**
 * <p>
 * Abstract model interface for representing a record of named data fields.
 * The map provides a uniform API for accessing data which may be contained
 * in a variety of data model constructs underneath, such as RowSet,
 * DefaultTableModelExt, or arbitrary JavaBean classes.  The user-interface
 * Binding classes use this interface to &quot;bind&quot; user-interface
 * components to field elements in a data model without having to understand
 * the specific flavor of data model being used by the application.
 * For example, a field element may map to a named column on a RowSet
 * or a property on a JavaBean, but the binding classes don't need to
 * understand those underlying data structures in order to read and write
 * values.</p>
 * <p>
 * For each named field, the data model provides access to:
 * <ul>
 * <li>meta-data: information describing the data field, such as type
 *                and edit constraints</li>
 * <li>value: the current value of the field</li>
 * </ul>
 * </p>
 * <p>
 * Often data models are collections of like-objects, such as the rows in a
 * RowSet, or a list of JavaBeans.  This interface provides a mechanism
 * to index into such a collection such that at any given time, the data model
 * contains the element values associated with the &quot;current&quot; record index
 * into that collection (the current row, or the current bean, etc).
 * </p>
 *
 *
 * @author Amy Fowler
 * @version 1.0
 */
public interface DataModel extends MetaDataProvider {

//---------------------- moved to MetaDataProvider
    
//    /**
//     * @return array containing the names of all data fields in this map
//     */
//    String[] getFieldNames();
//
//    MetaData[] getMetaData();
//
//    /**
//     *
//     * @param fieldName String containing the name of the field
//     * @return MetaData object which describes the named field
//     */
//    MetaData getMetaData(String fieldName);
//
//    /**
//    *
//    * @return integer containing the number of fields in this data model
//    */
//   int getFieldCount();
//
    
    /**
     *
     * @param fieldName String containing the name of the field
     * @return Object containing the current value of the named field
     */
    Object getValue(String fieldName);

    /**
     *
     * @param fieldName String containing the name of the field
     * @param value Object containing the current value of the named field
     */
    void setValue(String fieldName, Object value);

    /**
     * Adds the specified validator for the fields represented by this
     * data model.
     * A validator object may be used to perform validation checks which
     * require analyzing more than one field value in a single check.
     * This DataModel instance will be passed in as the <code>value</code>
     * parameter to the validator's <code>validate</code> method.
     *
     * @see #removeValidator
     * @see #getValidators
     * @param validator Validator object which performs validation checks on
     *        this set of data field values
     */
    public void addValidator(Validator validator);

    /**
     * Removes the specified validator from this data model.
     * @see #addValidator
     * @param validator Validator object which performs validation checks on
     *        this set of data field values
     */
    public void removeValidator(Validator validator);

    /**
     *
     * @return array containing the validators registered for data model
     */
    Validator[] getValidators();

    /**
     * Adds the specified value change listener to be notified when
     * the value is changed outside of calling <code>setValue</code> directly.
     * @param valueChangeListener ValueChangeListener object to receive events
     *        when the field value changes
     */
    void addValueChangeListener(ValueChangeListener valueChangeListener);

    /**
     * Removes the specified value change listener from this value adapter.
     * @param valueChangeListener ValueChangeListener object to receive events
     *        when the field value changes
     */
    void removeValueChangeListener(ValueChangeListener valueChangeListener);

    /**
     *
     * @return array containing the ValueChangeListener objects registered
     *         on this data model
     */
    ValueChangeListener[] getValueChangeListeners();


}
