/*
 * $Id: WebRowSetDataProvider.java,v 1.4 2005/10/15 11:43:20 pdoubleya Exp $
 *
 * Created on March 21, 2005, 9:52 AM
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

package org.jdesktop.dataset.provider.sql;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.sql.RowSetMetaData;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.swing.SwingUtilities;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataProvider;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.event.TableChangeEvent;
import org.jdesktop.dataset.provider.LoadTask;
import org.jdesktop.dataset.provider.SaveTask;
import org.jdesktop.dataset.provider.LoadTask.LoadItem;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Takes webrowset XML and uses it to populate a DataTable. This particular
 * DataProvider will drop all of the columns from the DataTable and add new
 * columns back in based on the meta data in the XML.
 *
 * @author Richard Bair
 */
public class WebRowSetDataProvider extends DataProvider {
    /**
     * A static instance of the Factory used to parse documents in this WebRowSet implementation
     */
    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();
    
    private URL url;
    
    private Runnable completionRunnable;
    private Runnable metadataCompleteRunnable;
    
    /**
     * Static initialization code for the FACTORY. Currently setting validating to false.
     */
    static {
        FACTORY.setValidating(false);
    }
    
    /** Creates a new instance of WebRowSetDataProvider */
    public WebRowSetDataProvider() {
    }
    
    protected SaveTask createSaveTask(DataTable[] tables) {
        return new SaveTask(tables) {
            protected void saveData(DataTable[] tables) throws Exception {}
        };
    }
    
    final class RowLoadItem {
        public Object[] values;
        public DataRow.DataRowStatus status;
    }
    
    
    protected LoadTask createLoadTask(DataTable[] tables) {
        return new LoadTask(tables) {
            
            class WebRowSetXMLHandler extends DefaultHandler implements LexicalHandler {
                
                DataTable table;
                /**
                 * List to keep track of rows that need to be loaded
                 */
                private List dataToLoad = new LinkedList();
                /**
                 * Stack to keep track of which tags have been seen
                 */
                private Stack<String> tagStack = new Stack<String>();
                /**
                 * Set to true if we have seen an opening "properties" tag, but not a closing.
                 * inProperties, inMetaData, and inData are mutually exclusive
                 */
                private boolean inProperties = false;
                /**
                 * Set to true if we have seen an opening "metadata" tag, but not a closing.
                 * inProperties, inMetaData, and inData are mutually exclusive
                 */
                private boolean inMetaData = false;
                /**
                 * Set to true if we have seen an opening "data" tag, but not a closing.
                 * inProperties, inMetaData, and inData are mutually exclusive
                 */
                private boolean inData = false;
                /**
                 * The meta data object being updated. This metaData object is modified until the
                 * closing "metadata" tag is found. Then the ShopLogicWebRowSet's meta data is updated
                 * in one fell stroke
                 */
                private RowSetMetaData metaData = null;
                /**
                 * Keeps track of which column we are currently dealing with. This is used both by the
                 * metadata parsing code, and the data parsing code
                 */
                private int columnIndex = 1;
                /**
                 * This is a stack of keyColumns parsed. After being parsed and added to this stack, they are
                 * added to an int[] and set in the WebRowSet.
                 */
                private Stack<Integer> keyColumnsStack = new Stack<Integer>();
                /**
                 * Keeps track of the "type" value while parsing the type map.
                 */
                private String mapType;
                /**
                 * Keeps track of the "class" value while parsing the type map.
                 */
                private String mapClass;
                /**
                 * This is the map of types, as parsed from the file. After the closing "map" tag is found, the
                 * WebRowSet is updated.
                 */
                private Map<String,Class<?>> typeMap = new HashMap<String,Class<?>>();
                /**
                 * Contains the data value parsed from the <code>characters</code> method. This is simply a string.
                 * The <code>setValue</code> method is responsible for parsing this into an int, bool, etc.
                 */
                private String data;
                /**
                 * Keeps track of whether the last tag read was null.
                 */
                private boolean wasNull;
                /**
                 * This String is used while in the data portion of parsing. It is used as the first part of they
                 * key in the cache for a row.
                 */
                private String tableName = "";
                /**
                 * This list maintains the rowValues as parsed from the file. After they are all parsed,
                 * they are passed to the row.
                 */
                private Object[] rowValues;
                /**
                 * The index of the current row being processed. This is 1 based
                 */
                private int currentRow = 0;
                
                
                public WebRowSetXMLHandler(DataTable table) {
                    this.table = table;
                }
                
                public void comment(char[] ch, int start, int length)
                throws SAXException {
                    
                }
                
                public void startCDATA()
                throws SAXException {
                }
                
                public void endCDATA()  throws SAXException {
                }
                
                public void startEntity(String name)
                throws SAXException {
                }
                
                public void endEntity(String name)
                throws SAXException {
                }
                
                public void startDTD(
                        String name, String publicId, String systemId)
                        throws SAXException {
                }
                
                public void endDTD()
                throws SAXException {
                }
                
                /**
                 * Updates the <code>data</code> variable with the given characters using the default character set
                 * @inheritDoc
                 */
                public void characters(char[] ch, int start, int length) throws SAXException {
                    //Remember that characters may be called multiple times for the same
                    //element, so if data is null then the characters delivered here are
                    //the data, otherwise append the characters to data
                    if (data == null) {
                        data = new String(ch, start, length);
                    } else {
                        data = data + new String(ch, start, length);
                    }
                    data = data.trim();
                }
                
                /**
                 * Helper method that updates the current row at the given column with the given value.
                 * This code actually determines the column type for the designated column, and uses that
                 * information to transform the value into the proper data type. Data MAY be null
                 * @param columnIndex
                 * @param value
                 * @throws Exception
                 */
                private void setValue(int columnIndex, String value) throws Exception {
                    //TODO Take into account the possiblity of a collision, and notify listeners if necessary
                    if (wasNull || value == null) {
                        rowValues[columnIndex - 1] = null;
                    } else {
                        switch (metaData.getColumnType(columnIndex)) {
                            case Types.TINYINT:
                                rowValues[columnIndex - 1] = Byte.valueOf(value.trim());
                                break;
                            case Types.SMALLINT:
                                rowValues[columnIndex - 1] = Short.valueOf(value.trim());
                                break;
                            case Types.INTEGER:
                                rowValues[columnIndex - 1] = Integer.valueOf(value.trim());
                                break;
                            case Types.BIGINT:
                                rowValues[columnIndex - 1] = Long.valueOf(value.trim());
                                break;
                            case Types.REAL:
                                rowValues[columnIndex - 1] = Float.valueOf(value.trim());
                                break;
                            case Types.FLOAT:
                            case Types.DOUBLE:
                                rowValues[columnIndex - 1] = Double.valueOf(value.trim());
                                break;
                            case Types.DECIMAL:
                            case Types.NUMERIC:
                                rowValues[columnIndex - 1] = new BigDecimal(value.trim());
                                break;
                            case Types.BOOLEAN:
                            case Types.BIT:
                                rowValues[columnIndex - 1] = Boolean.valueOf(value.trim());
                                break;
                            case Types.CHAR:
                            case Types.VARCHAR:
                            case Types.LONGVARCHAR:
                                rowValues[columnIndex - 1] = value;
                                break;
                            case Types.VARBINARY:
                            case Types.LONGVARBINARY:
                            case Types.BINARY:
                                byte[] bytes = Base64.decode(value);
                                rowValues[columnIndex - 1] = bytes;
                                break;
                            case Types.DATE:
                            case Types.TIME:
                            case Types.TIMESTAMP:
                                rowValues[columnIndex - 1] = new Timestamp(Long.parseLong(value.trim()));
                                break;
                            case Types.ARRAY:
                            case Types.BLOB:
                            case Types.CLOB:
                            case Types.DATALINK:
                            case Types.DISTINCT:
                            case Types.JAVA_OBJECT:
                            case Types.OTHER:
                            case Types.REF:
                            case Types.STRUCT:
                                //what to do with this?
                                break;
                            default :
                                //do nothing
                        }
                    }
                }
                
                /**
                 * @inheritDoc
                 */
                public void endDocument() throws SAXException {
                    doLoad(table, dataToLoad);
                    if (completionRunnable != null) {
                        SwingUtilities.invokeLater(completionRunnable);
                    }
                }
                
                /**
                 * @inheritDoc
                 */
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (tagStack.size() == 0) {
                        return;
                    }
                    //get the last tag seen
                    String tag = tagStack.pop();
                    //handle each tag. Handle the data tags first since those will be called most frequently
                    try {
                        if (tag.equals("null")) {
                            wasNull = true;
                        } else if (tag.equals("columnvalue")) {
                            //set the current column value
                            setValue(columnIndex++, wasNull ? null : data);
                        } else if (tag.equals("updatevalue")) {
                            //set the update column value (the column to
                            //apply the update to is the previous
                            //columnIndex)
                            setValue(columnIndex-1, wasNull ? null : data);
                        } else if (tag.equals("currentrow")) {
                            RowLoadItem loadItem = new RowLoadItem();
                            loadItem.values = new Object[rowValues.length];
                            System.arraycopy(rowValues, 0, loadItem.values, 0, rowValues.length);
                            loadItem.status = DataRow.DataRowStatus.UNCHANGED;
                            dataToLoad.add(loadItem);
                        } else if (tag.equals("insertrow")) {
                            RowLoadItem loadItem = new RowLoadItem();
                            loadItem.values = new Object[rowValues.length];
                            System.arraycopy(rowValues, 0, loadItem.values, 0, rowValues.length);
                            loadItem.status = DataRow.DataRowStatus.INSERTED;
                        } else if (tag.equals("deleterow")) {
                            RowLoadItem loadItem = new RowLoadItem();
                            loadItem.values = new Object[rowValues.length];
                            System.arraycopy(rowValues, 0, loadItem.values, 0, rowValues.length);
                            loadItem.status = DataRow.DataRowStatus.DELETED;
                        } else if (tag.equals("modifyrow")) {
                            RowLoadItem loadItem = new RowLoadItem();
                            loadItem.values = new Object[rowValues.length];
                            System.arraycopy(rowValues, 0, loadItem.values, 0, rowValues.length);
                            loadItem.status = DataRow.DataRowStatus.UPDATED;
                        }  else if (tag.equals("column")) {
                            //in key-columns
                            if (!wasNull) {
                                keyColumnsStack.push(new Integer(data.trim()));
                            }
                        } else if (tag.equals("type")) {
                            mapType = wasNull ? null : data;
                        } else if (tag.equals("class")) {
                            mapClass = wasNull ? null : data;
                            //add the type and class to the typeMap
                            typeMap.put(mapType, mapClass == null ? null : Class.forName(mapClass));
                        } else if (tag.equals("table-name")) {
                            if (inProperties) {
//                                        setTableName(wasNull ? null : data);
                            } else if (inMetaData) {
                                metaData.setTableName(columnIndex, wasNull ? null : data);
                            }
                        } else if (tag.equals("column-count")) {
                            metaData.setColumnCount(wasNull ? 0 : Integer.parseInt(data.trim()));
                        } else if (tag.equals("column-index")) {
                            columnIndex = Integer.parseInt(data.trim());
                        } else if (tag.equals("key-columns")) {
                            int[] kc = new int[keyColumnsStack.size()];
                            int i = 0;
                            for (Integer col : keyColumnsStack) {
                                kc[i++] = col.intValue();
                            }
                            try {
//                                        setKeyColumns(kc);
                            } catch (Exception e) {
                                e.printStackTrace();
//                                        LOG.error("Failed to set the key columns, even after parsing. Columns were " + keyColumnsStack, e);
                            }
                        } else if (tag.equals("map")) {
                            //if the tag was a map tag, then put the map
                            // elements into the map
//                                    setTypeMap(typeMap);
                        } else if (tag.equals("auto-increment")) {
                            metaData.setAutoIncrement(columnIndex, wasNull ? false : Boolean.parseBoolean(data));
                        } else if (tag.equals("case-sensitive")) {
                            metaData.setCaseSensitive(columnIndex, wasNull ? false : Boolean.parseBoolean(data));
                        } else if (tag.equals("currency")) {
                            metaData.setCurrency(columnIndex, wasNull ? false : Boolean.parseBoolean(data.trim()));
                        } else if (tag.equals("nullable")) {
                            metaData.setNullable(columnIndex, wasNull ? 0 : Integer.parseInt(data.trim()));
                        } else if (tag.equals("signed")) {
                            metaData.setSigned(columnIndex, wasNull ? false : Boolean.parseBoolean(data.trim()));
                        } else if (tag.equals("searchable")) {
                            metaData.setSearchable(columnIndex, wasNull ? false : Boolean.parseBoolean(data.trim()));
                        } else if (tag.equals("column-display-size")) {
                            metaData.setColumnDisplaySize(columnIndex, wasNull ? 255 : Integer.parseInt(data.trim()));
                        } else if (tag.equals("column-label")) {
                            metaData.setColumnLabel(columnIndex, wasNull ? null : data);
                        } else if (tag.equals("column-name")) {
                            metaData.setColumnName(columnIndex, data);
                        } else if (tag.equals("schema-name")) {
                            metaData.setSchemaName(columnIndex, wasNull ? null : data);
                        } else if (tag.equals("column-precision")) {
                            metaData.setPrecision(columnIndex, wasNull ? 0 : Integer.parseInt(data.trim()));
                        } else if (tag.equals("column-scale")) {
                            metaData.setScale(columnIndex, wasNull ? 0 : Integer.parseInt(data.trim()));
                        } else if (tag.equals("catalog-name")) {
                            metaData.setCatalogName(columnIndex, wasNull ? null : data);
                        } else if (tag.equals("column-type")) {
                            metaData.setColumnType(columnIndex, wasNull ? Types.VARCHAR : Integer.parseInt(data.trim()));
                        } else if (tag.equals("column-type-name")) {
                            metaData.setColumnTypeName(columnIndex, wasNull ? null : data);
                        } else if (tag.equals("metadata")) {
                            //drop all of the columns in the data table
                            //and rebuild them based on this meta-data
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run() {
                                    table.clear();
                                    DataColumn[] cols = table.getColumns().toArray(new DataColumn[table.getColumns().size()]);
                                    for (DataColumn col : cols) {
                                        table.dropColumn(col.getName());
                                    }
                                    
                                    //time to rebuild
                                    try {
                                        for (int i=0; i<metaData.getColumnCount(); i++) {
                                            DataColumn col = table.createColumn(metaData.getColumnName(i+1));
                                            switch (metaData.getColumnType(i+1)) {
                                                case Types.TINYINT:
                                                    col.setType(Byte.class);
                                                    break;
                                                case Types.SMALLINT:
                                                    col.setType(Short.class);
                                                    break;
                                                case Types.INTEGER:
                                                    col.setType(Integer.class);
                                                    break;
                                                case Types.BIGINT:
                                                    col.setType(BigInteger.class);
                                                    break;
                                                case Types.REAL:
                                                    col.setType(Float.class);
                                                    break;
                                                case Types.FLOAT:
                                                case Types.DOUBLE:
                                                    col.setType(Double.class);
                                                    break;
                                                case Types.DECIMAL:
                                                case Types.NUMERIC:
                                                    col.setType(BigDecimal.class);
                                                    break;
                                                case Types.BOOLEAN:
                                                case Types.BIT:
                                                    col.setType(Boolean.class);
                                                    break;
                                                case Types.CHAR:
                                                case Types.VARCHAR:
                                                case Types.LONGVARCHAR:
                                                    col.setType(String.class);
                                                    break;
                                                case Types.VARBINARY:
                                                case Types.LONGVARBINARY:
                                                case Types.BINARY:
                                                    col.setType(Byte[].class);
                                                    break;
                                                case Types.DATE:
                                                case Types.TIME:
                                                case Types.TIMESTAMP:
                                                    col.setType(Timestamp.class);
                                                    break;
                                                case Types.ARRAY:
                                                case Types.BLOB:
                                                case Types.CLOB:
                                                case Types.DATALINK:
                                                case Types.DISTINCT:
                                                case Types.JAVA_OBJECT:
                                                case Types.OTHER:
                                                case Types.REF:
                                                case Types.STRUCT:
                                                    //what to do with this?
                                                    break;
                                                default :
                                                    //do nothing
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    // TODO: new API
                                    // table.fireDataTableChanged(new TableChangeEvent(table));
                                    
                                }
                            });
                            if (metadataCompleteRunnable != null) {
                                SwingUtilities.invokeLater(metadataCompleteRunnable);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                                LOG.error("Failed to parse something", e);
                    }
                    data = null;
                    if (dataToLoad.size() >= 100) {
                        doLoad(table, dataToLoad);
                        dataToLoad = new LinkedList<RowLoadItem>();
                    }
                }
                
                /**
                 * @inheritDoc
                 */
                public void startDocument() throws SAXException {
//                            LOG.debug("Start of Document");
                }
                
                public void startElement(String uri, String localName, String qName, Attributes attributes)	throws SAXException {
                    //reset the wasNull flag
                    wasNull = false;
                    //push the element name onto the stack. In this way, when
                    // the characters method is called, I know what kind of data
                    //I'm dealing with.
                    String tag = qName.toLowerCase();
                    tagStack.push(tag);
                    try {
                        if (tag.equals("properties")) {
                            inProperties = true;
                            inMetaData = false;
                            inData = false;
                        } else if (tag.equals("metadata")) {
                            inProperties = false;
                            inMetaData = true;
                            inData = false;
                            metaData = new RowSetMetaDataImpl();
                        } else if (tag.equals("data")) {
                            inProperties = false;
                            inMetaData = false;
                            inData = true;
                            //construct the rowValues array
                            rowValues = new Object[metaData.getColumnCount()];
                        } else if (tag.equals("currentrow")) {
                            columnIndex = 1;
                            reinitRowValues();
                            currentRow++;
                        } else if (tag.equals("insertrow")) {
                            columnIndex = 1;
                            reinitRowValues();
                            currentRow++;
                        } else if (tag.equals("deleterow")) {
                            columnIndex = 1;
                            reinitRowValues();
                            currentRow++;
                        } else if (tag.equals("modifyrow")) {
                            columnIndex = 1;
                            reinitRowValues();
                            currentRow++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                /**
                 * Utility method that clears out the rowValues array
                 */
                private void reinitRowValues() {
                    for (int i=0; i<rowValues.length; i++) {
                        rowValues[i] = null;
                    }
                }
            }
            
            protected void loadData(LoadTask.LoadItem[] items) {
                for (LoadItem item : items) {
                    DataTable table = item.table;
                    List<RowLoadItem> loadItems = (List<RowLoadItem>)item.data;
                    for (RowLoadItem loadItem : loadItems) {
                        
                        DataRow row = table.appendRowNoEvent();
                        List<DataColumn> cols = table.getColumns();
                        for (int i=0; i<loadItem.values.length; i++) {
                            try {
                                row.setValue(cols.get(i).getName(), loadItem.values[i]);
                            } catch (Exception e) {
                                System.out.println(loadItem.values.length);
                            }
                        }
                        row.setStatus(loadItem.status);
                        item.table.fireDataTableChanged(TableChangeEvent.newRowAddedEvent(item.table, row));
                    }
                    item.table.fireDataTableChanged(TableChangeEvent.newLoadCompleteEvent(item.table));
                }
            }
            
            protected void readData(DataTable[] tables) throws Exception {
                //get the XML from the url
                try {
                    for (int i=0; i<tables.length; i++) {
                        InputStream is = url.openStream();
                        readXml(new InputStreamReader(is), tables[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            public void doLoad(DataTable table, List items) {
                super.scheduleLoad(new LoadTask.LoadItem(table, items));
            }
            
            /**
             * Originally this method was implemented with a PullParser. However, the
             * PullParser did not have very good error messages, so debugging the XML
             * was murder. A normal SAX parser is being used at this time.
             *
             * @inheritDoc
             */
            public void readXml(Reader reader, final DataTable table) throws SQLException {
                try {
                    /*
                     * Parsing is done via SAX. The basic methodology is to keep a stack of tags seen.
                     * Also, a flag is kept to keep track of whether or not a <null/> tag has been seen.
                     * This flag is reset to false whenever a new tag is started, but is set to true when
                     * the end of the <null> tag is found.
                     * Also, A single variable contains the results of the character data for the current tag.
                     * This variable is read after the end tag is found. You will find that all work is
                     * done after the entire element has been read (after the end tag).
                     */
                    
                    WebRowSetXMLHandler handler = new WebRowSetXMLHandler(table);
                    SAXParser parser = FACTORY.newSAXParser();
                    XMLReader xmlReader = parser.getXMLReader();
                    xmlReader.setProperty(
                            "http://xml.org/sax/properties/lexical-handler",
                            handler
                            );
                    
                    parser.parse(new InputSource(reader), handler );
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("Error while parsing a the xml document for a WebRowSetDataProvider");
                }
            }
        };
        
        
    }
/*
    public static void main(String... args) {
        DataSet ds = new DataSet();
        DataTable dt = ds.createTable();
        dt.setName("Rowset");
        DataProvider dp = new WebRowSetDataProvider();
        dt.setDataProvider(dp);
        JFrame frame = new JFrame();
    //   JTable table = new JTable();
 //
        //Binding b = BindingFactory.bind(table, dt);
 
        TabularDataModelAdapter adapter = new TabularDataModelAdapter(dt);
 
 
     //   JNTable table = new JNTable();
   //     new DirectTableBinding(table.getTable(),adapter);
 
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setVisible(true);
        dt.load();
//        dt.loadAndWait();
//        System.out.println(dt.getRows().size());
//        List<DataColumn> cols = dt.getColumns();
//        for (DataColumn col : cols) {
//            System.out.println(col.getName());
//        }
    }
 
 */
    
    public URL getUrl() {
        return url;
    }
    
    public void setUrl(URL url) {
        this.url = url;
    }
    
    public Runnable getCompletionRunnable() {
        return completionRunnable;
    }
    
    public void setCompletionRunnable(Runnable completionRunnable) {
        this.completionRunnable = completionRunnable;
    }
    
    public Runnable getMetadataCompleteRunnable() {
        return metadataCompleteRunnable;
    }
    
    public void setMetadataCompleteRunnable(Runnable metadataCompleteRunnable) {
        this.metadataCompleteRunnable = metadataCompleteRunnable;
    }
    
    
    
}
