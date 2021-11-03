/*
 * $Id: SaveTask.java,v 1.3 2005/10/10 17:01:19 rbair Exp $
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

package org.jdesktop.dataset.provider;

import javax.swing.Icon;

import org.jdesktop.dataset.DataTable;

/**
 *
 * @author rbair
 */
public abstract class SaveTask extends AbstractTask {
    private DataTable[] tables;

    public SaveTask(DataTable[] tables) {
        this.tables = tables == null ? new DataTable[0] : tables;
    }

    public void run() {
        setIndeterminate(true);
        try {
            saveData(tables);
            setProgress(getMaximum());
        } catch (Exception e) {
            final Throwable error = e;
            e.printStackTrace();
            setProgress(getMaximum());
        }
    }

    protected abstract void saveData(DataTable[] tables) throws Exception;

    /**
     * @inheritDoc
     */
    public String getDescription() {
        return "<html><h3>Saving data</h3></html>";
    }

    /**
     * @inheritDoc
     */
    public Icon getIcon() {
        return null;
    }

    /**
     * @inheritDoc
     */
    public String getMessage() {
        return "Saving item " + (getProgress() + 1) + " of " + getMaximum();
    }

    /**
     * @inheritDoc
     */
    public boolean cancel() throws Exception {
        return false;
    }
}