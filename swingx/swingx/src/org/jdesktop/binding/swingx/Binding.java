/*
 * $Id: Binding.java,v 1.2 2005/10/10 17:00:53 rbair Exp $
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

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;

/**
 * Class which binds a user-interface component to a specific element
 * in a data model.  A Binding instance implements the following tasks:
 * <ul>
 * <li>pulls values from the data model into the UI component</li>
 * <li>performs element-level validation on the value contained
 *     in the UI component to determine whether it can/should be
 *     pushed to the model.</li>
 * <li>pushes validated values from the UI component to the data model.</li>
 * </ul>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public interface Binding {

    public static final int AUTO_VALIDATE = 0;
    public static final int AUTO_VALIDATE_STRICT = 1;
    public static final int AUTO_VALIDATE_NONE = 2;

    public static final int UNVALIDATED = 0;
    public static final int VALID = 1;
    public static final int INVALID = 2;

    JComponent getComponent();

    DataModel getDataModel();

    String getFieldName();

    /**
     * Pulls the value of this binding's data model element
     * into its UI component.
     *
     * @return boolean indicating whether or not the value was pulled from the
     *         data model
     */
    boolean pull();

    /**
     *
     * @return boolean indicating whether or not the value contained in
     *         this binding's UI component has been modified since the
     *         value was last pushed or pulled
     */
    boolean isModified();

    /**
     * @return boolean indicating whether or not the value contained in
     *         this binding's UI component is valid
     */
    boolean isValid();

    int getValidState();

    /**
     * Returns validation error messages generated from the most
     * recent element-level validation pass.
     *
     * @return array containing any error messages which occurred during
     *         element-level validation
     */
    String[] getValidationErrors();

    /**
     * Pushes the current value contained in this binding's UI component
     * to this binding's data model element.  Only valid values
     * should be pushed to the model.
     * @return boolean indicating whether or not the value was pushed to the
     *         data model
     */
    boolean push();

    void addPropertyChangeListener(PropertyChangeListener pcl);

    void removePropertyChangeListener(PropertyChangeListener pcl);

    PropertyChangeListener[] getPropertyChangeListeners();

}
