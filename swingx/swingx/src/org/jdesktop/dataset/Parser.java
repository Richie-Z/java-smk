/*
 * $Id: Parser.java,v 1.5 2005/10/10 17:01:01 rbair Exp $
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

package org.jdesktop.dataset;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.Generator;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.fn.adaptor.ApplyUnary;
import net.sf.jga.fn.adaptor.Constant;
import net.sf.jga.fn.adaptor.ConstantUnary;
import net.sf.jga.fn.adaptor.Identity;
import net.sf.jga.fn.algorithm.Accumulate;
import net.sf.jga.fn.algorithm.Count;
import net.sf.jga.fn.algorithm.TransformUnary;
import net.sf.jga.fn.arithmetic.Average;
import net.sf.jga.fn.arithmetic.Divides;
import net.sf.jga.fn.arithmetic.Plus;
import net.sf.jga.fn.arithmetic.ValueOf;
import net.sf.jga.fn.comparison.Max;
import net.sf.jga.fn.comparison.Min;
import net.sf.jga.fn.property.ArrayBinary;
import net.sf.jga.fn.property.Construct;
import net.sf.jga.fn.property.GetProperty;
import net.sf.jga.fn.property.InvokeMethod;
import net.sf.jga.fn.property.InvokeNoArgMethod;
import net.sf.jga.parser.JFXGParser;
import net.sf.jga.parser.FunctorRef;
import net.sf.jga.parser.GeneratorRef;
import net.sf.jga.parser.ParseException;
import net.sf.jga.parser.UnaryFunctorRef;
import net.sf.jga.util.FilterIterator;

// NOTE: throughout this class, a generic parm <DataRow> had to be omitted, and has been
// replaced with '/**/'.  The getRowCountFn couldn't use the fully specified generic type
// List<DataRow>, because the compiler could not detect that List.class was the proper
// value for an argument of type Class<List<DataRow>>.  This may have been fixed in a
// later 1.5_0x compiler.

class Parser extends JFXGParser {
    private DataTable table;

    boolean inTableContext = false;

    // Functor that returns the list of rows for a given table
    private UnaryFunctor<DataTable,List/**/> getRowsFn =
        new GetProperty<DataTable,List/**/>(DataTable.class, "Rows");

    // Functor that returns the number of rows in a list
    private UnaryFunctor<DataTable,Integer> getRowCountFn =
        new InvokeNoArgMethod<List/**/,Integer>(List.class, "size").compose(getRowsFn);

    // Functor that returns an iterator over the rows in a list
    private UnaryFunctor<DataTable,Iterator/**/> iterateTableFn =
        new InvokeNoArgMethod<List/**/,Iterator/**/>(List.class, "iterator")
            .compose(getRowsFn);

    // Functor that returns the value of a DataValue
    private UnaryFunctor<DataValue,?> getValueFn =
        new GetProperty<DataValue,Object>(DataValue.class, "Value");
    
    // Functor that constructs a FilterIterator, given an InputIterator and a Predicate
    private Class[] filterCtorArgs = new Class[]{Iterator.class,UnaryFunctor.class};
    private BinaryFunctor<Iterator/**/,UnaryFunctor<DataRow,Boolean>,? extends Iterator/**/>
        makeFilterFn = new Construct<FilterIterator/**/>(filterCtorArgs,FilterIterator.class)
            .compose(new ArrayBinary<Iterator/**/,UnaryFunctor<DataRow,Boolean>>());
    
    // =============================
    // DataSet specific entry points
    // =============================

    /**
     * Parses a computed column expression, for the given table.  
     */
    UnaryFunctor<DataRow,?> parseComputedColumn(DataTable table, String expression)
        throws ParseException
    {
        setCurrentTable(table);
        try {
            return parseUnary(expression, DataRow.class);
        }
        finally {
            setCurrentTable(null);
        }
    }

    /**
     * Parses an expression that computes a summary value for the collection of data
     * currently available in the bound dataset.
     */
    Generator<?> parseDataValue(String expression) throws ParseException {
        inTableContext = true;
        try {
            return parseGenerator(expression);
        }
        finally {
            inTableContext = false;
            setCurrentTable(null);
        }
    }

    
    // =============================
    // FunctorParser extensions
    // =============================
    

    protected FunctorRef reservedWord(String name) throws ParseException {
        // If we're expecting a table but we don't know which one, then see if the name
        // is the name of a table.  If so, then this is the table we're expecting.
        if (table == null) {
            DataTable maybeTable = ((DataSet) getBoundObject()).getTable(name);
            if (maybeTable != null) {
                setCurrentTable(maybeTable);
                return new GeneratorRef(new Constant<DataTable>(table), DataTable.class);
            }
        }
        
        // Next, see if the name is the same as the current table then return a
        // constant reference to the table
        
        if (table != null && name.equals(table.getName()))
            return new GeneratorRef(new Constant<DataTable>(table), DataTable.class);

        // Otherwise, if we already have a current table, then see if the name is
        // a column reference within the table
        
        if (table != null) {
            DataColumn col = table.getColumn(name);
            if (col != null) 
                return makeColumnRef(table, col);
        }
        
        return null;
    }

    /**
     * Allows for (not necessarily constant) predefined fields to be added to the grammar
     */
    protected FunctorRef reservedField(FunctorRef prefix, String name) throws ParseException {
        if (isTableReference(prefix) && getReferencedTable(prefix).equals(table)) {
            DataColumn col = table.getColumn(name);
            if (col != null) {
                return makeColumnRef(table, col);
            }
        }                       
            
        return super.reservedField(prefix, name);
    }

    /**
     * Allows for function-style names to be added to the grammar.
     */
    protected FunctorRef reservedFunction(String name, FunctorRef[] args) throws ParseException{
        if (inTableContext) {
            assert table != null;

            // if the last argument returns a boolean, then it is interpreted as a
            // filter on the table: only those rows that meet the filter condition
            // are included in the computation
            FunctorRef lastArgRef = args[args.length - 1];
            boolean hasFilter = (lastArgRef.getReturnType() == Boolean.class);

            UnaryFunctor<DataRow,Boolean> filter = (hasFilter) ?
                ((UnaryFunctorRef) lastArgRef).getFunctor():
                new ConstantUnary<DataRow,Boolean>(Boolean.TRUE);
            
            // If count doesn't have a filter, then it doesn't require iterating over the
            // rows of the table: having the list of rows is good enough
            if ("count".equals(name)) {
                if (hasFilter) {
                    // Iterates the table, counting those that satisfy the filter.  Count
                    // returns Long, so we have to convert to Integer.
                    UnaryFunctor<DataTable,Integer> countFn =
                        new ValueOf<Long,Integer>(Integer.class)
                            .compose(new Count(filter))
                            .compose(iterateTableFn); 
                    
                    return new GeneratorRef(countFn.bind(table),Long.class);
                }
                else {
                    return new GeneratorRef(getRowCountFn.bind(table), Integer.class);
                }
            }

            UnaryFunctor<DataTable,? extends Iterator/**/> filterTableFn = (hasFilter) ?
                makeFilterFn.bind2nd(filter).compose(iterateTableFn): iterateTableFn;

            Generator<? extends Iterator/**/> iterateRows = filterTableFn.bind(table);

            // The remaining functions evaluate an expression on every qualifying row
            // in the table.  The first argument is the expression to be evaluated,
            // so we'll set up a transform that returns the value of the expression
            // when passed the row
            Class type = args[0].getReturnType();
            if (type.isPrimitive())
                type = getBoxedType(type);
            
            TransformUnary xform = new TransformUnary(((UnaryFunctorRef) args[0]).getFunctor());
            
            // Average doesn't have an easy way to compute without iterating, but the
            // functor (like Count) takes an iteration and returns the computed value
            if ("avg".equals(name)) {
                validateArgument(Number.class, type, name);

                Average avg = new Average(type);
                return new GeneratorRef(avg.generate(xform.generate(iterateRows)), type);
            }
            
            // All of the other summary functions require iterating over the list of
            // rows in the table, and applying a binary function to the current value
            // and the previous intermediate value
            BinaryFunctor bf = null;
            
            if ("max".equals(name)) {
                validateArgument(Comparable.class, type, name);
                bf = new Max(new net.sf.jga.util.ComparableComparator());
            }
            else if ("min".equals(name)) {
                validateArgument(Comparable.class, type, name);
                bf = new Min(new net.sf.jga.util.ComparableComparator());
            }
            else if ("sum".equals(name)) {
                validateArgument(Number.class, type, name);
                bf = new Plus(type);
            }
            
            // The simpler summary functions only require an iteration, with no further
            // processing.
            if (bf != null) {
                Generator gen = new Accumulate(bf).generate(xform.generate(iterateRows));
                return new GeneratorRef(gen, type);
            }
        }

        return super.reservedFunction(name, args);
    }


    // ======================
    // implementation details
    // ======================

    
    /**
     * Sets the table against which column references will be created.  There can only be
     * a single table involved in any given expression.
     */
    private void setCurrentTable(DataTable table) throws ParseException {
        if (this.table != null && table != null)
            throw new ParseException("Parser is currently associated with table " +this.table);

        this.table = table;
    }

    /**
     * Builds a functor for a given table and column.  The functor takes a row in the
     * table and returns the value of the appropriate column.
     */
    private UnaryFunctorRef makeColumnRef(DataTable table, DataColumn column) {
        // Builds a functor that takes a Row, and returns an array consisting
        // of that row and the column we've been given
        ApplyUnary<DataRow> args =
            new ApplyUnary<DataRow>(new UnaryFunctor[]
                { new Identity<DataRow>(),new ConstantUnary<DataRow,DataColumn>(column) });
        
        // getValue(col,row) method as a functor
        InvokeMethod<DataTable,?> getValue =
            new InvokeMethod<DataTable,Object>(DataTable.class,"getValue",
                                               new Class[] {DataRow.class, DataColumn.class});
        
        // tie the two together.  The result is a functor that takes a row and returns
        // the value in the designated column
        UnaryFunctor<DataRow,?> value = getValue.bind1st(table).compose(args);
        
        // Return a parser description of the functor we've built.
        return new UnaryFunctorRef(value, DataRow.class, ARG_NAME[0], column.getType());
    }
    
    
    /**
     * returns true if the functor reference is one that returns a specific data table.
     */
    private boolean isTableReference(FunctorRef ref) {
        return ref != null && ref.getReturnType().equals(DataTable.class) &&
               ref.getReferenceType() == FunctorRef.CONSTANT;
    }

    
    /**
     * returns the specific table to which the given functor refers.  Assumes isTableReference(ref),
     * will throw ClassCastException if not
     */
    private DataTable getReferencedTable(FunctorRef ref) {
        return (DataTable)((GeneratorRef) ref).getFunctor().gen();
    }


    /**
     * Constructs and throws a ParseException if the return type of a subexpression
     * (the found type) is not a subtype of the type required for the given function.
     */
    private void validateArgument(Class reqType, Class foundType, String fnName)
            throws ParseException
    {
        if (reqType.isAssignableFrom(foundType)) {
            return;
        }
        
        String msg = "Unable to compute {0} of type {1}";
        Object[] msgargs = new Object[] { fnName, foundType.getSimpleName() };
        throw new ParseException(MessageFormat.format(msg, msgargs));
    }
}
