/*
 * $Id: MetaDataProviderAdapter.java,v 1.3 2005/10/10 17:01:13 rbair Exp $
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

import java.util.List;

import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.MetaDataProvider;
import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.event.DataTableListener;
import org.jdesktop.dataset.event.RowChangeEvent;
import org.jdesktop.dataset.event.TableChangeEvent;

/**
 *
 * @author rbair
 */
public class MetaDataProviderAdapter implements MetaDataProvider {
    private DataTable table;
    private MetaData[] cachedMetaData;
    private String[] cachedFieldNames;
    
    /** Creates a new instance of MetaDataProviderAdapter */
    public MetaDataProviderAdapter(DataTable table) {
        assert table != null;
        this.table = table;
        initMetaData();
        //add listener to table for column change events
        table.addDataTableListener(new DataTableListener(){
            public void rowChanged(RowChangeEvent evt) {
                //null op
            }

            public void tableChanged(TableChangeEvent evt) {
                //reload the metaData
                initMetaData();
            }
        });
    }

    public int getFieldCount() {
        return cachedMetaData == null ? 0 : cachedMetaData.length;
    }

    public final String[] getFieldNames() {
        return cachedFieldNames ==  null ? new String[0] : cachedFieldNames;
    }

    public final MetaData[] getMetaData() {
        return cachedMetaData == null ? new MetaData[0] : cachedMetaData;
    }

    public MetaData getMetaData(String dataID) {
        if (cachedMetaData == null) {
            return new MetaData(dataID);
        }
        for (MetaData md : cachedMetaData) {
            if (md.getName().equals(dataID)) {
                return md;
            }
        }
        return null;
    }
    
    private void initMetaData() {
        List<DataColumn> cols = table.getColumns();
        cachedMetaData = new MetaData[cols.size()];
        cachedFieldNames = new String[cachedMetaData.length];
        for (int i=0; i<cachedMetaData.length; i++) {
            DataColumn col = cols.get(i);
            //TODO if the column name changes, my cache is invalidated!!!
            cachedMetaData[i] = new MetaData(col.getName(), col.getType());
            cachedFieldNames[i] = col.getName();
        }
    }
}