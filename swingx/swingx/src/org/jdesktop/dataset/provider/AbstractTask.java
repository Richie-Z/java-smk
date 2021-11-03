/*
 * $Id: AbstractTask.java,v 1.2 2005/10/10 17:01:19 rbair Exp $
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

/**
 *
 * @author rb156199
 */
public abstract class AbstractTask implements Task {
        private int min = 0;
        private int max = 100;
        private int progress = 0;
        private boolean indeterminate = true;
        private boolean cancellable = false;
        private boolean modal = true;

        protected void setMinimum(int val) {
            min = val < 0 || val > max ? 0 : val;
        }
        
        protected void setMaximum(int val) {
            max = val < 0 || val < min ? min : val;
        }
        
        protected void setProgress(int progress) {
            this.progress = progress < 0 ? 0 : progress;
        }
        
        protected void setIndeterminate(boolean b) {
            indeterminate = b;
        }

        public int getMinimum() {
            return min;
        }

        public int getMaximum() {
            return max;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isIndeterminate() {
            return indeterminate;
        }

        public boolean isModal() {
            return modal;
        }

        public boolean canCancel() {
            return cancellable;
        }
        
        public void setModel(boolean b) {
            modal = b;
        }
        
        public void setCanCancel(boolean b) {
            cancellable = b;
        }
}
