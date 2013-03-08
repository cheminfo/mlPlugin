/**
 * Name: Variable
 * Goal: represents a variable used by a fuzzy system. It is caracterised by a name,
 *       linguistic terms the variable may take and membership function. A variable
 *       also has a flag to indicate its original position in the dataset.
 * Methods: Only get and set methods. A Variable only stores data.
 * Usage: Except for construction, which requires above specified information,
 *        a variable should only be used internally.
 * @see: MembershipFunction
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.FuzzyLogic;

import java.util.Enumeration;
import java.util.LinkedList;

public class Variable {
    
    //the variables name
    private String name;
    
    //the linguistic terms the variable can take
    private String[] terms;
    
    //the membership function defining the variable
    private MembershipFunction mf;
    
    //the attribute index in the Weka Instance, used for practical purpose
    private int attribute_index;
    
    //default triple terms variable
    public static final String[] DEFAULT_TRIPLE_TERMS = {"low", "medium", "high"};
    
    //default binary terms variable
    public static final String[] DEFAULT_BINARY_TERMS = {"low", "high"};
    
    //default binary terms variable
    public static final String[] OUT_TERMS = {"yes", "no"};
    
    
    /**
     * Name: Variable
     * Goal: creates a variable, terms are in Enumeration form, since Weka gives
     *       attributes in this form
     * @param name: the variable's name
     * @param terms: the linguistic terms the variable can take
     * @param mf: the membership function defining the variable
     * @param index: the attribute index from the weka instances, used for practical purpose only
     */
    public Variable(String name, Enumeration terms, MembershipFunction mf, int index){
        this.name = name;
        LinkedList<String> list = new LinkedList<String>();
        Object term;
        while(terms.hasMoreElements()){
            term = terms.nextElement();
            list.add((String) term);
        }
        this.terms = list.toArray(new String[list.size()]);
        this.attribute_index = index;
        if(mf != null)
            this.mf = mf;
        else{
            this.mf = new MembershipFunction(null, Double.MIN_VALUE, Double.MAX_VALUE, new double[this.terms.length]);
        }
        this.mf.setVar(this);
    }/*end Variable*/
    
    /**
     * Name: Variable
     * Goal: creates a Variable from given parameters
     * @param name: the variable's name
     * @param terms: the terms the variable can take
     * @param mf: the membership function defining the variable
     * @param index: the attribute index from the weka instances, used for practical purpose only
     */
    public Variable(String name, String[] terms, MembershipFunction mf, int index){
        this.attribute_index = index;
        this.name = name;
        this.terms = terms;
        this.mf = mf;
        this.mf.setVar(this);
    }/*end Variable*/
    
    /**
     * Name: Variable
     * Goal: creates a default variable, either binary or triple
     * @param index: the attribute index from the weka instances, used for practical purpose only
     * @param is_binary: indicates if the variable has to be binary or triple
     */
    public Variable(int index, boolean is_binary){
        this("variable "+index,
             is_binary?DEFAULT_BINARY_TERMS:DEFAULT_TRIPLE_TERMS,
             is_binary?new MembershipFunction(true):new MembershipFunction(false),
             index);
        mf.setVar(this);
    }/*end Variable*/

    /**
     * Name: getAttributeIndex
     * @return int the weka instances attribute index
     */
    public int getAttributeIndex() {
        return attribute_index;
    }/*end getAttributeIndex*/

    /**
     * Name: setAttributeIndex
     * @param index: the weka instances attribute index
     */
    public void setAttributeIndex(int index) {
        this.attribute_index = index;
    }/*end setAttributeIndex*/

    /**
     * Name: getMf
     * @return MembershipFunction: the MF of the variable
     */
    public MembershipFunction getMf() {
        return mf;
    }/*end getMF*/

    /**
     * Name: setMf
     * @param mf: the membership function
     */
    public void setMf(MembershipFunction mf) {
        this.mf = mf;
    }/*end setMF*/

    /**
     * Name: getName
     * @return String: the variable's name
     */
    public String getName() {
        return name;
    }/*end getName*/

    /**
     * Name: setName
     * @param name: the new variable's name
     */
    public void setName(String name) {
        this.name = name;
    }/*end setName*/

    /**
     * Name: getTerms
     * @return String[]: the terms the variable can take
     */
    public String[] getTerms() {
        return terms;
    }/*end getTerms*/

    /**
     * Name: setTerms
     * @param terms: the terms the variable can take
     */
    public void setTerms(String[] terms) {
        this.terms = terms;
    }/*end setTerms*/
    
    
    @Override
    public String toString(){
        String str = "Variable: "+name+"\n";
        str += "Terms:\n";
        for(String t : terms)
            str += t+"\t";
        str += "\nMembership Function points:";
        str += "\nmin: "+mf.getMinVal()+"\n";
        for(int i = 0; i < mf.getZeroPoints().length; i++)
            str += "zero point "+i+": "+mf.getZeroPoints()[i]+"\n";
        str += "max: "+mf.getMaxVal();
        return str;
    }
    
}/*end Variable*/
