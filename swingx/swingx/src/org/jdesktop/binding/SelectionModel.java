/*
 * $Id: SelectionModel.java,v 1.2 2005/10/10 17:01:02 rbair Exp $
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

/**
 * A basic selection model interface that can be used in any situation
 * where one or more contiguous or noncontiguous "rows" may be
 * selected.
 * <p/>
 * The SelectionModel is used to define which rows are selected in a
 * collection of rows. From the perspective of the SelectionModel, index
 * 0 is the first index. Any index value &lt; 0 is invalid, as is any index
 * greater than the size of the collection. It is up to the implementation
 * of SelectionModel to ensure that the upper index is valid.
 * <p/>
 * @author Richard Bair
 */
public interface SelectionModel {
	/**
	 * 
	 * @param indices
	 */
    public void setSelectionIndices(int[] indices);
    
    /**
     * 
     * @return
     */
    public int[] getSelectionIndices();
    
	/**
	 * Add a listener to the list that's notified each time a change to the selection
	 * occurs.
	 * @param listener The listener to be notified
	 */
	public void addSelectionModelListener(SelectionModelListener listener);
    
	/**
	 * Remove the given listener from the list that's notified each time a change
	 * to the selection occurs
	 * @param listener the listener to be removed
	 */
	public void removeSelectionModelListener(SelectionModelListener listener);
//	/**
//	 * Change the selection to be between index0 and index1 inclusive. If this represents
//	 * a change to the current selection, then notify each SelectionModelListener. Note
//	 * that index0 doesn't have to be less than or equal to index1.
//	 * @param index0 one end of the interval
//	 * @param index1 other end of the interval.
//	 */
//	public void setSelectionInterval(int index0, int index1);
//	/**
//	 * Change the selection to be the set union of the current selection and the indices
//	 * between index0 and index1 inclusive. If this represents a change to the current
//	 * selection, then notify each SelectionModelListener. Note that index0 doesn't have
//	 * to be less than or equal to index1.
//	 * @param index0 one end of the interval
//	 * @param index1 other end of the interval
//	 */
//	public void addSelectionInterval(int index0, int index1);
//	/**
//	 * Change the selection to be the set difference of the current selection and the
//	 * indices between index0 and index1 inclusive. If this represents a change to the
//	 * current selection, then notify each SelectionModelListener. Note that index0
//	 * doesn't have to be less than or equal to index1.
//	 * @param index0 one end of the interval
//	 * @param index1 other end of the interval
//	 */
//	public void removeSelectionInterval(int index0, int index1);
//	/**
//	 * @return the first selected index or -1 if the selection is empty
//	 */
//	public int getMinSelectionIndex();
//	/**
//	 * @return the last selected index or -1 if the selection is empty
//	 */
//	public int getMaxSelectionIndex();
//	/**
//	 * @param index a valid index
//	 * @return true if the specified index is selected
//	 */
//	public boolean isSelectedIndex(int index);
//	/**
//	 * Change the selection to the empty set. If this represents a change to the current
//	 * selection, then notify each listener.
//	 */
//	public void clearSelection();
//	/**
//	 * @return true if no indices are selected
//	 */
//	public boolean isSelectionEmpty();
}
