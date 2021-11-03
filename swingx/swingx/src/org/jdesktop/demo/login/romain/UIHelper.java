/*
 * $Id: UIHelper.java,v 1.1 2005/05/25 23:13:23 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.login.romain;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class UIHelper {
    public static ImageIcon readImageIcon(String fileName)
    {
        Image image = readImage(fileName);
        if (image == null)
            return null;

        return new ImageIcon(image);
    }

    public static Image readImage(String fileName)
    {
        URL url = UIHelper.class.getResource("images/" + fileName);
        if (url == null)
            return null;

        return java.awt.Toolkit.getDefaultToolkit().getImage(url);
    }
}
