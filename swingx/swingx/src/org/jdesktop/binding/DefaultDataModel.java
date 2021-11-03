/*
 * $Id: DefaultDataModel.java,v 1.2 2005/10/10 17:01:03 rbair Exp $
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

import java.util.ArrayList;
import java.util.HashMap;

import org.jdesktop.binding.metadata.MetaData;


/**
 * Default data model implementation designed to hold a single record of
 * field values.  This class provides storage of the model's values and
 * may be used when there is no underlying data model.
 *
 * @see TableModelExtAdapter
 * @see JavaBeanDataModel
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class DefaultDataModel extends AbstractDataModel {
    private ArrayList fieldNames = new ArrayList();
    private HashMap values = new HashMap();
    private HashMap metaData = new HashMap();
    private HashMap fieldAdapters = new HashMap();

    public DefaultDataModel() {
    }

    public DefaultDataModel(MetaData fieldMetaData[]) {
        for(int i = 0; i < fieldMetaData.length; i++) {
            addField(fieldMetaData[i], null);
        }
    }

    public void addField(MetaData fieldMetaData,
                           Object defaultValue) {
        String name = fieldMetaData.getName();
        addField(fieldMetaData);
        values.put(name, defaultValue);
    }

    public void addField(MetaData fieldMetaData) {
        String name = fieldMetaData.getName();
        fieldNames.add(name); // track order fields were added
        metaData.put(name, fieldMetaData);
    }

    public void removeField(MetaData fieldMetaData) {
        String name = fieldMetaData.getName();
        fieldNames.remove(name);
        metaData.remove(name);
    }

    public String[] getFieldNames() {
        return (String[])fieldNames.toArray(new String[fieldNames.size()]);
    }

    public MetaData getMetaData(String fieldName) {
        return (MetaData)metaData.get(fieldName);
    }

    public int getFieldCount() {
        return metaData.size();
    }

    public Object getValue(String fieldName) {
        return values.get(fieldName);
    }

    protected void setValueImpl(String fieldName, Object value) {
        values.put(fieldName, value);
    }

    public int getRecordCount() {
        return 1;
    }

    public int getRecordIndex() {
        return 0;
    }

    public void setRecordIndex(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("DefaultDataModel contains only 1 record");
        }
    }


}
