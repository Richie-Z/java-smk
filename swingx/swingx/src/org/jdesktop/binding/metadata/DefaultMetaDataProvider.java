/*
 * $Id: DefaultMetaDataProvider.java,v 1.2 2005/10/10 17:01:07 rbair Exp $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Jeanette Winzenburg
 */
public class DefaultMetaDataProvider implements MetaDataProvider {

  private List fieldNames;
  private Map metaData;

  public DefaultMetaDataProvider() {
    
  }
  public DefaultMetaDataProvider(MetaDataProvider provider) {
    this(provider.getMetaData());
  }

  public DefaultMetaDataProvider(MetaData[] metaDatas) {
    
    setMetaData(metaDatas);
}
//------------------------ metaDataProvider  
  public MetaData[] getMetaData() {
    MetaData[] metaData = new MetaData[getFieldCount()];
    for (int i = 0; i < metaData.length; i++) {
      metaData[i] = getMetaData(getFieldName(i));
    }
    return metaData;
  }

  public MetaData getMetaData(String dataID) {
    return (MetaData) getMetaDataMap().get(dataID);
  }

  public String[] getFieldNames() {
    return (String[]) getFieldNameList().toArray(new String[getFieldCount()]);
  }

  public int getFieldCount() {
    return getFieldNameList().size();
  }

//-------------------------- convenience accessing
  
  /**
   * PRE: 0 <= index < getFieldCount()
   * @param index
   * @return
   */
  public String getFieldName(int index) {
    return (String) getFieldNameList().get(index);
  }

  /**
   * PRE: 0 <= index < getFieldCount()
   * @param index
   * @return
   */
  public MetaData getMetaData(int index) {
    String fieldName = getFieldName(index);
    return getMetaData(fieldName);
  }
  
  public int getFieldIndex(String fieldName) {
    for (int i = 0; i < getFieldCount(); i++) {
      if (fieldName.equals(getFieldName(i))) {
        return i;
      }
    }
    return -1;
  }

  public boolean hasField(String fieldName) {
    return getFieldNameList().contains(fieldName);
  }

//-------------------------- mutating (use on init mostly)
  public void setMetaData(MetaData[] metaData) {
    clear();
    for (int i = 0; i < metaData.length; i++) {
      addField(metaData[i]);
    }
  }

  public void setMetaData(List metaData) {
    clear();
    for (Iterator iter = metaData.iterator(); iter.hasNext(); ) {
      addField((MetaData) iter.next());
    }
  }

  public void addField(MetaData data) {
    getFieldNameList().add(data.getName());
    getMetaDataMap().put(data.getName(), data);
  }

  public void clear() {
    fieldNames = null;
    metaData = null;
  }

//-------------------------- helper
  private List getFieldNameList() {
    if (fieldNames == null) {
      fieldNames = new ArrayList();
    }
    return fieldNames;
  }

  private Map getMetaDataMap() {
    if (metaData == null) {
      metaData = new HashMap();
    }
    return metaData;
  }
}
