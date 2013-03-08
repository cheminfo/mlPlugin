/**
 * Name: RuleTerm
 * Goal: represents the term of a rule. It is composed of a variable, which gives the basis
 *       for evaluation, an expected linguistic term and the index of said term in its list.
 *       the term index is only used for internal purpose.
 * Methods:
 *      - getters and setters: all above specified elements have a get and set method,
 *        since this structure only contains information
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.FuzzyLogic;

public class RuleTerm {
    
    //variable associated with rule term
    private Variable var;
    
    //term variable is expected to have to fulfill rule
    private String term;
    
    //index of the expected term
    private int term_index;
    
    /**
     * Name: RuleTerm
     * Goal: creates a term that will be used in a rule
     * @param var: the variable used for the rule
     * @param term: the expected term for the variable
     * @param term_index: term index in the variable, used for practical purpose
     */
    public RuleTerm(Variable var, String term, int term_index){
        this.var = var;
        this.term = term;
        this.term_index = term_index;
    }/*end RuleTerm*/
    
    /**
     * Name: RuleTerm
     * Goal: creates an empty rule term
     */
    public RuleTerm(){
        this(null, null, 0);
    }

    /**
     * Name: getTermIndex
     * @return int: the index of the term expected in the rule
     */
    public int getTermIndex() {
        return term_index;
    }/*end getTermIndex*/

    /**
     * Name: setTermIndex
     * @param term_index: the new index for the term
     */
    public void setTermIndex(int term_index) {
        this.term_index = term_index;
    }
    
    /**
     * Name: getTerm
     * @return String: the linguistic term of the variable
     */
    public String getTerm() {
        return term;
    }

    /**
     * Name: setTerm
     * @param term: the new term the rule is expecting
     */
    public void setTerm(String term) {
        this.term = term;
    }/*end setTerm*/

    /**
     * Name getVar
     * @return Variable: the variable associated to the rule term
     */
    public Variable getVar() {
        return var;
    }/*end getVar*/

    /**
     * Name: setVar
     * @param var: the new variable for this rule term
     */
    public void setVar(Variable var) {
        this.var = var;
    }/*end setVar*/
    
    @Override
    public String toString(){
        return var.getName()+" IS "+term;
    }
    
}/*end RuleTerm*/
