/*
 * $Id: LoadTask.java,v 1.5 2005/10/10 17:01:18 rbair Exp $
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

import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.jdesktop.dataset.DataTable;


/**
 *
 * @author rbair
 */
public abstract class LoadTask extends AbstractTask {
    /**
     * This linked list contains "LoadItem" objects. As a specific unit of data
     * is loaded (for instance, all of the data needed for a specific
     * DataTable), a new LoadItem is created and placed at the tail of the
     * loadQueue. The scheduleLoad method then executes and the current items in
     * the queue are removed and passed to the loadData method. This queue is
     * accessed from multiple threads (many threads put data into the queue, one
     * thread removes items from the queue). Synchronization occurs on the
     * loadQueue.
     */
    private LinkedList loadQueue = new LinkedList();

    /**
     * Object used for collescing multiple calls to scheduleLoad into one call
     * to loadData.
     */
    private LoadNotifier loadNotifier = new LoadNotifier();

    /**
     * The DataTables that are being loaded.
     */
    private DataTable[] tables;

    private boolean loadOnEDT = true;

    public void setLoadOnEDT(boolean val) {
        loadOnEDT = val;
    }
    
    /**
     * Creates a new LoadTask. The param is the array of DataTables to be
     * loaded on this thread
     */
    public LoadTask(DataTable[] tables) {
        this.tables = tables == null ? new DataTable[0] : tables;
    }

    /**
     * @inheritDoc
     */
    public void run() {
        setIndeterminate(true);
        try {
            //TODO If meta-data of any kind needs to be loaded, this is the place to do it
            readData(tables);
            scheduleLoad();
            setProgress(getMaximum());
        } catch (Exception e) {
            final Throwable error = e;
            e.printStackTrace();
            setProgress(getMaximum());
        }
    }

    /**
     * Subclasses must implement this
     * method to read the data from the data store and place it in a data
     * structure which is <b>disconnected </b> from the DataTable. When
     * increments of data are ready to be loaded into the model, this method
     * should invoke <code>scheduleLoad</code>, which will cause
     * <code>loadData</code> to be called on the event dispatch thread, where
     * the model may be safely updated. Progress events may be fired from this
     * method.
     * <p>
     * A final <code>scheduleLoad</code> will be called automatically at the
     * conclusion of the loading process, so it is not technically necessary
     * for this method to call <code>scheduleLoad</code> at all unless you
     * want to support partial loads.
     * 
     * @see #scheduleLoad
     * @throws Exception
     *             if errors occur while reading data
     */
    protected abstract void readData(final DataTable[] tables) throws Exception;

    /**
     * Invoked internally once the <code>readData</code> method calls
     * <code>scheduleLoad</code> to schedule the loading of an increment of
     * data to the DataTable. This method is called on the event dispatch thread,
     * therefore it is safe to mutate the model from this method. Subclasses
     * must implement this method to load the current contents of the
     * disconnected data structure into the DataTable. Note that because there
     * is an unpredictable delay between the time <code>scheduleLoad</code> is
     * called from the &quot;reader&quot; thread and <code>loadData</code>
     * executes on the event dispatch thread, there may be more data available
     * for loading than was available when <code>scheduleLoad</code> was
     * invoked. All available data should be loaded from this method.
     * <p>
     * This method should fire an appropriate progress event to notify progress
     * listeners when:
     * <ul>
     * <li>incremental load occurs(for determinate load operations)</li>
     * <li>load completes</li>
     * <li>exception occurs</li>
     * </ul>
     * </p>
     * 
     */
    protected abstract void loadData(LoadItem[] items);

    /**
     * Invoked by the <code>readData</code> method from the &quot;reader&quot;
     * thread to schedule a subsequent call to <code>loadData</code> on the
     * event dispatch thread. If <code>readData</code> invokes
     * <code>scheduleLoad</code> multiple times before <code>loadData</code>
     * has the opportunity to execute on the event dispatch thread, those
     * requests will be collapsed, resulting in only a single call to
     * <code>loadData</code>.
     * 
     * @see #readData
     * @see #loadData
     */
    protected void scheduleLoad(LoadItem item) {
        synchronized (loadQueue) {
            if (item != null) {
                loadQueue.addLast(item);
            }
            if (!loadNotifier.isPending()) {
                loadNotifier.setPending(true);
                if (loadOnEDT) {
                    SwingUtilities.invokeLater(loadNotifier);
                } else {
                    loadNotifier.run();
                }
            }
        }
    }

    /**
     * Same as <code>scheduleLoad(LoadItem)</code>, except that this method
     * will simply schedule a load operation for any remaining items in the
     * queue, but will not add any items to the queue.
     */
    protected void scheduleLoad() {
        scheduleLoad(null);
    }

    /**
     * @inheritDoc
     */
    public String getDescription() {
        return "<html><h3>Loading data</h3></html>";
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
        return "Loading item " + (getProgress() + 1) + " of " + getMaximum();
    }

    /**
     * @inheritDoc
     */
    public boolean cancel() throws Exception {
        return false;
    }

    /**
     * Represents a Unit of data, ready to be loaded.
     */
    public static final class LoadItem<E> {
        public DataTable table;

        public E data;

        public LoadItem(DataTable table, E data) {
            this.table = table;
            this.data = data;
        }
    }

    /**
     *
     */
    private class LoadNotifier implements Runnable {
        private boolean pending = false;

        LoadNotifier() {
        }

        public synchronized void setPending(boolean pending) {
            this.pending = pending;
        }

        public synchronized boolean isPending() {
            return pending;
        }

        public void run() {
            synchronized (loadQueue) {
                if (loadQueue.size() > 0) {
                    LoadItem[] items = (LoadItem[]) loadQueue
                            .toArray(new LoadItem[loadQueue.size()]);
                    loadQueue.clear();
                    loadData(items);
                }
                setPending(false);
            }
        }
    }
}
