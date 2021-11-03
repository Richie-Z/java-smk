/*
 * $Id: HierarchicalDataMetaData.java,v 1.2 2005/10/10 17:01:11 rbair Exp $
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

package org.jdesktop.binding.swingx.adapter;

import java.util.HashMap;

import org.jdesktop.binding.metadata.Converter;
import org.jdesktop.binding.metadata.Converters;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Ramesh Gupta
 */
public class HierarchicalDataMetaData extends TabularDataMetaData {
    private		Element		columns = null;
    private static HashMap	typesMap;

    // Merge with realizer.attr.Decoder.typesMap()
    static {
        typesMap = new HashMap();
        typesMap.put("boolean", java.lang.Boolean.class);
        typesMap.put("date", java.util.Date.class);
        typesMap.put("double", java.lang.Double.class);
        typesMap.put("float", java.lang.Float.class);
        typesMap.put("integer", java.lang.Integer.class);
        typesMap.put("string", java.lang.String.class);
    }

    public HierarchicalDataMetaData(Element metaDataElement) {
        super((metaDataElement == null) ?
            0 : Integer.parseInt(metaDataElement.getAttribute("columnCount")));
    	columns = metaDataElement;
        init();
    }

    protected void init() {
        if (columns != null) {
            NodeList list = ( (Element) columns).getChildNodes();
            int i = 0, k = 0, max = list.getLength();
            Node node;
            Element elem;
            while (i < max) {
                node = list.item(i++);
                if (node instanceof Element) {
                    elem = (Element) node;
                    if (elem.getLocalName().equals("columnMetaData")) {
                        setColumnName(++k, elem.getAttribute("name"));
                        String	type = elem.getAttribute("type");
                        if (type.length() > 0) {
                            Class klass = decodeType(type);
                            if (klass != null) {
                                setColumnClass(k, klass);
                                if (klass != String.class) {
                                    Converter converter =
                                        Converters.get(klass);
                                    if (converter == null) {
                                        System.err.println(
                                            "warning: couldn't find converter for " +
                                            klass.getName() +
                                            ". Reseting class of column " +
                                            k + "to String.class");
                                        setColumnClass(k, String.class);
                                    }
                                    else {
                                        setColumnConverter(k, converter); //stash it
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Merge with realizer.attr.Decoder.decodeType()
    private Class decodeType(String value) {
        Class klass = (Class)typesMap.get(value);
        if (klass == null) {
            try {
                klass = Class.forName(value);
            } catch (ClassNotFoundException e) {
                System.out.println("Could not convert type: " + value + " to a java type or class");
            }
        }
        return klass;
    }

}
