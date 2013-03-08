/**
 * Name: Rule
 * Goal: represents a rule of fuzzy system. It is composed of antecedents and a
 *       consequent. These are rule terms, and are composed of a variable and a term
 *       Antecedents are linked by a connector (AND or OR), indicating if the
 *       activation of the rule should be computed with a min or a max.
 * Methods:
 *      - computeActivation: computes how much a rule is activated with given data
 *        instance. First, the antecedents are evaluated and the highest or lowest
 *        term is chosen, according to the connector. Then, the conequent also
 *        gives the expected value with given data instance.
 *      - getters and setters: all above specified elements have a get and set method
 * @see: RuleTerm
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.FuzzyLogic;

import java.util.LinkedList;
import org.cheminfo.scripting.Utils.Tools;
import weka.core.Instance;

public class Rule {
    
    //rule connectors
    //And connector
    public static final String AND = "AND";
    
    //Or connector
    public static final String OR = "OR";
    
    //list of antecedents for rule
    private LinkedList<RuleTerm> antecedents;
    
    //consequent of rule
    private LinkedList<RuleTerm> consequent;
    
    //term connector for rule (OR/AND)
    private String connector;
    
    /**
     * Name: Rule
     * Goal: creates a new rule, with given parameters
     * @param antecedents: list of rule terms, indicating antecedents
     * @param consequent: rule term indicating the consequent of the rule
     * @param connector: indicates which operation will be used to compute rule activation
     *                   (AND => min, OR => max)
     * @see RuleTerm
     */
    public Rule(LinkedList<RuleTerm> antecedents, LinkedList<RuleTerm> consequent, String connector){
        this.antecedents = antecedents;
        this.consequent = consequent;
        this.connector = connector;
    }/*end rule*/
    
    /**
     * Name: Rule
     * Goal: creates a new default rule, without any antecedents or consequent
     *       Connector is AND. Used by the genetic algorithm
     */
    public Rule(){
        this(null, null, AND);
    }/*end Rule*/
    
    /**
     * Name: computeActivation
     * Goal: computes the numeric value of the activation of the rule, depending on
     *       the provided data
     * @param inst: the instance with which the activation is computed
     * @return double:  the activation value of the rule
     */
    public double[][] computeActivation(Instance inst){
        //compute how much each term is activated
        double[][] result = new double[inst.classAttribute().numValues()][2];
        //check if default rule
        if(antecedents == null){
            for(int i = 0; i < inst.classAttribute().numValues(); i++){
                result[i][1] = consequent.get(i).getVar().getMf().getZeroPoints()[consequent.get(i).getTermIndex()];;
            }
            return result;
        }
        
        double[] rule_term_activation = new double[antecedents.size()];
        Variable var;
        RuleTerm current;
        double[] mf_eval;
        //if antecedents are null, the rule is the default one
        //get the zero point of the consequents value.
        
        for(int i = 0; i < antecedents.size(); i++){
            current = antecedents.get(i);
            var = current.getVar();
            //TODO check missing values
            
            mf_eval = var.getMf().eval(inst.value(var.getAttributeIndex()));
            rule_term_activation[i] = mf_eval[current.getTermIndex()];
        }
        double activation;
        if(connector.equals(AND))
            activation = Tools.min(rule_term_activation);
        else
            activation = Tools.max(rule_term_activation);

        for(int i = 0; i < result.length; i++){
            result[i][0] = activation;
            result[i][1] = consequent.get(i).getVar().getMf().getZeroPoints()[consequent.get(i).getTermIndex()];
        }
        return result;
    }/*end computeActivation*/

    /**
     * Name: getAntecedents
     * @return LinkedList<RuleTerm>: the antecedents of the rule
     */
    public LinkedList<RuleTerm> getAntecedents() {
        return antecedents;
    }/*end getAntecedents*/

    /**
     * Name: setAntecedents
     * @param antecedents: the new antecedents
     */
    public void setAntecedents(LinkedList<RuleTerm> antecedents) {
        this.antecedents = antecedents;
    }/*end setAntecedents*/

    /**
     * Name: getConnector
     * @return String: the connector used for the rule
     */
    public String getConnector() {
        return connector;
    }/*end getConnector*/

    /**
     * Name: setConnector
     * @param connector: the new connector to be used
     */
    public void setConnector(String connector) {
        this.connector = connector;
    }/*end setConnector*/

    /**
     * Name: getConsequent
     * @return RuleTerm: the consequent of the rule
     */
    public LinkedList<RuleTerm> getConsequent() {
        return consequent;
    }/*end getConsequent*/

    /**
     * Name: setConsequent
     * @param consequent: the new consequent to the rule
     */
    public void setConsequent(LinkedList<RuleTerm> consequent) {
        this.consequent = consequent;
    }/*end setConsequent*/
    
    @Override
    public String toString(){
        String str = "";
        if(antecedents == null)
            str = "ELSE ";
        else{
            str += "IF ";
            for(int i = 0; i < antecedents.size(); i++){
                str += antecedents.get(i).toString() + " ";
                if(i < antecedents.size()-1)
                    str += connector+" ";
            }
            str += "THEN ";
        }
        str += "{";
        for(int i = 0; i < consequent.size(); i++){
            str += consequent.get(i).toString()+" ";
        }
        str += "}";
        //str += consequent.toString();
        return str;
    }
    
}/*end Rule*/
