/*
 * $Id: FramedEditorPaneLinkVisitor.java,v 1.3 2005/10/12 08:48:12 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx.common;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.EditorPaneLinkVisitor;
import org.jdesktop.swingx.LinkModel;

/**
 * A LinkVisitor opening itself in a JFrame.
 * 
 * @author  Jeanette Winzenburg
 */
public class FramedEditorPaneLinkVisitor extends EditorPaneLinkVisitor {
    JFrame frame;
    
    
    public void visit(LinkModel model){
        showFrame(model);
        super.visit(model);
    }

    private void showFrame(LinkModel model) {
        if (frame == null) {
            frame = createFrame();
        }
        frame.setVisible(true);
        frame.toFront();
        frame.setTitle(String.valueOf(model.getURL()));
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.add(new JScrollPane(getOutputComponent()));
        frame.setSize(800, 600);
        return frame;
    }

}
