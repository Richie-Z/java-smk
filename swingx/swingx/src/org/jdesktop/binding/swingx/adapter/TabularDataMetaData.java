/*
 * $Id: TabularDataMetaData.java,v 1.2 2005/10/10 17:01:12 rbair Exp $
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

package org.jdesktop.binding.swingx.adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import org.jdesktop.binding.metadata.Converter;


/**
 *
 * This class will be going away once the DOMAdapter converts its API
 * to use org.jdesktop.swing.data.MetaData.
 *
 * @version 1.0
 */
public class TabularDataMetaData {
    private Column columns[];

    private PropertyChangeSupport pcs;

    /**
     * Creates a new meta data object with 0 columns.
     */
    public TabularDataMetaData() {
        this(0);
    }

    /**
     * Creates a new meta data object with the specified number of columns
     * @param columnCount integer containing the number of columns
     */
    public TabularDataMetaData(int columnCount) {
        pcs = new PropertyChangeSupport(this);
        setColumnCount(columnCount);
    }

    /**
     * Adds the specified property change listener to this meta data.
     * This listener will be notified when either the number of columns
     * change or properties on the columns are modified.
     * @param listener PropertyChangeListener to be notified when meta data changes
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes the specified property change listener from this meta data.
     * @param listener PropertyChangeListener to be notified when meta data changes
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Initializes the number of columns in this meta data object.  If columns
     * already exist when this method is called, they are discarded.
     * @param columnCount integer containing the number of columns
     */
    public void setColumnCount(int columnCount) {
        int oldColumnCount = columns != null? columns.length : 0;
        columns = new Column[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columns[i] = new Column("column"+i, java.lang.String.class, true);
        }
        pcs.firePropertyChange("columnCount", oldColumnCount, columnCount);
    }

    /**
     * @return the number of columns
     */
    public int getColumnCount() {
        return columns.length;
    }

    public int getColumnIndex(String name) {
        int index = 0;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].name.equals(name)) {
                return i+1;
            }
        }
        return index;
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return String containing the column name at the specified index
     */
    public String getColumnName(int columnIndex) {
        return columns[columnIndex-1].name;
    }

    /**
     * Sets the column name at the specified index
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param columnName String containing the column name at the specified inde
     */
    public void setColumnName(int columnIndex, String columnName) {
        String oldColumnName = columns[columnIndex-1].name;
        columns[columnIndex-1].name = columnName;
        pcs.firePropertyChange("columnName"+columnIndex, oldColumnName, columnName);
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return String containing the column label at the specified index
     */
    public String getColumnLabel(int columnIndex) {
        return columns[columnIndex-1].label;
    }

    /**
     * Sets the column label at the specified index.  The label is used
     * to display this column to the end-user and should be localized.
     *
     * @param columnIndex index of the column (first is 1, second is 2...)
     * @param columnLabel the column text to set
     */
    public void setColumnLabel(int columnIndex, String columnLabel) {
        String oldColumnLabel = columns[columnIndex-1].label;
        columns[columnIndex-1].label = columnLabel;
        pcs.firePropertyChange("columnLabel"+columnIndex, oldColumnLabel, columnLabel);
    }


    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return Class representing the column's type
     */
    public Class getColumnClass(int columnIndex) {
        return columns[columnIndex-1].klass;
    }

    /**
     * Sets the column class at the specified index
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param columnClass Class representing the column's type
     */
    public void setColumnClass(int columnIndex, Class columnClass) {
        Class oldColumnClass = columns[columnIndex-1].klass;
        columns[columnIndex-1].klass = columnClass;
        pcs.firePropertyChange("columnClass"+columnIndex, oldColumnClass, columnClass);
    }

    public void setColumnDisplaySize(int columnIndex, int numChars) {
        columns[columnIndex-1].displayLength = numChars;
    }

    public int getColumnDisplaySize(int columnIndex) {
        return columns[columnIndex-1].displayLength;
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return boolean indicating whether or not values in this column may be modified
     */
    public boolean isColumnWritable(int columnIndex) {
        return columns[columnIndex-1].writable;
    }

    /**
     * Sets whether or not values in the column at the specified index may be modified.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param writable boolean indicating whether or not values in this column
     *        may be modified
     */
    public void setColumnWritable(int columnIndex, boolean writable) {
        columns[columnIndex-1].writable = writable;
    }

    /**
      * @param columnIndex integer index of the column (first is 1, second is 2...)
      * @return boolean indicating whether or not values in this column may be null
      */
    public boolean isColumnNullable(int columnIndex) {
        return columns[columnIndex-1].nullable;
    }

    /**
     * Sets whether or not values in the column may have a null value.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param nullable boolean indicating whether or not values in the column
     *        may be null
     */
    public void setColumnNullable(int columnIndex, boolean nullable) {
        columns[columnIndex-1].nullable = nullable;
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return Object representing minimum value for values in the column, or null
     *         if no minimum value constraint exists
    */
    public Object getColumnMinimum(int columnIndex) {
        return columns[columnIndex-1].minimum;
    }

    /**
     * Sets the minimum value for values in the column.  This may be used
     * to provide optimal UI controls for editing to minimize erroneous input.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param minimum Object representing minimum value for values in the column, or null
     *         if no minimum value constraint exists
     */
    public void setColumnMinimum(int columnIndex, Object minimum) {
        columns[columnIndex-1].minimum = minimum;
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return Object representing maximum value for values in the column, or null
     *         if no maximum value constraint exists
    */
    public Object getColumnMaximum(int columnIndex) {
        return columns[columnIndex-1].maximum;
    }

    /**
     * Sets the maximum value for values in the column.  This may be used
     * to provide optimal UI controls for editing to minimize erroneous input.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param maximum Object representing maximum value for values in the column,
     *        or null if no maximum value constraint exists
     */
    public void setColumnMaximum(int columnIndex, Object maximum) {
        columns[columnIndex-1].maximum = maximum;
    }

    /**
      * @param columnIndex integer index of the column (first is 1, second is 2...)
      * @return Iterator containing the set of valid values for this column, or
      *         null if the value is not constrained by a set
     */
    public Iterator getColumnValues(int columnIndex) {
        final Object values[] = new Object[(columns[columnIndex-1].values.length)];

        System.arraycopy(columns[columnIndex-1].values, 0,
                         values, 0,
                         columns[columnIndex-1].values.length);

        return new Iterator() {
            int current = 0;
            public boolean hasNext() {
                return current < values.length;
            }
            public Object next() {
                return values[current++];
            }
            public void remove() {
                // not supported
            }
        };
    }

    /**
     * Sets the set of valid values for this column.  This may be used
     * to provide optimal UI controls (picklist) for editing to minimize erroneous input.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param values array containing set of valid values, or null if no value
     *        set constraint exists
     */
    public void setColumnValues(int columnIndex, Object values[]) {
        Object newValues[] = new Object[values.length];
        System.arraycopy(values, 0, newValues, 0, values.length);
        columns[columnIndex-1].values = newValues;
    }

    /**
     * Sets the converter object to be used when converting values in the
     * column to and from <code>String</code>.
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @param converter DataConverter object used to convert values in column to
     *        and from String
     */
    public void setColumnConverter(int columnIndex, Converter converter) {
        columns[columnIndex-1].converter = converter;
    }

    /**
     * @param columnIndex integer index of the column (first is 1, second is 2...)
     * @return Converter object used to convert values in column to
     *         and from String, or null if no converter was specified
     */
    public Converter getColumnConverter(int columnIndex) {
        return columns[columnIndex-1].converter;
    }

    // Data structure for holding column-specific meta data
    private class Column {
        public String name;
        public Class klass;
        public String label;
        public Converter converter;
        public boolean writable = false;
        public boolean nullable = false;
        public int displayLength = -1;
        public Object minimum;
        public Object maximum;
        public Object[] values;

        public Column(String name, Class klass, boolean writable) {
            this.name = name;
            this.klass = klass;
            this.writable = writable;
        }
    }
}
