/*
 * $Id: DataSet.java,v 1.21 2005/10/15 11:43:19 pdoubleya Exp $
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jdesktop.dataset.event.TableChangeEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>A DataSet is the top-level class for managing multiple {@link DataTable}s as a 
 * group, allowing explicit support for master-detail relationships. A DataSet
 * contains one or more DataTables, one or more {@link DataRelation}s, and one or more 
 * {@link DataValue}s. Through the DataSet API one has a sort of unified access to all
 * these elements in a single group. As a canonical example, a single DataSet could
 * have DataTables for Customers, Orders, and OrderItems, and the relationships
 * between them.
 *
 * <p>The DataSet internal structure read from a DataSet XML 
 * schema using the {@link #createFromSchema(String)} method, which also supports
 * reading from an {@link InputStream} or {@link File}. The internal structure
 * can then be persisted back as an XML schema by retrieving its String form
 * using {@link #getSchema()} and then saving that.
 *
 * <p>Separately, all the data in a DataSet (e.g. rows in the internal DataTables)
 * can be read ({@link #readXml(String)}) or written ({@link #writeXml()}).
 * 
 * <p>DataSet has standard methods for adding or removing single DataTables,
 * DataRelationTables, and DataValues. Note that these instances are automatically
 * tracked, so if their names are changed through their own API, that name change
 * is reflected automatically in the DataSet.
 * 
 * @see DataTable
 * @see DataRelationTable
 * @see DataValue
 * @see DataRelation
 *
 * @author rbair
 */
public class DataSet {
    /** 
     * Flag used for {@link #writeXML(OutputControl)} to indicate whether
     * all rows, or just modified rows should be spit out.
     */
    // TODO: replace with some kind of filter; maybe a functor (PWW 04/29/05)
    public enum OutputControl { ALL_ROWS, MODIFIED_ONLY };
    
    /**
     * The Logger
     */
    private static final Logger LOG = Logger.getLogger(DataSet.class.getName());
    
    //protected for testing
    /** Prefix used by the name generator for automatically-generated DataSet names. */
    protected static final String DEFAULT_NAME_PREFIX = "DataSet";
    
    /** The shared instance of the NameGenerator for DataSets not assigned a name. */
    private static final NameGenerator NAMEGEN = new NameGenerator(DEFAULT_NAME_PREFIX);
    
    //used for communicating changes to this JavaBean, especially necessary for
    //IDE tools, but also handy for any other component listening to this dataset
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** The name of this DataSet; should be unique among all instantiated DataSets. */
    private String name;
    
    /** DataTables in this set, keyed by table name. */
    private Map<String,DataTable> tables = new HashMap<String,DataTable>();
    
    /** DataRelations in this set, keyed by relation name. */
    private Map<String,DataRelation> relations = new HashMap<String,DataRelation>();

    /** DataValues in this set, keyed by value name. */
    private Map<String,DataValue> values = new HashMap<String,DataValue>();
    private Parser parser = new Parser();    
    
    /** 
     * A PropertyChangeListener instance--inner class--used to track changes
     * to table, relation and value names.
     */
    private NameChangeListener nameChangeListener = new NameChangeListener();
    
    /**
     * Instantiates a DataSet with an automatically-generated name.
     */
    public DataSet() {
        parser.bindThis(this);
        parser.setUndecoratedDecimal(true);
        setName(NAMEGEN.generateName(this));
    }
    
    /**
     * Instantiates a DataSet with a specific name; the name can be useful
     * when persisting the DataSet or when working with multiple DataSets.
     * See {@link #setName(String)}
     * for comments about DataSet naming.
     *
     * @param name The name for the DataSet.
     */
    public DataSet(String name) {
        parser.bindThis(this);
        parser.setUndecoratedDecimal(true);
        setName(name);
    }

    /**
     * Assigns a new name to this DataSet. Names are not controlled for
     * uniqueness, so the caller must take care to not use
     * the same name for more than one DataSet. Any String that is a valid
     * Java identifier can be used; see comments on {@link DataSetUtils#isValidName(String)}..
     *
     * @param name The new name for the DataSet.
     */
    public void setName(String name) {
        if (this.name != name) {
            assert DataSetUtils.isValidName(name);
            String oldName = this.name;
            this.name = name;
            pcs.firePropertyChange("name", oldName, name);
        }
    }
    
    /**
     * Returns the current name for the DataSet.
     *
     * @return the current name for the DataSet.
     */
    // TODO: should we check for (static) uniqueness of names? (PWW 04/28/04)
    public String getName() {
        return name;
    }
    
    /**
     * Creates a new DataTable with an automatically-generated name, 
     * and adds it to this DataSet; the 
     * table will belong to this DataSet.
     *
     * @return a new DataTable assigned to this DataSet.
     */
    public DataTable createTable() {
        return createTable(null);
    }

    /**
     * Creates a new, named DataTable, and adds it to this DataSet; the 
     * table will belong to this DataSet. If the DataSet already has a table
     * with that name, the new, empty DataTable will take its place.
     *
     * @param name The new DataTable's name.
     * @return a new unnamed DataTable assigned to this DataSet.
     */
    // TODO: error if table already exists--can orphan an existing table for the same name (PWW 04/27/05)
    public DataTable createTable(String name) {
        DataTable table = new DataTable(this, name);
        table.addPropertyChangeListener("name",  nameChangeListener);
        tables.put(table.getName(), table);
        return table;
    }
    
    /**
     * Creates a new DataRelationTable with an automatically-generated name, 
     * and adds it to this DataSet; the 
     * table will belong to this DataSet.
     *
     * @return a new DataRelationTable assigned to this DataSet.
     */
    public DataRelationTable createRelationTable() {
        return createRelationTable(null);
    }
    
    /**
     * Creates a new, named DataRelationTable, and adds it to this DataSet; the 
     * table will belong to this DataSet. If the DataSet already has a table
     * with that name, the new, empty DataRelationTable will take its place.
     *
     * @param name The new DataRelationTable's name.
     * @return a new unnamed DataRelationTable assigned to this DataSet.
     */
    // TODO: error if table already exists--can orphan an existing table for the same name (PWW 04/27/05)
    public DataRelationTable createRelationTable(String name) {
        DataRelationTable table = new DataRelationTable(this, name);
        table.addPropertyChangeListener("name",  nameChangeListener);
        tables.put(table.getName(), table);
        return table;
    }
    
    /**
     * Creates a new DataRelation with an automatically-generated name, 
     * and adds it to this DataSet; the 
     * table will belong to this DataSet.
     *
     * @return a new DataRelation assigned to this DataSet.
     */
    public DataRelation createRelation() {
        return createRelation(null);
    }
    
    /**
     * Creates a new, named DataRelation, and adds it to this DataSet; the 
     * table will belong to this DataSet. If the DataSet already has a table
     * with that name, the new, empty DataRelation will take its place.
     *
     * @param name The new DataRelation's name.
     * @return a new unnamed DataRelation assigned to this DataSet.
     */
    // TODO: error if relation already exists--can orphan an existing relation for the same name (PWW 04/27/05)
    public DataRelation createRelation(String name) {
        DataRelation relation = new DataRelation(this, name);
        relation.addPropertyChangeListener("name",  nameChangeListener);
        relations.put(relation.getName(), relation);
        return relation;
    }
    
    /**
     * Creates a new DataValue with an automatically-generated name, 
     * and adds it to this DataSet; the 
     * table will belong to this DataSet.
     *
     * @return a new DataValue assigned to this DataSet.
     */
    public DataValue createValue() {
        return createValue(null);
    }
        
    /**
     * Creates a new, named DataValue, and adds it to this DataSet; the 
     * table will belong to this DataSet. If the DataSet already has a table
     * with that name, the new, empty DataValue will take its place.
     *
     * @param name The new DataValue's name.
     * @return a new unnamed DataValue assigned to this DataSet.
     */
    // TODO: error if value already exists--can orphan an existing value for the same name (PWW 04/27/05)
    public DataValue createValue(String name) {
        DataValue value = new DataValue(this, name);
        value.addPropertyChangeListener("name", nameChangeListener);
        values.put(value.getName(), value);
        return value;
    }

    /** 
     * Removes a given DataTable from this DataSet. 
     * @param table The DataTable instance to remove.
     */
    public void dropTable(DataTable table) {
        dropTable(table.getName());
    }
    
    /** 
     * Removes a given DataTable from this DataSet. 
     * @param tableName The name of the table to remove.
     */
    public void dropTable(String tableName) {
        //drop the DataTable. Remove any remaining references to it by the
        //DataRelations or DataRelationTables
        DataTable table = tables.remove(tableName);
        if (table != null) {
            table.removePropertyChangeListener("name",  nameChangeListener);
            for (DataRelation r : relations.values()) {
                DataColumn col = r.getChildColumn();
                if (col != null && col.getTable() == table) {
                    r.setChildColumn(null);
                }
                col = r.getParentColumn();
                if (col != null && col.getTable() == table) {
                    r.setParentColumn(null);
                }
            }

            for (DataTable t : tables.values()) {
                if (t instanceof DataRelationTable) {
                    DataRelationTable drt = (DataRelationTable)t;
                    if (drt.getParentTable() == table) {
                        drt.setParentTable(null);
                    }
                    if (drt.getParentSelector() != null && drt.getParentSelector().getTable() == table) {
                        drt.setParentSelector(null);
                    }
                }
            }
        }
    }
    
    /** 
     * Removes a given DataRelationTable from this DataSet. 
     * @param table The DataRelationTable instance to remove.
     */
    public void dropRelationTable(DataRelationTable table) {
        dropTable(table.getName());
    }
    
    /** 
     * Removes a given DataRelationTable from this DataSet. 
     * @param tableName The name of the table to remove.
     */
    public void dropRelationTable(String tableName) {
        dropTable(tableName);
    }
    
    /** 
     * Removes a given DataRelation from this DataSet. 
     * @param relation The DataRelation instance to remove.
     */
    public void dropRelation(DataRelation relation) {
        dropRelation(relation.getName());
    }
    
    /** 
     * Removes a given DataRelation from this DataSet. 
     * @param relationName The name of the relation to remove.
     */
    public void dropRelation(String relationName) {
        DataRelation relation = relations.remove(relationName);
        if (relation != null) {
            relation.removePropertyChangeListener("name",  nameChangeListener);
            for (DataTable t : tables.values()) {
                if (t instanceof DataRelationTable) {
                    DataRelationTable drt = (DataRelationTable)t;
                    if (drt.getRelation() == relation) {
                        drt.setRelation(null);
                    }
                }
            }
        }
    }
    
    /** 
     * Removes a given DataValue from this DataSet. 
     * @param value The DataValue instance to remove.
     */
    public void dropValue(DataValue value) {
        dropValue(value.getName());
    }
    
    /** 
     * Removes a given DataValue from this DataSet. 
     * @param valueName The name of the value to remove.
     */
    public void dropValue(String valueName) {
        values.remove(valueName).removePropertyChangeListener("name",  nameChangeListener);
    }
    
    /** 
     * Checks whether this DataSet has a relation, table or value by the given 
     * name.
     *
     * @param name The name to lookup.
     * @return true if this DataSet has a table, relation or value with that name.
     */
    protected boolean hasElement(String name) {
        boolean b = relations.containsKey(name);
        if (!b) {
            b = tables.containsKey(name);
        }
        if (!b) {
            b = values.containsKey(name);
        }
        return b;
    }
    
    /**
     * Given some path, return the proper element. Paths are:
     * <ul>
     * <li>tableName</li>
     * <li>tableName.colName</li>
     * <li>dataRelationTableName</li>
     * <li>dataRelationTableName.colName</li>
     * <li>relationName</li>
     * <li>valueName</li>
     * </ul>
     * @param path The identifier for the element in question; see desc.
     * @return the element in question as an Object, or null if it doesn't
     * identify anything in this DataSet.
     */
    protected Object getElement(String path) {
        if (path == null) {
            return null;
        }
        if (path.contains(".")) {
            //must be a table
            String[] steps = path.split("\\.");
            assert steps.length == 2;
            DataTable table = tables.get(steps[0]);
            DataColumn col = table.getColumn(steps[1]);
            if (col != null) {
                return col;
            } else {
                return table.getSelector(steps[1]);
            }
        } else {
            if (relations.containsKey(path)) {
                return relations.get(path);
            } else if (tables.containsKey(path)) {
                return tables.get(path);
            } else if (values.containsKey(path)) {
                return values.get(path);
            }
        }
        return null;
    }
    
    /** TODO */
    /*
     * TODO: this is a method of retrieving a subset of rows from a DataSet, by 
     * naming a table or relation and optional selectors in a path expression--
     * need to document!
     * if not found, will be an empty list, and if found, list is unmodifiable
     */
    public List<DataRow> getRows(String path) {
        if (path == null || path.trim().equals("")) {
            return Collections.EMPTY_LIST;
        }
        
        path = path.trim();
        
        //first, split on "."
        String[] steps = path.split("\\.");
        
        //each step is either a specific name ("myTable"), or is a composite
        //of a name and an index ("myTable[mySelector]")
        
        //maintain a collection of results
        List<DataRow> workingSet = null;
        
        for (String step : steps) {
            String name = null;
            String selectorName = null;
            if (step.contains("[")) {
                name = step.substring(0, step.indexOf('['));
                selectorName = step.substring(step.indexOf('[')+1,  step.indexOf(']'));
            }
            
            if (workingSet == null) {
                //get all of the results from the named object (better be a
                //table, not a relation!)
                DataTable table = tables.get(name);
                if (table == null) {
                    assert false;
                }
                workingSet = table.getRows();
                if (selectorName != null) {
                    // TODO: why is the reassignment for workingSet commented out (PWW 04/27/05)
//                    workingSet = filterRows(workingSet, selectors.get(selectorName));
                }
            } else {
                //better be a relation...
                DataRelation relation = relations.get(name);
                if (relation == null) {
                    assert false;
                }
                workingSet = relation.getRows((DataRow[])workingSet.toArray(new DataRow[workingSet.size()]));
                if (selectorName != null) {
                    // TODO: why is the reassignment for workingSet commented out (PWW 04/27/05)
//                    workingSet = filterRows(workingSet, selectors.get(selectorName));
                }
            }
        }
        return Collections.unmodifiableList(workingSet);
    }
    
    /** 
     * Returns a list of rows that match a given selector; convenience method.
     *
     * @param rows The rows to select from.
     * @param ds The DataSelector with indices to filter with.
     * @return a List of DataRows matching the given DataSelector; empty if
     * none match.
     */
    // TODO: this has nothing to do with DataSet--put in utils? (PWW 04/27/05)
    public List<DataRow> filterRows(List<DataRow> rows, DataSelector ds) {
        List<Integer> indices = ds.getRowIndices();
        List<DataRow> results = new ArrayList<DataRow>(indices.size());
        for (int index : indices) {
            results.add(rows.get(index));
        }
        return results;
    }
    
    // TODO: document (PWW 04/27/05)
    public List<DataColumn> getColumns(String path) {
        //path will either include a single table name, or a single
        //relation name, followed by a single column name
        String[] parts = path.split("\\.");
        assert parts.length == 1 || parts.length == 2;
        
        DataTable table = tables.get(parts[0]);
        if (table == null) {
            DataRelation relation = relations.get(parts[0]);
            if (relation == null) {
                return new ArrayList<DataColumn>();
            } else {
                table = relation.getChildColumn().getTable();
            }
        }
        
        if (parts.length == 1) {
            return table.getColumns();
        } else {
            List<DataColumn> results = new ArrayList<DataColumn>();
            results.add(table.getColumn(parts[1]));
            return Collections.unmodifiableList(results);
        }
    }
    
    /**
     * Looks up a DataTable in this DataSet, by name.
     *
     * @param tableName The name of the table to retrieve.
     * @return the DataTable, if found, or null, if not.
     */
    public DataTable getTable(String tableName) {
        return tables.get(tableName);
    }
    
    /**
     * Returns a List of the DataTables in this DataSet.
     *
     * @return a List of the DataTables in this DataSet; empty if no tables
     * in this DataSet.
     */
    public List<DataTable> getTables() {
        List<DataTable> tableList = new ArrayList<DataTable>();
        for(DataTable table : tables.values()) {
            tableList.add(table);
        }
        return tableList;
    }
    
    /**
     * Looks up a DataRelationTable in this DataSet, by name.
     *
     * @param name The name of the relation table to retrieve.
     * @return the DataRelationTable, if found, or null, if not.
     */
    public DataRelationTable getRelationTable(String name) {
        return (DataRelationTable)tables.get(name);
    }
    
    /** 
     * Looks up a DataValue in this set by name.
     *
     * @param valueName The name of the DataValue.
     * @return the named DataValue, or null if no value with that name.
     */
    public DataValue getValue(String valueName) {
        return values.get(valueName);
    }
    
    /**
     * Retrieves a list of all DataValues in this set.
     *
     * @return list of all DataValues in this set, empty if none assigned.
     */
    public List<DataValue> getValues() {
        List<DataValue> values = new ArrayList<DataValue>();
        for (DataValue v : this.values.values()) {
            values.add(v);
        }
        return values;
    }
    
    /** 
     * Looks up a DataRelation in this set by name.
     *
     * @param relationName The name of the DataRelation.
     * @return the named DataRelation, or null if no value with that name.
     */
    public DataRelation getRelation(String relationName) {
        return relations.get(relationName);
    }
    
    /**
     * Retrieves a list of all DataRelation in this set.
     *
     * @return list of all DataRelation in this set, empty if none assigned.
     */
    public List<DataRelation> getRelations() {
        List<DataRelation> relations = new ArrayList<DataRelation>();
        for (DataRelation r : this.relations.values()) {
            relations.add(r);
        }
        return relations;
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet load itself; this is 
     * an <em>asynchronous</em> operation. See {@link DataTable#load()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void load() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.load();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet load itself; this is 
     * a <em>synchronous</em> operation. See {@link DataTable#loadAndWait()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void loadAndWait() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.loadAndWait();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet refresh itself; this is 
     * an <em>asynchronous</em> operation. See {@link DataTable#refresh()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void refresh() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.refresh();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet refresh itself; this is 
     * a <em>synchronous</em> operation. See {@link DataTable#refreshAndWait()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void refreshAndWait() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.refreshAndWait();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet clear itself; this is 
     * a <em>synchronous</em> operation since the clear operation is not anticipated to
     * take more than a couple of milliseconds at worst.
     * See {@link DataTable#clear()};
     */
    public void clear() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.clear();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet save itself; this is 
     * an <em>asynchronous</em> operation. See {@link DataTable#save()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void save() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.save();
            }
        }
    }
    
    /** 
     * Requests that each {@link DataTable} in this DataSet save itself; this is 
     * a <em>synchronous</em> operation. See {@link DataTable#saveAndWait()}; tables must have
     * been assigned a {@link DataProvider} already.
     */
    public void saveAndWait() {
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                table.saveAndWait();
            }
        }
    }

    /**
     * @return the Functor parser
     */
    Parser getParser() { 
        return parser; 
    }

    /**
     * @deprecated use DataSetUtils.createFromXmlSchema instead
     */
    public static DataSet createFromSchema(String schema) {
        return DataSetUtils.createFromXmlSchema(schema);
    }
    
    /**
     * Construct and return a proper schema file describing the DataSet
     *
     * @return A schema representing this DataSet.
     * @deprecated use DataSetUtils.getXmlSchema instead
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public String getSchema() {
        return DataSetUtils.getXmlSchema(this);
    }
    
    /** 
     * Same as {@link #createFromSchema(String)}, but using a File as input source.
     *
     * @param f The File to read from.
     * @return a newly instantiated DataSet.
     * @deprecated use DataSetUtils.createFromXmlSchema instead
     */
    public static DataSet createFromSchema(File f) throws FileNotFoundException {
        return DataSetUtils.createFromXmlSchema(f);
    }
    
    /** 
     * Same as {@link #createFromSchema(String)}, but using a InputStream as input source.
     *
     * @param is The InputStream to read from.
     * @return a newly instantiated DataSet.
     * @deprecated use DataSetUtils.createFromXmlSchema instead
     */
    public static DataSet createFromSchema(InputStream is) {
        return DataSetUtils.createFromXmlSchema(is);
    }
    
    /** 
     * Same as {@link #readXml(String)}, but using a File as input source.
     *
     * @param f The XML Document, as a File.
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public void readXml(File f) {
        String xml = "";
        try {
            FileInputStream fis = new FileInputStream(f);
            readXml(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Same as {@link #readXml(String)}, but using an InputStream as input source.
     *
     * @param is The XML Document, as an InputStream.
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public void readXml(InputStream is) {
        String xml = "";
        try {
            StringBuilder builder = new StringBuilder();
            byte[] bytes = new byte[4096];
            int length = -1;
            while ((length = is.read(bytes)) != -1) {
                builder.append(new String(bytes, 0, length));
            }
            xml = builder.toString();
            readXml(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Loads DataSet data from an XML Document in String format; when complete, the DataSet
     * will have been populated from the Document.
     *
     * @param xml The XML Document, as a String.
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public void readXml(String xml) {
        //TODO when parsing the xml, validate it against the xml schema
        
        try { 
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dom = db.parse(new ByteArrayInputStream(xml.getBytes()));

            //create the xpath
            XPath xpath = XPathFactory.newInstance().newXPath();
            
            String path = null;
            
            //for each table, find all of its items
            for (DataTable table : tables.values()) {
                if (!(table instanceof DataRelationTable)) {
                    table.fireDataTableChanged(TableChangeEvent.newLoadStartEvent(table));
                    //clear out the table
                    table.clear();
                    
                    if ( !table.isAppendRowSupported()) {
                        LOG.fine("Table '" + table.getName() + "' does " +
                                "not support append row; skipping (regardless of " +
                                "input).");
                        continue;
                    }
                    
                    LOG.finer("loading table " + table.getName());
                    
                    //get the nodes
                    path = "/" + name + "/" + table.getName();
                    NodeList nodes = (NodeList)xpath.evaluate(path, dom, XPathConstants.NODESET);
                    
                    LOG.finer("  found " + nodes.getLength() + " rows for path " + path);
                    
                    //for each node, iterate through the columns, loading their values
                    for (int i=0; i<nodes.getLength(); i++) {
                        //each rowNode node represents a row
                        Node rowNode = nodes.item(i);
                        DataRow row = table.appendRowNoEvent();
                        NodeList cols = rowNode.getChildNodes();
                        for (int j=0; j<cols.getLength(); j++) {
                            Node colNode = cols.item(j);
                            if (colNode.getNodeType() == Node.ELEMENT_NODE) {
                                //TODO this doesn't take into account type conversion...
                                //could use a default converter...
//                            	System.out.println(colNode.getNodeName() + "=" + colNode.getTextContent());
                                String text = colNode.getTextContent();
                                //convert the text to the appropriate object of the appropriate type
                                Object val = text;
                                Class type = table.getColumn(colNode.getNodeName()).getType();
                                if (type == BigDecimal.class) {
                                    val = new BigDecimal(text);
                                }
                                //TODO do the other types
                                row.setValue(colNode.getNodeName(), val);                            }
                        }
                        row.setStatus(DataRow.DataRowStatus.UNCHANGED);
                    }
                    table.fireDataTableChanged(TableChangeEvent.newLoadCompleteEvent(table));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Writes out the data in this DataSet as XML, and returns this as a String;
     * all rows are exported.
     *
     * @return the contents of this DataSet, as XML, in a String.
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public String writeXml() {
        return writeXml(OutputControl.ALL_ROWS);
    }

    /** 
     * Writes out the data in this DataSet as XML, and returns this as a String.
     *
     * @param flags Value indicating whether all rows (OutputControl.ALL_ROWS)
     * or only new and modified rows should be spit out.
     * @return the contents of this DataSet, as XML, in a String.
     */
     //* TODO: need the schema documented somewhere; we might want to list the URL for the schema here (PWW 04/27/05)
    public String writeXml(OutputControl flags) {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" ?>\n");
        builder.append("<");
        builder.append(name);
        builder.append(">\n");
        for (DataTable table : tables.values()) {
            if (!(table instanceof DataRelationTable)) {
                for (DataRow row : table.rows) {
                    if ( flags == OutputControl.MODIFIED_ONLY && row.getStatus() == DataRow.DataRowStatus.UNCHANGED ) {
                        continue;                        
                    }
                        
                    builder.append("\t<");
                    builder.append(table.getName());
                    builder.append(">\n");

                    for (DataColumn col : table.columns.values()) {
                        builder.append("\t\t<");
                        builder.append(col.getName());
                        builder.append(">");
                        String val = row.getValue(col) == null ? "" : row.getValue(col).toString();
                        //escape val
                        val = val.replaceAll("&", "&amp;");
                        val = val.replaceAll("<", "&lt;");
                        val = val.replaceAll(">", "&gt;");
                        val = val.replaceAll("'", "&apos;");
                        val = val.replaceAll("\"", "&quot;");
                        builder.append(val);
                        builder.append("</");
                        builder.append(col.getName());
                        builder.append(">\n");
                    }

                    builder.append("\t</");
                    builder.append(table.getName());
                    builder.append(">\n");
                }
            }
        }
        builder.append("</");
        builder.append(name);
        builder.append(">");
        
        return builder.toString();
    }
    
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("DataSet: ").append(name).append("\n");
        for (DataTable table : tables.values()) {
            if (table instanceof DataRelationTable) {
                buffer.append("\tDataRelationTable: ").append(table.getName()).append("\n");
            } else {
                buffer.append("\tDataTable: ").append(table.getName()).append("\n");
            }
            for (DataColumn col : table.getColumns()) {
                buffer.append("\t\tDataColumn: ").append(col.getName()).append("\n");
            }
        }
        for (DataRelation relation : relations.values()) {
            buffer.append("\tRelation: ").append(relation.getName()).append("\n");
            DataColumn parentColumn = relation.getParentColumn();
            buffer.append("\t\tParentColumn: ").append(parentColumn == null ? "<none>" : parentColumn.getTable().getName()).append(".").append(parentColumn == null ? "<none>" : parentColumn.getName()).append("\n");
            DataColumn childColumn = relation.getChildColumn();
            buffer.append("\t\tChildColumn: ").append(childColumn == null ? "<none>" : childColumn.getTable().getName()).append(".").append(childColumn == null ? "<none>" : childColumn.getName()).append("\n");
        }
        buffer.append(getSchema());
        return buffer.toString();
    }
    
    /**
     * Test function from command line; no arguments needed
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            DataSet ds = DataSetUtils.createFromXmlSchema(new File("/usr/local/src/databinding/src/test/org/jdesktop/dataset/contact.ds"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long stopTime = System.currentTimeMillis();
//        System.out.println(ds.toString());
        System.out.println((stopTime - startTime));
        
//        //now, populate
//        ds.readXml(new File("/usr/local/src/swinglabs-demos/src/java/org/jdesktop/demo/adventure/resources/dataset.xml"));
//        System.out.println(ds.getTable("package").getRowCount());
//        DataTable table = ds.getTable("activity");
//        double total = 0;
//        for (DataRow row : table.getRows()) {
//            total += ((Number)row.getValue("price")).doubleValue();
//        }
//        System.out.println(total);
//        System.out.println(ds.getValue("totalValue").getValue());
        
        
//        try {
//            Class.forName("org.postgresql.Driver");
//            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost/ab", "rb156199", "Tesooc7x");
//            java.util.Set<String> names = new java.util.HashSet<String>();
//            names.add("package.name");
//            names.add("activity");
//            names.add("activitylist");
//            DataSet ds = DataSetUtils.createFromDatabaseSchema(conn, "ab", names);
//            System.out.println(ds);
//            java.io.File f = new java.io.File("/usr/local/src/databinding/src/test/org/jdesktop/dataset/blar.ds");
//            java.io.OutputStream os = new java.io.FileOutputStream(f);
//            os.write(ds.getSchema().getBytes());
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /** 
     * A PropertyChangeListener--used to track changes to table, relation and 
     * value names as our Maps are keyed by those names--will automatically
     * pick up new names and adjust Map entries. Tables, relations and values
     * have this listener attached when added to the DataSet.
     */
    private final class NameChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            Object source = evt.getSource();
            if (source instanceof DataTable) {
                DataTable table = (DataTable)source;
                tables.remove(evt.getOldValue());
                tables.put((String)evt.getNewValue(), table);
            } else if (source instanceof DataRelation) {
                DataRelation relation = (DataRelation)source;
                relations.remove(evt.getOldValue());
                relations.put((String)evt.getNewValue(), relation);
            } else if (source instanceof DataValue) {
                DataValue value = (DataValue)source;
                values.remove(evt.getOldValue());
                values.put((String)evt.getNewValue(), value);
            }
        }
    }
}
