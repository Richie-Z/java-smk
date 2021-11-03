/*
 * $Id: BindingFactory.java,v 1.2 2005/10/10 17:00:51 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.binding.swingx;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import org.jdesktop.binding.DataModel;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXRadioGroup;

/**
 * Choosing strategy for creating Bindings.<p>
 *
 * Extracted from DefaultFormFactory to have a 
 * "pluggable" place for creating custom bindings. The usage
 * of a BindingCreator should be viewed as an implementation
 * detail, they don't do much.
 *
 * PENDING: there's a implicit coupling to ComponentMap -
 * the BindingMap assumes that the ComponentMap did a
 * good enough job when choosing components. <p>
 *
 * PENDING: should be factored into an interface and
 * a default implementation.<p>
 *
 * PENDING: really want to configure the component here?
 *
 * @author Jeanette Winzenburg
 */
public class BindingFactory {

  private Map bindingMap;
  private static BindingFactory instance;
private LabelMetaBindingCreator metaBindingCreator;
  
  
  public static BindingFactory getInstance() {
    // this is not thread safe...  
    if (instance == null) {
      instance = new BindingFactory();
    }
    return instance;
  }

  public static void setInstance(BindingFactory bindingMap) {
    instance = bindingMap;
  }

  /**
   * Creates and returns Binding between the
   * component and a field of the DataModel.
   *  
   *  PENDING: null return value? Or better 
   *  throw BindingException if no appropriate creator found?
   *  
   * @param component
   * @param model
   * @param fieldName
   * @return 
   * 
   * @throws NullPointerException if any of the parameters is null.
   */
  public Binding createBinding(JComponent component, DataModel model, String fieldName) {
    BindingCreator creator = getBindingCreator(component);
    if (creator != null) {
        return creator.createBinding(component, model, fieldName);
    }
    return null;
  }

  /**
   * Creates and returns Binding between the
   * component and the metaData of a field of the DataModel.
   * Typically this is used to bind a label to the label
   * property of the field's metaData.
   *  
   *  PENDING: null return value? Or better 
   *  throw BindingException if no appropriate creator found?
   *  
   * @param component
   * @param model
   * @param fieldName
   * @return 
   * 
   * @throws NullPointerException if any of the parameters is null.
   */
  public Binding createMetaBinding(JLabel label, DataModel model, String fieldName) {
      BindingCreator creator = getMetaBindingCreator();
      if (creator != null) {
          return creator.createBinding(label, model, fieldName);
      }
      return null;
  }

  /**
   * creates and returns a Binding between the label and 
   * the MetaData of the given binding's field.
   *  
   */
//  public Binding createMetaBinding(JLabel label, Binding binding) {
//      BindingCreator creator = getMetaBindingCreator();
//      if (creator != null) {
//          return creator.createBinding(label, binding.getDataModel(), binding.getFieldName());
//      }
//      return null;
//  }

  /**
   * returns the unique BindingCreator for binding (a label) to
   * the metaData - typically to the label property.
   * 
   * @return
   */
  protected BindingCreator getMetaBindingCreator() {
      if (metaBindingCreator == null) {
          metaBindingCreator = new LabelMetaBindingCreator();
      }
      return metaBindingCreator;
    }

/** encapsulates lookup strategy to find an appropriate 
   * BindingCreator for the given component. <p>
   * 
   * Here:
   * <ol>
   * <li> look-up by component class
   * <li> look-up by assignables to component class
   * </ol>
   * 
   *  
   * @param component
   * @return a BindingCreator which can create a binding to
   *   the component or null if none is found.
   */
  protected BindingCreator getBindingCreator(JComponent component) {
    // PENDING: implement better lookup...
    BindingCreator creator =
      (BindingCreator) getBindingMap().get(component.getClass());
    if (creator == null) {
      creator = findByAssignable(component.getClass());
    }
    return creator;
  }

  protected BindingCreator findByAssignable(Class componentClass) {
    Set keys = getBindingMap().keySet();
    for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
      Class element = (Class) iter.next();
      if (element.isAssignableFrom(componentClass)) {
        return (BindingCreator) getBindingMap().get(element);
      }
    }
    return null;
  }

  protected Map getBindingMap() {
    if (bindingMap == null) {
      bindingMap = new HashMap();
      initBindingMap(bindingMap);
    }
    return bindingMap;
  }

  protected void initBindingMap(Map map) {
    map.put(JXHyperlink.class, new HyperlinkBindingCreator());  
    map.put(JXRadioGroup.class, new RadioGroupBindingCreator());
    map.put(JLabel.class, new LabelBindingCreator());
    map.put(JCheckBox.class, new CheckBoxBindingCreator());
    BindingCreator textBindingCreator = new TextBindingCreator();
    map.put(JTextComponent.class, textBindingCreator);
    map.put(JComboBox.class, new ComboBoxBindingCreator());
    BindingCreator tableBindingCreator = new TableBindingCreator();
    map.put(JTable.class, tableBindingCreator);
    BindingCreator listBindingCreator = new ListBindingCreator();
    map.put(JList.class, listBindingCreator);
    map.put(JSpinner.class, new SpinnerBindingCreator());
    map.put(JXImagePanel.class, new ImagePanelBindingCreator());
    map.put(JXDatePicker.class, new DatePickerBindingCreator());
  }

//-------------------------------- BindingCreators  

  /**
   */
  public static class ListBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new ListBinding((JList) component, dataModel, fieldName);
      
    }
  }

  /**
   */
  public class TableBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new TableBinding((JTable) component, dataModel, fieldName);
    }
  }


  /**
   */
  public static class TextBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      Binding binding = doCreateBinding((JTextComponent) component,
                          dataModel, fieldName);
//      configureComponent(component, binding);
      return binding;
    }

    protected Binding doCreateBinding(JTextComponent component,
        DataModel dataModel, String fieldName) {
      Binding binding = new TextBinding(component, dataModel, fieldName);
      return binding;
    }

    /**
     * PENDING: it's a view issue, should not be done here?.
     * @param component
     * @param binding
     */
    protected void configureComponent(JComponent component, Binding binding) {
      int iconPosition = (component instanceof JTextArea)
                           ? SwingConstants.NORTH_EAST : SwingConstants.WEST;
//      BindingBorder bborder = new BindingBorder(binding, iconPosition);
////      Insets insets = bborder.getBorderInsets(component);
////      Dimension prefSize = component.getPreferredSize();
////      prefSize.width += (insets.left + insets.right);
////      // JW: arrgghhh... never do, prevents correct resizing.
////      component.setPreferredSize(prefSize);
//      component.setBorder(new CompoundBorder(component.getBorder(), bborder));
    }
  }

  /**
   */
  public static class LabelBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new LabelBinding((JLabel) component, dataModel, fieldName);
    }
  }

  /**
   */
  public static class ImagePanelBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new ImagePanelBinding((JXImagePanel) component, dataModel, fieldName);
    }
  }

  /**
   */
  public static class HyperlinkBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new HyperlinkBinding((JButton) component, dataModel, fieldName);
    }
  }
  /**
   */
  public static class DatePickerBindingCreator implements BindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      return new DatePickerBinding((JXDatePicker) component, dataModel, fieldName);
    }
  }

  /**
   */
  public static abstract class RequiredBindingCreator implements BindingCreator {

    protected void doAddBindingBorder(JComponent component, Binding binding) {
//      component.setBorder(new CompoundBorder(new BindingBorder(binding),
//          component.getBorder()));
    }
  }

  /**
   */
  public static class ComboBoxBindingCreator extends RequiredBindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      Binding binding = new ComboBoxBinding((JComboBox) component, dataModel,
                          fieldName);
      doAddBindingBorder(component, binding);
      return binding;
    }
  }

  /**
   */
  public static class RadioGroupBindingCreator extends RequiredBindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      Binding binding = new RadioBinding((JXRadioGroup) component, dataModel,
                          fieldName);
      doAddBindingBorder(component, binding);
      return binding;
    }
  }

  /**
   */
  public static class CheckBoxBindingCreator extends RequiredBindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      Binding binding = new BooleanBinding((JCheckBox) component, dataModel,
                          fieldName);
      doAddBindingBorder(component, binding);
      return binding;
    }
  }

  /**
   */
  public static class SpinnerBindingCreator extends RequiredBindingCreator {

    public Binding createBinding(JComponent component, DataModel dataModel,
        String fieldName) {
      Binding binding = new SpinnerBinding((JSpinner) component, dataModel,
                          fieldName);
      doAddBindingBorder(component, binding);
      return binding;
    }
  }

  /**
   * BindingCreator for binding a label to the metaData of the given field.
   */
  public class LabelMetaBindingCreator implements BindingCreator {

      public Binding createBinding(JComponent component, DataModel dataModel,
              String fieldName) {
          if (component instanceof JLabel) {
              return new LabelMetaBinding((JLabel) component, dataModel, fieldName);
          }
          return null;
      }

  }

}
