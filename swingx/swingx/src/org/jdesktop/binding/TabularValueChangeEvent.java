/*
 * $Id: TabularValueChangeEvent.java,v 1.2 2005/10/10 17:01:04 rbair Exp $
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
 * Event fired by TabularDataModel on cell updates.
 * 
 * PENDING: how to handle adding/removing rows?
 * 
 * @author Jeanette Winzenburg
 * 
 */
public class TabularValueChangeEvent extends EventObject {
    

    private int rowIndex;
    private String fieldName;

    /**
     * Instantiates a change event for the cell given in 
     * rowIndex/fieldName coordinates.
     * 
     * @param source
     * @param rowIndex the row which is updated. -1 indicates all rows.
     * @param fieldName the field which is updated. null indicates all fields.
     */
    public TabularValueChangeEvent(TabularDataModel source, int rowIndex, String fieldName) {
        super(source);
        this.rowIndex = rowIndex;
        this.fieldName = fieldName;
    }

    /** the column coordinate.
     * 
     * @return the column which is updated. null indicates all columns.
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * 
     * @return the rowIndex which is updated. -1 indicates all columns.
     */
    public int getRowIndex() {
        return rowIndex;
    }
}
