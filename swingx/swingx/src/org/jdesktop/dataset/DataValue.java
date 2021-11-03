/*
 * $Id: DataValue.java,v 1.5 2005/10/10 17:01:00 rbair Exp $
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

import net.sf.jga.fn.EvaluationException;
import net.sf.jga.fn.Generator;
import net.sf.jga.fn.adaptor.Constant;
import net.sf.jga.parser.ParseException;
import net.sf.jga.parser.UncheckedParseException;

/**
 * <p>A DataValue is represents an expression attached to a DataSet, which 
 * can be evaluated in the context of that DataSet to produce an Object end-value.
 * The expression is a String expression intended to be processed by a subclass
 * so that the {@link #getValue()} method returns an Object representing the 
 * expression's result. 
 *
 * <p>A <code>DataValue</code> has a name, which should be unique for a given 
 * <code>DataSet</code>.
 *
 * <p>A <code>DataValue</code> belongs to a single <code>DataSet</code>.
 *
 * @see DataSet
 *
 * @author rbair
 */
 // TODO implement a PropertyChangeListener on the synthetic "value" field.
 // When some value that the DataValue expression depends on changes, it
 // becomes necessary to recompute the "value", and then notify that the
 // "value" has changed.
public class DataValue {
    //protected for testing
    /** Used as a prefix for auto-generated names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataValue";
    
    /** The shared instance of the NameGenerator for DataValues not assigned a name. */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);

    //used for communicating changes to this JavaBean, especially necessary for
    //IDE tools, but also handy for any other component listening to this data value
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** The DataSet instance that owns this DataValue. */
    private DataSet dataSet;
    
    /** The name of this DataValue. */
    private String name;
   
    /** 
     * The expression which, when evaluated, returns the actual value for this
     * DataValue. 
     */
    private String expression;
    private Generator<?> exprImpl = new Constant<Object>(null);
    
    /** 
     * Creates a new instance of DataValue with an auto-generated name, for a 
     * given DataSet.
     *
     * @param ds The DataSet that owns this DataValue
     */
    public DataValue(DataSet ds) {
        assert ds != null;
        this.dataSet = ds;
        name = NAMEGEN.generateName(this);
    }
    
    /** 
     * Creates a new instance of DataValue, 
     * for a given DataSet.
     *
     * @param ds The DataSet that owns this DataValue
     * @param name The DataValue's name.
     */
    public DataValue(DataSet ds, String name) {
        this(ds);
        if (name != null) {
            setName(name);
        }
    }
    
	/**
     * Changes the name for this DataValue.
	 * @param name The new name.
	 */
	public void setName(String name) {
        if (this.name != name) {
            assert DataSetUtils.isValidName(name);
            String oldName = this.name;
            this.name = name;
            pcs.firePropertyChange("name", oldName, name);
        }
	}

    /** 
     * Returns the current name for this DataValue.
     *
     * @return the DataValue's name.
     */
	public String getName() {
		return name;
	}

    /** 
     * Returns the DataSet this DataValue belongs to.
     *
     * @return the DataSet this DataValue belongs to.
     */
    public DataSet getDataSet() {
        return dataSet;
    }
    
    /**
     * Returns the expression which will be evaluated to result in an actual
     * value for this DataValue.
     *
     * @return the expression that underlies this DataValue
     */
    public String getExpression() {
        return expression;
    }
    
    /**
     * Sets the string expression which, when evaluated, returns a DataValue.
     * 
     * @param expression The new expression for this DataValue.
     */
    public void setExpression(String expression) {
        if (expression ==  null || expression.equals(""))
            exprImpl = new Constant<Object>(null);
        else {
            try {
                exprImpl = getParser().parseDataValue(expression);
            }
            catch (ParseException x) { throw new UncheckedParseException(x); }
        }
        this.expression = expression;
    }
    
    /**
     * Returns the actual value resulting from the DataValue's expression being
     * evaluated in the context of a DataSet. This value may be constant or may
     * change on each invocation--that's up to the expression.
     *
     * @return the actual value of this DataValue, once evaluated.
     */
    // TODO: this should be abstract (PWW 04/28/04)
    public Object getValue() {
        try {
            return exprImpl.gen();
        } catch (EvaluationException e) {
            e.printStackTrace();
            return null;
        }
    }

    Parser getParser() { 
        return dataSet.getParser();
    }

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
     * @param property The name of the property to listen to changes for.
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
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