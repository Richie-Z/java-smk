/*
 * SelectionBinding.java
 *
 * Created on February 24, 2005, 6:33 AM
 */

package org.jdesktop.binding;


/**
 *
 * @author rbair
 */
public abstract class SelectionBinding {
    protected SelectionModel selectionModel;
    
    public SelectionBinding(SelectionModel model) {
        assert model != null;
        selectionModel = model;
    }
}
