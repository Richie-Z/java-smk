/*
 * $Id: BindException.java,v 1.2 2005/10/10 17:00:53 rbair Exp $
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

package org.jdesktop.binding.swingx;

/**
 * Thrown when a binding could not be established between a user-interface
 * component and a data model.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class BindException extends Exception {

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     */
    public BindException(Object dataModel) {
        this("could not bind to "+dataModel.getClass().getName());
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param cause the specific throwable which caused the bind failure
     */
    public BindException(Object dataModel, Throwable cause) {
        this("could not bind to "+dataModel.getClass().getName(), cause);
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param fieldName string containing the name of the field or element
     *        within the data model
     */
    public BindException(Object dataModel, String fieldName) {
        this("could not bind to field"+fieldName+" on "+dataModel.getClass().getName());
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param fieldName string containing the name of the field or element
     *        within the data model
     * @param cause the specific throwable which caused the bind failure
     */
    public BindException(Object dataModel, String fieldName, Throwable cause) {
        this("could not bind to field"+fieldName+" on "+dataModel.getClass().getName(),
             cause);
    }


    /**
     * Instantiates bind exception.
     * @param message String containing description of why exception occurred
     */
    public BindException(String message) {
        super(message);
    }

    /**
     * Instantiates bind exception.
     * @param message String containing description of why exception occurred
     * @param cause the specific throwable which caused bind failure
     */
    public BindException(String message, Throwable cause) {
        super(message, cause);
    }

}
