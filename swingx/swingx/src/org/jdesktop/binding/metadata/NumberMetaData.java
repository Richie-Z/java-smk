/*
 * $Id: NumberMetaData.java,v 1.3 2005/10/10 17:01:09 rbair Exp $
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

import java.util.Locale;

/**
 * <p>
 * Class for representing meta-data for a numerical data field which is
 * one of the following types:
 * <ul>
 * <li>java.lang.Integer</li>
 * <li>java.lang.Long</li>
 * <li>java.lang.Short</li>
 * <li>java.lang.Float</li>
 * <li>java.lang.Double</li>
 * </ul>
 * This meta-data class defines additional properties and edit constraints
 * which are applicable to numerical values, such as minimum, maximum,
 * whether or not the value is a currency, etc.  Example usage:
 * <pre><code>
 *     NumberMetaData metaData = new NumberMetaData(&quot;interestrate&quot;,
 *                                                  Float.class, &quot;Interest Rate&quot;);
 *     metaData.setMinimum(new Float(4.5));
 *     metaData.setMaximum(new Float(6.8));
 * </code></pre>
 * Setting a minimum and/or maximum constraint will implicitly cause a range
 * validator to be added to the meta-data object.
 * </p>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class NumberMetaData extends MetaData {

    protected Number minimum = null;
    protected Number maximum = null;
    protected boolean currency = false;

    private Validator rangeValidator = null;

    /**
      * Instantiates a meta-data object with a default name &quot;numbervalue&quot; and
      * a default field class equal to <code>java.lang.Integer</code>.
      * This provides the no-argument constructor required for JavaBeans.
      * It is recommended that the program explicitly set a meaningful
      * &quot;name&quot; property.
      */
     public NumberMetaData() {
         this("numbervalue");
     }

    /**
     * Instantiates a meta-data object with the specified name and
     * a default field class equal to <code>java.lang.Integer</code>.
     * @param name String containing the name of the data field
     */
    public NumberMetaData(String name) {
        super(name);
        this.klass = Integer.class;
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * field class.
     * @param name String containing the name of the data field
     * @param klass Class indicating type of data field
     */
    public NumberMetaData(String name, Class klass) {
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
    public NumberMetaData(String name, Class klass, String label) {
        this(name, klass);
        this.label = label;
    }

    /**
     * Gets the meta-data &quot;minimum&quot; property which indicates
     * the minimum value of the associated data field.
     * The default is <code>null</code>, which indicates no minimum.
     * @see #setMinimum
     * @return Number containing the minimum value of the data field.
     */
    public Number getMinimum() {
        return minimum;
    }

    /**
     * Sets the meta-data &quot;minimum&quot; property.
     * Setting a minimum and/or maximum will cause an appropriate
     * range validator object to be added to this meta-data object.
     * @see #getMinimum
     * @param minimum Number containing the minimum value of the data field.
     * @throws IllegalArgumentException if the minimum object's class does
     *         not equal the meta-data's field class
     */
    public void setMinimum(Number minimum) {
        if (klass != minimum.getClass()) {
            throw new IllegalArgumentException(getName() + ": minimum value is class "+
                                               minimum.getClass().getName() +
                                               " but should be "+ getClass().getName());
        }
        Number oldMinimum = this.minimum;
        this.minimum = minimum;
        setupRangeValidator();
        firePropertyChange("minimum", oldMinimum, minimum);
    }

    /**
     * Gets the meta-data &quot;maximum&quot; property which indicates
     * the maximum value of the associated data field.
     * The default is <code>null</code>, which indicates no maximum.
     * @see #setMaximum
     * @return Number containing the maximum value of the data field.
     */
    public Number getMaximum() {
        return maximum;
    }

    /**
     * Sets the meta-data &quot;maximum&quot; property.
     * Setting a minimum and/or maximum will cause an appropriate
     * range validator object to be added to this meta-data object.
     * @see #getMaximum
     * @param maximum Number containing the maximum value of the data field.
     * @throws IllegalArgumentException if the maximum object's class does
     *         not equal the meta-data's field class
     */
    public void setMaximum(Number maximum) {
        if (getElementClass() != maximum.getClass()) {
            throw new IllegalArgumentException(getName() +
                                               ": maximum value is class " +
                                               maximum.getClass().getName() +
                                               " but should be " +
                                               getClass().getName());
        }
        Number oldMaximum = this.maximum;
        this.maximum = maximum;
        setupRangeValidator();
        firePropertyChange("maximum", oldMaximum, maximum);
    }

    /**
     * Gets the meta-data &quot;currency&quot; property which indicates
     * whether this data field represents a currency value.
     * The default is <code>false</code>.
     * @see #setCurrency
     * @return boolean indicating whether the data field represents a currency
     */
    public boolean isCurrency() {
        return currency;
    }

    /**
     * Sets the meta-data &quot;currency&quot; property.
     * @see #isCurrency
     * @param currency boolean indicating whether the data field represents a currency
     */
    public void setCurrency(boolean currency) {
        boolean oldCurrency = this.currency;
        this.currency = currency;
        firePropertyChange("currency", oldCurrency, currency);
    }

    private void setupRangeValidator() {
        if (maximum != null || minimum != null) {
            if (rangeValidator == null) {
                rangeValidator = getRangeValidator();
                addValidator(rangeValidator);
            }
        } else if (rangeValidator != null) {
            removeValidator(rangeValidator);
            rangeValidator = null;
        }
    }

    private Validator getRangeValidator() {
        if (klass.equals(Integer.class)) {
            return new IntegerRangeValidator();
        } else if (klass.equals(Long.class)) {
            return new LongRangeValidator();
        } else if (klass.equals(Float.class)) {
            return new FloatRangeValidator();
        } else if (klass.equals(Short.class)) {
            return new ShortRangeValidator();
        } else if (klass.equals(Double.class)) {
            return new DoubleRangeValidator();
        }
        return null;
    }

    /**@todo aim: these could be exposed as public static Validator classes,
     *  but they would then have to become stateful.
     */
    private class IntegerRangeValidator implements Validator {

        public boolean validate(Object value, Locale locale, String[] error) {

            int intValue = ((Integer)value).intValue();
            if (maximum != null &&
                intValue > ((Integer)maximum).intValue()) {
                error[0] = getName()+ ": value " + intValue + " exceeds maximum "+
                        ((Integer)maximum).intValue();
                return false;
            }
            if (minimum != null &&
                intValue < ((Integer)minimum).intValue()) {
                error[0] = getName() + ": value " + intValue + " is less than the minimum" +
                        ((Integer)minimum).intValue();
                return false;
            }
            return true;
        }
    }

    private class LongRangeValidator implements Validator {

        public boolean validate(Object value, Locale locale, String[] error) {

            long longValue = ((Long)value).longValue();
            if (maximum != null &&
                longValue > ((Long)maximum).longValue()) {
                error[0] = getName()+ ": value " + longValue + " exceeds maximum "+
                        ((Long)maximum).longValue();
                return false;
            }
            if (minimum != null &&
                longValue < ((Long)minimum).longValue()) {
                error[0] = getName() + ": value " + longValue + " is less than the minimum" +
                        ((Long)minimum).longValue();
                return false;
            }
            return true;
        }
    }

    private class ShortRangeValidator implements Validator {

        public boolean validate(Object value, Locale locale, String[] error) {

            short shortValue = ((Short)value).shortValue();
            if (maximum != null &&
                shortValue > ((Short)maximum).shortValue()) {
                error[0] = getName()+ ": value " + shortValue + " exceeds maximum "+
                        ((Short)maximum).shortValue();
                return false;
            }
            if (minimum != null &&
                shortValue < ((Short)minimum).shortValue()) {
                error[0] = getName() + ": value " + shortValue + " is less than the minimum" +
                        ((Short)minimum).shortValue();
                return false;
            }
            return true;
        }
    }

    private class FloatRangeValidator implements Validator {

        public boolean validate(Object value, Locale locale, String[] error) {

            float floatValue = ((Float)value).floatValue();
            if (maximum != null &&
                floatValue > ((Float)maximum).floatValue()) {
                error[0] = getName()+ ": value " + floatValue + " exceeds maximum "+
                        ((Float)maximum).floatValue();
                return false;
            }
            if (minimum != null &&
                floatValue < ((Float)minimum).floatValue()) {
                error[0] = getName() + ": value " + floatValue + " is less than the minimum" +
                        ((Float)minimum).floatValue();
                return false;
            }
            return true;
        }
    }

    private class DoubleRangeValidator implements Validator {

        public boolean validate(Object value, Locale locale, String[] error) {

            double doubleValue = ((Double)value).doubleValue();
            if (maximum != null &&
                doubleValue > ((Double)maximum).doubleValue()) {
                error[0] = getName()+ ": value " + doubleValue + " exceeds maximum "+
                        ((Double)maximum).doubleValue();
                return false;
            }
            if (minimum != null &&
                doubleValue < ((Double)minimum).doubleValue()) {
                error[0] = getName() + ": value " + doubleValue + " is less than the minimum" +
                        ((Double)minimum).doubleValue();
                return false;
            }
            return true;
        }
    }
}
