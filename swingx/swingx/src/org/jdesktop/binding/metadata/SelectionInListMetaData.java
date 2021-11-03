/*
 * $Id: SelectionInListMetaData.java,v 1.3 2005/10/10 17:01:08 rbair Exp $
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
 * MetaData for declaring binding a ComboBox to
 * dynamic drop-down data.
 * 
 * NOTE: this is experimental. 
 * 
 * @author Jeanette Winzenburg
 */
public class SelectionInListMetaData extends MetaData {
    private String sourceFieldName;
    private TabularDataModel sourceDataModel;
    private String[] displayFieldNames;
    
    public SelectionInListMetaData() {
        this("tabularvalue");
    }

    public SelectionInListMetaData(String name) {
        super(name);
     
    }

    public SelectionInListMetaData(String name, Class elementType) {
        super(name, elementType);
    }
    
    public SelectionInListMetaData(String name, String label, String sourceFieldName) {
        this(name);
        setLabel(label);
        setSourceFieldName(sourceFieldName);
    }

    /**
     * sets the bound column in the drop-down.<p>
     * 
     * @param sourceFieldName
     */
    public void setSourceFieldName(String sourceFieldName) {
        String oldNames = getSourceFieldName();
        // PENDING JW: make sure the bound field classe 
        // is compatibel to the the element class
        this.sourceFieldName = sourceFieldName;
        firePropertyChange("sourceFieldName", oldNames, getSourceFieldName());
        
    }

    /** 
     * returns bound column in the drop-down.
     * @return
     */
    public String getSourceFieldName() {
        return sourceFieldName;
    }
 
    /** 
     * returns the model for the drop-down.
     * @return
     */
    public TabularDataModel getSourceDataModel() {
        return sourceDataModel;
    }
    
    /**
     * sets the model for the drop-down.
     * @param sourceDataModel
     */
    public void setSourceDataModel(TabularDataModel sourceDataModel) {
        TabularDataModel old = getSourceDataModel();
        this.sourceDataModel = sourceDataModel;
        firePropertyChange("sourceDataModel", old, getSourceDataModel());
    }

    
    public String[] getDisplayFieldNames() {
        return displayFieldNames;
    }
    
    public void setDisplayFieldNames(String[] displayFieldNames) {
        String[] old = getDisplayFieldNames();
        this.displayFieldNames = displayFieldNames;
        firePropertyChange("displayFieldNames", old, getDisplayFieldNames());
    }

    public int getBoundColumnIndex() {
        if ((getSourceFieldName() == null) || getSourceDataModel() == null) return -1;
        String[] fieldNames = getDisplayFieldNames();
        if (fieldNames == null) {
            fieldNames = getSourceDataModel().getFieldNames();
        }
        for (int i = 0; i < fieldNames.length; i++) {
            if (getSourceFieldName().equals(fieldNames[i])) {
                return i;
            }
        }
        return -1;
    }
}
