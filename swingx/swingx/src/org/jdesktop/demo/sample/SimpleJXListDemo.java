/*
 * SimpleJXListDemo.java is a 1.4 application that requires no other files. It is derived from
 * SimpleTableDemo in the Swing tutorial.
 */
package org.jdesktop.demo.sample;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;




/**
 * This SimpleJXListDemo is a very simple example of how to use the extended features of the
 * JXList in the SwingX project. The major features are covered, step-by-step. You can run
 * this demo from the command-line without arguments
 * java org.jdesktop.demo.sample.SimpleJXListDemo
 *
 * If looking at the source, the interesting code is in configureJXList().
 *
 * This is derived from the SimpleTableDemo in the Swing tutorial.
 *
 * @author Patrick Wright (with help from the Swing tutorial :))
 */
public class SimpleJXListDemo extends JPanel {
    public SimpleJXListDemo() {
        super(new BorderLayout());
        initUI();
    }
    
    private void initUI() {
        JXList JXList = initList();
        configureJXList(JXList);
        
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(JXList);
        
        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        
        // add our search panel
        // TODO: not ready yet
        // add(initSearchPanel(JXList), BorderLayout.NORTH);
        add(initConfigPanel(JXList), BorderLayout.NORTH);
    }
    
    /** Initialize our JXList; this is standard stuff, just as with JTable */
    private JXList initList() {
        // boilerplate table-setup; this would be the same for a JTable
        SampleListModel model = new SampleListModel();
        JXList list = new JXList(model);
        model.loadData();
        
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }
    
    /**
     * For demo purposes, the special features of the JXList are configured here. There is
     * otherwise no reason not to do this in initList().
     */
    private void configureJXList(JXList JXList) {
        // We'll add a highlighter to offset different row numbers
        // Note the setHighlighters() takes an array parameter; you can chain these together.
        JXList.setHighlighters(new HighlighterPipeline(new Highlighter[]{ AlternateRowHighlighter.classicLinePrinter }));
        
        // ...oops! we forgot one
        JXList.getHighlighters().addHighlighter(new RolloverHighlighter(Color.CYAN, Color.WHITE ));
        JXList.setRolloverEnabled(true);
        
        // add a filter--filter on name starting with A, and add a shuttle sort 
        // TODO: not implemented in JXList yet
        // JXList.setFilters(new FilterPipeline(new Filter[] { new PatternFilter("A.*", 0, 0), new ShuttleSorter() }));
    }
    
    /** This shows off some additional JXList configuration, controlled by checkboxes in a Panel. */
    private JPanel initConfigPanel(final JXList JXList) {
        JPanel config = new JPanel();
        FlowLayout fll = (FlowLayout)config.getLayout();
        fll.setAlignment(FlowLayout.LEFT);
        fll.setHgap(30);
        
        
        // This shows or hides the column control--note this is possible at runtime
        final JCheckBox rollover = new JCheckBox();
        rollover.setSelected(JXList.isRolloverEnabled());
        rollover.setAction(new AbstractAction("Rollover") {
            public void actionPerformed(ActionEvent e) {
                JXList.setRolloverEnabled(rollover.isSelected());
            }
        });
        
        config.add(rollover);
        return config;
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
        JFrame frame = new JFrame("SimpleJXListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        SimpleJXListDemo newContentPane = new SimpleJXListDemo();
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
    
    class SampleListModel extends DefaultListModel {
        void loadData() {
            try {
                URL url = SampleListModel.class.getResource("/org/jdesktop/demo/sample/resources/countries.txt");
                loadData(url);
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadData(URL url) {
            try {
                List<String> list = new ArrayList<String>();
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(url.openStream()));
                String line = null;
                while (( line = lnr.readLine()) != null ) {
                    if ( line.trim().length() > 0 )
                        list.add(line);
                }
                Collections.sort(list);
                for ( String e : list ) {
                    addElement(e);
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDefaultData() {
            int colCnt = 6;
            int rowCnt = 10;
            for ( int i=0; i <= rowCnt; i++ ) {
                addElement( "Row-" + i );
            }
        }
    }
}
