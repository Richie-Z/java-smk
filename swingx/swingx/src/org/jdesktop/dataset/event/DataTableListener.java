/*
 * $Id: DataTableListener.java,v 1.3 2005/10/15 11:43:21 pdoubleya Exp $
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

import java.util.EventListener;

/**
 *
 * @author rbair
 */
public interface DataTableListener extends EventListener {
    /*
     * Called when the values of a single row changes (updates, etc)
     */
    public void rowChanged(RowChangeEvent evt);
    
    /**
     * Called when the table changes. For instance, when a DataColumn is
     * added or removed, or when a DataRow is added.
     */
    public void tableChanged(TableChangeEvent evt);
}
