/*
 * $Id: JXTableDemoPanel.java,v 1.17 2005/06/29 09:54:50 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.binding.JavaBeanDataModel;
import org.jdesktop.binding.metadata.EnumeratedMetaData;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.swingx.BindingFactory;
import org.jdesktop.binding.swingx.BindingHandler;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.swingx.common.ComponentTableModel;
import org.jdesktop.demo.swingx.common.ComponentTreeTableModel;
import org.jdesktop.demo.swingx.common.XYComparator;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the JXTable/TreeTable components
 *
 * @author  Jeanette Winzenburg
 */
public class JXTableDemoPanel extends DemoPanel {
    
    // ----------------- demo components
    private JXTable table;
    private JXTreeTable treeTable;

    // ---------------- demo models
    private ComponentTreeTableModel treeTableModel;
    private ComponentTableModel tableModel;
    
    
    // ----------------- demo decorators
    private Highlighter currentHighlighter;
    private String currentHighlighterKey;
    private String currentComparatorKey;

    // ---------------- controlling components/models
    private JComboBox highlighterCombo;
    private Map highlighterMap;
    private ArrayList highlighterKeys;
    private JLabel highlighterLabel;
    private JXRadioGroup sorterGroup;
    private JLabel sorterGroupLabel;
    private HashMap comparatorMap;
    private ArrayList comparatorKeys;

    /** Creates new form JXTableDemoPanel */
    public JXTableDemoPanel() {
        setName("JXTable Demo");
        initDemoModels();
        initDecorators();
        initComponents();
        configureComponents();
        build();
        bind();
    }


    private void bind() {
        // setting tree/table models manually
        treeTable.setTreeTableModel(treeTableModel);
        table.setModel(tableModel);
        // use binding for control panel interaction
        BindingHandler bindings = new BindingHandler();
        bindings.setAutoCommit(true);
        bindHighlighterControl(bindings);
        bindings.pull();
    }


    private void bindHighlighterControl(BindingHandler bindings) {
        
        // Enumeration not working correctly with classes
        // different from string (?)
        JavaBeanDataModel dataModel = null;
        try {
            EnumeratedMetaData highlighterMeta = new EnumeratedMetaData("highlighterKey",
                    String.class, "Highlighter");
            highlighterMeta.setEnumeration(getHighlighterKeys());
            EnumeratedMetaData comparatorMeta = new EnumeratedMetaData("comparatorKey",
                    String.class, "Sorter");
            comparatorMeta.setEnumeration(getComparatorKeys());
            dataModel = new JavaBeanDataModel(getClass(), this,
                    new MetaData[] { highlighterMeta, comparatorMeta });
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        bindings.add(BindingFactory.getInstance().createBinding(
                highlighterCombo, dataModel, "highlighterKey"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                highlighterLabel, dataModel, "highlighterKey"));

        bindings.add(BindingFactory.getInstance().createBinding(
                sorterGroup, dataModel, "comparatorKey"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                sorterGroupLabel, dataModel, "comparatorKey"));
    }

    // ------------------------ control find modus

    /**
     * PRE: getFindModi() contains value.
     */
    public void setHighlighterKey(String value) {
//        if (value.equals(getFindModus()))
//            return;
        Object old = getHighlighterKey();
        this.currentHighlighterKey = value;
        updateHighlighter(old, value);
        firePropertyChange("highlighterKey", old, getHighlighterKey());
    }

    /**
     * returns the current find modus.
     * 
     * @return
     */
    public String getHighlighterKey() {
        return currentHighlighterKey;
    }

    /**
     * returns array of available find modi.
     * 
     * @return
     */
    public String[] getHighlighterKeys() {
        // @todo: return copy!
        return (String[] )highlighterKeys.toArray(new String[0]);
    }


    private void updateHighlighter(Object oldHighlighterKey, Object highlighterKey) {
        Highlighter oldHighlighter = (Highlighter) highlighterMap.get(oldHighlighterKey);
        if (oldHighlighter !=  null) {
            table.getHighlighters().removeHighlighter(oldHighlighter);
        }
        Highlighter highlighter = (Highlighter) highlighterMap.get(highlighterKey);
        if (highlighter != null) {
            table.getHighlighters().addHighlighter(highlighter);
        }
      //  repaint();

    }

    public void setComparatorKey(String value) {
        Object old = getComparatorKey();
        this.currentComparatorKey = value;
        updateComparator(value);
        firePropertyChange("comparatorKey", old, getComparatorKey());
        
    }
    
    private void updateComparator(String value) {
        Comparator comparator = (Comparator) comparatorMap.get(value);
        // use a custom interactive sorter for the size column
        table.getColumnExt("Size").getSorter().setComparator(comparator);
     
    }

    public String getComparatorKey() {
        return currentComparatorKey;
    }
    
    public String[] getComparatorKeys() {
        return (String[]) comparatorKeys.toArray(new String[0]);
    }
    
    private void initDecorators() {
        highlighterMap = new HashMap();
        highlighterMap.put("ledgerBackground", Highlighter.ledgerBackground);
        highlighterMap.put("notePadBackground", Highlighter.notePadBackground);
        highlighterMap.put("alternate.beige", AlternateRowHighlighter.beige);
        highlighterMap.put("altenate.classicLinePrinter", AlternateRowHighlighter.classicLinePrinter);
        highlighterMap.put("altenate.floralWhite", AlternateRowHighlighter.floralWhite);
        highlighterMap.put("altenate.linePrinter", AlternateRowHighlighter.linePrinter);
        highlighterMap.put("altenate.quickSilver", AlternateRowHighlighter.quickSilver);
        
        highlighterKeys = new ArrayList();
        highlighterKeys.addAll(highlighterMap.keySet());
        Collections.sort(highlighterKeys);
        highlighterKeys.add(0, null);
        
        comparatorMap = new HashMap();
        comparatorMap.put("Point/Dimension", new XYComparator());
        comparatorKeys = new ArrayList();
        comparatorKeys.addAll(comparatorMap.keySet());
        comparatorKeys.add("Lexical (default)");
        Collections.sort(comparatorKeys);
        currentComparatorKey = (String) comparatorKeys.get(0);
        
    }



    private void configureComponents() {
        // configure table
        configureCommonTableProperties(table);
        table.getTableHeader().setToolTipText("<html>" +
                "<b> Sorting: </b> <p> " +
                "Click to toggle sorting <br>" +
                "Ctrl-Shift-Click to unsort <p>" +
                "<b> Auto-Resizing: </b> <p>" +
                "Double-Click in resize region" +
                "</html>");
        // configure treeTable
        configureCommonTableProperties(treeTable);
        treeTable.getTableHeader().setToolTipText("<html>" +
                "<b> Sorting: </b> <p> " +
                "Not supported <p>" +
                "<b> Auto-Resizing: </b> <p>" +
                "Double-Click in resize region" +
                "</html>");
        treeTable.setRootVisible(true);
        treeTable.setShowsRootHandles(false);
        // share highlighter pipeline
        table.setHighlighters(new HighlighterPipeline());
        treeTable.setHighlighters(table.getHighlighters());
    }



    private void configureCommonTableProperties(JXTable table) {
        table.setColumnControlVisible(true);
  //      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRolloverEnabled(true);
//        table.packTable(0);
        TableCellRenderer renderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                if (value instanceof Point) {
                    Point p = (Point) value;
                    value = createString(p.x, p.y);
                } else if (value instanceof Dimension) {
                    Dimension dim = (Dimension) value;
                    value = createString(dim.width, dim.height);
                }
                super.setValue(value);
            }

            private Object createString(int width, int height) {
                return "(" + width + ", " + height + ")";
            }
        };
        table.setDefaultRenderer(Point.class, renderer);
        table.setDefaultRenderer(Dimension.class, renderer);
        
    }



//------------------ create models
    
    private void initDemoModels() {
        treeTableModel = new ComponentTreeTableModel(null);
        tableModel = new ComponentTableModel();
        tableModel.updateComponentList(treeTableModel);
    }

    public void addNotify() {
        super.addNotify();
        // a quick hack to get a static snapshot of the
        // container hierarchy
        treeTableModel.setRoot(SwingUtilities.windowForComponent(this));
        treeTable.expandAll();
        tableModel.updateComponentList(treeTableModel);
    }
    

//-------------------- init UI
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        treeTable = new JXTreeTable();
        table = new JXTable();
        highlighterCombo = new JComboBox();
        sorterGroup = new JXRadioGroup();
        
    }
    
    private void build() {
//        COLUMN SPECS:
//            f:d:g, l:4dluX:n, f:d:g
//            ROW SPECS:   
//            c:d:n, t:3dluY:n, f:d:g, t:4dluY:n, c:d:n
//
//            COLUMN GROUPS:  {}
//            ROW GROUPS:     {}
//
//            COMPONENT CONSTRAINTS
//            ( 1,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "table"; name=tableLabel
//            ( 3,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "treeTable"; name=treeTableLabel
//            ( 1,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=table
//            ( 3,  3,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=treeTable
//            ( 1,  5,  3,  1, "d=f, d=c"); javax.swing.JPanel; name=tablecontrol
//
        
        FormLayout formLayout = new FormLayout(
                "f:p:g, l:4dlu:n, f:p:g", // columns
                "c:d:n, t:3dlu:n, f:d:g, t:4dlu:n, c:d:n");  // rows
        PanelBuilder builder = new PanelBuilder(this, formLayout);
        builder.setDefaultDialogBorder();
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel("JXTable:", cl.xywh(1, 1, 1, 1), new JScrollPane(table), cc.xywh(1,  3,  1,  1) );
        builder.addLabel("JXTreeTable:", cl.xywh(3, 1, 1, 1), new JScrollPane(treeTable), cc.xywh(3,  3,  1,  1));
        builder.add(buildControl(), cc.xywh(1, 5, 3, 1));
    }

   
    private JComponent buildControl() {
//        COLUMN SPECS:
//            r:p:n, l:3dluX:n, f:max(p;50dluX):n, f:0px:g
//            ROW SPECS:   
//            c:d:n, t:4dluY:n, c:d:n
//
//            COLUMN GROUPS:  {}
//            ROW GROUPS:     {}
//
//            COMPONENT CONSTRAINTS
//            ( 1,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "highlighter"; name=highlighterLabel
//            ( 3,  1,  1,  1, "d=f, d=c"); javax.swing.JComboBox; name=highlighter
//            ( 1,  3,  1,  1, "d=f, d=c"); javax.swing.JLabel      "sorterGroup"; name=sorterGroupLabel
//            ( 3,  3,  2,  1, "d=f, d=c"); de.kleopatra.view.JTitledPlaceHolder; name=sorterGroup
//
        FormLayout formLayout = new FormLayout(
                "r:p:n, l:3dlu:n, f:max(p;50dlu):n, f:0px:g", // columns
                "c:d:n, t:4dlu:n, c:d:n"); // rows
        JXPanel control = new JXPanel();
        PanelBuilder builder = new PanelBuilder(control, formLayout);
        builder.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        highlighterLabel = builder.addLabel("", cl.xywh(1, 1, 1, 1), highlighterCombo, 
                cc.xywh(3, 1, 1, 1));
        sorterGroupLabel = builder.addLabel("", cl.xywh(1, 3, 1, 1), sorterGroup, 
                cc.xywh(3, 3, 2, 1));
        return control;
    }



    // --------------- super overrides
    public String getHtmlDescription() {
        return "<html>" +
                "With the SwingX <strong>JXTable</strong> and <strong>JXTreeTable</strong> " +
                "controls your users can <b>select displayed columns</b> at runtime, " +
                "<b>rearrange columns</b> via drag and drop, <b>highlight rows</b> with " +
                "pluggable background highlighters, <b>sort their data</b> via clickable headers " +
                "and more.<br><br>" +
                "In this demo, try rearranging columns by dragging the the column " +
                "headers with your mouse. Select the columns shown in the table by " +
                "clicking on the column control in the top right corner of the tables. " +
                "Change background color highlighting using the Highlighter combobox. " +
                "Sort rows by clicking on the column headers, and change the " +
                "sort characteristics using the Sorter radio buttons. Note the you can " +
                "also re-size table columns automatically from the column control, " +
                "as well as apply horizontal scrolling. " +
               // "<a href=\"" + getHowToURLString() + "\">Read More</a>" +
                "</html>";
    }

    public String getHowToURLString() {
        return "resources/howto/JXTableHowTo.html";
    }
    
    public String getInformationTitle() {
         return "JXTable/JXTreeTable";
    }
    
    public String getName() {
        return "Extended Tables and Trees";
    }
}
