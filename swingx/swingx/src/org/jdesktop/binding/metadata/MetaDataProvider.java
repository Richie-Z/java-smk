/*
 * $Id: MetaDataProvider.java,v 1.3 2005/10/10 17:01:09 rbair Exp $
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


/**
 * Interface for marking objects which can return MetaData instances for
 * data objects.
 *
 * @author Jeanette Winzenburg
 * @author Amy Fowler
 * @version 1.0
 */
public interface MetaDataProvider {

    /**
     * Note: if the type for id is changed to Object type this will
     * have to change to returning Object[].
     * 
     * @return array containing the names of all data fields in this map
     */
    String[] getFieldNames();

    /**
    *
    * @return integer containing the number of contained MetaData
    */

    int getFieldCount();

    /**
     * Note: String will likely be converted to type Object for the ID
     * @param dataID String containing the id of the data object
     * @return MetaData object which describes properties, edit constraints
     *         and validation logic for a data object
     */
    MetaData getMetaData(String dataID);

    /**
     * convenience to return all MetaData.
     * 
     * @return
     */
    MetaData[] getMetaData();


}