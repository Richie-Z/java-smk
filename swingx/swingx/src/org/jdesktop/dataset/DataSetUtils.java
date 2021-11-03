/*
 * $Id: DataSetUtils.java,v 1.8 2005/10/10 17:00:59 rbair Exp $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdesktop.dataset.provider.sql.JDBCDataConnection;
import org.jdesktop.dataset.provider.sql.SQLCommand;
import org.jdesktop.dataset.provider.sql.SQLDataProvider;
import org.jdesktop.dataset.provider.sql.TableCommand;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author rbair
 */
public class DataSetUtils {
    
    /** Creates a new instance of DataSetUtils */
    private DataSetUtils() {
    }
    
    /**
     * Checks to see if the given name is valid. If not, then return false.<br>
     * A valid name is one that follows the Java naming rules for
     * indentifiers, <b>except</b> that Java reserved words can
     * be used, and the name may begin with a number.
     */
    static boolean isValidName(String name) {
        return !name.matches(".*[\\s]");
    }
    
    public static String getXmlSchema(DataSet ds) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<?xml version=\"1.0\" standalone=\"yes\" ?>\n");
        buffer.append("<xs:schema id=\"");
        buffer.append(ds.getName());
        buffer.append("\" targetNamespace=\"http://jdesktop.org/tempuri/");
        buffer.append(ds.getName());
        buffer.append(".xsd\" xmlns=\"http://javadesktop.org/tempuri/");
        buffer.append(ds.getName());
        buffer.append(".xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" attributeFormDefault=\"qualified\" elementFormDefault=\"qualified\">\n");
        buffer.append("\t<xs:element name=\"");
        buffer.append(ds.getName());
        buffer.append("\">\n");
        buffer.append("\t\t<xs:complexType>\n");
        buffer.append("\t\t\t<xs:choice maxOccurs=\"unbounded\">\n");
        for (DataTable table : ds.getTables()) {
            if (!(table instanceof DataRelationTable)) {
                buffer.append("\t\t\t\t<xs:element name=\"");
                buffer.append(table.getName());
                buffer.append("\" appendRowSupported=\"");
                buffer.append(table.isAppendRowSupported());
                buffer.append("\" deleteRowSupported=\"");
                buffer.append(table.isDeleteRowSupported());
                buffer.append("\">\n");
                buffer.append("\t\t\t\t\t<xs:complexType>\n");
                buffer.append("\t\t\t\t\t\t<xs:sequence>\n");
                for (DataColumn col : table.getColumns()) {
                    buffer.append("\t\t\t\t\t\t\t<xs:element name=\"");
                    buffer.append(col.getName());
                    buffer.append("\" type=\"");
                    if (col.getType() == String.class || col.getType() == Character.class) {
                        buffer.append("xs:string");
                    } else if (col.getType() == BigDecimal.class) {
                        buffer.append("xs:decimal");
                    } else if (col.getType() == Integer.class) {
                        buffer.append("xs:integer");
                    } else if (col.getType() == Boolean.class) {
                        buffer.append("xs:boolean");
                    } else if (col.getType() == Date.class) {
                        buffer.append("xs:dateTime");
                    } else if (col.getType() == Byte.class) {
                        buffer.append("xs:unsignedByte");
                    } else {
                        System.out.println("Couldn't find type for xsd for Class " + col.getType());
                    }
                    if (col.getDefaultValue() != null) {
                        buffer.append("\" default=\"");
                        buffer.append(col.getDefaultValue());
                    }
                    if (!col.isRequired()) {
                        buffer.append("\" minOccurs=\"0");
                    }
                    buffer.append("\" keyColumn=\"");
                    buffer.append(col.isKeyColumn());
                    buffer.append("\" readOnly=\"");
                    buffer.append(col.isReadOnly());
                    if (col.getExpression() != null && !(col.getExpression().trim().equals(""))) {
                        buffer.append("\" expression=\"");
                        buffer.append(col.getExpression());
                    }
                    buffer.append("\" />\n");
                }
                buffer.append("\t\t\t\t\t\t</xs:sequence>\n");
                buffer.append("\t\t\t\t\t</xs:complexType>\n");
                buffer.append("\t\t\t\t</xs:element>\n");
            }
        }
        buffer.append("\t\t\t</xs:choice>\n");
        buffer.append("\t\t</xs:complexType>\n");

        buffer.append("\t\t<xs:annotation>\n");
        buffer.append("\t\t\t<xs:appinfo>\n");
        
        //write the relations out
        for (DataRelation r : ds.getRelations()) {
            buffer.append("\t\t\t\t<dataRelation name=\"");
            buffer.append(r.getName());
            buffer.append("\" parentColumn=\"");
            DataColumn col = r.getParentColumn();
            if (col != null) {
                buffer.append(col.getTable().getName());
                buffer.append(".");
                buffer.append(col.getName());
            }
            buffer.append("\" childColumn=\"");
            col = r.getChildColumn();
            if (col != null) {
                buffer.append(col.getTable().getName());
                buffer.append(".");
                buffer.append(col.getName());
            }
            buffer.append("\" />\n");
        }

        //write the data relation tables out
        for (DataTable table : ds.getTables()) {
            if (table instanceof DataRelationTable) {
                DataRelationTable drt = (DataRelationTable)table;
                buffer.append("\t\t\t\t<dataRelationTable name=\"");
                buffer.append(drt.getName());
                buffer.append("\" relation=\"");
                DataRelation dr = drt.getRelation();
                buffer.append(dr == null ? "" : dr.getName());
                buffer.append("\" parentSelector=\"");
                DataSelector sel = drt.getParentSelector();
                buffer.append(sel == null ? "" : sel.getName());
                buffer.append("\" parentTable=\"");
                DataTable parent = drt.getParentTable();
                buffer.append(parent == null ? "" : parent.getName());
                buffer.append("\" />\n");
            }
        }
        
        //write the data values out
        for (DataValue value : ds.getValues()) {
            buffer.append("\t\t\t\t<dataValue name=\"");
            buffer.append(value.getName());
            buffer.append("\" expression=\"");
            if (value.getExpression() != null) {
                buffer.append(value.getExpression());
            }
            buffer.append("\" />\n");
        }
        
        //close the annotation section
        buffer.append("\t\t\t</xs:appinfo>\n");
        buffer.append("\t\t</xs:annotation>\n");

        //close the document down
        buffer.append("\t</xs:element>\n");
        buffer.append("</xs:schema>\n");
	
        return buffer.toString();
    }
    
    public static DataSet createFromXmlSchema(String schema) {
        return createFromXmlSchema(new StringReader(schema));
    }
    
    public static DataSet createFromXmlSchema(File f) throws FileNotFoundException {
        return createFromXmlSchema(new FileInputStream(f));
    }
    
    public static DataSet createFromXmlSchema(InputStream is) {
        return createFromXmlSchema(new InputStreamReader(is));
    }
    
    public static DataSet createFromXmlSchema(Reader schema) {
        DataSet ds = new DataSet();
        //set up an XML parser to parse the schema
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource is = new InputSource(schema);
            parser.parse(is, new DataSetParser(ds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
    
    /**
     * parses the document.
     *
     * The xs:element tag is used repeatedly. With a depth of 0, it is the
     * DataSet element. With a depth of 1, it is a DataTable, and with a depth of
     * 2 it is a DataColumn
     */
    private static final class DataSetParser extends DefaultHandler {
        public int elementDepth = 0;
        private Attributes attrs;
        private DataSet ds;
        private DataTable table;
        private DataColumn column;
        private DataProvider dataProvider;
        
        public DataSetParser(DataSet ds) {
            this.ds = ds == null ? new DataSet() : ds;
        }
        
        public DataSet getDataSet() {
            return ds;
        }
        
        public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes atts) throws org.xml.sax.SAXException {
            this.attrs = atts;
            if (qName.equals("xs:element")) {
                elementDepth++;
                switch(elementDepth) {
                    case 1:
                        //this is a DataSet
                        ds.setName(attrs.getValue("name"));
                        break;
                    case 2:
                        //this is a table tag
                        table = ds.createTable(attrs.getValue("name"));
                        String val = attrs.getValue("appendRowSupported");
                        table.setAppendRowSupported(val == null || val.equalsIgnoreCase("true"));
                        val = attrs.getValue("deleteRowSupported");
                        table.setDeleteRowSupported(val == null || val.equalsIgnoreCase("true"));
                        break;
                    case 3:
                        //this is a column tag
                        column = table.createColumn(attrs.getValue("name"));
                        //set the required flag
                        val = attrs.getValue("minOccurs");
                        if (val != null && val.equals("")) {
                            column.setRequired(true);
                        }

                        //find out if this is a keycolumn
                        val = attrs.getValue("keyColumn");
                        column.setKeyColumn(val == null ? false : val.equalsIgnoreCase("true"));

                        //find out if this column is readOnly
                        val = attrs.getValue("readOnly");
                        column.setReadOnly(val == null ? false : val.equalsIgnoreCase("true"));

                        //grab the default, if one is supplied
                        String defaultValue = attrs.getValue("default"); //TODO This will require some kind of type conversion

                        //get the type. Convert from XSD types to java types
                        val = attrs.getValue("type");
                        if (val.equals("xs:string")) {
                            column.setType(String.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(defaultValue);
                            }
                        } else if (val.equals("xs:decimal")) {
                            column.setType(BigDecimal.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(new BigDecimal(defaultValue));
                            }
                        } else if (val.equals("xs:integer") || val.equals("xs:int")) {
                            column.setType(Integer.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(new Integer(defaultValue));
                            }
                        } else if (val.equals("xs:boolean")) {
                            column.setType(Boolean.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(Boolean.parseBoolean(defaultValue));
                            }
                        } else if (val.equals("xs:date") || val.equals("xs:time") || val.equals("xs.dateTime")) {
                            column.setType(Date.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(new Date(Date.parse(defaultValue)));
                            }
                        } else if (val.equals("xs:unsignedByte")) {
                            column.setType(Byte.class);
                            if (defaultValue != null && !defaultValue.equals("")) {
                                column.setDefaultValue(new Byte(defaultValue));
                            }
                        } else {
                            System.err.println("unexpected classType: '"  + val + "'");
                        }

                        //set the column expression
                        val = attrs.getValue("expression");
                        if (val != null && !("".equals(val))) {
                            column.setExpression(val);
                        }
                        break;
                    default:
                        //error condition
                        System.out.println("Error in DataSetParser");
                }
            } else if (qName.equals("dataProvider")) {
                String classType = attrs.getValue("class");
                if (classType != null) {
                    try {
                        dataProvider = (DataProvider)Class.forName(classType).newInstance();
                        table.setDataProvider(dataProvider);
                        //TODO There needs to be a more general configuration solution
                        if (dataProvider instanceof SQLDataProvider) {
                            String tableName = attrs.getValue("tableName");
                            if (tableName != null && !tableName.equals("")) {
                                TableCommand cmd = new TableCommand(tableName);
                                cmd.setWhereClause(attrs.getValue("whereClause"));
                                cmd.setOrderByClause(attrs.getValue("orderByClause"));
                                cmd.setHavingClause(attrs.getValue("havingClause"));
                                dataProvider.setCommand(cmd);
                            } else {
                                SQLCommand command = new SQLCommand();
                                command.setCustom(true);
                                command.setSelectSQL(attrs.getValue("select"));
                                command.setInsertSQL(attrs.getValue("insert"));
                                command.setUpdateSQL(attrs.getValue("update"));
                                command.setDeleteSQL(attrs.getValue("delete"));
                                dataProvider.setCommand(command);
                            }
                        }
                    } catch (Exception e) {
                        //hmmm
                        e.printStackTrace();
                    }
                }
            } else if (qName.equals("dataRelationTable")) {
                DataRelationTable drt = ds.createRelationTable(attrs.getValue("name"));
                drt.setRelation((DataRelation)ds.getElement(attrs.getValue("relation")));
                drt.setParentSelector((DataSelector)ds.getElement(attrs.getValue("parentSelector")));
                drt.setParentTable((DataTable)ds.getElement(attrs.getValue("parentTable")));
            } else if (qName.equals("dataRelation")) {
                DataRelation relation = ds.createRelation(attrs.getValue("name"));
                relation.setParentColumn((DataColumn)ds.getElement(attrs.getValue("parentColumn")));
                relation.setChildColumn((DataColumn)ds.getElement(attrs.getValue("childColumn")));
            } else if (qName.equals("dataValue")) {
                DataValue value = ds.createValue(attrs.getValue("name"));
                value.setExpression(attrs.getValue("expression"));
            }
        }

        public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
            if (qName.equals("xs:element")) {
                switch(elementDepth) {
                    case 1:
                        //this is a dataset tag
                        break;
                    case 2:
                        //this is a table tag
                        break;
                    case 3:
                        //this is a column tag
                        break;
                    default:
                        //error condition
                        System.out.println("Error in DataSetParser");
                }
                elementDepth--;
            }
        }

        public void endDocument() throws org.xml.sax.SAXException {
        }
    }

    public static DataSet createFromDatabaseSchema(JDBCDataConnection conn, String databaseName, String... names) {
        java.util.Set<String> set = new java.util.HashSet<String>();
        for (String name : names) {
            set.add(name);
        }
        DataSet ds = createFromDatabaseSchema(conn.getConnection(), databaseName, set);
        for (DataTable table : ds.getTables()) {
            if (!(table instanceof DataRelationTable) && table.getDataProvider() != null) {
                table.getDataProvider().setConnection(conn);
            }
        }
        return ds;
    }
    
    /**
     * Creates a DataSet based on some DatabaseMetaData information. This method
     * takes a Set of database entity &quot;names&quot; which are of the form:<br>
     * table or table.column
     */
    public static DataSet createFromDatabaseSchema(java.sql.Connection conn, String databaseName, java.util.Set<String> names) {
        DataSet ds = new DataSet();
        try {
            java.util.Set<String> tableNames = new java.util.HashSet<String>();
            for (String name : names) {
                if (name.contains(".")) {
                    tableNames.add(name.substring(0, name.indexOf(".")));
                } else {
                    tableNames.add(name);
                }
            }

            java.sql.DatabaseMetaData md = conn.getMetaData();
//            printResultSet(md.getCatalogs());
//            printResultSet(md.getSchemas());
//            printResultSet(md.getTables(databaseName, null, "package", null));
            for (String tableName : tableNames) {
                DataTable table = ds.createTable(tableName);
                //check to see whether the entire table is to be included, or only some of the table
                if (names.contains(tableName)) {
                    //import all of the table columns
                    System.out.println("Including all columns");
                    java.sql.ResultSet rs = md.getColumns(databaseName, null, tableName, null);
                    while (rs.next()) {
                        DataColumn col = table.createColumn(rs.getString(4));
                        col.setDefaultValue(rs.getObject(13));
//                        col.setKeyColumn(value);
                        col.setReadOnly(false);
                        col.setRequired(rs.getString(18).equals("NO"));
                        col.setType(getType(rs.getInt(5)));
                    }
                } else {
                    //import the key columns, and the specified columns
                    //first, create a set of columns for this table, including
                    //the key column names
                    java.util.Set<String> colNames = new java.util.HashSet<String>();
                    for (String name : names) {
                        if (name.startsWith(tableName) && name.contains(".")) {
                            String colName = name.substring(name.indexOf(".") +1);
                            colNames.add(colName);
                        }
                    }
                    java.sql.ResultSet rs = md.getPrimaryKeys(databaseName, null, tableName);
                    while (rs.next()) {
                        colNames.add(rs.getString(4));
                    }
                
                    //now, get the column information for these columns
                    System.out.println("Including columns: " + colNames);
                    for (String colName : colNames) {
                        rs = md.getColumns(databaseName, null, tableName, colName);
                        while (rs.next()) {
                            DataColumn col = table.createColumn(rs.getString(4));
                            col.setDefaultValue(rs.getObject(13));
//                            col.setKeyColumn(value);
                            col.setReadOnly(false);
                            col.setRequired(rs.getString(18).equals("NO"));
                            col.setType(getType(rs.getInt(5)));
                        }
                    }
                    
//                md.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
//                printResultSet(md.getExportedKeys(databaseName, null, tableName));
//                printResultSet(md.getImportedKeys(databaseName, null, tableName));
//                md.getTables(catalog, schemaPattern, tableNamePattern, types);
                }
                
                //create the DataProvider
                org.jdesktop.dataset.provider.sql.SQLDataProvider dp = new org.jdesktop.dataset.provider.sql.SQLDataProvider(tableName);
                table.setDataProvider(dp);
            }
            
            //now that all of the tables are created, create the relations
            for (String tableName : tableNames) {
                java.sql.ResultSet rs = md.getImportedKeys(databaseName, null, tableName);
//                System.out.println(tableName + ":");
//                printResultSet(rs);
                while (rs.next()) {
                    //TODO for now, DataRelations only support a single column,
                    //so if the sequence is > 1, skip it
                    if (rs.getInt(9) > 1) {
                        continue;
                    }
                    String childTableName = rs.getString(7);
                    String childColName = rs.getString(8);
                    DataTable parentTable = ds.getTable(tableName);
                    DataColumn parentColumn = parentTable.getColumn(rs.getString(4));
                    DataTable childTable = ds.getTable(childTableName);
                    DataColumn childColumn = childTable.getColumn(childColName);
                    if (parentColumn != null && childColumn != null && parentColumn != childColumn) {
                        DataRelation rel = ds.createRelation(rs.getString(12));
                        rel.setParentColumn(parentColumn);
                        rel.setChildColumn(childColumn);
                    } else if (parentColumn == childColumn) {
                        System.out.println("column identity: " + childTableName + "." + childColName + " = " + parentTable.getName() + "." + parentColumn.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ds;
    }
    
    private static Class getType(int type) {
        switch (type) {
            case java.sql.Types.ARRAY:
                return Object.class;
            case java.sql.Types.BIGINT:
                return java.math.BigInteger.class;
            case java.sql.Types.BINARY:
                return Boolean.class;
            case java.sql.Types.BIT:
                return Boolean.class;
            case java.sql.Types.BLOB:
                return byte[].class;
            case java.sql.Types.BOOLEAN:
                return Boolean.class;
            case java.sql.Types.CHAR:
                return Character.class;
            case java.sql.Types.CLOB:
                return char[].class;
            case java.sql.Types.DATALINK:
                return Object.class;
            case java.sql.Types.DATE:
                return java.util.Date.class;
            case java.sql.Types.DECIMAL:
                return java.math.BigDecimal.class;
            case java.sql.Types.DISTINCT:
                return Object.class;
            case java.sql.Types.DOUBLE:
                return BigDecimal.class;
            case java.sql.Types.FLOAT:
                return java.math.BigDecimal.class;
            case java.sql.Types.INTEGER:
                return java.math.BigInteger.class;
            case java.sql.Types.JAVA_OBJECT:
                return Object.class;
            case java.sql.Types.LONGVARBINARY:
                return byte[].class;
            case java.sql.Types.LONGVARCHAR:
                return String.class;
            case java.sql.Types.NULL:
                return Object.class;
            case java.sql.Types.NUMERIC:
                return java.math.BigDecimal.class;
            case java.sql.Types.OTHER:
                return Object.class;
            case java.sql.Types.REAL:
                return java.math.BigDecimal.class;
            case java.sql.Types.REF:
                return Object.class;
            case java.sql.Types.SMALLINT:
                return Integer.class;
            case java.sql.Types.STRUCT:
                return Object.class;
            case java.sql.Types.TIME:
                return java.util.Date.class;
            case java.sql.Types.TIMESTAMP:
                return java.util.Date.class;
            case java.sql.Types.TINYINT:
                return Integer.class;
            case java.sql.Types.VARBINARY:
                return byte[].class;
            case java.sql.Types.VARCHAR:
                return String.class;
            default:
                System.out.println("Unsupported type");
                return Object.class;
        }
    }
    
    //helper method used for debugging
    private static void printResultSet(java.sql.ResultSet rs) throws Exception {
        //print out the result set md
        java.sql.ResultSetMetaData md = rs.getMetaData();
        StringBuilder buffer = new StringBuilder();
        StringBuilder lineBuffer = new StringBuilder();
        for (int i=0; i<md.getColumnCount(); i++) {
            buffer.append(pad(md.getColumnName(i+1), ' '));
            for (int j=0; j<20; j++) {
                lineBuffer.append("-");
            }
            if (i < (md.getColumnCount() -1)) {
                buffer.append(" | ");
                lineBuffer.append("---");
            }
        }
        System.out.println(buffer.toString());
        System.out.println(lineBuffer.toString());
        
        buffer = new StringBuilder();
        while (rs.next()) {
            for (int i=0; i<md.getColumnCount(); i++) {
                Object obj = rs.getObject(i+1);
                String data = obj == null ? "<null>" : obj.toString();
                data = pad(data, ' ');
                buffer.append(data);
                if (i < (md.getColumnCount() -1)) {
                    buffer.append(" | ");
                }
            }
            buffer.append("\n");
        }
        System.out.println(buffer.toString());
    }
    
    //helper method used for debugging
    private static String pad(String s, char padChar) {
        if (s == null) {
            return "<null>              ";
        } else if (s.length() > 20) {
            return s.substring(0, 20);
        } else if (s.length() < 20) {
            StringBuilder buffer = new StringBuilder(s);
            for (int i=s.length(); i<20; i++) {
                buffer.append(padChar);
            }
            return buffer.toString();
        } else {
            return s;
        }
    }
}
