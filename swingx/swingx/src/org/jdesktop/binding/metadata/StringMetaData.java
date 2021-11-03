/*
 * $Id: StringMetaData.java,v 1.3 2005/10/10 17:01:09 rbair Exp $
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

package org.jdesktop.binding.metadata;


/**
 * <p>
 * Class for representing meta-data for data fields which contain string
 * values.
 * Example usage:<br>
 * <pre><code>
 *     StringMetaData metaData = new StringMetaData(&quot;streetaddress&quot;,
 *                                                  &quot;Street Address&quot;);
 *     metaData.setRequired(true);
 *     metaData.setMaxLength(32);
 * </code></pre>
 * </p>

 * @author Amy Fowler
 * @version 1.0
 */

public class StringMetaData extends MetaData {
    private boolean multiLine = false;
    protected int minLength = 0;
    protected int maxLength = Integer.MAX_VALUE;

    public StringMetaData() {
        super("stringvalue");
    }

    public StringMetaData(String name) {
        super(name);
    }

    public StringMetaData(String name, String label) {
        super(name, String.class, label);
    }

    /**
     * Gets the meta-data's &quot;minLength&quot; property, which indicates
     * the minimum number of characters permitted for the data field.  The default
     * is 0, which means there is no minimum.
     * @see #setMinLength
     * @return integer indicating the minimum number of characters permitted for
     *         the data field
     */
     public int getMinLength() {
         return minLength;
     }

     /**
      * Sets the meta-data's &quot;minLength&quot; property.
      * @param minLength integer indicating the minimum number of characters permitted for
      *         the data field
      */
     public void setMinLength(int minLength) {
         int oldMinLength = this.minLength;
         this.minLength = minLength;
         firePropertyChange("minLength", oldMinLength, minLength);
     }

     /**
      * Gets the meta-data's &quot;maxLength&quot; property, which indicates
      * the maximum number of characters permitted for the data field.  The default
      * is <code>Integer.MAX_VALUE</code>, which means there is no maximum.
      * @see #setMaxLength
      * @return integer indicating the maximum number of characters permitted for
      *         the data field
      */
      public int getMaxLength() {
          return maxLength;
      }

      /**
       * Sets the meta-data's &quot;maxLength&quot; property.
       * @param maxLength integer indicating the maximum number of characters
       *        permitted for the data field
       */
      public void setMaxLength(int maxLength) {
          int oldMaxLength = this.maxLength;
          this.maxLength = maxLength;
          firePropertyChange("maxLength", oldMaxLength, maxLength);
      }

      /**
       *
       * @return boolean indicating whether or not the value can be a multi-line string
       */
      public boolean isMultiLine() {
          return multiLine;
      }

      /**
       * Sets the meta-data's &quot;multLine&quot; property.
       * @param multiLine boolean indicating whether or not the value can be a
       *        multi-line string
       */
      public void setMultiLine(boolean multiLine) {
          boolean oldMultiLine = this.multiLine;
          this.multiLine = multiLine;
          firePropertyChange("multiLine", oldMultiLine, multiLine);
      }
}