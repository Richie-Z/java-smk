/*
 * $Id: Validator.java,v 1.2 2005/10/10 17:01:08 rbair Exp $
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

import java.util.Locale;

/**
 * Interface for defining an object which performs validation checks
 * on a value object to determine whether or not it is valid.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public interface Validator {
    /**@todo aim: change String array to StringBuffer */
    /**
     * Determines whether or not the specified value is valid.  If
     * validation passes, returns <code>true</code>.  If
     * validation fails, returns <code>false</code> and an
     * appropriate localized error message will be placed in the
     * first index of the error String array.
     *
     * @param value the value to be validated
     * @param locale Locale object which should be used to encode any
     *        returned error messages
     * @param error String array used to return an error message if
     *        validation fails
     * @return boolean indicating whether or not the specified object
     *         is valid
     */
    boolean validate(Object value, Locale locale, String[] error);

}
