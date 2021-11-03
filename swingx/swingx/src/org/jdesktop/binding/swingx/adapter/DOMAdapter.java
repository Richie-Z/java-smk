/*
 * $Id: DOMAdapter.java,v 1.2 2005/10/10 17:01:11 rbair Exp $
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
import org.jdesktop.binding.metadata.Converter;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


// CLEAN: temporarily moved from old jdnc/swingx tree to allow
// markup project to compile
public class DOMAdapter extends DefaultTreeTableModel {
	protected	Document	dom = null;
//    private		Element		columns = null;
    private		HierarchicalDataMetaData metaData;
/*
    private		int	userDataSupportType = 0;	// 1 == getUserData; 2 == getUserObject
    private static final int GET_USER_DATA = 1;
    private static final int GET_USER_OBJECT = 2;
*/
	public DOMAdapter() {
	}

    public DOMAdapter(Document dom) {
        bind(dom);
    }

    public final void bind(Document dom) {
        if (dom == null) {
            throw new IllegalArgumentException("null document object model");
        }

        if (this.dom == null) {
            this.dom = dom;
            /** @todo Handle case where no metaData is present! */
            Element	metaDataElem = (Element) dom.getDocumentElement().getElementsByTagNameNS(
                "http://www.jdesktop.org/2004/05/jdnc", "metaData").item(0);
            setMetaData(new HierarchicalDataMetaData(metaDataElem));
/*
            Object[]	cells = null;
            try {
                cells = (Object[]) (new Expression(columns, "getUserData",
                                                  new Object[0])).getValue();
                userDataSupportType = GET_USER_DATA;
            }
            catch (Exception ex) {
                try {
                    cells = (Object[]) (new Expression(columns,
                        "getUserObject", new Object[0])).getValue();
                    userDataSupportType = GET_USER_OBJECT;
                }
                catch (Exception nex) {

                }
            }
*/
        }
        else {
            throw new IllegalArgumentException("dom already bound");
        }
    }

    public String convertValueToText(Object value) {
        // This method requires support from JXTree. Will NOT work with JTree!
        if(value != null) {
            Object	realValue = getValueAt(value, 0);
            if (realValue != null) {
                return realValue.toString();
            }
        }
        return "";
    }

	public Object getRoot() {
        return dom.getDocumentElement().getElementsByTagNameNS(
                "http://www.jdesktop.org/2004/05/jdnc", "rows").item(0);
    }

    public Class getColumnClass(int column) {
        return column == 0 ? super.getColumnClass(0) :
            getMetaData().getColumnClass(column+1);
    }

	public Object getChild(Object parent, int index) {
        Element		parentElement = (Element) parent;
        NodeList	list = ((Element) parentElement).getChildNodes();
        int			i = 0, k = index, max = list.getLength();
        Node		node;
        Element		elem;
        while (i < max) {
            node = list.item(i++);
            if (node instanceof Element) {
                elem = (Element) node;
                if (elem.getLocalName().equals("row")) {
                    if (k-- == 0) {
                        /*
                        System.out.print("got child:");
                        NamedNodeMap	attributes = elem.getAttributes();
                        for (int a = 0; a < attributes.getLength(); a++) {
                            System.out.print(" " +
                                             attributes.item(a).getNamespaceURI() + ":" +
                                             attributes.item(a).getLocalName() + "=" +
                                             attributes.item(a).getNodeValue());
                        }
                        System.out.println(";");
*/
                        return elem;
                    }
                }
            }
        }
/*
        if (true) {
            // We should never get here!
            System.out.print(parentElement.getLocalName());
            NamedNodeMap attributes = parentElement.getAttributes();
            for (int a = 0; a < attributes.getLength(); a++) {
                System.out.print(" " + attributes.item(a).getNamespaceURI() + ":" +
                                 attributes.item(a).getLocalName() + "=" +
                                 attributes.item(a).getNodeValue());
            }
            System.out.println(" has no child at index " + k);
        }
 */
        return null;
	}

	public int getChildCount(Object parent) {
        Element		parentElement = (Element) parent;
        NodeList	list = ((Element) parentElement).getChildNodes();
        int			i = 0, k = 0, max = list.getLength();
        Node		node;
        Element		elem = null;
        while (i < max) {
            node = list.item(i++);
            if (node instanceof Element) {
                elem = (Element) node;
                if (elem.getLocalName().equals("row")) {
                    k++;
                }
            }
        }
/*
		if (true) {
            System.out.print(parentElement.getLocalName());
            NamedNodeMap attributes = parentElement.getAttributes();
            for (int a = 0; a < attributes.getLength(); a++) {
                System.out.print(" " + attributes.item(a).getNamespaceURI() + ":" +
                                 attributes.item(a).getLocalName() + "=" +
                                 attributes.item(a).getNodeValue());
            }
            System.out.println(" has " + k + " children");
        }
 */
        return k;
	}

    public void setMetaData(HierarchicalDataMetaData metaData) {
        this.metaData = metaData;
    }

    public HierarchicalDataMetaData getMetaData() {
        return metaData;
    }

	public int getColumnCount() {
        return getMetaData().getColumnCount();
	}

    /**
     * @throws IllegalArgumentException if the column name does not exist in
     *         this tabular data model
     * @param columnName String containing the name of the column
     * @return integer index of column in the data model which corresponds
     *         to the specified column name
     */
    public int getColumnIndex(String columnName) {
        return getMetaData().getColumnIndex(columnName);
    }

	public String getColumnName(int column) {
        return getMetaData().getColumnName(column+1);
	}

	public Object getValueAt(Object node, int column) {
        if (node == null)
            throw new IllegalArgumentException("Node is null; " + column);
        Element		parentElement = (Element) node;
        NodeList	list = ((Element) parentElement).getChildNodes();
        int			i = 0, k = column, max = list.getLength();
        Node		n;
        Element		elem;
        while (i < max) {
            n = list.item(i++);
            if (n instanceof Element) {
                elem = (Element) n;
                if (elem.getLocalName().equals("cell")) {
                    if (k-- == 0) {
                        //    System.out.println("Value of " + node + " at column " +
                        //                       column + "=" + elem + ";" + elem.getFirstChild());
                        Node cellData = elem.getFirstChild();
                        String	rawValue = cellData == null ?
                            	"" : cellData.getNodeValue();
                        Converter converter =
                            getMetaData().getColumnConverter(column + 1);
                        if (converter == null) {
                            return rawValue;
                        }
                        else {
                            try {
                                /** @todo cache converted value */
                                return converter.decode(rawValue, null);
                            }
                            catch (Exception ex) {
                                return rawValue;
                            }
                        }
                    }
                }
            }
        }
        return null;
	}
/*
	private Object[] getCells(Element element) {
        Object[]	cells = null;
        switch (userDataSupportType) {
            case	GET_USER_DATA: {
                try {
                    cells = (Object[]) (new Expression(element, "getUserData",
                        new Object[0])).getValue();
                }
                catch (Exception ex) {
                }
                break;
            }
            case	GET_USER_OBJECT: {
                try {
                    cells = (Object[]) (new Expression(element, "getUserObject",
                        new Object[0])).getValue();
                }
                catch (Exception ex) {
                }
                break;
            }
            default: {
                break;
            }
        }
        return cells;
    }
*/
}
