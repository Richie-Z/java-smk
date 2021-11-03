/*
 * $Id: SelectionModelAdapter.java,v 1.5 2005/10/10 17:01:13 rbair Exp $
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

package org.jdesktop.dataset.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.SelectionModelEvent;
import org.jdesktop.binding.SelectionModelListener;
import org.jdesktop.dataset.DataSelector;

/**
 * 
 * @author rbair
 * 
 */
public class SelectionModelAdapter implements SelectionModel {
    private DataSelector selector;
    private List<SelectionModelListener> listeners = new ArrayList<SelectionModelListener>();
//    private boolean changing = false;
    
    /** Creates a new instance of SelectionModelAdapter */
    public SelectionModelAdapter(DataSelector ds) {
        assert ds != null;
        this.selector = ds;
        this.selector.addPropertyChangeListener("rowIndices",  new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // JW: the flag prevents firing an SelectionEvent if the 
                // DataSelector's selection is set from here.
                // TODO: formally test
                // TODO: minimize the change interval
//                if (!changing) {
//                    changing = true;
                    SelectionModelEvent e = new SelectionModelEvent(
                            SelectionModelAdapter.this, 0, 
                            selector.getDataTable().getRowCount() - 1);
                    for (SelectionModelListener listener : listeners) {
                        listener.selectionChanged(e);
                    }
//                    changing = false;
//                }
            }
        });
        
    }

    public void addSelectionModelListener(SelectionModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeSelectionModelListener(SelectionModelListener listener) {
        listeners.remove(listener);
    }

    public void setSelectionIndices(int[] indices) {
        // JW: must fire selectionEvent!
//        changing = true;
        selector.setRowIndices(indices);
//       changing = false;
    }

    public int[] getSelectionIndices() {
        List<Integer> indices = selector.getRowIndices();
        int[] results = new int[indices.size()];
        for (int i=0; i<results.length; i++) {
            results[i] = indices.get(i);
        }
        return results;
    }
}