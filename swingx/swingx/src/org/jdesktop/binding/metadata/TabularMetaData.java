/*
 * $Id: TabularMetaData.java,v 1.3 2005/10/10 17:01:08 rbair Exp $
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
package org.jdesktop.binding.metadata;

import org.jdesktop.binding.TabularDataModel;

/**
 * MetaData for declaring a field of type TabularDataModel.
 * 
 * @author Jeanette Winzenburg
 */
public class TabularMetaData extends MetaData {
    private String[] fieldNames;

    public TabularMetaData() {
        this("tabularvalue");
    }

    public TabularMetaData(String name) {
        super(name, TabularDataModel.class, null);
     
    }
    
    public TabularMetaData(String name, String label, String[] fieldNames) {
        this(name);
        setLabel(label);
        setFieldNames(fieldNames);
    }

    /**
     * sets subset of fields to show. null means all.
     * @param fieldNames
     */
    public void setFieldNames(String[] fieldNames) {
        String[] oldNames = getFieldNames();
        this.fieldNames = fieldNames;
        firePropertyChange("fieldNames", oldNames, getFieldNames());
        
    }

    /** 
     * returns subset of fields to show in the asociated tabular
     * structure. may be null to indicate all or empty array to
     * indicate none (hmmm...)
     * @return
     */
    public String[] getFieldNames() {
        // todo: return unmodifiable array
        return fieldNames;
    }
    
    
    public String getLabel() {
        return label;
    }
}
