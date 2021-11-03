/*
 * $Id: TabularDataModel.java,v 1.3 2005/10/10 17:01:05 rbair Exp $
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
 *
 * @author rbair
 */
public interface TabularDataModel extends DataModel {
    
    /**
     *
     * @return integer containing the number of records accessible
     *         from this data model
     */
    public int getRecordCount();

    public Object getValueAt(String fieldName, int index);
    
    public void setValueAt(String fieldName, int index, Object value);
    
    public void addTabularValueChangeListener(TabularValueChangeListener l);
    
    public void removeTabularValueChangeListener(TabularValueChangeListener l);
    
    public TabularValueChangeListener[] getTabularValueChangeListeners();
}
