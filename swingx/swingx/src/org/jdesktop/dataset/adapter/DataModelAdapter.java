/*
 * $Id: DataModelAdapter.java,v 1.3 2005/10/10 17:01:14 rbair Exp $
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

package org.jdesktop.dataset.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.ValueChangeEvent;
import org.jdesktop.binding.ValueChangeListener;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.metadata.Validator;
import org.jdesktop.dataset.DataSelector;
import org.jdesktop.dataset.event.DataTableListener;
import org.jdesktop.dataset.event.RowChangeEvent;
import org.jdesktop.dataset.event.TableChangeEvent;

/**
 *
 * @author rbair
 */
public class DataModelAdapter implements DataModel {
    private DataSelector selector;
    private MetaDataProviderAdapter mdp;
    private List<Validator> validators = new ArrayList<Validator>();
    private List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
    
    /** Creates a new instance of DataModelAdapter */
    public DataModelAdapter(DataSelector s) {
        assert s != null;
        this.selector = s;
        mdp = new MetaDataProviderAdapter(selector.getDataTable());
        selector.addPropertyChangeListener("rowIndices", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
                for (String fieldName : mdp.getFieldNames()) {
                    fireValueChanged(fieldName);
                }
			}
		});
        selector.getDataTable().addDataTableListener(new DataTableListener() {
           public void rowChanged(RowChangeEvent evt) {
           } 
           
           public void tableChanged(TableChangeEvent evt) {
//               fireModelChanged();
                for (String fieldName : mdp.getFieldNames()) {
                    fireValueChanged(fieldName);
                }
           }
        });
    }

    public void addValidator(Validator validator) {
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    public void removeValidator(Validator validator) {
        validators.remove(validator);
    }

    public Validator[] getValidators() {
        return validators.toArray(new Validator[validators.size()]);
    }

    public void addValueChangeListener(ValueChangeListener valueChangeListener) {
        if (!listeners.contains(valueChangeListener)) {
            listeners.add(valueChangeListener);
        }
    }

    public void removeValueChangeListener(ValueChangeListener valueChangeListener) {
        listeners.remove(valueChangeListener);
    }

    public ValueChangeListener[] getValueChangeListeners() {
        return listeners.toArray(new ValueChangeListener[listeners.size()]);
    }

    public int getFieldCount() {
        return mdp.getFieldCount();
    }

    public String[] getFieldNames() {
        return mdp.getFieldNames();
    }

    public MetaData[] getMetaData() {
        return mdp.getMetaData();
    }

    public MetaData getMetaData(String dataID) {
        return mdp.getMetaData(dataID);
    }

    public Object getValue(String fieldName) {
        List<Integer> indices = selector.getRowIndices();
        if (indices.size() > 0) {
            return selector.getDataTable().getValue(indices.get(0), fieldName);
        }
        return null;
    }

    public void setValue(String fieldName, Object value) {
        List<Integer> indices = selector.getRowIndices();
        for (int index : indices) {
            selector.getDataTable().setValue(index, fieldName, value);
        }
    }
    
    //-----------------event handling code-----------------------
	/**
	 * Fires the value change event. This is called normally during the set operation
	 * @param fieldName
	 */
	protected void fireValueChanged(String fieldName) {
		ValueChangeEvent e = new ValueChangeEvent(this, fieldName);//getCachedEvent(fieldName);
        for (ValueChangeListener listener : listeners) {
			try {
			    listener.valueChanged(e);
			} catch (Exception ex) {
                //TODO some real exception handling needs to occur
			    ex.printStackTrace();
			}
		}
	}

	/**
	 * Fires the model change event.
	 */
//	protected void fireModelChanged() {
//		ModelChangeEvent e = new ModelChangeEvent(this);
//		for (int i = 0; i < dataModelListeners.size(); i++) {
//			DataModelListener dml = (DataModelListener)dataModelListeners.get(i);
//			try {
//				dml.modelChanged(e);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//		//If the row has changed, then details of this master
//		//need to be updated as well. Loop through the detail model
//		//and tell them to reload themselves, etc.
////		notifyMasterChanged();
//	}

//	protected void fireModelMetaDataChanged() {
//		MetaDataChangeEvent e = new MetaDataChangeEvent(this);
//		for (int i = 0; i < dataModelListeners.size(); i++) {
//			DataModelListener dml = (DataModelListener)dataModelListeners.get(i);
//			try {
//				dml.metaDataChanged(e);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//		//If the row has changed, then details of this master
//		//need to be updated as well. Loop through the detail model
//		//and tell them to reload themselves, etc.
////		notifyMasterChanged();
//	}
	
    /**
     * Fires a MetaDataChangeEvent if the *set* of meta data has changed. To
     * know whether the contents of a specific MetaData object have changed,
     * register with that MetaData object.
     */
//	protected void fireMetaDataChanged() {
//		MetaDataChangeEvent e = new MetaDataChangeEvent(this);
//        for (DataModelListener dml : dataModelListeners) {
//			try {
//				dml.metaDataChanged(e);
//			} catch (Exception ex) {
//                //TODO some real exception handling needs to occur
//				ex.printStackTrace();
//			}
//		}
//	}
    
//    private final class DataSetListener implements PropertyChangeListener {
//        public void propertyChange(PropertyChangeEvent evt) {
//            if (evt.getPropertyName().equals("rowIndices") && evt.getSource() == s) {
//                fireSelectionModelChanged(new SelectionModelEvent(DataModelImpl.this, 0, getRecordCount()));
//            }
//        }
//    }
}