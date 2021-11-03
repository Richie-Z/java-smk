/*
 * $Id: DataCommand.java,v 1.6 2005/10/13 18:17:25 rbair Exp $
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

package org.jdesktop.dataset;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Represents a command that can be executed against a data store by a {@link 
 * DataProvider}, for example, commands for retrieving data, or for persisting
 * data to the data store. For an example of how this is used, see {@link
 * org.jdesktop.dataset.provider.sql.AbstractSqlCommand}. A concrete DataCommand
 * is assigned to a DataProvider using {@link DataProvider#setCommand(DataCommand)}
 * and retrieved using {@link DataProvider#getCommand()}.
 *
 * <p>A DataCommand holds a set of named parameters, each of which is assigned a value.
 * These parameters are then used in executing the command.
 *
 * <p>A DataCommand allows a {@link DataProvider} to provide a harness around load and save
 * operations, without needing the specifics of interaction with a data store. For
 * SQL databases, a DataCommand may be a SELECT for reads and an INSERT, UPDATE
 * or DELETE for writes. The DataProvider doesn't need to know how these SQL statements
 * are actually built for the table in question--in fact, stored procedures could be
 * used just by substituting the DataCommand used for reads or writes. Note that this 
 * abstract DataCommand class does not define the semantics of the commands--that must 
 * be done in its subclasses.
 *
 * <p><b>Internal</b>--This is a class used internally in this package and is not of 
 * general purpose use.
 *
 * <p>TODO: Appears to be a bug with getParamValues() -- it should only return
 * the values associated with the specified param names. Futher, set/get/clear
 * methods should only work for the specified param names. Alternatively, there
 * should be a default implementation of getParamNames, that returns the keys
 * used in the param map.</p>
 *
 * <p> The struggle here is that the SQL based DataCommands have a specific
 * set of params -- no more, no less. However, and HTTP based DataCommand can
 * have any number of parameters. It could be a dynamic set.
 *
 * @author rbair
 */
public abstract class DataCommand {
    /**
     * A short description of the command.
     */
    private String shortDescription;
    
    /**
     * A special marker indicating that a parameter has been undefined.
     */
    private static final Object UNDEFINED = new Object();
    
    /**
     * Contains all of the params
     */
    private Map<String,Object> params = new HashMap<String,Object>();
    
    /**
     * Set a short description for this Task. This description is used
     * within a GUI builder to describe what the Task does, or wherever a short
     * description might be useful, such as within some logging statements.
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription == null ? "" : shortDescription;
    }
    
    /**
     * Returns the short description of this DataCommand
     * @return the short description
     */
    public String getShortDescription() {
        return shortDescription;
    }
    
    /**
     * Sets the given named parameter to the given value, overwriting any value
     * already assigned. Passing in a value of
     * "null" will *not* clear the parameter, but will set the parameter to the
     * null value.
     * @param name The parameter's name
     * @param value The parameter's value
     */
    public void setParameter(String name, Object value) {
        params.put(name, value);
    }
    
    /**
     * Clears the given named parameter of any associated value. The parameter is
     * still mapped, but has an undefined value.
     * @param name The named parameter to clear.
     */
    public void clearParameter(String name) {
        params.put(name, UNDEFINED);
    }
    
    /**
     * Clears all of the parameters; see {@link #clearParameter(String)}
     */
    public void clearParameters() {
        for (String name : params.keySet()) {
            params.put(name, UNDEFINED);
        }
    }
    
    /**
     * Returns the value for the given named parameter.
     * @param name The name of the parameter to look up
     * @return the named parameter's value; null if the parameter
     * was never assigned a value.
     */
    public Object getParameter(String name) {
        return params.get(name);
    }
    
    /**
     * Returns an array containing all of the parameter names for this DataCommand
     * @return an array of the parameter names for this DataCommand
     */
    public abstract String[] getParameterNames();
    
    /**
     * Returns an object array containing all of the parameter values for this
     * DataCommand.
     * @return an object array of the parameter values for this
     * DataCommand.
     */
    public Object[] getParameterValues() {
        return params.values().toArray();
    }
}