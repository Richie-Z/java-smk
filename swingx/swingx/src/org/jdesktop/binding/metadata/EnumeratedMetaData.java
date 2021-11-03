/*
 * $Id: EnumeratedMetaData.java,v 1.3 2005/10/10 17:01:07 rbair Exp $
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

import java.util.List;

/**
 * <p>
 * Class for representing meta-data for data fields which have a finite
 * set of possible values.  The type of each value in the enumeration must
 * match the type (&quot;elementClass&quot; property) of the meta-data object.
 * Example usage:<br>
 * <pre><code>
 *     String weekdays[] = {&quot;Sunday&quot;,&quot;Monday&quot;,&quot;Tuesday&quot;,&quot;Wednesday&quot;,
 *                          &quot;Thursday&quot;,&quot;Friday&quot;,&quot;Saturday&quot;};
 *     EnumeratedMetaData metaData = new EnumeratedMetaData(&quot;weekday&quot;, String.class,
 *                                                          &quot;Day of Week&quot;);
 *     metaData.setEnumeration(weekdays);
 * </code></pre>
 * </p>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class EnumeratedMetaData extends MetaData {

    protected Object[] enumeration;

    /**
      * Instantiates a meta-data object with a default name &quot;enumvalue&quot; and
      * a default field class equal to <code>java.lang.String</code>.
      * This provides the no-argument constructor required for JavaBeans.
      * It is recommended that the program explicitly set a meaningful
      * &quot;name&quot; property.
      */
     public EnumeratedMetaData() {
         this("enumvalue");
     }

    /**
     * Instantiates a meta-data object with the specified name and
     * a default field class equal to <code>java.lang.String</code>.
     * @param name String containing the name of the data field
     */
    public EnumeratedMetaData(String name) {
        super(name);
    }

    /**
     * Instantiates a meta-data object with the specified name and
     * field class.
     * @param name String containing the name of the data field
     * @param klass Class indicating type of data field
     */
    public EnumeratedMetaData(String name, Class klass) {
        super(name, klass);
    }

    /**
     * Instantiates a meta-data object with the specified name,
     * field class, and label.
     * @param name String containing the name of the data field
     * @param klass Class indicating type of data field
     * @param label String containing the user-displayable label for the
     *        data field
     */
    public EnumeratedMetaData(String name, Class klass, String label) {
        super(name, klass, label);
    }

    /**
     * Gets the meta-data &quot;enumeration&quot; property which
     * contains the set of possible values for the associated data field.
     * The returned array is a copy, therefore modifications to that array
     * will have no affect on the property.
     * @see #setEnumeration
     * @return array containing 0 or more enumerated values for the data field
     */
    public Object[] getEnumeration() {
        Object[] evalues;
        if (enumeration != null) {
            evalues = new Object[enumeration.length];
            System.arraycopy(enumeration, 0, evalues, 0,
                             enumeration.length);
        }
        else {
            evalues = new Object[0];
        }
        return evalues;
    }

    /**
     * Sets the meta-data &quot;enumeration&quot; property by copying
     * the values contained in the specified array to an internal representation.
     * @see #getEnumeration
     * @param enumeration array containing 0 or more enumerated values for the data field
     */
    public void setEnumeration(Object[] enumeration) {
        Object oldEnumeration[] = this.enumeration;
        this.enumeration = new Object[enumeration.length];
        System.arraycopy(enumeration, 0, this.enumeration, 0,
                         enumeration.length);
        firePropertyChange("enumeration", oldEnumeration, enumeration);
    }

    /**
     * Sets the meta-data &quot;enumeration&quot; property by copying
     * the values contained in the specified list to an internal representation.
     * @see #getEnumeration
     * @param enumeration list containing 0 or more enumerated values for the data field
     */
    public void setEnumeration(List enumeration) {
        setEnumeration(enumeration.toArray());
    }


}
