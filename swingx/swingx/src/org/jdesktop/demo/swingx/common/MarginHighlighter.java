/*
 * $Id: MarginHighlighter.java,v 1.1 2005/06/28 14:08:06 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx.common;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;

/**
 * Highlighter that applies a border compound from the given marginBorder and
 * the renderer's defaultBorder.
 * 
 * NOTE: this is a quick hack to give a left/right margin to JList.
 * 
 * @author Jeanette Winzenburg
 */
public class MarginHighlighter extends Highlighter {

    private Border marginBorder;

    /**
     * 
     * PRE: marginBorder != null;
     * 
     * @param marginBorder
     */
    public MarginHighlighter(Border marginBorder) {
        if (marginBorder == null)
            throw new NullPointerException("border must not be null");
        this.marginBorder = marginBorder;
    }

    public Component highlight(Component renderer, ComponentAdapter adapter) {
        Border border = ((JComponent) renderer).getBorder();
        if (border != null) {
            border = BorderFactory.createCompoundBorder(border, marginBorder);
        } else {
            border = marginBorder;
        }
        ((JComponent) renderer).setBorder(border);
        return renderer;
    }

}
