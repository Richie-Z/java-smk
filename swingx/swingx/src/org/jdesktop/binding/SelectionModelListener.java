/*
 * $Id: SelectionModelListener.java,v 1.2 2005/10/10 17:01:04 rbair Exp $
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

import java.util.EventListener;

/**
 * Listener for tracking changes in what rows are selected in a
 * SelectionModel
 * @author Richard Bair
 */
public interface SelectionModelListener extends EventListener {
	/**
	 * Called whenever the value of the selection changes
	 * @param e an object containing a description of the event
	 */
	public void selectionChanged(SelectionModelEvent e);
}
