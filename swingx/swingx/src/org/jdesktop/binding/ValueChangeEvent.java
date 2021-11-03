/*
 * $Id: ValueChangeEvent.java,v 1.2 2005/10/10 17:01:05 rbair Exp $
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

import java.util.EventObject;

/**
 * Event indicating the value of a named data field within a
 * DataModel has changed.
 *
 * @see ValueChangeListener
 * @see DataModel
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class ValueChangeEvent extends EventObject {

    private String fieldName = null;

    /**
     * Instantiates a new value change event for the specified named
     * field in the data model.
     * @param source DataModel containing the changed data field
     * @param fieldName String containing the name of the field that has changed
     */
    public ValueChangeEvent(DataModel source, String fieldName) {
        super(source);
        this.fieldName = fieldName;
    }

    /**
     *
     * @return String containing the name of the field that has changed
     */
    public String getFieldName() {
        return fieldName;
    }

}
