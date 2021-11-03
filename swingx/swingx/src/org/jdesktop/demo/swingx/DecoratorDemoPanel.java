/*
 * $Id: DecoratorDemoPanel.java,v 1.22 2005/09/06 09:29:12 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx;

import java.awt.Color;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

import org.jdesktop.binding.JavaBeanDataModel;
import org.jdesktop.binding.metadata.EnumeratedMetaData;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.swingx.BindingFactory;
import org.jdesktop.binding.swingx.BindingHandler;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.swingx.common.ActionMapTableModel;
import org.jdesktop.demo.swingx.common.ActionMapTreeTableModel;
import org.jdesktop.demo.swingx.common.MarginHighlighter;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.PatternModel;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.RolloverHighlighter;
import org.jdesktop.swingx.decorator.SearchHighlighter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * Demonstrates usage of Decorators.
 * 
 * @author Jeanette Winzenburg
 */
public class DecoratorDemoPanel extends DemoPanel {

    // ----------------- demo components
    private JXTree tree;

    private JXList list;

    private JXTable table;

    // ----------------- demo decorators
    /** shared highlighter for rollover effect. */
    private Highlighter rolloverHighlighter;
    /** shared highlighter for matches. */
    private SearchHighlighter searchHighlighter;
    
    /** hacking highlighter for list's border adjustment. */
    private Highlighter marginHighlighter;

    /** Filter for matching entries used in list. */
    private PatternFilter listPatternFilter;
    private FilterPipeline listFilterPipeline;

    /** Filter for matching entries used in table. */
    private PatternFilter tablePatternFilter;
    private FilterPipeline tableFilterPipeline;
    
    // ---------------- controlling components/models
    private JXRadioGroup radioGroup;

    private JTextField inputText;

    private JButton nextButton;

    private JTextField patternText;

    private PatternModel patternModel;

    private String[] findModi = new String[] { "highlight", "filter", "search" };

    private String findModus = findModi[0];

    private BoundAction findNextAction;

    private JLabel radioGroupLabel;

    private JLabel inputTextLabel;

    private JLabel patternLabel;

    /** Creates new form DecoratorDemoPanel */
    public DecoratorDemoPanel() {
        setName("Decorator Demo");
        createDecorators();
        initComponents();
        configureComponents();
        build();
        bind();
    }

    /**
     * called after pattern changed.
     * 
     * @param pattern
     */
    protected void updatePattern(Pattern pattern) {
        searchHighlighter.setPattern(pattern);
        tablePatternFilter.setPattern(pattern);
        listPatternFilter.setPattern(pattern);
   //     repaint();
    }

    /**
     * called after find modus changed.
     * 
     */
    private void updateDecorators() {
        boolean highlightMatches = findModi[0].equals(getFindModus());
        // update the highlighter pipelines
        updatePipeline(table.getHighlighters(), highlightMatches);
        updatePipeline(list.getHighlighters(), highlightMatches);
        
        // supported in highlight mode only
//        list.setEnabled(highlightMatches);
        tree.setEnabled(highlightMatches);

        // filter/search support is implemented for JXList 
        FilterPipeline listFilters = findModi[1].equals(getFindModus()) ? 
                getFilterPipeline("list"): null;
        list.setFilters(listFilters);

        // filter/search support is implemented for JXTable only
        FilterPipeline filters = findModi[1].equals(getFindModus()) ? 
                getFilterPipeline("table"): null;
        table.setFilters(filters);

        findNextAction.setEnabled(findModi[2].equals(getFindModus()));
       // repaint();

    }

    private void updatePipeline(HighlighterPipeline highlighters, boolean highlightMatches) {
        if (highlightMatches) {
            highlighters.addHighlighter(rolloverHighlighter, true);
            highlighters.addHighlighter(searchHighlighter, true);
        } else {
            highlighters.removeHighlighter(searchHighlighter);
            highlighters.removeHighlighter(rolloverHighlighter);
        }
        
    }

//--------------------------- init decorators
    
    private void createDecorators() {
        createHighlighters();
        createFilters();
        findNextAction = new BoundAction("Find Next");
    }

//--------------------------- Highlighters    
    /** 
     * create all highlighters.
     * 
     *
     */
    private void createHighlighters() {
        // rollover effect - blue foreground
        rolloverHighlighter = new RolloverHighlighter(null, Color.BLUE);
        // highlight matching effect - magenta foreground
        searchHighlighter = new SearchHighlighter(null, Color.MAGENTA);
        // highlight matches in all columns
        searchHighlighter.setHighlightAll();
        // hacking a right/left margin
        marginHighlighter = new MarginHighlighter(marginBorder);
        
    }



//----------------------- Filters
    
    private void createFilters() {
        // filtering on first column
        // JW: need option to filter all columns?
        tablePatternFilter = new PatternFilter(null, 0, 0);
        listPatternFilter = new PatternFilter(null, 0, 0);
    }

    private FilterPipeline getFilterPipeline(String viewKey) {
        if ("table".equals(viewKey)) {
            // JW: we have to store the filter pipeline
            // because filters can be bound once only
            if (tableFilterPipeline == null) {
                tableFilterPipeline = new FilterPipeline(
                        new Filter[] { tablePatternFilter });
            }
            return tableFilterPipeline;
        } else if ("list".equals(viewKey)) {
            if (listFilterPipeline == null) {
                listFilterPipeline = new FilterPipeline( new Filter[] { listPatternFilter });
            }
            return listFilterPipeline;
        }
        return null;
    }

    // ----------------------- initial configure components
    private void configureComponents() {
        // show column control
        table.setColumnControlVisible(true);
        
        // enable rollover in collection views
        table.setRolloverEnabled(true);
        list.setRolloverEnabled(true);
        tree.setRolloverEnabled(true);
        
        // share highlighterPipeline in table/tree
        table.setHighlighters(new HighlighterPipeline());
        tree.setHighlighters(table.getHighlighters());
        // use different pipeline for list 
        list.setHighlighters(new HighlighterPipeline(new Highlighter[] { marginHighlighter}));

        // enable list filtering
        list.setFilterEnabled(true);
        // initial update of decorations for collection views
        updateDecorators();

    }

    // ------------------------ control find modus

    /**
     * PRE: getFindModi() contains value.
     */
    public void setFindModus(String value) {
        if (value.equals(getFindModus()))
            return;
        Object old = getFindModus();
        this.findModus = value;
        updateDecorators();
        firePropertyChange("findModus", old, getFindModus());
    }

    /**
     * returns the current find modus.
     * 
     * @return
     */
    public String getFindModus() {
        return findModus;
    }

    /**
     * returns array of available find modi.
     * 
     * @return
     */
    public String[] getFindModi() {
        // @todo: return copy!
        return findModi;
    }

    /**
     * callback for findNext action.
     * 
     */
    public void findNext() {
        int foundIndex = table.getSearchable().search(patternModel.getPattern(), patternModel
                .getFoundIndex());
        patternModel.setFoundIndex(foundIndex);
    }

    // ----------------- create control bindings

    private void bind() {
        BindingHandler bindings = new BindingHandler();
        bindings.setAutoCommit(true);
        bindModusControl(bindings);
        bindPatternModel(bindings);
        bindActions(bindings);
        bindings.pull();
    }

    private void bindActions(BindingHandler bindings) {
        findNextAction.registerCallback(this, "findNext");
        nextButton.setAction(findNextAction);
    }

    private void bindModusControl(BindingHandler bindings) {

        // Enumeration not working correctly with classes
        // different from string (?)
        JavaBeanDataModel dataModel = null;
        try {
            EnumeratedMetaData meta = new EnumeratedMetaData("findModus",
                    String.class, "Find Modus");
            meta.setEnumeration(getFindModi());
            dataModel = new JavaBeanDataModel(getClass(), this,
                    new MetaData[] { meta });
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        bindings.add(BindingFactory.getInstance().createBinding(
                radioGroup, dataModel, "findModus"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                radioGroupLabel, dataModel, "findModus"));
    }

    private void bindPatternModel(BindingHandler bindings) {
        patternModel = new PatternModel();

        // init DataModel
        JavaBeanDataModel dataModel = null;
        try {
            dataModel = new JavaBeanDataModel(patternModel);
            dataModel.getMetaData("pattern").setReadOnly(true);
            dataModel.getMetaData("pattern").setLabel("Pattern");
            dataModel.getMetaData("rawText").setLabel("Input Text");
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ------------------------ create control bindings

        bindings.add(BindingFactory.getInstance().createBinding(
                inputText, dataModel, "rawText"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                inputTextLabel, dataModel, "rawText"));
        bindings.add(BindingFactory.getInstance().createBinding(
                patternText, dataModel, "pattern"));
        bindings.add(BindingFactory.getInstance().createMetaBinding(
                patternLabel, dataModel, "pattern"));

        // ------- wire control value changes to presentation changes
        patternModel.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("pattern".equals(evt.getPropertyName())) {
                    updatePattern((Pattern) evt.getNewValue());
                }

            }

        });
    }

    // ----------------------- create models

    private TableModel createTableModel() {
        return new ActionMapTableModel(new JXTable().getActionMap());
    }
    
    private ListModel createListModel() {
        JXList list = new JXList();
        return new DefaultComboBoxModel(list.getActionMap().allKeys());
    }

    private TreeModel createTreeModel() {
        // return new FileSystemModel();
        return new ActionMapTreeTableModel(new JXTree());
    }

    // ---------------------- init UI

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        table = new JXTable(createTableModel());

        // table = new JXTreeTable(new ActionMapTreeTableModel(new
        // JXTreeTable()));

        list = new JXList(createListModel());

        tree = new JXTree(createTreeModel());

        // control components
        nextButton = new JButton("Find next");
        ConstantSize size = LayoutStyle.getCurrent().getRelatedComponentsPadX();
        radioGroup = new JXRadioGroup(size.getPixelSize(nextButton));
        inputText = new JTextField();
        patternText = new JTextField();
    }

    private void build() {

        // COLUMN SPECS:
        // f:p:g, l:4dluX:n, f:d:g, l:4dluX:n, f:d:g
        // ROW SPECS:
        // c:d:n, t:3dluY:n, f:d:g, t:4dluY:n, c:d:n
        //
        // COLUMN GROUPS: {}
        // ROW GROUPS: {}
        //
        // COMPONENT CONSTRAINTS
        // ( 1, 1, 1, 1, "d=f, d=c"); javax.swing.JLabel "table";
        // name=tableLabel
        // ( 3, 1, 1, 1, "d=f, d=c"); javax.swing.JLabel "list"; name=listLabel
        // ( 5, 1, 1, 1, "d=f, d=c"); javax.swing.JLabel "tree"; name=treeLabel
        // ( 1, 3, 1, 1, "d=f, d=f"); javax.swing.JScrollPane; name=table
        // ( 3, 3, 1, 1, "d=f, d=f"); javax.swing.JScrollPane; name=list
        // ( 5, 3, 1, 1, "d=f, d=f"); javax.swing.JScrollPane; name=tree
        // ( 1, 5, 5, 1, "d=f, d=c"); javax.swing.JPanel; name=decoratorcontrol2
        //

        FormLayout formLayout = new FormLayout(
                "f:p:g, l:4dlu:n, f:d:g, l:4dlu:n, f:d:g", // columns
                "c:d:n,  t:3dlu:n, f:d:g, t:4dlu:n, c:d:n"); // rows
        PanelBuilder builder = new PanelBuilder(this, formLayout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        CellConstraints cl = new CellConstraints();
        // builder.add(new JLabel("JXTable:"), cl.xywh(1, 1, 1, 1));

        builder.addLabel("JXTable:", cl.xywh(1, 1, 1, 1),
                new JScrollPane(table), cc.xywh(1, 3, 1, 1));
        builder.addLabel("JXList:", cl.xywh(3, 1, 1, 1), new JScrollPane(list),
                cc.xywh(3, 3, 1, 1));
        builder.addLabel("JXTree:", cl.xywh(5, 1, 1, 1), new JScrollPane(tree),
                cc.xywh(5, 3, 1, 1));
        builder.add(buildControl(), cc.xywh(1, 5, 5, 1));

        list.setBorder(listBorder);
        tree.setBorder(treeBorder);

    }

    private JComponent buildControl() {
        // COLUMN SPECS:
        // r:p:n, l:4dluX:n, f:max(p;100dluX):n, l:4dluX:n, f:max(p;50dluX):n
        // ROW SPECS:
        // c:d:n, t:4dluY:n, c:d:n
        //
        // COLUMN GROUPS: {}
        // ROW GROUPS: {}
        //
        // COMPONENT CONSTRAINTS
        // ( 1, 1, 1, 1, "d=r, d=c"); javax.swing.JLabel "Modus"; name=modus
        // ( 3, 1, 3, 1, "d=f, d=c"); javax.swing.JPanel; name=moduscontrol
        // ( 1, 3, 1, 1, "d=r, d=c"); javax.swing.JLabel "Input"; name=input
        // ( 3, 3, 1, 1, "d=f, d=c"); javax.swing.JTextField; name=searchText
        // ( 5, 3, 1, 1, "d=f, d=c"); de.kleopatra.view.JButton; name=next
        //

        FormLayout formLayout = new FormLayout(
                "r:p:n, l:4dlu:n, f:max(p;100dlu):n, l:4dlu:n, f:max(p;50dlu):n", // columns
                "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n"); // rows
        JXPanel control = new JXPanel();
        PanelBuilder builder = new PanelBuilder(control, formLayout);
        builder.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        radioGroupLabel = builder.addLabel("", cl.xywh(1, 1, 1, 1), radioGroup, cc.xywh(3,
                1, 3, 1));
        inputTextLabel = builder.addLabel("", cl.xywh(1, 3, 1, 1), inputText, cc
                .xywh(3, 3, 1, 1));
        patternLabel = builder.addLabel("", cl.xywh(1, 5, 1, 1), patternText, cc.xywh(
                3, 5, 1, 1));
        builder.add(nextButton, cc.xywh(5, 3, 1, 1));
        return control;
    }

    // --------------- super overrides
    public String getName() {
        return "Decorators";
    }

    public String getInformationTitle() {
        return "Highlighters :: Filters";
    }
    
    public String getHtmlDescription() {
        return "<html> "
                + " Demonstrates the use Decorators (Highlighters, Filters)"
                + " and searching "
                + " in collection components JXTable, JXList, JXTree. "
                + " "
                + "<p> <b>Find matching text </b> - this demo supports three find modes: "
                + " <ul>"
                + " <li> highlight all matching occurrences  "
                + " <li> filter away all non-matching rows (JXTable only - match is tested on first "
                + " column)"
                + " <li> search and select next occurrence (JXTable only) "
                + " </ul>"
                + "<p> <b>Mouse rollover</b> - this demo highlights the foreground in the row below "
                + " the current mouse position if the find mode is set to highlight. "
                + " </html>";
    }

}
