/*
 * $Id: JXHyperlinkDemoPanel.java,v 1.17 2005/10/12 08:48:11 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.demo.swingx;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.demo.DemoPanel;
import org.jdesktop.swingx.EditorPaneLinkVisitor;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.LinkModel;
import org.jdesktop.swingx.action.LinkAction;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the JXHyperlink component.
 *
 * @author Richard Bair
 * @author Jeanette Winzenburg
 * 
 */
public class JXHyperlinkDemoPanel extends DemoPanel {
    //---------------- demo views    
    private JXTable table;
    private JXList list;
    private JXHyperlink textOnlyHyperlink;
    
    //---------------- models
    private EditorPaneLinkVisitor linkVisitor;

    /** Creates new form JXHyperlinkDemoPanel */
    public JXHyperlinkDemoPanel() {
        setName("JXHyperlink Demo");
        initLinkVisitor();
        initComponents();
        configureComponents();
        build();
    }

    private void initLinkVisitor() {
        // the link visitor - the action which is triggered on clicking the link
        linkVisitor = new EditorPaneLinkVisitor();
    }

    private void configureComponents() {
        TableModel commonModel = createTableModel();
        table.setModel(commonModel);
        table.setDefaultLinkVisitor(linkVisitor);
        table.setColumnControlVisible(true);
        table.packAll();
        list.setModel(createListModel(commonModel));
        list.setLinkVisitor(linkVisitor);

        LinkAction textOnly = createLinkAction("SwingX", "https://swingx.dev.java.net");
        textOnlyHyperlink.setAction(textOnly);
//        textOnly.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/jdesktop/demo/swingx/resources/duke_thumbsup.gif")));

    }



//------------------------ create models
    
    private TableModel createTableModel() {
        DefaultTableModel model = new DefaultTableModel(new Object[] { "Description", "LinkModel"}, 0) {
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        model.addRow(new Object[] {"not existing", createLink("Nirvana", "http://www.nirvana.all") });
        model.addRow(new Object[] {"local resource", new LinkModel("Bike me!", null,getClass().getResource("resources/test.html")) });
        model.addRow(new Object[] {"external resource", createLink("SwingX", "https://swingx.dev.java.net")});
        model.addRow(new Object[] {"closed", createLink("Heaven", "http://www.hell.all") });
        return model;
    }


    private ListModel createListModel(TableModel tableModel) {
        int linkColumn = -1;
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (LinkModel.class == tableModel.getColumnClass(i)) {
                linkColumn = i;
                break;
            }
        }
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            LinkModel link = (LinkModel) tableModel.getValueAt(i, linkColumn);
            model.addElement(new LinkModel(link.getText(), null, link.getURL()));
        }
 
        return model;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        // a link-aware JTable
        table = new JXTable();
        
        // a link-aware JList
        list = new JXList();
        
        textOnlyHyperlink = new JXHyperlink();
    }
    
    private void build() {
     
//        COLUMN SPECS:
//            f:d:n, l:7dluX:n, f:d:g
//            ROW SPECS:   
//            c:d:n, t:4dluY:n, c:d:n, t:7dluY:n, f:d:g, t:7dluY:n, c:d:n
//
//            COLUMN GROUPS:  {}
//            ROW GROUPS:     {}
//
//            COMPONENT CONSTRAINTS
//            ( 1,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "linkTable"; name=linkTableLabel
//            ( 3,  1,  1,  1, "d=f, d=c"); javax.swing.JLabel      "outputPane"; name=outputPaneLabel
//            ( 1,  3,  1,  1, "d=f, d=c"); de.kleopatra.view.JButton; name=textButton
//            ( 3,  3,  1,  5, "d=f, d=f"); javax.swing.JScrollPane; name=outputPane
//            ( 1,  5,  1,  1, "d=f, d=f"); javax.swing.JScrollPane; name=linkTable
//            ( 1,  7,  1,  1, "d=f, d=c"); javax.swing.JScrollPane; name=linkList
//
        FormLayout formLayout = new FormLayout(
                "f:d:n, l:7dlu:n, f:d:g", // columns
                "c:d:n, t:4dlu:n, c:d:n, t:7dlu:n, f:d:g, t:7dlu:n, c:d:n"); // rows
        PanelBuilder builder = new PanelBuilder(this, formLayout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        CellConstraints cl = new CellConstraints();
        
        builder.addLabel("Links: ", cl.xywh(1, 1, 1, 1), 
                textOnlyHyperlink, cc.xywh(1,  3,  1,  1));
        builder.addLabel("Link Content: ", cl.xywh(3, 1, 1, 1), 
                new JScrollPane(linkVisitor.getOutputComponent()), cc.xywh(3,  3,  1,  5));
        builder.add(new JScrollPane(table), cc.xywh(1, 5,  1,  1) );
        builder.add(new JScrollPane(list), cc.xywh(1, 7, 1, 1));
    }

    private LinkAction createLinkAction(String description, String urlString) {
        LinkModel swingXLink = createLink(description, urlString);
        LinkAction textOnly = new LinkAction(swingXLink);
        textOnly.setVisitingDelegate(linkVisitor);
        return textOnly;
    }

    private LinkModel createLink(String description, String urlString) {
        try {
            return new LinkModel(description, null, new URL(urlString));
        } catch (MalformedURLException e) {
            // ignore - something went wrong
        }
        return null;
    }


//---------------------- override super    
    public String getHtmlDescription() {
        return "<html>" +
                "<b>JXHyperlink component </b> - is a button which " +
                "by default looks like a classic HTML link element. " +
                "The button is typically configured with a LinkAction  " +
                "which guarantees to stay in synch with an underlying LinkModel object. " +
                "A LinkAction can be configured with a link visitor - that's an" +
                "actionListener which tries to visit the link. " +
                "There are predefined link visitors: " +
                "<ul>" +
                "<li> EditorPaneLinkVisitor tries to connect to the link url and show its" +
                " content in the contained EditorPane." +
                "<li> ApplicationLinkVisitor (in jdnc-api) tries to use the context " +
                " dependent default browser/output to show the link url " +
                "(TODO: not working)." +
                "</ul>" +
                "<b>Hyperlink in collective views </b> - are supported for JX/Tree/Table, " +
                " JXList, (TODO: JXTree) which have a rolloverEnabled property (disabled by default)." +
                " Internal collaborators: " +
                "<ul>" +
                "<li> LinkRenderer/LinkEditor using a JXHyperlink to show/visit a LinkModel." +
                "<li> RolloverProducer/LinkController tracking mouseEvents to trigger " +
                " rollover effects, re-/setting the link cursor and visit a LinkModel." +
                "</ul>" +
                "</html>";
    }

    public String getName() {
        return "Hyperlinks";
    }
    
    public String getInformationTitle() {
        return "JXHyperlink";
    }

}
