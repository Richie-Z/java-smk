/*
 * $Id: DataProvider.java,v 1.10 2005/10/10 17:01:00 rbair Exp $
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
package org.jdesktop.dataset;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jdesktop.dataset.provider.LoadTask;
import org.jdesktop.dataset.provider.SaveTask;
import org.jdesktop.dataset.provider.Task;


/**
 * Provides a basic implementation of DataProvider that handles all of the
 * threading issues normally associated with writing a DataProvider.
 *
 * @author rbair
 */
public abstract class DataProvider {
    /**
     * thread pool from which to get threads to execute the loader.
     */
    private static final Executor EX = Executors.newCachedThreadPool();

	/**
     * Helper used for notifying of bean property changes.
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    /**
     * The command associated with this DataProvider (used for saving, retrieving, etc)
     */
    private DataCommand command;
    /**
     * (Optional) The DataConnection to use with this DataProvider
     */
    private DataConnection connection;
    
    public void load(DataTable[] tables) {
        Task task = createLoadTask(tables);
        runTask(task);
    }
    
    public void loadAndWait(DataTable[] tables) {
        LoadTask task = createLoadTask(tables);
        task.setLoadOnEDT(false);
        runTaskAndWait(task);
    }
    
	public void load(DataTable t) {
        load(new DataTable[]{t});
    }
    
    public void loadAndWait(DataTable t) {
        loadAndWait(new DataTable[]{t});
    }
    
	public void save(DataTable t) {
        save(new DataTable[]{t});
    }
    
    public void saveAndWait(DataTable t) {
        saveAndWait(new DataTable[]{t});
    }
    
    public void save(DataTable[] tables) {
        Task task = createSaveTask(tables);
        runTask(task);
    }
    
    public void saveAndWait(DataTable[] tables) {
        Task task = createSaveTask(tables);
        runTaskAndWait(task);
    }
    
    /**
     * Creates a task that saves data from an array of DataTables to the
     * data store. All of these tables will be saved serially on the same
     * background thread.
     */
    protected abstract SaveTask createSaveTask(DataTable[] tables);
    
    /**
     * Creates a Task that loads data from the data store into one or more
     * DataTables. All of these tables will be loaded serially using the same
     * background thread.
     */
    protected abstract LoadTask createLoadTask(DataTable[] tables);
    
    /**
     * Invoked by the <code>load</code> or <code>save</code> methods.
     * This method will be called on the EventDispatch thread, and therefore
     * must not block. This method is provided to allow concrete subclasses to
     * provide a custom thread creation/scheduling implementation.
     * 
     * @param runner
     */
    protected void runTask(Task runner) {
//      Application.getInstance().getProgressManager().addProgressable(runner);
        EX.execute(runner);
    }
    
    protected void runTaskAndWait(Task runner) {
        runner.run();
    }
    
    /**
     * Adds a PropertyChangeListener to this class for any changes to bean 
     * properties.
     *
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

   /**
     * Adds a PropertyChangeListener to this class for specific property changes.
     *
     * @param propertyName The name of the property to listen to changes for.
     * @param listener The PropertyChangeListener to notify of changes to this 
     * instance.
     */
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     * Stops notifying a specific listener of changes to a specific property.
     *
     * @param propertyName The name of the property to ignore from now on.
     * @param listener The listener to stop receiving notifications.
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    	pcs.removePropertyChangeListener(propertyName, listener);
    }

    /** 
     * Assigns the single DataCommand instance used by this provider to execute
     * load and store operations. Like DataProvider, the DataCommand is abstract
     * so the specific load/store operations on a DataCommand are described in a 
     * DataCommand subclass, and a specific subclass of DataProvider must know how
     * to work with that command instance.
     *
     * @param cmd The DataCommand instance to use for load/store operations. 
     */
    public void setCommand(DataCommand cmd) {
        if (this.command != cmd) {
            DataCommand oldCommand = this.command;
            this.command = cmd;
            pcs.firePropertyChange("command", oldCommand, cmd);
        }
    }
    
    /**
     * Returns the DataCommand instance for this DataProvider, assigned with
     * {@link #setCommand(DataCommand)}.
     * @return the DataCommand instance for this DataProvider.
     */
    public DataCommand getCommand() {
        return command;
    }
    
    public void setConnection(DataConnection conn) {
        if (this.connection != conn) {
            DataConnection oldConn = this.connection;
            this.connection = conn;
            pcs.firePropertyChange("connection", oldConn, conn);
        }
    }
    
    public DataConnection getConnection() {
        return connection;
    }    
}