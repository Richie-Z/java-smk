/*
 * $Id: SwingXDemo.java,v 1.39 2005/09/29 14:46:49 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.demo.DemoPanel;
import org.jdesktop.demo.MainWindow;
import org.jdesktop.demo.swingx.common.FramedEditorPaneLinkVisitor;
import org.jdesktop.demo.swingx.common.MarginHighlighter;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.LinkModel;
import org.jdesktop.swingx.SearchFactory;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.action.LinkAction;
import org.jdesktop.swingx.action.TargetableAction;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import com.jgoodies.forms.factories.Borders;


/**
 * A JPanel that demonstrates the use of various SwingX components. Each
 * SwingX Component is shown in its own independent panel, and the user
 * can navigate between the different demos from a list.
 *
 * @author  Richard Bair
 * @author Patrick Wright
 * @author Jeanette Winzenburg
 * 
 */
public class SwingXDemo extends DemoPanel {
    
    //  components
    private JXTitledPanel descriptionContainer;
    // convenience alias to descriptionContainer's content
    private JXEditorPane descriptionPane;
    private JXTitledPanel demoContainer;
    private DropShadowBorder dsb = new DropShadowBorder(UIManager.getColor("Control"), 0, 8, .5f, 12, false, true, true, true);
    
    // controlling views and models
    private JXList demoList;
    /** A List of each component we're demonstrating--all of which are DemoPanel
     * instances. */
    private List<DemoPanel> demoPanels = new ArrayList<DemoPanel>();
    private DemoPanel currentDemo = null;
    private LinkAction readMoreLinkAction;
 
    /**
     * Creates new form SwingXDemo
     */
    public SwingXDemo() {
        demoPanels.add(new JXTableDemoPanel());
        demoPanels.add(new DecoratorDemoPanel());
        demoPanels.add(new JXTaskPaneDemoPanel());
        demoPanels.add(new JXTipOfTheDayDemoPanel());
        demoPanels.add(new JXHyperlinkDemoPanel());
        demoPanels.add(new JXDatePickerDemoPanel());
        demoPanels.add(new JXMonthViewDemoPanel());
        demoPanels.add(new AutoCompleteDemoPanel());
        demoPanels.add(new ActionDemoPanel());
        demoPanels.add(new AuthenticationDemoPanel());
        demoPanels.add(new JXErrorDialogDemoPanel());
        demoPanels.add(new DropShadowBorderPanel());
        demoPanels.add(new JXGlassBoxDemoPanel());
        demoPanels.add(new JXPanelTranslucencyDemoPanel());
        
        initComponents();
        configureComponents();
        build();
        bind();
        bindMenuActions();
        
    }
 
 
    public void useFindBar(boolean useFindBar) {
        SearchFactory.getInstance().setUseFindBar(useFindBar);
    }
    
    private void bindMenuActions() {
        BoundAction findModeAction = new BoundAction("Incremental Search", "useFindBar");
        findModeAction.setStateAction();
        findModeAction.registerCallback(this, "useFindBar");
        getActionMap().put("useFindBar", findModeAction);
        
//        TargetableAction findAction = new TargetableAction("Search", "find");
//        getActionMap().put("triggerFind", findAction);
    }


    @Override
    public void addMenuItems(JMenuBar menuBar) {
        ActionContainerFactory factory = new ActionContainerFactory(null);
        JMenu menu = new JMenu("Find");
        menu.add(factory.createMenuItem(getActionMap().get("triggerFind")));
        menu.add(factory.createMenuItem(getActionMap().get("useFindBar")));
        menuBar.add(menu);
    }


    private void bind() {
        demoList.setModel(new AbstractListModel() {
            public int getSize() {
                return demoPanels.size();
            }
            
            public Object getElementAt(int index) {
                return demoPanels.get(index).getName();
            }
        });
        demoList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
             //   if (e.getValueIsAdjusting()) return;
                demoListValueChanged(e);
            }
            
        });
        descriptionPane.addHyperlinkListener(linkListener);    
        readMoreLinkAction = createReadMoreLink();
        descriptionContainer.addRightDecoration(new JXHyperlink(readMoreLinkAction));
    }
    
    private LinkAction createReadMoreLink() {
        LinkModel readMoreLink = new LinkModel("Read More...");
        readMoreLink.setURLString(getHowToURLString());
        LinkAction linkAction = new LinkAction(readMoreLink);
        linkAction.setVisitingDelegate(new FramedEditorPaneLinkVisitor());
//        URL url = getClass().getResource("/toolbarButtonGraphics/general/Information16.gif");
//        linkAction.putValue(Action.SMALL_ICON, new ImageIcon(url));
        return linkAction;
    }


    /**
     * create components we need access to.
     *
     */
    private void initComponents() {
        descriptionContainer = new JXTitledPanel();
        descriptionPane = new JXEditorPane();
        demoContainer = new JXTitledPanel();
        demoList = new JXList();
    }
    
    private void configureComponents() {
        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        demoList.setHighlighters(new HighlighterPipeline(
                new Highlighter[] {new MarginHighlighter(marginBorder)}));
    }

 
    private void build() {
        build(descriptionContainer, descriptionPane, false, "Information");
        JXTitledPanel listContainer = new JXTitledPanel();
        build(listContainer, demoList, true, "Table of Contents");
        build(demoContainer, null, true, "Demo");
        // this is a hack - components should have "nice" borders 
        // by default
        descriptionPane.setBorder(descriptionBorder);
        // top/bottom margins - same as descriptionPane
        demoList.setBorder(listBorder);
        
        JSplitPane detailsSP = createSplitPane(150, JSplitPane.VERTICAL_SPLIT);
        detailsSP.setLeftComponent(descriptionContainer);
        detailsSP.setRightComponent(demoContainer);
        
        JSplitPane mainSP = createSplitPane(200, JSplitPane.HORIZONTAL_SPLIT);
        mainSP.setLeftComponent(listContainer);
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

    
    private void demoListValueChanged(ListSelectionEvent evt) {
        DemoPanel dp = demoPanels.get(demoList.getSelectedIndex());
        currentDemo = dp;
        updateReadMoreLink();
        descriptionContainer.setTitle("Information :: " + dp.getInformationTitle());
        descriptionPane.setText(dp.getHtmlDescription());
        descriptionPane.setCaretPosition(0);
        demoContainer.setContentContainer(dp.getContent());
        demoContainer.revalidate();
        demoContainer.repaint();
    }

    private void updateReadMoreLink() {
        URL url = getHowToURL();
        if (url != null) {
            readMoreLinkAction.getLink().setURL(url);
            
        }
    }
    
    private URL getHowToURL() {
        return SwingXDemo.class.getResource(currentDemo.getHowToURLString());
    }

    public File getSourceFile() {
        if (currentDemo != null) {
            return currentDemo.getSourceFile();
        }
        return super.getSourceFile();
    }
    
    private HyperlinkListener linkListener=new HyperlinkListener(){
        public void hyperlinkUpdate(final HyperlinkEvent hle){
            
            if(hle.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
                try{
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            URL url = getHowToURL();
//                             readMoreLinkAction.getLink().setURL(url);
//                             readMoreLinkAction.actionPerformed(null);
                            new HowToWindow(currentDemo.getInformationTitle(), url).show();
                        }
                    });
                } catch(Exception e){
                    System.err.println("Couldn't load page");
                }
            }
        }
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        UIManager.addAuxiliaryLookAndFeel(new ContextMenuAuxLF());
        MainWindow.main(new String[]{"-d", SwingXDemo.class.getName()});
    }
    
 }
