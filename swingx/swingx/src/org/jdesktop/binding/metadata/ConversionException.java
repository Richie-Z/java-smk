/*
 * $Id: ConversionException.java,v 1.2 2005/10/10 17:01:08 rbair Exp $
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
 * Thrown by Converter instances when errors occur during value conversion.
 * @see Converter
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class ConversionException extends Exception {

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified object value to a string.
     * @param value object which could not be converted
     * @param fromClass class of object value being converted
     */
    public ConversionException(Object value, Class fromClass) {
        this("could not convert value from class " + fromClass.getName() +
             "to a string");
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified object value to a string.
     * @param value object value which could not be converted
     * @param fromClass class of object value being converted
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(Object value, Class fromClass, Throwable cause) {
        this("could not convert value from class " + fromClass.getName() +
             "to a string", cause);
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified string value to an object
     * of the specified class.
     * @param value string value which could not be converted
     * @param toClass class the value was being converted to
     */
    public ConversionException(String value, Class toClass) {
        this("could not convert string value \"" + value + "\" to " +
             toClass.getName());
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified string value to an object
     * of the specified class.
     * @param value object value which could not be converted
     * @param toClass class of object value being converted
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(String value, Class toClass, Throwable cause) {
        this("could not convert string value \"" + value + "\" to " +
             toClass.getName(), cause);
    }

    /**
     * Instantiates conversion exception.
     * @param message String containing description of why exception occurred
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Instantiates conversion exception.
     * @param message String containing description of why exception occurred
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
