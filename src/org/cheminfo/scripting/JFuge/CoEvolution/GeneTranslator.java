/**
 * Name: GeneTranslator
 * Goal: Transforms a rule gene and a mf gene into a fuzzy system and reciprocally
 * Methods:
 *      - fuzzyToGene: transforms a fuzzy system into a mf and a rule gene. the
 *                     array the function returns contains the mf gene in the
 *                     first cell and the rule gene in the second
 *      - geneToFuzzy: transforms a rule and a mf gene into a fuzzy system
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.CoEvolution;

import java.util.Enumeration;
import java.util.LinkedList;
import org.cheminfo.scripting.JFuge.FuzzyLogic.FuzzySystem;
import org.cheminfo.scripting.JFuge.FuzzyLogic.MembershipFunction;
import org.cheminfo.scripting.JFuge.FuzzyLogic.Rule;
import org.cheminfo.scripting.JFuge.FuzzyLogic.RuleTerm;
import org.cheminfo.scripting.JFuge.FuzzyLogic.Variable;
import weka.core.Attribute;
import weka.core.Instances;

public class GeneTranslator {
    
    /**
     * Name: fuzzyToGene
     * Goal: returns the mf and rule gene representing the given system
     * @param system: the system to transform into genes
     * @return Gene[]: the mf and rule genes computed from the system
     */
    public static Gene[] fuzzyToGene(FuzzySystem system, double[][] min_max){
        
        Gene[] result = new Gene[2];
        
        LinkedList<Variable> in_vars = system.getInVars();
        LinkedList<Variable> out_var = system.getOutVar();
        result[0] = makeMFGene(in_vars, out_var, min_max);
        result[1] = makeRuleGene(system.getRules(), in_vars.size()+1);
        return result;
    }/*end fuzzyToGene*/
    
    /**
     * Name: geneToFuzzy
     * Goal: transforms the given genes into a fuzzy system. Since systems are
     *       built on the data they have to classify, the dataset is also
     *       provided for construction information. Additional data is also provided
     * @param mf_gene: the membership function gene
     * @param rule_gene: the rule gene
     * @param train_data: the dataset the system has to classify.
     *                    used for attribute information
     * @param is_binary: indicates if membership functions are binary or tertiary
     * @param min: indicates the minimum value a mf may take
     * @param max: indicates the maximum value a mf may take
     * @return FuzzySystem: the system computed from given genes
     */
    public static FuzzySystem geneToFuzzy(MFGene mf_gene, RuleGene rule_gene, Instances train_data, boolean is_binary, double[][] min_max){
        
        FuzzySystem fs;
        //compute variables from mf gene and dataset information
        LinkedList<Variable> in_vars = makeVariables(mf_gene, train_data.enumerateAttributes(), train_data.classAttribute(), min_max, is_binary);
        //output variable is always the last one
        LinkedList<Variable> out_vars = new LinkedList<Variable>();
        int count = train_data.numAttributes()-1;
        int size = in_vars.size();
        while(count++ < size)
                out_vars.addFirst(in_vars.removeLast());
        //compute rules from rule gene and variables
        LinkedList<Rule> rules = makeRules(rule_gene, in_vars, out_vars, Rule.AND);
        String defuzzy = FuzzySystem.DEFUZZY_SINGLETON;
        //construct system
        fs = new FuzzySystem(in_vars, out_vars, rules, defuzzy, train_data);
        /*for(Rule v : fs.getRules())
            System.out.println(v);*/
        
        return fs;
    }/*end geneToFuzzy*/
    
    /**
     * Name: makeRules
     * Goal: reconstructs rules form the rule gene and the variables
     * @param gene: gene containing rule information
     * @param in_vars: input variables to associate with rule terms
     * @param out_var: output variables to associate with rule terms
     * @param connector: the connector used to link rule terms
     * @return the list of rules
     */
    private static LinkedList<Rule> makeRules(RuleGene gene, LinkedList<Variable> in_vars, LinkedList<Variable> out_var, String connector){
        LinkedList<Rule> result = new LinkedList<Rule>();
        LinkedList<RuleTerm> ante;
        LinkedList<RuleTerm> cons;
        Rule rule;
        Variable var;
        String term;
        int term_index;
        int column = 0;
        //read each rule
        for(int i = 0; i < gene.getDNA().length; i++){
            ante = new LinkedList<RuleTerm>();
            cons = new LinkedList<RuleTerm>();
            //add input vars and expected terms (== RuleTerm) to antecedents
            for(int j = 0; j < in_vars.size(); j++){
                if(gene.getDNA()[i][j][1] != -1){
                    var = in_vars.get(gene.getDNA()[i][j][0].intValue());
                    term = in_vars.get(gene.getDNA()[i][j][0].intValue()).getTerms()[gene.getDNA()[i][j][1].intValue()];
                    //System.out.println((int)gene.getDNA()[i][j][0]+": "+var);
                    term_index = gene.getDNA()[i][j][1].intValue();
                    ante.add(new RuleTerm(var, term, term_index));
                }
            }
            if(ante.size() == 0 && i != gene.getDNA().length-1)
                continue;
            //set current rule output value
            column = in_vars.size();
            for(int j = 0; j < out_var.size(); j++){
                var = out_var.get(j);
                term = out_var.get(j).getTerms()[gene.getDNA()[i][column][1].intValue()];
                term_index = gene.getDNA()[i][column++][1].intValue();
                cons.add(new RuleTerm(var, term, term_index));
            }
            rule = new Rule(ante.size()==0?null:ante, cons, connector);
            result.add(rule);
        }
        
        return result;
    }/*end makeRules*/
    
    /**
     * Name: makeVariables
     * Goal: reconstructs variables from a mf gene and information from the dataset
     * @param gene: the mf gene containing zero points
     * @param attributes: an enumeration containing dataset attribute information
     * @param class_attribute: the output attribute
     * @param min
     * @param max
     * @return 
     */
    private static LinkedList<Variable> makeVariables(MFGene gene, Enumeration attributes, Attribute class_attribute, double[][] min_max, boolean is_binary){
        LinkedList<Variable> result = new LinkedList<Variable>();
        LinkedList<String> name_list = new LinkedList<String>();
        LinkedList<String> term_list = new LinkedList<String>();
        LinkedList<Enumeration> terms = new LinkedList<Enumeration>();
        Attribute att;
        while(attributes.hasMoreElements()){
            att = (Attribute)attributes.nextElement();
            name_list.add(att.name());
            if(!(att.isNumeric())){
                terms.add(att.enumerateValues());
            }
            else{
                terms.add(null);
            }
                
        }
        
        terms.add(class_attribute.enumerateValues());
        name_list.add(class_attribute.name());
        Object[] names = name_list.toArray();
        Variable var;
        MembershipFunction mf;
        double[] zero_points;
        String[] default_terms = is_binary?Variable.DEFAULT_BINARY_TERMS:Variable.DEFAULT_TRIPLE_TERMS;
        //input variables
        for(int i = 0; i < terms.size()-1; i++){
            zero_points = new double[gene.getDNA()[i].length];
            for(int j = 0; j < gene.getDNA()[i].length; j++){
                zero_points[j] = gene.getDNA()[i][j][0];
            }
            mf = new MembershipFunction(null, min_max[i][0], min_max[i][1], zero_points);
            if(terms.get(i) == null){
                var = new Variable((String)names[i], default_terms, mf, i);
            }
            else{
                var = new Variable((String)names[i], terms.get(i), mf, i);
            }
            mf.setVar(var);
            result.add(var);
        }
        //output variables
        int done;
        int value_done = 0;
        for(int i = terms.size()-1; i < gene.getDNA().length; i++){
            zero_points = new double[2];
            done = 0;
            for(int j = 0; j < gene.getDNA()[i].length; j++){
                if(gene.getDNA()[i][j][0]!=null){
                    Double d = gene.getDNA()[i][j][0];
                    zero_points[done++] = d;
                }
            }
            //value_done = 0;
            mf = new MembershipFunction(null, min_max[i][0], min_max[i][1], zero_points);
            var = new Variable(class_attribute.value(value_done++), Variable.OUT_TERMS, mf, i);
            mf.setVar(var);
            result.add(var);
        }
        /*double[] out_zp = new double[class_attribute.numValues()];
        for(int i = 0; i < out_zp.length; i++)
            out_zp[i] = i;
        result.getLast().getMf().setZeroPoints(out_zp);*/
        return result;
    }/*end makeVariables*/
    
    private static MFGene makeMFGene(LinkedList<Variable> in_vars, LinkedList<Variable> out_var, double[][] min_max ){
        MFGene mf_gene;
        int mf_length = in_vars.getFirst().getMf().getZeroPoints().length;
        
        Double[][][] mf_adn = new Double[in_vars.size()+1][mf_length][1];
        
        //input variables
        MembershipFunction mf;
        double min = in_vars.getFirst().getMf().getMinVal();
        double max = in_vars.getFirst().getMf().getMaxVal();
        for(int i = 0; i < in_vars.size(); i++){
            mf = in_vars.get(i).getMf();
            for(int j = 0; j < mf_length; j++){
                mf_adn[i][j][0] = mf.getZeroPoints()[j];
            }
        }
        
        //output variable
        for(int i = 0; i < 2; i++){
            mf_adn[in_vars.size()][i][0] = out_var.get(i).getMf().getZeroPoints()[i];
        }
        
        mf_gene = new MFGene(min_max, in_vars.size());
        mf_gene.setDNA(mf_adn);
        return mf_gene;
    }/*end makeMFGene*/
    
    private static RuleGene makeRuleGene(LinkedList<Rule> rules, int var_count){
        RuleGene rule_gene;
        Double[][][] adn = new Double[rules.size()][var_count][2];
        Rule rule;
        int max_input = rules.getFirst().getAntecedents().getFirst().getVar().getTerms().length;
        int max_output = rules.getFirst().getAntecedents().getLast().getVar().getTerms().length;
        for(int i = 0; i < adn.length; i++){
            rule = rules.get(i);
            if(rule.getAntecedents() != null){
                for(int j = 0; j < var_count-1; j++){
                    if(rule.getAntecedents().size() <= j){
                        adn[i][j][0] = -1.0;
                        adn[i][j][1] = -1.0;
                    }
                    else{
                        adn[i][j][0] = (double)rule.getAntecedents().get(j).getVar().getAttributeIndex();
                        adn[i][j][1] = (double)rule.getAntecedents().get(j).getTermIndex();
                        if(rule.getAntecedents().get(j).getVar().getTerms().length > max_input)
                            max_input = rule.getAntecedents().get(j).getVar().getTerms().length;
                    }
                }
            }
            else{
                for(int j = 0; j < var_count-1; j++){
                    adn[i][j][0] = -1.0;
                    adn[i][j][1] = -1.0;
                }
            }
            adn[i][var_count-1][0] = (double)rule.getConsequent().get(0).getVar().getAttributeIndex();
            adn[i][var_count-1][1] = (double)rule.getConsequent().get(0).getTermIndex();
        }
        rule_gene = new RuleGene(var_count, max_input, max_output);
        rule_gene.setDNA(adn);
        return rule_gene;
    }/*end makeRuleGene*/
    
}/*end GeneTranslator*/
