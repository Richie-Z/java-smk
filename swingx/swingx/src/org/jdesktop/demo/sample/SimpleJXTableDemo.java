/*
 * SimpleJXTableDemo.java is a 1.4 application that requires no other files. It is derived from
 * SimpleTableDemo in the Swing tutorial.
 */
package org.jdesktop.demo.sample;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.RolloverHighlighter;
import org.jdesktop.swingx.decorator.Sorter;
import org.jdesktop.swingx.table.TableColumnExt;


/**
 * This SimpleJXTableDemo is a very simple example of how to use the extended features of the
 * JXTable in the SwingX project. The major features are covered, step-by-step. You can run
 * this demo from the command-line without arguments
 * java org.jdesktop.demo.sample.SimpleJXTableDemo
 *
 * If looking at the source, the interesting code is in configureJXTable().
 *
 * This is derived from the SimpleTableDemo in the Swing tutorial.
 *
 * @author Patrick Wright (with help from the Swing tutorial :))
 */
public class SimpleJXTableDemo extends JPanel {
    public SimpleJXTableDemo() {
        super(new BorderLayout());
        initUI();
    }
    
    private void initUI() {
        JXTable jxTable = initTable();
        configureJXTable(jxTable);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(jxTable);
        
        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        
        // add our search panel
        // TODO: not ready yet
        // add(initSearchPanel(jxTable), BorderLayout.NORTH);
        add(initConfigPanel(jxTable), BorderLayout.NORTH);
    }
    
    /** Initialize our JXTable; this is standard stuff, just as with JTable */
    private JXTable initTable() {
        // boilerplate table-setup; this would be the same for a JTable
        SampleTableModel model = new SampleTableModel();
        JXTable table = new JXTable(model);
        model.loadData();
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }
    
    /** For demo purposes, the special features of the JXTable are configured here. There is
     * otherwise no reason not to do this in initTable().
     */
    private void configureJXTable(JXTable jxTable) {
        // This shows the column control on the right-hand of the header.
        // All there is to it--users can now select which columns to view
        jxTable.setColumnControlVisible(true);
        
        // our data is pulling in too many columns by default, so let's hide some of them
        // column visibility is a property of the TableColumnExt class; we can look up a
        // TCE using a column's display name or its index
        TableColumnExt latitude = jxTable.getColumnExt("LATITUDE");
        latitude.setVisible(false);
        jxTable.getColumnExt("LONGITUDE").setVisible(false);
        jxTable.getColumnExt("DEWPOINT").setVisible(false);
        jxTable.getColumnExt("VISIBILITY").setVisible(false);
        jxTable.getColumnExt("WIND_SPEED").setVisible(false);
        jxTable.getColumnExt("GUST_SPEED").setVisible(false);
        jxTable.getColumnExt("VISIBILITY_QUAL").setVisible(false);
        jxTable.getColumnExt("WIND_DIR").setVisible(false);
        jxTable.getColumnExt("WIND_DEG").setVisible(false);
        jxTable.getColumnExt("REGION").setVisible(false);
        
        
        // note that now, getColumnExt() will fail for any of these columns we have hidden
        // JW: changed to api to not fail
        try {
            jxTable.getColumnExt("LATITUDE");
        } catch (Exception e) {
            // we expect this to happen
            // ignore
        }
        
        // however, if we still have the TableColumnExt we can change our mind
        latitude.setVisible(true);
        
        // now we can change the remaining column titles. This is a convenience for calling
        // setHeaderValue() on a TableColumn.
        //
        // We'll do a trick, though, and that is to set the identifiers of each column
        // to their current header value, so we can still use the same names for identifiers
        // in the rest of our code
        
        // First, a trick: by default, the "identifier" for a TableColumn is actually null
        // unless we specifically set it; the header value is used instead. By doing this get,
        // we're pulling the header value, and setting that as the identifier; then we can
        // change the header value independently. This could also be done in creating the
        // TableModel, of course.
//        Enumeration en = jxTable.getColumnModel().getColumns();
//        while ( en.hasMoreElements() ) {
//            TableColumn tc = (TableColumn)en.nextElement();
//            tc.setIdentifier(tc.getIdentifier());
//        }
//        
//
//        // ...and now change the titles
//        int c = jxTable.getColumnCount();
//        for ( int i=0; i < c; i++ ) {
//            TableColumnExt tce = jxTable.getColumnExt(i);
//            String title = tce.getTitle();
//            title = title.substring(0,1).toUpperCase() + title.substring(1).toLowerCase();
//            title.replace('_', ' ');
//            tce.setTitle(title);
//        }

        //JW: use new api to access visible and invisible columns
        // there's a slight catch: the getColumns does not (cannot) guarantee
        // that the elements are of type TableColumnExt
        for (Iterator iter = jxTable.getColumns(true).iterator(); iter.hasNext();) {
            TableColumnExt column = (TableColumnExt) iter.next();
            column.setIdentifier(column.getIdentifier());
            // ...and now change the title
            String title = column.getTitle();
            title = title.substring(0,1).toUpperCase() + title.substring(1).toLowerCase();
            title.replace('_', ' ');
            column.setTitle(title);
        }

        // Sorting by clicking on column headers is on by default. However, the comparison
        // between rows uses a default compare on the column's type, and elevations
        // are not sorting how we want.
        //
        // We will override the Comparator assigned to the Sorter instance assigned
        // to the elevation column. Every column has a Sorter using a default Comparator.
        // By using a custom Comparator we can control how sorting in any column takes place
        Comparator numberComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Double d1 = Double.valueOf(o1 == null ? "0" : (String)o1);
                Double d2 = Double.valueOf(o2 == null ? "0" : (String)o2);
                return d1.compareTo(d2);
            }
        };
        // First, the long way to assign--just to show there's a Sorter involved
        Sorter sorter = jxTable.getColumnExt("ELEVATION").getSorter();
        sorter.setComparator(numberComparator);
        
        // and, shorthand
        jxTable.getColumnExt("TEMPERATURE").getSorter().setComparator(numberComparator);
        
        // comparators are good for special situations where the default comparator doesn't
        // understand our data.
        
        // This turns horizontal scrolling on or off. If the table is too large for the scrollpane,
        // and horizontal scrolling is off, columns will be resized to fit within the pane, which can
        // cause them to be unreadable. Setting this flag causes the table to be scrollable right to left.
        jxTable.setHorizontalScrollEnabled(false);
        
        // We'll add a highlighter to offset different row numbers
        // Note the setHighlighters() takes an array parameter; you can chain these together.
        jxTable.setHighlighters(new HighlighterPipeline(new Highlighter[]{ AlternateRowHighlighter.classicLinePrinter }));
        
        // ...oops! we forgot one
        jxTable.getHighlighters().addHighlighter(new RolloverHighlighter(Color.BLACK, Color.WHITE ));
        jxTable.setRolloverEnabled(true);
        
        // add a filter: include countries starting with a only
        int col = jxTable.getColumn("COUNTRY").getModelIndex();
        jxTable.setFilters(new FilterPipeline(new Filter[] { new PatternFilter("^A", 0, col) }));
        
        // resize all the columns in the table to fit their contents
        // this is available as an item in the column control drop down as well, so the user can trigger it.
        int margin = 5;
        jxTable.packTable(margin);
        
        // we want the country name to always show, so we'll repack just that column
        // we can set a max size; if -1, the column is forced to be as large as necessary for the
        // text
        margin = 10;
        int max = -1;
        // JW: don't - all column indices are view coordinates
        // JW: maybe we need xtable api to take a TableColumn as argument?
        //jxTable.packColumn(jxTable.getColumnExt("COUNTRY").getModelIndex(), margin, max);
        int viewIndex = jxTable.convertColumnIndexToView(jxTable.getColumnExt("COUNTRY").getModelIndex());
        jxTable.packColumn(viewIndex, margin, max);
    }
    
    /** This shows off some additional JXTable configuration, controlled by checkboxes in a Panel. */
    private JPanel initConfigPanel(final JXTable jxTable) {
        JPanel config = new JPanel();
        FlowLayout fll = (FlowLayout)config.getLayout();
        fll.setAlignment(FlowLayout.LEFT);
        fll.setHgap(30);
        
        
        // This shows or hides the column control--note this is possible at runtime
        final JCheckBox control = new JCheckBox();
        control.setSelected(jxTable.isColumnControlVisible());
        control.setAction(new AbstractAction("Show column control") {
            public void actionPerformed(ActionEvent e) {
                jxTable.setColumnControlVisible(control.isSelected());
            }
        });
        
        
        // turn sorting by column on or off
        // bug: there is no API to read the current value! we will assume it is false
        final JCheckBox sorting = new JCheckBox();
        
        sorting.setSelected(jxTable.isSortable());
        sorting.setAction(new AbstractAction("Sortable") {
            public void actionPerformed(ActionEvent e) {
                jxTable.setSortable(sorting.isSelected());
            }
        });
        
        // add checkbox for horizontal scrolling. basically, the table has an action for this,
        // and we need to link the checkbox up in both directions--so that if the property changes
        // the checkbox is updated, and vice-versa. we use an AbstractActionExt to make this easier.
        // you aren't supposed to understand this :) and yes, it will be refactored
        final JCheckBox horiz = new JCheckBox();
        
        AbstractActionExt hA = (AbstractActionExt)jxTable.getActionMap().get(jxTable.HORIZONTALSCROLL_ACTION_COMMAND);
        hA.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                
                if (propertyName.equals("selected")) {
                    Boolean selected = (Boolean)evt.getNewValue();
                    horiz.setSelected(selected.booleanValue());
                }
            }
        });
        horiz.addItemListener(hA);
        horiz.setAction(hA);
        
        config.add(control);
        config.add(sorting);
        config.add(horiz);
        return config;
    }
    /** TODO: this is not working, need to figure out how PatternFilter is applied. */
    private JPanel initSearchPanel(final JXTable jxTable) {
        JPanel search = new JPanel();
        final JButton filter = new JButton("Show");
        final JButton reset = new JButton("Clear");
        
        final JTextField text = new JTextField("");
        text.setColumns(30);
        
        filter.setAction(new AbstractAction("Show") {
            FilterPipeline fp = null;
            PatternFilter pf = new PatternFilter();
            boolean isAdded = false;
            
            public void actionPerformed(ActionEvent e) {
                
                String searchString = text.getText().trim();
                if ( searchString.length() > 0 ) {
                    pf.setPattern(Pattern.compile(searchString));
                    if ( ! isAdded ) {
                        fp = new FilterPipeline(new Filter[]{ pf });
                        jxTable.setFilters(fp);
                        isAdded = true;
                    }
                }
            }
        });
        
        
        search.add(text);
        search.add(filter);
        search.add(reset);
        
        return search;
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        SimpleJXTableDemo newContentPane = new SimpleJXTableDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Display the window.
        frame.pack();
        frame.setSize(1024, 768);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    class SampleTableModel extends DefaultTableModel {
        void loadData() {
            try {
                URL url = SampleTableModel.class.getResource("/org/jdesktop/demo/sample/resources/weather.txt");
                loadDataFromCSV(url);
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDataFromCSV(URL url) {
            try {
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(url.openStream()));
                String line = lnr.readLine();
                String[] cols = line.split("\t");
                for ( String col : cols ) {
                    addColumn(col);
                }
                while (( line = lnr.readLine()) != null ) {
                    addRow(line.split("\t"));
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDefaultData() {
            int colCnt = 6;
            int rowCnt = 10;
            for ( int i=0; i < colCnt; i++ ) {
                addColumn("Column-" + (i + 1));
            }
            for ( int i=0; i <= rowCnt; i++ ) {
                String[] row = new String[colCnt];
                for ( int j=0; j < colCnt; j++ ) {
                    row[j] = "Row-" + i + "Column-" + (j + 1);
                }
                addRow(row);
            }
        }
    }
}
