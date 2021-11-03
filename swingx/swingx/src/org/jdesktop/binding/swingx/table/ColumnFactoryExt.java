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
package org.jdesktop.binding.swingx.table;

import java.text.Collator;

import javax.swing.table.TableModel;

import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.MetaDataProvider;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * @author Jeanette Winzenburg
 */
public class ColumnFactoryExt extends ColumnFactory {

    
    public void configureTableColumn(TableModel model, TableColumnExt column) {
        if (model instanceof MetaDataProvider) {
            configureTableColumn((MetaDataProvider) model, column);
        } else {
            super.configureTableColumn(model, column);
            
        }
    }

    public void configureTableColumn(MetaDataProvider provider,
            TableColumnExt column) {
        MetaData metaData = provider
                .getMetaData(provider.getFieldNames()[column.getModelIndex()]);
        column.setIdentifier(metaData.getName());
        column.setHeaderValue(metaData.getLabel());
        if (metaData.getElementClass() == String.class) {

            if (metaData.getDisplayWidth() > 0) {
                StringBuffer buf = new StringBuffer(metaData.getDisplayWidth());
                for (int i = 0; i < metaData.getDisplayWidth(); i++) {
                    buf.append("r");

                }
                column.setPrototypeValue(buf.toString());
            }
        } else if (metaData.getElementClass() == Number.class) {
            if (metaData.getDisplayWidth() > 0) {
                StringBuffer buf = new StringBuffer(metaData.getDisplayWidth());
                for (int i = 0; i < metaData.getDisplayWidth(); i++) {
                    buf.append("1");

                }
                column.setPrototypeValue(buf.toString());
                
            }
            
        }

    }
}
