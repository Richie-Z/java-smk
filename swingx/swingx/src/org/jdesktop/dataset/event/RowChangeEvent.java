/*
 * $Id: RowChangeEvent.java,v 1.4 2005/10/15 11:43:20 pdoubleya Exp $
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
package org.jdesktop.dataset.event;

import java.util.EventObject;
import org.jdesktop.dataset.DataColumn;

import org.jdesktop.dataset.DataRow;

/**
 * A <CODE>RowChangeEvent</CODE> is broadcast by a data table to {@linkplain DataTableListener listeners} as {@linkplain DataRow rows} are changed, 
 * on a per-row basis. An <CODE>EventType</CODE> enumeration identifies the type of change. A change to a column's value which affects row
 * status results in two events being broadcast, one for the column change and one for the row status change.
 * @author Richard Bair
 * @author Patrick Wright
 */
public class RowChangeEvent extends EventObject {
    public enum EventType {
        ROW_STATUS_CHANGED,
        CELL_CHANGED
    };
    
    private EventType eventType;
    private DataColumn columnAffected;
    private Object priorColumnValue;
    private DataRow.DataRowStatus priorRowStatus;
    
    private RowChangeEvent(DataRow source) {
        super(source);
    }

    
    public static RowChangeEvent newRowStatusChangeEvent(DataRow source, DataRow.DataRowStatus priorStatus) {
        RowChangeEvent rce = new RowChangeEvent(source);
        rce.eventType = EventType.ROW_STATUS_CHANGED;
        rce.priorRowStatus = priorStatus;
        return rce;
    }

    public static RowChangeEvent newCellChangedEvent(DataRow source, DataColumn col, Object priorValue) {
        RowChangeEvent rce = new RowChangeEvent(source);
        rce.eventType = EventType.CELL_CHANGED;
        rce.columnAffected = col;
        rce.priorColumnValue = priorValue;
        return rce;
    }
    
    /**
     * The event classification.
     **/
    public EventType getEventType() {
        return eventType;
    }
    
    /** 
     * The column whose value was updated or set to null; will be null if this event is a row
     * status change. 
     **/
    public DataColumn getColumnAffected() {
        return columnAffected;
    }
    
    /** On a change to a column's value, the column's value before the change. */
    public Object getPriorColumnValue() {
        return priorColumnValue;
    }
    
    /** On a row status change, the row status before the change. */
    public DataRow.DataRowStatus getPriorRowStatus() {
        return priorRowStatus;
    }
}
