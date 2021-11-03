/*
 * $Id: Converters.java,v 1.2 2005/10/10 17:01:10 rbair Exp $
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class containing the static converter registry and a set of static Converter
 * classes for the common Java data types:
 * <ul>
 * <li>java.lang.Boolean</li>
 * <li>java.lang.String</li>
 * <li>java.lang.Integer</li>
 * <li>java.lang.Long</li>
 * <li>java.lang.Short</li>
 * <li>java.lang.Float</li>
 * <li>java.lang.Double</li>
 * <li>java.lang.Date</li>
 * 
 * </ul>
 * Converter instances are retrieved from the registry using the class
 * as the key.  Example usage:
 * <pre><code>    Converter converter = Converters.get(Integer.class);
 *     try {
 *         Integer value = (Integer)converter.decode("99", null);
 *     }
 *     catch (ConversionException e) {
 *         // conversion error!
 *     }
 * </code></pre>
 * <p>
 * Converters can also be added or replaced in the registry:
 * <pre><code>    Converters.put(Foo.class, new FooConverter());
 * </code></pre>
 * </p>
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class Converters {
    private static Map map;

    static {
        map = new HashMap();
        map.put(Boolean.class, "org.jdesktop.binding.metadata.Converters$BooleanConverter");
        map.put(Date.class, "org.jdesktop.binding.metadata.Converters$DateConverter");
        map.put(Double.class, "org.jdesktop.binding.metadata.Converters$DoubleConverter");
        map.put(Float.class, "org.jdesktop.binding.metadata.Converters$FloatConverter");
        map.put(Integer.class, "org.jdesktop.binding.metadata.Converters$IntegerConverter");
//        map.put(LinkModel.class, "org.jdesktop.binding.Converters$LinkConverter");
        map.put(Long.class, "org.jdesktop.binding.metadata.Converters$LongConverter");
        map.put(Short.class, "org.jdesktop.binding.metadata.Converters$ShortConverter");
        map.put(String.class, "org.jdesktop.binding.metadata.Converters$StringConverter");
    }

    /**
     * Retrieves the converter for the class.
     *
     * @param klass class used as key for converter lookup
     * @return Converter instance registered for specified class, or null if
     *         no converter is currently registered for that class
     */
    public static Converter get(Class klass) {
	Object obj = map.get(klass);
	Converter converter = null;
	if (obj != null) {
	    if (obj instanceof String) {
		try {
		    Class cls = Class.forName((String)obj);
		    converter = (Converter)cls.newInstance();
		    map.put(klass, converter);
		} catch (Exception ex) {
		    converter = null;
		}
	    } else {
		converter = (Converter)obj;
	    }
	}
        return converter;
    }

    /**
     * Registers the specified converter for the specified class, overriding
     * any prior converter mapping for that class if it existed.
     * @param klass class used as key for converter lookup
     * @param converter Converter instance to be registered for the class
     */
    public static void put(Class klass, Converter converter) {
        map.put(klass, converter);
    }

    /**
     * Return all the types which currently have supported type converters.
     * 
     * @return an non-null array of supported types
     */
    public static Class[] getTypes() {
	Set keys = map.keySet();
	return (Class[])keys.toArray(new Class[0]);
    }

    protected Converters() {
    } //prevent instantiation

    /**
     * Converter for java.lang.String which passes the value
     * unchanged.  The <code>format</code> parameter is ignored.
     */
    static class StringConverter implements Converter {

        public String encode(Object value, Object format)
            throws ConversionException{
            if (value != null && value instanceof String) {
                return (String)value;
            }
            throw new ConversionException(value, String.class);
        }

        public Object decode(String value, Object format)
            throws ConversionException {
            try {
                return value.toString();
            }
            catch (Exception e) {
                throw new ConversionException(value, String.class, e);
            }
        }
    }

    /**
     * Converter for java.lang.Boolean.
     * Conversion from String to Boolean will return Boolean.TRUE if the
     * string value is equal to &quot;true&quot; using a case-insensitive
     * compare and will return Boolean.FALSE for all other string values.
     * The <code>format</code> parameter is ignored.
     */
    static class BooleanConverter implements Converter {

        public String encode(Object value, Object format)
            throws ConversionException {
            try {
                Boolean boolValue = (Boolean) value;
                return boolValue.toString();
            }
            catch (Exception e) {
                throw new ConversionException(value, Boolean.class, e);
            }
        }

        public Object decode(String value, Object format)
            throws ConversionException {
            try {
                // this returns Boolean.FALSE for any string that is not
                // equivelent to "true" using a case-insensitive compare
                return Boolean.valueOf(value);
            }
            catch (Exception e) {
                throw new ConversionException(value, Boolean.class, e);
            }
        }
    }

    /**
     * Converter for java.lang.Integer.
     * The <code>format</code> parameter may either be an Integer object
     * representing the radix, or null.  If <code>format</code> is null,
     * a default radix of 10 is used.
     */
    static class IntegerConverter implements Converter {

        public String encode(Object value, Object format)
            throws ConversionException {
            try {
                int intValue = ((Integer)value).intValue();
                int radix = (format == null? 10 : ((Integer)format).intValue());
                return Integer.toString(intValue, radix);
            }
            catch (Exception e) {
                throw new ConversionException(value, Integer.class, e);
            }
         }

         public Object decode(String value, Object format)
             throws ConversionException {
             try {
                 int radix = (format == null? 10 : ((Integer)format).intValue());
                 return Integer.valueOf(value, radix);
             }
             catch (Exception e) {
                throw new ConversionException(value, Integer.class, e);
             }
         }
     }

     /**
      * Converter for java.lang.Long.
      * The <code>format</code> parameter may either be an Integer object
      * representing the radix or null.  If <code>format</code> is null,
      * a default radix of 10 is used.
      */
     static class LongConverter implements Converter {

         public String encode(Object value, Object format)
             throws ConversionException {
             try {
                 long longValue = ((Long)value).longValue();
                 int radix = (format == null? 10 : ((Integer)format).intValue());
                 return Long.toString(longValue, radix);
             }
             catch (Exception e) {
                 throw new ConversionException(value, Long.class, e);
             }
          }

          public Object decode(String value, Object format)
              throws ConversionException {
              try {
                  int radix = (format == null? 10 : ((Integer)format).intValue());
                  return Long.valueOf(value, radix);
              }
              catch (Exception e) {
                 throw new ConversionException(value, Long.class, e);
              }
          }
      }

      /**
       * Converter for java.lang.Short.
       * The <code>format</code> parameter may either be an Integer object
       * representing the radix or null.  If <code>format</code> is null,
       * a default radix of 10 is used.
       */
      static class ShortConverter implements Converter {

          public String encode(Object value, Object format) throws
              ConversionException {
              try {
                  // Short doesn't have toString(short value, int radix) method
                  int shortValue = ( (Short) value).intValue();
                  int radix = (format == null ? 10 : ( (Integer) format).intValue());
                  return Integer.toString(shortValue, radix);
              }
              catch (Exception e) {
                  throw new ConversionException(value, Short.class, e);
              }
          }

          public Object decode(String value, Object format) throws
              ConversionException {
              try {
                  int radix = (format == null ? 10 : ( (Integer) format).intValue());
                  return Short.valueOf(value, radix);
              }
              catch (Exception e) {
                  throw new ConversionException(value, Short.class, e);
              }
          }
      }

     /**
      * Converter for java.lang.Float.
      * The <code>format</code> parameter is ignored.
      */
     static class FloatConverter implements Converter {

         public String encode(Object value, Object format)
             throws ConversionException {
             try {
                 Float floatValue = (Float) value;
                 return floatValue.toString();
             }
             catch (Exception e) {
                 throw new ConversionException(value, Float.class, e);
             }
         }

         public Object decode(String value, Object format)
             throws ConversionException {
             try {
                 return Float.valueOf(value);
             }
             catch (Exception e) {
                throw new ConversionException(value, Float.class, e);
             }
         }
    }

    /**
      * Converter for java.lang.Double.
      * The <code>format</code> parameter is ignored.
      */
     static class DoubleConverter implements Converter {

         public String encode(Object value, Object format)
             throws ConversionException {
             try {
                 Double doubleValue = (Double) value;
                 return doubleValue.toString();
             }
             catch (Exception e) {
                 throw new ConversionException(value, Double.class, e);
             }
         }

         public Object decode(String value, Object format)
             throws ConversionException {
             try {
                 return Double.valueOf(value);
             }
             catch (Exception e) {
                throw new ConversionException(value, Double.class, e);
             }
         }
    }


    /**
     * Converter for java.util.Date.
     * The <code>format</code> parameter must be either an instance of
     * <code>DateFormat</code> or <code>null</code>.  If <code>null</code> is
     * specified, the converter will use the default <code>SimpleDateFormat</code>
     * object, whose format defaults to &quot;EEE MMM dd hh:mm:ss z yyyy&quot;.
     *
     * @see java.text.DateFormat
     * @see java.text.SimpleDateFormat
     */
    public static class DateConverter implements Converter {
        private DateFormat defaultInputFormat;
        private DateFormat defaultOutputFormat;

        public DateConverter() {
            defaultInputFormat = defaultOutputFormat =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        }

        public DateConverter(DateFormat defaultInputFormat,
                             DateFormat defaultOutputFormat) {
            this.defaultInputFormat = defaultInputFormat;
            this.defaultOutputFormat = defaultOutputFormat;
        }

        public String encode(Object value, Object format)
            throws ConversionException {
            try {
                DateFormat dateFormat = format == null ? defaultOutputFormat :
                    (DateFormat) format;
                return dateFormat.format((Date)value);
            }
            catch (Exception e) {
                throw new ConversionException(value, Date.class, e);
            }
        }

        public Object decode(String value, Object format)
            throws ConversionException {
            try {
                DateFormat dateFormat = format == null? defaultInputFormat :
                                                 (DateFormat)format;
                return dateFormat.parse(value);
            }
            catch (Exception e) {
                throw new ConversionException(value, Date.class, e);
            }
        }
    }

    /**
     * Converter for org.jdesktop.swing.Link.
     * Currently the <code>format</code> parameter is ignored and the conversion
     * uses the HTML href tag format to encode and decode the values:<br>
     * <pre>    &lt;a href=&quot;%HREF%&quot; target=&quot;%TARGET%&quot;&gt;%DISPLAYSTRING%&lt;/a&gt;
     * </pre>
     */
//    static class LinkConverter implements Converter {
//        /** @todo aim: quick hack, reimplement with sax parser instead! */
//
//        private static final String URL_BEGIN = "<a href=\"";
//        private static final String URL_MARKER = "%u";
//        private static final String URL_END = "\"";
//        private static final String TARGET_BEGIN = " target=\"";
//        private static final String TARGET_MARKER = "%t";
//        private static final String TARGET_END = "\"";
//        private static final String DISPLAY_BEGIN = ">";
//        private static final String DISPLAY_MARKER = "%d";
//        private static final String DISPLAY_END = "</a>";
//
//        private static final String TEMPLATE =
//            URL_BEGIN + URL_MARKER + URL_END +
//            TARGET_BEGIN + TARGET_MARKER + TARGET_END +
//            DISPLAY_BEGIN + DISPLAY_MARKER + DISPLAY_END;
//
//        private static final String TEMPLATE2 =
//            URL_BEGIN + URL_MARKER + URL_END +
//            DISPLAY_BEGIN + DISPLAY_MARKER + DISPLAY_END;
//
//        public String encode(Object value, Object format)
//            throws ConversionException {
//            try {
//                LinkModel link = (LinkModel) value;
//                String linkString;
//                String target = link.getTarget();
//                if (target != null) {
//                    linkString = TEMPLATE.replaceFirst(URL_MARKER,
//                        link.getURL().toExternalForm());
//                    linkString = linkString.replaceFirst(TARGET_MARKER,
//                        target);
//                }
//                else {
//                    linkString = TEMPLATE2.replaceFirst(URL_MARKER,
//                        link.getURL().toExternalForm());
//                }
//                linkString = linkString.replaceFirst(DISPLAY_MARKER,
//                                                     link.getText());
//
//                return linkString;
//            }
//            catch (Exception e) {
//                throw new ConversionException(value, LinkModel.class, e);
//            }
//        }
//
//        public Object decode(String value, Object format)
//            throws ConversionException {
//            try {
//                String url = value.substring(URL_BEGIN.length(),
//                                             value.indexOf(URL_END, URL_BEGIN.length() + 1));
//                String target = null;
//                int targetIndex = value.indexOf(TARGET_BEGIN);
//                if (targetIndex != -1) {
//                    target = value.substring(targetIndex + TARGET_BEGIN.length(),
//                                             value.indexOf(TARGET_END,
//                        targetIndex + TARGET_BEGIN.length() + 1));
//                }
//                String display = value.substring(value.indexOf(DISPLAY_BEGIN) +
//                                                 DISPLAY_BEGIN.length(),
//                                                 value.indexOf(DISPLAY_END));
//
//
//                return new LinkModel(display, target, new URL(url));
//            }
//            catch (Exception e) {
//                throw new ConversionException(value, LinkModel.class, e);
//            }
//        }
//    }
}
