/*
 * $Id: AbstractSqlCommand.java,v 1.6 2005/10/10 17:01:15 rbair Exp $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.dataset.provider.sql;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdesktop.dataset.DataCommand;
import org.jdesktop.dataset.DataRow;

/**
 * <p>An AbstractSqlCommand is a {@link org.jdesktop.dataset.DataCommand} meant to 
 * be used with a {@link SQLDataProvider} by defining 
 * methods to generate a SELECT, INSERT, UPDATE or DELETE statement. How these
 * SQL statements are built is up to the concrete implementation of an
 * AbstractSqlCommand.
 *
 * <p>The methods for retrieving the SQL statements (as PreparedStatements) are
 * all protected, meaning they can be accessed by subclasses as well as classes
 * within this package. Thus, within the package, the AbstractSqlCommand defines
 * an interface for preparing a PreparedStatement for consumers of the command.
 *
 * <p>An AbstractSqlCommand also defines methods to normalize SQL statements to 
 * make them easier to process using JDBC--namely, to convert between named-
 * parameter style statements and index-based parameterized statements. 
 *
 * <p>AbstractSqlCommand is useful in defining the structure of a concrete SQLCommand
 * or TableCommand, and is not meant to be used on its own.
 *
 * @author rbair
 */
public abstract class AbstractSqlCommand extends DataCommand {
    
    /**
     * @param conn An active JDBCDataConnection to use to prepare the statement
     * @return A PreparedStatement, ready to execute, for the SELECT SQL statement.
     */
    protected abstract PreparedStatement getSelectStatement(JDBCDataConnection conn) throws Exception;
    /**
     * @param conn An active JDBCDataConnection to use to prepare the statement
     * @param row The {@link DataRow} that will be inserted
     * @return A PreparedStatement, ready to execute, for the INSERT SQL statement.
     */
    protected abstract PreparedStatement getInsertStatement(JDBCDataConnection conn, DataRow row) throws Exception;
    /**
     * @param conn An active JDBCDataConnection to use to prepare the statement
     * @param row The {@link DataRow} that will be updated
     * @return A PreparedStatement, ready to execute, for the UPDATE SQL statement.
     */
    protected abstract PreparedStatement getUpdateStatement(JDBCDataConnection conn, DataRow row) throws Exception;
    /**
     * @param conn An active JDBCDataConnection to use to prepare the statement
     * @param row The {@link DataRow} that will be deleted
     * @return A PreparedStatement, ready to execute, for the DELETE SQL statement.
     */
    protected abstract PreparedStatement getDeleteStatement(JDBCDataConnection conn, DataRow row) throws Exception;
    
    /** 
     * Generates a new String for a SQL statement, replacing named parameters 
     * with ? symbols, as required by the 
     * {@link java.sql.Connection#prepareStatement(String)} method. The Map
     * parameter is populated with parameter names, mapped to a List
     * of indexes numbering that parameter within the SQL statement. Thus, as each
     * named parameter is replaced, we get a list of the position that parameter 
     * had within the statement; the List of indexes can then be used on a call to 
     * {@link PreparedStatement#setObject(int, Object)}, using the index in the list
     * as the index parameter in setObject().
     *
     * @param sql A SQL statement with 1 or more named parameters.
     * @param indexes An empty Map which will be populated with a list of parameter
     * names, and the number of that parameter within the SQL statement.
     * @return A SQL statement ready to use in the JDBC prepareStatement() method; note
     * that the Map parameter is also populated in this method call.
     */
    protected String constructSql(String sql, Map<String,List<Integer>> indexes) {
        //replace all of the named parameters in the sql with their
        //corrosponding values. This is done by first converting the sql
        //to proper JDBC sql by inserting '?' for each and every param.
        //As this is done, a record is kept of which parameters go with
        //which indexes. Then, the parameter values are applied.
        StringBuilder buffer = new StringBuilder(sql);
        //variable containing the index of the current parameter. So,
        //for the first named param this is 0, then 1 for the next, and so on
        int paramIndex = 0;

        //iterate through the buffer looking for a colon outside of any
        //single or double quotes. This represents the beginning of a named
        //parameter
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        for (int i=0; i<buffer.length(); i++) {
            char c = buffer.charAt(i);
            if (c == '\'') {
                inSingleQuote = !inSingleQuote;
            } else if (c == '\"') {
                inDoubleQuote = !inDoubleQuote;
            } else if (c == ':' && !inSingleQuote && !inDoubleQuote) {
                //found the beginning of a named param. find the whole
                //name by looking from here to the first whitespace
                //character

                int firstCharIndex = i;
                i++;
                boolean found = false;
                while (!found) {
                    if (i >= buffer.length()) {
                        //I've gotten to the end of the string, so I must
                        //now have the entire variable name
                        found = true;
                    } else {
                        char next = buffer.charAt(i);
                        if (next == ' ' || next == '\n' || next == '\t' || next == '\r' || next == ',' || next == ')') {
                            found = true;
                        }
                    }
                    i++;
                }

                //ok, i-1 is the index following the last character in this sequence.
                String paramName = buffer.substring(firstCharIndex+1, i-1);

                //now that I have the name, replace it with a ? and add it
                //to the map of paramName->index values.
                buffer.replace(firstCharIndex, i-1, "?");
                if (!indexes.containsKey(paramName)) {
                    indexes.put(paramName, new ArrayList<Integer>());
                }
                List<Integer> list = indexes.get(paramName);
                list.add(paramIndex++);

                //reposition "i" to a valid value since a lot of chars were
                //just removed
                i = firstCharIndex + 1;
            }
        }
        return buffer.toString();
    }

    /**
     * Creates a PreparedStatement from a SQL statement, setting parameter
     * values using the supplied Map of parameter names to values. If there are
     * parameters in the SQL that require values assigned in the PreparedStatements,
     * the parameter in the SQL should appear as ":<parameter-name>", and the
     * parameter name should be assigned a value in the Map argument to this
     * method.
     *
     * @param sql A SQL statement, including named parameters if desired.
     * @param values A Map of parameter names to Object values for the parameter--
     * the values used for each parameter when this statement is executed.
     * @param conn An valid JDBCDataConnection.
     * @return A PreparedStatement build from the SQL argument, with parameters
     * assigned.
     * @throws Exception if any error occurs during execution.
     */
    protected PreparedStatement prepareStatement(String sql, Map<String,Object> values, JDBCDataConnection conn) throws Exception {
        //map containing the indexes for each named param
        Map<String,List<Integer>> indexes = new HashMap<String,List<Integer>>();
        PreparedStatement ps = conn.prepareStatement(constructSql(sql, indexes));

        //now, apply the given set of parameters
        for (String paramName : getParameterNames(new String[]{sql})) {
            List<Integer> list = indexes.get(paramName);
            if (list != null) {
                for (int index : list) {
                    ps.setObject(index + 1, values.get(paramName));
                }
            }
        }
        // TODO: should check that we have values for all parameters in the SQL, and that no parameters in the Map are unused (PWW 04/25/05)
        return ps;
    }

    /**
     * Searches the statements for param names, and returns the unique set of
	 * param names.
     *
     * @param statements An array of SQL statements, optionally with named 
     * parameters embedded, in the form ":<parameter-name>".
     * @return Array of parameter names, unique across all the statements.
    */
	public String[] getParameterNames(String[] statements) {
		StringBuilder buffer = new StringBuilder();
		for (String s : statements) {
			buffer.append(s);
		    buffer.append("\n");
		}
		Set<String> names = new HashSet<String>();

        // TODO: this search routine is redundant with constructSQL above (PWW 04/25/05)
        
        //iterate through the buffer looking for a colon outside of any
        //single or double quotes. This represents the beginning of a named
        //parameter
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        for (int i=0; i<buffer.length(); i++) {
            char c = buffer.charAt(i);
            if (c == '\'') {
                inSingleQuote = !inSingleQuote;
            } else if (c == '\"') {
                inDoubleQuote = !inDoubleQuote;
            } else if (c == ':' && !inSingleQuote && !inDoubleQuote) {
                //found the beginning of a named param. find the whole
                //name by looking from here to the first whitespace
                //character

                int firstCharIndex = i;
                i++;
                boolean found = false;
                while (!found) {
                    if (i >= buffer.length()) {
                        //I've gotten to the end of the string, so I must
                        //now have the entire variable name
                        found = true;
                    } else {
                        char next = buffer.charAt(i);
                        if (next == ' ' || next == '\n' || next == '\t' || next == '\r' || next == ',' || next == ')') {
                            found = true;
                        }
                    }
                    i++;
                }

                //ok, i-1 is the index following the last character in this sequence.
                String paramName = buffer.substring(firstCharIndex+1, i-1);
                names.add(paramName);
            }
        }
        String[] results = new String[names.size()];
        return names.toArray(results);
	}
}
