/*
 * $Id: TableChangeEvent.java,v 1.4 2005/10/15 11:43:21 pdoubleya Exp $
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
import org.jdesktop.dataset.DataSelector;

import org.jdesktop.dataset.DataTable;


/**
 * <P>A <CODE>TableChangeEvent</CODE> is broadcast when a {@link DataTable} is changed--either structurally, or 
 * has data modified.  Events are identified by an <CODE>EventType</CODE> enumeration in the class. <CODE>LOAD_STARTED</CODE> 
 * and <CODE>LOAD_COMPLETE</CODE> are broadcast at the start and end of a {@link DataTable#load()}, respectively,
 * while <CODE>SAVE_STARTED</CODE> and <CODE>SAVE_COMPLETE</CODE> notify on {@link DataTable#save()}. 
 * <CODE>TABLE_CLEARED</CODE> is broadcast when a table is cleared out completely.
 * <CODE>ROW_ADDED</CODE>, <CODE>ROW_DELETED</CODE> and <CODE>ROW_DISCARDED</CODE> are broadcast per-row
 * for add, delete and discard operations. <CODE>COLUMN_ADDED</CODE> and <CODE>COLUMN_REMOVED</CODE>
 * are broadcast per-column for add or remove operations on columns. To track row status changes, or changes to column values, see
 * {@link RowChangeEvent}.
 *
 * <P>TableChangeEvents instances are re-sent to all listeners on a table, and are unmodifiable.
 *
 * @author Richard Bair
 * @author Patrick Wright
 */
public class TableChangeEvent extends EventObject {
    public enum EventType {
        LOAD_STARTED,
        LOAD_COMPLETE,
        SAVE_STARTED,
        SAVE_COMPLETE,
        TABLE_CLEARED,
        ROW_ADDED,
        ROW_DELETED,
        ROW_DISCARDED,
        COLUMN_ADDED,
        COLUMN_REMOVED
    };
    
    private EventType eventType;
    private DataColumn columnAffected;
    private DataRow rowAffected;
    
    private TableChangeEvent(DataTable source) { super(source); }
    
    /** Creates a new instance of TableChangeEvent; for whole-table events */
    private TableChangeEvent(DataTable source, EventType what ) {
        super(source);
        eventType = what;
    }
    
    /** Creates a new instance of TableChangeEvent; for events involving DataRows */
    private TableChangeEvent(DataTable source, EventType what, DataRow row ) {
        this(source, what);
        rowAffected = row;
    }
    
    /** Creates a new instance of TableChangeEvent; for events involving DataColumns */
    private TableChangeEvent(DataTable source, EventType what, DataColumn column ) {
        this(source, what);
        columnAffected = column;
    }
    
    public static TableChangeEvent newLoadStartEvent(DataTable source) { 
        return new TableChangeEvent(source, EventType.LOAD_STARTED);
    }
    
    public static TableChangeEvent newLoadCompleteEvent(DataTable source) { 
        return new TableChangeEvent(source, EventType.LOAD_COMPLETE);
    }

    public static TableChangeEvent newSaveStartEvent(DataTable source) { 
        return new TableChangeEvent(source, EventType.SAVE_STARTED);
    }
    
    public static TableChangeEvent newSaveCompleteEvent(DataTable source) { 
        return new TableChangeEvent(source, EventType.SAVE_COMPLETE);
    }

    public static TableChangeEvent newTableClearedEvent(DataTable source) { 
        return new TableChangeEvent(source, EventType.TABLE_CLEARED);
    }
    
    public static TableChangeEvent newColumnAddedEvent(DataTable source, DataColumn col) { 
        return new TableChangeEvent(source, EventType.COLUMN_ADDED, col);
    }

    public static TableChangeEvent newColumnRemovedEvent(DataTable source, DataColumn col) { 
        return new TableChangeEvent(source, EventType.COLUMN_REMOVED, col);
    }

    public static TableChangeEvent newRowAddedEvent(DataTable source, DataRow row) { 
        return new TableChangeEvent(source, EventType.ROW_ADDED, row);
    }

    public static TableChangeEvent newRowDeletedEvent(DataTable source, DataRow row) { 
        return new TableChangeEvent(source, EventType.ROW_DELETED, row);
    }

    public static TableChangeEvent newRowDiscardedEvent(DataTable source, DataRow row) { 
        return new TableChangeEvent(source, EventType.ROW_DISCARDED, row);
    }

    /** Returns the EventType enumerated value for this event, never null.  */
    public TableChangeEvent.EventType getEventType() {
        return eventType;
    }
    
    /** Returns the DataRow affected by this event, or null if no row was involved. */
    public DataRow getRowAffected() {
        return rowAffected;
    }
    
    /** Returns the DataColumn affected by this event, or null if no column was involved. */
    public DataColumn getColumnAffected() {
        return columnAffected;
    }
}
