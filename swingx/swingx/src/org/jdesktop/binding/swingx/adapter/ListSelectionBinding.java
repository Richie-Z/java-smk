/*
 * $Id $Exp
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

package org.jdesktop.binding.swingx.adapter;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.binding.IndexMapper;
import org.jdesktop.binding.SelectionBinding;
import org.jdesktop.binding.SelectionModel;
import org.jdesktop.binding.SelectionModelEvent;
import org.jdesktop.binding.SelectionModelListener;

/**
 * Note (JW): ListSelectionModel indices are in view coordinates,
 * SelectionModel indices are in model coordinates. Need to convert.
 * 
 * PENDING: Done here via IndexMapper - but need to think if it's 
 * the correct place.
 * 
 *
 * @author Richard Bair
 * @author Jeanette Winzenburg
 */
public class ListSelectionBinding extends SelectionBinding {
    private boolean indexIsChanging = false;
    private IndexMapper indexMapper;
    private ListSelectionModel listSelectionModel;
    
    /** Creates a new instance of ListSelectionBinding */
    public ListSelectionBinding(final SelectionModel dataSelectionModel, final ListSelectionModel viewSelectionModel) {
        this(dataSelectionModel, viewSelectionModel, null);
    }
    
    public ListSelectionBinding(final SelectionModel dataSelectionModel, 
            final ListSelectionModel listSelectionModel, IndexMapper mapper) { 
        super(dataSelectionModel);
        this.indexMapper = mapper;
        this.listSelectionModel = listSelectionModel;
		installViewSelectionListener();
		installDataSelectionListener();
        updateViewSelectionModelFromData();
    }

    private void installDataSelectionListener() {
        //add a property change listener to listen for selection change events
		//in the selection model. Set the currently selected row(s) to be the
		//same as the current row(s) in the selection model
		selectionModel.addSelectionModelListener(new SelectionModelListener() {
			public void selectionChanged(SelectionModelEvent evt) {
				updateViewSelectionModelFromData();
			}
		});
    }

    private void installViewSelectionListener() {
        //listen for changes in the list selection and maintain the
		//synchronicity with the selection model
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !indexIsChanging) {
                //    invokeSetDataSelectionIndices(getSelectedIndices(listSelectionModel));
                    selectionModel.setSelectionIndices(getSelectedIndices(listSelectionModel));
				}
			}
		});
    }
    
    private int[] getSelectedIndices(ListSelectionModel sm) {
        int iMin = sm.getMinSelectionIndex();
        int iMax = sm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new int[0];
        }

        int[] rvTmp = new int[1+ (iMax - iMin)];
        int n = 0;
        // selected indices in view coordinates
        for(int i = iMin; i <= iMax; i++) {
            if (sm.isSelectedIndex(i)) {
                rvTmp[n++] = i;
            }
        }
        int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        rv = convertIndicesToModel(rv);
        return rv;
    }

    private void setSelectedIndices(int[] indices, ListSelectionModel sm) {
        sm.clearSelection();
        // indices are in model coordinates
        // must be converted to view coordinates
        indices = convertIndicesToView(indices);
        for(int i = 0; i < indices.length; i++) {
            sm.addSelectionInterval(indices[i], indices[i]);
        }
    }

    private int[] convertIndicesToView(int[] indices) {
        if (indexMapper == null) return indices;
        for (int i = 0; i < indices.length; i++) {
            indices[i] = indexMapper.modelToView(indices[i]);
        }
        return indices;
    }
    private int[] convertIndicesToModel(int[] rv) {
        if (indexMapper == null) return rv;
        for (int i = 0; i < rv.length; i++) {
            rv[i] = indexMapper.viewToModel(rv[i]);
        }
        return rv;
    }

    private void updateViewSelectionModelFromData() {
        indexIsChanging = true;
        setSelectedIndices(selectionModel.getSelectionIndices(), listSelectionModel);
        indexIsChanging = false;
    }

//    private void invokeSetDataSelectionIndices(final int[] is) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                // must be set in model coordinates
//                selectionModel.setSelectionIndices(is);
//
//            }
//        });
//    }

}
