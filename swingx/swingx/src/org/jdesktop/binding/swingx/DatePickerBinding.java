/*
 * $Id: DatePickerBinding.java,v 1.2 2005/10/10 17:00:53 rbair Exp $
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.swingx.JXDatePicker;

public class DatePickerBinding extends AbstractBinding {

    private JXDatePicker picker;

    public DatePickerBinding(JXDatePicker picker,
			     DataModel model, String fieldName) {
        super(picker, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
	return picker;
    }

    public void setComponent(JComponent component) {
	this.picker = (JXDatePicker)component;
    }

    protected Object getComponentValue() {
	Class klazz = metaData.getElementClass();
	if (klazz == Date.class) {
	    return picker.getDate();
	}
	else if (klazz == Calendar.class) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(picker.getDateInMillis());
	    return cal;
	}
        else if (klazz == Long.class) {
            return new Long(picker.getDateInMillis());
        }
	// default?
	return picker.getDate();
    }

    protected void setComponentValue(Object value) {
	Class klazz = metaData.getElementClass();
	if (klazz == Date.class) {
	    picker.setDate((Date)value);
	}
	else if (klazz == Calendar.class) {
	    picker.setDateInMillis(((Calendar)value).getTimeInMillis());
	}
        else if (klazz == Long.class) {
            picker.setDateInMillis(((Long)value).longValue());
        }
    }
}
