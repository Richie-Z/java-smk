/*
 * $Id: DataColumn.java,v 1.10 2005/10/10 17:00:57 rbair Exp $
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
package org.jdesktop.dataset;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.parser.ParseException;


/**
 * <p>A <code>DataColumn</code> defines information for values in a single column of a {@link DataTable}. 
 * The <code>DataColumn</code> doesn't contain an actual value for a column, but rather gives the data type, 
 * and name, and tells us whether the column is required, whether it is writeable, and 
 * whether it is the primary key column for the table. The data type for a value in a <code>DataColumn</code> 
 * is always a Java Class. 
 *
 * <p>A <code>DataColumn</code> is always associated with a specific <code>DataTable</code>, usually the <code>DataTable</code> that 
 * instantiated it.
 *
 * <p>If a <code>DataColumn</code> is marked as a primary key, this is for tables with a single
 * column primary key; multiple-column keys are not supported.
 *
 * <p>Note as well that a <code>DataColumn</code> is purely passive and doesn't itself 
 * validate actions against the <code>DataTable</code>. If the column is required, or if it
 * is read-only, the <code>DataColumn</code> will not enforce this, nor will it enforce
 * uniqueness on primary key columns.
 *
 * @author rbair
 */
public class DataColumn {
    /**
     * The Logger
     */
    private static final Logger LOG = Logger.getLogger(DataColumn.class.getName());
    
    //protected for testing
    /** Used as a prefix for auto-generated DataColumn names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataColumn";
    
    /**
     * Used to generate a name for the DataColumn, since each DataColumn must
     * have a name.
     */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);
    
    //used for communicating changes to this JavaBean, especially necessary for
    //IDE tools, but also handy for any other component listening to this column
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * The <code>DataTable</code> that created this DataColumn. This is an immutable property
     * that is set in the constructor.
     */
    private DataTable table;
    /**
     * The name of this DataColumn. The name cannot contain any whitespace.
     * In general, it should conform to the same rules as an identifier
     * in Java.
     */
    private String name = NAMEGEN.generateName(this);
    /**
     * The Class of the data values for this DataColumn. This cannot be null. If
     * the type is unknown, then this should be Object.class.
     */
    private Class type = Object.class;
    /**
     * Flag indicating whether the fields within this column are readonly or
     * not.
     */
    private boolean readOnly = false;
    /**
     * Flag indicating whether the fields within this column are required.
     * If a column is required, then the field must be filled in prior to
     * a save, or an exception occurs.<br>
     * TODO constraint logic isn't specified yet. When it is, make sure to
     * include this check.
     */
    private boolean required = false;
    /**
     * The default value for the column. When a new row is added, the various
     * cells set their values to this default.
     */
    private Object defaultValue;
    /**
     * Indicates whether this DataColumn is a key column. Key Columns enforce
     * a unique constraint on the DataColumn (no two values in the column can
     * be the same, as determined by .equals()).
     */
    private boolean keyColumn;
    /**
     * Expression to be evaluated for computed columns
     */
    private String expression;
    private UnaryFunctor<DataRow,?> expImpl;

    /**
     * Create a new DataColumn. To construct a DataColumn, do not call
     * <code>new DataColumn(table)</code> directly. Rather, call
     * <code>table.addColumn()</code>.<br>
     * @param table cannot be null. The <code>DataTable</code> that created this
     *        DataColumn.
     */
    protected DataColumn(DataTable table) {
        assert table != null;
        this.table = table;
    }
    
    /**
     * Returns the <code>DataTable</code> that this column belongs to.
     */
    public DataTable getTable() {
        return table;
    }
    
    /**
     * Returns the name of the DataColumn.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the DataColumn. The name must be valid., or the change
     * will not be made. If the name is invalid, a warning will be posted. Validity
     * for names is defined in {@link DataSetUtils#isValidName(java.lang.String)}.
     */
    public void setName(String name) {
        if (this.name != name) {
            assert DataSetUtils.isValidName(name);
            assert !table.columns.containsKey(name) && !table.selectors.containsKey(name);
            String oldName = this.name;
            this.name = name;
            pcs.firePropertyChange("name", oldName, name);
        }
    }

    /**
     * Returns the type of the values for this DataColumn as a Java Class.
     */
    public Class getType() {
        return type;
    }

    /**
     * Sets the type for the values of this DataColumn as a Java Class.
     * @param type If null, then the type is set to Object.class.
     */
    public void setType(Class type) {
        if (this.type != type) {
            Class oldType = this.type;
            this.type = type == null ? Object.class : type;
            pcs.firePropertyChange("type", oldType, type);
        }
    }

    /**
     * @return true if this DataColumn is read-only.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets whether this column is read-only or not.
     * @param readOnly If true, column is read-only.
     */
    public void setReadOnly(boolean readOnly) {
        if (this.readOnly != readOnly) {
            boolean oldValue = this.readOnly;
            this.readOnly = readOnly;
            pcs.firePropertyChange("readOnly", oldValue, readOnly);
        }
    }

    /**
     * @return true if the fields in this column need to have a value
     * before they can be saved to the data store. The DataColumn is required
     * if the required flag is set by the {@link #setRequired(boolean)} method, 
     * or if the DataColumn is a keyColumn. <br>
     * 
     * TODO need to decide if that is true, or if it is always required!
     */
    public boolean isRequired() {
        return required || keyColumn;
    }

    /**
     * Specifies whether the fields in this column must have a value (cannot
     * be null).
     * @param required
     */
    public void setRequired(boolean required) {
        if (this.required != required) {
            boolean oldValue = this.required;
            this.required = required;
            pcs.firePropertyChange("required", oldValue, required);
        }
    }

    /**
     * @return the value to use as a default value when a new field for
     * this column is created, such as when a new row is created.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * Set the value to use as a default when a new field for
     * this column is created, such as when a new row is created.
     * @param defaultValue
     */
    public void setDefaultValue(Object defaultValue) {
        if (this.defaultValue != defaultValue) {
            Object oldVal = this.defaultValue;
            this.defaultValue = defaultValue;
            pcs.firePropertyChange("defaultValue", oldVal, defaultValue);
        }
    }
    
    /**
     * Returns whether the column is a key column or not
     */
    public boolean isKeyColumn() {
        return keyColumn;
    }
    
    /**
     * Sets this column to be a key column. This implicitly places a unique
     * constraint on the column. When this flag is set, no checks are made to
     * ensure correctness. However, the column will automatically be marked as
     * being required.
     *
     * @param value
     */
    public void setKeyColumn(boolean value) {
        if (value != keyColumn) {
            boolean oldVal = keyColumn;
            keyColumn = value;
            pcs.firePropertyChange("keyColumn", oldVal, value);
            // If the column is a key column, then it is also required
            // by virtue of its key-column-ness. Hence, if the column was NOT
            // required before and keyColumn WAS false but is now true, then
            // everybody needs to be notified that required is now true.
            // Conversly, if the keyColumn WAS true but is now false and
            // required WAS (still is, technically) false, then everybody
            // needs to be notified that required is now false.
            if (!oldVal && value && !required) {
                pcs.firePropertyChange("required", required, true);
            } else if (oldVal && !value && !required) {
                pcs.firePropertyChange("required", true, required);
            }
        }
    }

    /**
     * @return the expression for calculating values in this column
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the expression that this column will use to calculate its values.
     * If the <code>expression</code> property on this column is set, then
     * the values for each row in the column are determined based on the expression.
     *
     * <p>This is currently unimplemented. TODO</p>
     *
     * @param expression the expression for calculating values in this column
     */
    public void setExpression(String expression) /*throws ParseException*/ {
        if (expression == null) {
            expression = "";
        }
            
        try {
            UnaryFunctor<DataRow,?> newExpImpl =
                getParser().parseComputedColumn(getTable(), expression);

            if ( !(expression.equals(this.expression))) {
                UnaryFunctor<DataRow,?> oldExpImpl = expImpl;
                String oldExpression = this.expression;
                this.expression = expression;
                this.expImpl = newExpImpl;
                pcs.firePropertyChange("expression", oldExpression, expression);
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    public Object getValueForRow(DataRow row) {
        return expImpl.fn(row);
    }

    Parser getParser() { return getTable().getDataSet().getParser(); }

    /**
     * Adds a PropertyChangeListener to this class for any changes to bean 
     * properties.
     *
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     */    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
    /**
     * Adds a PropertyChangeListener to this class for specific property changes.
     *
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     * @param property The name of the property to listen to changes for.
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,  listener);
    }
    
    /**
     * Stops notifying a specific listener of any changes to bean properties.
     *
     * @param listener The listener to stop receiving notifications.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    /**
     * Stops notifying a specific listener of changes to a specific property.
     *
     * @param propertyName The name of the property to ignore from now on.
     * @param listener The listener to stop receiving notifications.
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName,  listener);
    }
}
