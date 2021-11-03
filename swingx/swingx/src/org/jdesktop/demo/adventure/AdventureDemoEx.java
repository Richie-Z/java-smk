/*
 * $Id: AdventureDemoEx.java,v 1.4 2005/07/01 15:53:16 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.adventure;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.binding.DataModel;
import org.jdesktop.binding.metadata.MetaData;
import org.jdesktop.binding.swingx.BindingFactory;
import org.jdesktop.binding.swingx.BindingHandler;
import org.jdesktop.binding.swingx.DirectListBinding;
import org.jdesktop.binding.swingx.DirectTableBinding;
import org.jdesktop.binding.swingx.table.ColumnFactoryExt;
import org.jdesktop.dataset.DataRelation;
import org.jdesktop.dataset.DataRelationTable;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.adapter.DataModelAdapter;
import org.jdesktop.dataset.adapter.SelectionModelAdapter;
import org.jdesktop.dataset.adapter.TabularDataModelAdapter;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.MainWindow;
import org.jdesktop.demo.swingx.common.MarginHighlighter;
import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.table.ColumnFactory;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A modified version of the Adventure Demo.
 * 
 * Demonstrates ui-building in three phases
 * 
 * <ul>
 * <li> create and configure components
 * <li> build up the screen
 * <li> load and bind the data to components
 * </ul>
 * 
 * Note that data-related component configuration is left to the  
 * binding phase - it's done by modifying the MetaData of the DataModel.
 * 
 *
 * @author Richard Bair
 * @author Jeanette Winzenburg
 */
public class AdventureDemoEx extends DemoPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3544670668122894901L;
    private DropShadowBorder dsb = new DropShadowBorder(UIManager.getColor("Control"), 0, 8, .5f, 12, false, true, true, true);
	
	private DataSet ds;
    private JTextField nameTF;
    private JTextField locationTF;
    private JFormattedTextField priceTF;
    private JTextArea descriptionTA;
    private JXImagePanel imagePanel;
    private JXList navigator;
    private JTextField catNameTF;
    private JXTable activitiesTBL;

    private JLabel nameLabel;

    private JLabel locationLabel;

    private JLabel priceLabel;

    private JLabel descriptionLabel;
  
    /** Creates new form Adventure Demo */
    public AdventureDemoEx() {
        setName("Adventure Demo 2.3");
        
        //init the gui components
        initComponents();
        configureComponents();
        // build the screen
        build();
        // model, load and bind the data
        bind(); 
    }

    private void bind() {
        preBind();
        // initialize the binding infrastructure
        BindingFactory factory = BindingFactory.getInstance();
        BindingHandler bindings = new BindingHandler();
        bindings.setAutoCommit(true);
        
        // bind packageDetails
        DataModelAdapter packageModel = new DataModelAdapter(ds.getTable("package").getSelector("current"));
        customizeMetaData(packageModel, false);
        bindings.add(factory.createBinding(locationTF, packageModel, "location"));
        bindings.add(factory.createMetaBinding(locationLabel, packageModel, "location"));
        bindings.add(factory.createBinding(priceTF, packageModel, "price"));
        bindings.add(factory.createMetaBinding(priceLabel, packageModel, "price"));
        bindings.add(factory.createBinding(nameTF, packageModel, "name"));
        bindings.add(factory.createMetaBinding(nameLabel, packageModel, "name"));
        bindings.add(factory.createBinding(descriptionTA, packageModel, "description"));
        bindings.add(factory.createMetaBinding(descriptionLabel, packageModel, "description"));
//        bindings.add(new ImagePanelBinding(imagePanel, packageModel, "imageuri"));

        // bind package list and selection of current details
        TabularDataModelAdapter navModel = new TabularDataModelAdapter(ds.getTable("package"));
        SelectionModelAdapter sm = new SelectionModelAdapter(ds.getTable("package").getSelector("current"));
        bindings.add(new DirectListBinding(navigator, navModel, "name", sm));
        // bind category table and details
        // PENDING: category? why doesnt it work?
//        DataModelAdapter categoryDetailModel = new DataModelAdapter(ds.getTable("categoryDetail").getSelector("current"));
//        bindings.add(factory.createBinding(catNameTF, categoryDetailModel, "name"));
        TabularDataModelAdapter activitiesDetailModel = new TabularDataModelAdapter(ds.getTable("activitiesDetail"));
        customizeMetaData(activitiesDetailModel, true);
        bindings.add(new DirectTableBinding(activitiesTBL, activitiesDetailModel, new String[]{"name", "description", "price"}));
        postBind();
    }

 
    private void customizeMetaData(DataModel packageModel, boolean isActivitiesModel) {
        // dumb label setting ... real world apps would f.i. read
        // them from a localized resource file
        String[] fieldNames = packageModel.getFieldNames();
        for (int i = 0; i < fieldNames.length; i++) {
            MetaData metaData = packageModel.getMetaData(fieldNames[i]);
            String label = fieldNames[i].substring(1);
            String capital = fieldNames[i].substring(0, 1).toUpperCase();
            metaData.setLabel(capital + label);
        }
        if (isActivitiesModel) {
            // layout hints for default column width
            MetaData metaData = packageModel.getMetaData("description");
            metaData.setDisplayWidth(64);
            MetaData priceMeta = packageModel.getMetaData("price");
            // JW: hack - elementClass should be set by the TabularDataModelAdapter
            priceMeta.setElementClass(Number.class);
            priceMeta.setDisplayWidth(8);
        }
  
    }

    private void preBind() {
        // model and load the data
        createDataSet();

        // JW: enable enhanced table column configuration
        // this is temporary - should be automatically loaded 
        ColumnFactory.setInstance(new ColumnFactoryExt());
    }


    private void postBind() {
        // JW: Hack - changing the selector value leads to firing tableStructureChanged
        // so we turn auto-creation of tables off once they are bound
        activitiesTBL.setAutoCreateColumnsFromModel(false);
    }

    private void configureComponents() {
        activitiesTBL.setShowHorizontalLines(false);
        activitiesTBL.setBorder(BorderFactory.createEmptyBorder());
        activitiesTBL.setVisibleRowCount(6);
        // fiddling the content inset
        navigator.setBorder(listBorder);
        navigator.setHighlighters(new HighlighterPipeline(new Highlighter[] { new MarginHighlighter(marginBorder)}));

        descriptionTA.setLineWrap(true);
        descriptionTA.setWrapStyleWord(true);
    }

    
    public void addNotify() {
        super.addNotify();
        // JW: quickhack - need to pack to configure JXTable... hmmm
        // this is veery dirty - forget it quickly
        Window window = SwingUtilities.windowForComponent(this);
        window.pack();
    }
    
    private void initComponents() {
        navigator = new JXList();
        activitiesTBL = new JXTable();
        imagePanel = new JXImagePanel();
        nameTF = new JTextField();
        locationTF = new JTextField();
        priceTF = new JFormattedTextField();
        catNameTF = new JTextField();
        descriptionTA = new JTextArea();
   }
 

    
    private void build() {
        JXTitledPanel navigatorPanel = new JXTitledPanel();
        build(navigatorPanel, navigator, true, "Packages");
        JXTitledPanel detailPanel = new JXTitledPanel();
        build(detailPanel, null, true, "Package Details");
        detailPanel.getContentContainer().add(buildPackageDetails());
        JXTitledPanel activitiesPanel = new JXTitledPanel();
        build(activitiesPanel, activitiesTBL, true, "Activities");
        

        JSplitPane detailsSP = createSplitPane(350, JSplitPane.VERTICAL_SPLIT);
        detailsSP.setLeftComponent(detailPanel);
        detailsSP.setRightComponent(activitiesPanel);
        
        JSplitPane mainSP = createSplitPane(240, JSplitPane.HORIZONTAL_SPLIT);
        mainSP.setLeftComponent(navigatorPanel);
        mainSP.setRightComponent(detailsSP);
        setLayout(new BorderLayout());
        
        setBorder(Borders.TABBED_DIALOG_BORDER);
        add(mainSP);
    }

    private JSplitPane createSplitPane(int dividerLocation, int orientation) {
        JSplitPane splitPane = new JSplitPane(orientation);
        splitPane.setDividerLocation(dividerLocation);
        splitPane.setBorder(null);
      ((BasicSplitPaneUI)splitPane.getUI()).getDivider().setBorder(BorderFactory.createEmptyBorder());
        return splitPane;
    }
    
    private void build(JXTitledPanel container, JComponent component, boolean opaque, String title) {
        container.getContentContainer().setLayout(new BorderLayout());
        container.setBorder(dsb);
        container.setTitle(title);
        if (component != null) {
            component = buildScrollPane(component, opaque);
            container.getContentContainer().add(component);
        }
    }

    private JScrollPane buildScrollPane(JComponent component, boolean opaque) {
      JScrollPane scrollPane = new JScrollPane(component);
      scrollPane.setBorder(null);
      scrollPane.setOpaque(opaque);
      scrollPane.getViewport().setOpaque(opaque);
      component.setOpaque(opaque);
      return scrollPane;
    }


    private JComponent buildPackageDetails() {
//        COLUMN SPECS:
//            r:d:n, l:3dluX:n, f:max(p;100dluX):g
//            ROW SPECS:   
//            c:d:n, t:4dluY:n, c:d:n, t:4dluY:n, c:d:n, t:4dluY:n, c:d:n, c:0px:g
//
//            COLUMN GROUPS:  {}
//            ROW GROUPS:     {}
//
//            COMPONENT CONSTRAINTS
//            ( 1,  1,  1,  1, "d=r, d=c"); javax.swing.JLabel      "name"; name=nameLabel
//            ( 3,  1,  1,  1, "d=f, d=c"); javax.swing.JTextField; name=name
//            ( 1,  3,  1,  1, "d=r, d=c"); javax.swing.JLabel      "location"; name=locationLabel
//            ( 3,  3,  1,  1, "d=f, d=c"); javax.swing.JTextField; name=location
//            ( 1,  5,  1,  1, "d=r, d=c"); javax.swing.JLabel      "price"; name=priceLabel
//            ( 3,  5,  1,  1, "d=f, d=c"); javax.swing.JTextField; name=price
//            ( 1,  7,  1,  1, "d=r, d=c"); javax.swing.JLabel      "description"; name=descriptionLabel
//            ( 3,  7,  1,  2, "d=f, d=f"); javax.swing.JScrollPane; name=description


        JXPanel details = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "r:d:n, l:3dlu:n, f:max(p;100dlu):g", // columns
                "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, c:0px:g");  // rows
        PanelBuilder builder = new PanelBuilder(details, formLayout);
        builder.setDefaultDialogBorder();
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        // JW PENDING: 
        // label creation should be moved to initComponents - too lazy to do so for now
        nameLabel = builder.addLabel("", cl.xywh(1, 1, 1, 1), nameTF, cc.xywh(3, 1, 1, 1));
        locationLabel = builder.addLabel("", cl.xywh(1, 3, 1, 1), locationTF, cc.xywh(3, 3, 1, 1));
        priceLabel = builder.addLabel("", cl.xywh(1, 5, 1, 1), priceTF, cc.xywh(3, 5, 1, 1));
        descriptionLabel = builder.addLabel("", cl.xywh(1, 7, 1, 1), new JScrollPane(descriptionTA), cc.xywh(3, 7, 1, 2));
        return details;
    }


     
    private void createDataSet() {
    	ds = DataSet.createFromSchema(getClass().getResourceAsStream("resources/dataset.xsd"));

        DataRelation packageToCategory = ds.createRelation();
    	packageToCategory.setName("packageToCategory");
    	packageToCategory.setParentColumn(ds.getTable("package").getColumn("catid"));
    	packageToCategory.setChildColumn(ds.getTable("category").getColumn("catid"));
    	
    	DataRelation packageToActivityList = ds.createRelation();
    	packageToActivityList.setName("packageToActivityList");
    	packageToActivityList.setParentColumn(ds.getTable("package").getColumn("packageid"));
    	packageToActivityList.setChildColumn(ds.getTable("activityList").getColumn("packageid"));
        
        DataRelationTable activityListDetails = ds.createRelationTable();
        activityListDetails.setName("activityListDetails");
        activityListDetails.setRelation(packageToActivityList);
        activityListDetails.setParentSelector(ds.getTable("package").getSelector("current"));
        
        DataRelation activityListToActivity = ds.createRelation();
        activityListToActivity.setName("activityListToActivity");
        activityListToActivity.setParentColumn(ds.getTable("activityList").getColumn("activityid"));
        activityListToActivity.setChildColumn(ds.getTable("activity").getColumn("activityid"));
        
        DataRelationTable categoryDetail = ds.createRelationTable();
        categoryDetail.setName("categoryDetail");
        categoryDetail.setRelation(packageToCategory);
        categoryDetail.setParentSelector(ds.getTable("package").getSelector("current"));
        
        DataRelationTable activitiesDetail = ds.createRelationTable();
        activitiesDetail.setName("activitiesDetail");
        activitiesDetail.setRelation(activityListToActivity);
        activitiesDetail.setParentTable(ds.getTable("activityListDetails"));

        ds.readXml(getClass().getResourceAsStream("resources/dataset.xml"));
    }

    public static void main(String[] args) {
       MainWindow.main(new String[]{"-d", "org.jdesktop.demo.adventure.AdventureDemoEx"});
        //new AdventureDemo().setVisible(true);
    }
}
