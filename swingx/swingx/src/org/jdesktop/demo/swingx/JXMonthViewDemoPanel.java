package org.jdesktop.demo.swingx;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.*;
import org.jdesktop.demo.DemoPanel;
import org.jdesktop.swingx.calendar.*;

/**
 * Show off the month view demo panel.
 *
 * @author Joshua Outwater
 */
public class JXMonthViewDemoPanel extends DemoPanel implements ActionListener {
	private JXMonthView monthView;
	private JComboBox dayOfWeekComboBox;
	private JColorChooser colorChooser;
	private JDialog colorDialog;
    
    public JXMonthViewDemoPanel() {
        initComponents();
    }

    public String getHtmlDescription() {
        return "Demonstrates the month view component.  By default the component " +
            "will display as many months as possible in the area provided.  It " +
            "supports various properties, some of which can be played with by " +
            "using the control panel at the bottom of the demo.";
    }
    
    public String getName() {
        return "More Date Selection";
    }
    

    public Container getContents() {
        return this;
    }

    public void initComponents() {
        monthView = new JXMonthView();
        monthView.setFirstDayOfWeek(Calendar.MONDAY);
        monthView.setSelectionMode(JXMonthView.NO_SELECTION);
        monthView.setTodayBackground(Color.BLUE);

        // Create controller panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        
        JButton button = new JButton("Toggle orientation");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                monthView.setComponentOrientation(
                    (monthView.getComponentOrientation() ==
                        ComponentOrientation.RIGHT_TO_LEFT) ?
                            ComponentOrientation.LEFT_TO_RIGHT :
                            ComponentOrientation.RIGHT_TO_LEFT);
                monthView.repaint();
            }
        });
        panel.add(button);
        panel.add(Box.createHorizontalStrut(5));
        
        JLabel label = new JLabel("Selection Mode:");
        panel.add(label);
        JComboBox cBox = new JComboBox(new String[] { "None", "Single",
            "Multiple", "Week" });
        cBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JComboBox c = (JComboBox)ev.getSource();
                int index = c.getSelectedIndex();
                monthView.setSelectedDateSpan(null);
                monthView.setSelectionMode(index);
            }
        });
        panel.add(cBox);
        panel.add(Box.createHorizontalStrut(5));
        
        label = new JLabel("Anti-aliased text:");
        panel.add(label);
        JCheckBox checkBox = new JCheckBox();
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JCheckBox c = (JCheckBox)ev.getSource();
                monthView.setAntialiased(c.isSelected());
            }
        });
        panel.add(checkBox);
        panel.add(Box.createHorizontalStrut(5));
        controlPanel.add(panel);
        
        panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        label = new JLabel("Traversable:");
        panel.add(label);
        checkBox = new JCheckBox();
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JCheckBox c = (JCheckBox)ev.getSource();
                monthView.setTraversable(c.isSelected());
            }
        });
        panel.add(checkBox);
        panel.add(Box.createHorizontalStrut(5));

        // Create a combo box with the days of the week and add a button that will
        // show a color chooser to allow the user to select a color.
        dayOfWeekComboBox = new JComboBox(new String[] { "Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday" });
        colorChooser = new JColorChooser();
        colorDialog = colorChooser.createDialog(this,
                                                "Choose a color", true, colorChooser, this, this);		
        button = new JButton("Select A Color!");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                colorDialog.setVisible(true);
            }
        });
        panel.add(dayOfWeekComboBox);
        panel.add(button);
        controlPanel.add(panel);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.SOUTH);
        add(monthView, BorderLayout.CENTER);
    }               

    public void actionPerformed(ActionEvent ev) {
        String command = ev.getActionCommand();

        if (command == "OK") {
            int index = dayOfWeekComboBox.getSelectedIndex();
            monthView.setDayForeground(index + 1, colorChooser.getColor());
            monthView.repaint();
        }
    }
}
