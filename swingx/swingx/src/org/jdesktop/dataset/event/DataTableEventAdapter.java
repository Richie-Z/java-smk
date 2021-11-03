/*
 * $Id: DataTableEventAdapter.java,v 1.1 2005/10/15 11:43:21 pdoubleya Exp $
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

import java.beans.PropertyChangeListener;
import org.jdesktop.dataset.DataRow;



/**
 * <CODE>DataTableEventAdapter</CODE> is a {@link DataTableListener} that captures {@link TableChangeEvent TableChangeEvents} and 
 * {@link RowChangeEvent RowChangeEvents} and re-directs them to handler methods by event type. For example, you
 * can capture a {@link #rowAdded(TableChangeEvent)} or {@link rowStatusChanged(RowChangeEvent)} event instead of checking for the 
 * event type on capturing an event directly. To use this class, create an instance and assign it to a {@link DataTable} using 
 * {@link DataTable#addDataTableListener(DataTableListener)}.
 *
 * @author Patrick Wright
 */
public abstract class DataTableEventAdapter implements DataTableListener, PropertyChangeListener {
    
    /** Creates a new instance of DataTableEventAdapter */
    public DataTableEventAdapter() {
    }
    
    /** 
     * Fired when a property on the bound object is changed. In this case, we are only interested
     * in status changes on a DataRow; these are forwarded to {@link rowStatusChanged(RowChangeEvent)}. All
     * other property changes are ignored.
     */
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        System.out.println("propertyChange: " + evt);
        if ( evt.getSource().getClass() == DataRow.class ) {
            System.out.println("  is data row");
            DataRow row = (DataRow)evt.getSource();
            if ( evt.getPropertyName().equals("status")) {
                System.out.println("  is status change");
                rowStatusChanged(RowChangeEvent.newRowStatusChangeEvent(row, (DataRow.DataRowStatus)evt.getOldValue()));
            }
        }
    }

    /**
     * Fired when the row is changed, either status change or a cell's value was changed.
     * @param evt The RowChangeEvent capturing the change.
     */
    public void rowChanged(RowChangeEvent evt) {
        switch ( evt.getEventType() ) {
            case ROW_STATUS_CHANGED:
                rowStatusChanged(evt);
                break;
            case CELL_CHANGED:
                cellChanged(evt);
                break;
            default:
                throw new RuntimeException("Unknown event type on row change event " + evt.getEventType());
        }
    }
    
    /**
     * Fired when the row's status changes.
     * @param evt The RowChangeEvent capturing the change.
     */
    public void rowStatusChanged(RowChangeEvent evt) {}
    
    /**
     * Fired when a cell's value changes.
     * @param evt The RowChangeEvent capturing the change.
     */
    public void cellChanged(RowChangeEvent evt) {}
    
    /**
     * Fired when the table is changed, either structurally (columns added or removed) or in data (rows added, removed, etc.)
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableChanged(TableChangeEvent evt) {
        switch ( evt.getEventType()) {
            case LOAD_STARTED:
                tableLoadStarted(evt);
                break;
            case LOAD_COMPLETE:
                tableLoadComplete(evt);
                break;
            case SAVE_STARTED:
                tableSaveStarted(evt);
                break;
            case SAVE_COMPLETE:
                tableSaveComplete(evt);
                break;
            case TABLE_CLEARED:
                tableCleared(evt);
                break;
            case ROW_ADDED:
                rowAdded(evt);
                break;
            case ROW_DELETED:
                rowDeleted(evt);
                break;
            case ROW_DISCARDED:
                rowDiscarded(evt);
                break;
            case COLUMN_ADDED:
                columnAdded(evt);
                break;
            case COLUMN_REMOVED:
                columnRemoved(evt);
                break;
            default:
                throw new RuntimeException("Unknown event type on row change event " + evt.getEventType());
        }
    }
    
    /**
     * Fired right before a DataProvider begins loading the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableLoadStarted(TableChangeEvent evt) {}
    /**
     * Fired after a DataProvider finishes loading the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableLoadComplete(TableChangeEvent evt) {}
    /**
     * Fired right before a DataProvider begins saving the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableSaveStarted(TableChangeEvent evt) {}
    /**
     * Fired after a DataProvider finishes saving the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableSaveComplete(TableChangeEvent evt) {}
    /**
     * Fired when the table is completely cleared.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void tableCleared(TableChangeEvent evt) {}
    /**
     * Fired when a row is added to the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void rowAdded(TableChangeEvent evt) {}
    /**
     * Fired when a row is deleted from the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void rowDeleted(TableChangeEvent evt) {}
    /**
     * Fired when a row is discarded from the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void rowDiscarded(TableChangeEvent evt) {}
    /**
     * Fired when a column is added to the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void columnAdded(TableChangeEvent evt) {}
    /**
     * Fired when a column is removed from the table.
     * @param evt The TableChangeEvent capturing the change.
     */
    public void columnRemoved(TableChangeEvent evt) {}    

}
