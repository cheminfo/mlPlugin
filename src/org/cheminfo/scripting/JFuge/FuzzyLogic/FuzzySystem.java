/**
 * Name: FuzzySystem
 * Goal: Allows representation of a complete fuzzy system. This includes input
 *       variables, an output variable and rules.
 * Methods:
 *      - classifyInstances: returns the systems classification for each instance
 *        of given dataset.
 *      - getters: input, output and rules each have a get function
 *      - toString: a system may be printed in a comprehensible way automatically
 * Usage: Create a fuzzy system based on input variables, output variable, rules
 *        and defuzzyfication method specified by the user. A default system based
 *        on the dataset may also be created.
 *        The user may classify data according to the systems specifications.
 * @see: Variable, Rule
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge.FuzzyLogic;

import java.util.LinkedList;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class FuzzySystem {
    
    //defuzzyfication methods
    //center of gravity
    public static final String DEFUZZY_COG = "COG";
    
    //mean of maxima
    public static final String DEFUZZY_MOM = "MOM";
    
    //singleton
    public static final String DEFUZZY_SINGLETON = "SINGLETON";
    
    //input variables for fuzzy system
    private LinkedList<Variable> in_vars;
    
    
    //output variable for fuzzy system
    private LinkedList<Variable> out_var;
    
    
    //list of rules for fuzzy system
    private LinkedList<Rule> rules;
    
    
    //Defuzzification method used by the system
    private String defuzzy_method;
    
    
    //dataset used by fuzzy system
    private Instances data_set;
    
    //the systems fitness value
    private double fitness;
    
    /**
     * Name: FuzzySystem
     * Goal: creates a new fuzzy system, with all required parameters
     * @param in_vars: input variables, indicates linguistic terms, MFs
     *                 @see: Variable
     * @param out_var: output variable generated by the system.
     *                 @see: Variable
     * @param rules: the rules used by the system to determine output
     *               @see: Rule
     * @param defuzzy_method: Method of defuzzyfication used by the system
     *                        Available methods are:
     *                        - Signleton: FuzzySystem.DEFUZZY_SINGLETON
     *                        - Mean Of Maxima: FuzzySystem.DEFUZZY_MOM (not implemented)
     *                        - Center Of Gravity: FuzzySystem.DEFUZZY_COG (not implemented)
     * @param train_dataset: the dataset the system uses to train. Instances are in Weka format 
     */
    public FuzzySystem(LinkedList<Variable> in_vars,
                       LinkedList<Variable> out_var,
                       LinkedList<Rule> rules,
                       String defuzzy_method,
                       Instances train_dataset){
        this.in_vars = in_vars;
        this.out_var = out_var;
        this.rules = rules;
        this.defuzzy_method = defuzzy_method;
        this.data_set = train_dataset;
    }/*end FuzzySystem*/
    
    
    /**
     * Name: FuzzySystem
     * Goal: creates a default fuzzy system to match the dataset
     * @param train_dataset: the dataset used to create the variables of the system and for training
     * @throws Exception: throws exceptions in case of errors, particularly with attribute types
     */
    public FuzzySystem(Instances train_dataset) throws Exception{
        this.data_set = train_dataset;
        this.defuzzy_method = DEFUZZY_SINGLETON;
        this.rules = new LinkedList<Rule>();
        this.rules.add(new Rule());
        this.in_vars = new LinkedList<Variable>();
        Instance inst = train_dataset.instance(0);
        Attribute att;
        Variable var = null;
        for(int i = 0; i < inst.numAttributes(); i++){
            att = inst.attribute(i);
            //input variables
            if(i != inst.classIndex()){
                //creation of variable depending on type
                switch(att.type()){
                    case Attribute.NOMINAL: var = new Variable(att.name(), att.enumerateValues(), null, i); break;
                    case Attribute.NUMERIC: ; var = new Variable(i, false);break;
                    case Attribute.STRING: var = new Variable(att.name(), att.enumerateValues(), null, i); break;
                    default: System.out.println("Attribute type not supported: "+att.type());
                             throw new Exception("Attribute type not supported: "+att.type());
                }
                in_vars.add(var);
                
            }
            //output variable
            else{
                switch(att.type()){
                    case Attribute.NOMINAL: var = new Variable(att.name(), att.enumerateValues(), null, i); break;
                    case Attribute.NUMERIC: ; var = new Variable(i, false); break;
                    case Attribute.STRING: var = new Variable(att.name(), att.enumerateValues(), null, i); break;
                    default: System.out.println("Attribute type not supported: "+att.type());
                             throw new Exception("Attribute type not supported: "+att.type());
                }
                out_var.add(var);
            }
        }
    }/*end FuzzySystem*/
    
    
    /**
     * Name: defuzzy
     * Goal: computes the defuzzyfication for an instance of data
     * @param rule_activation: the activation proportion for each rule
     * @param out_var_values: 
     * @return double: the defuzzyfied value returned by the system
     */
    private double[] defuzzy(double[][][] rule_activation){
        double[] value;
        if(defuzzy_method.equals(DEFUZZY_COG)){
            value = defuzzyCOG(rule_activation);
        }
        else if(defuzzy_method.equals(DEFUZZY_MOM)){
            value = defuzzyMOM(rule_activation);
        }
        else if(defuzzy_method.equals(DEFUZZY_SINGLETON)){
            value = defuzzySingleton(rule_activation);
        }
        else{
            System.out.println("defuzzification method not recognised! "+defuzzy_method);
            value = null;
        }
        return value;
    }/*end defuzzy*/

    /**
     * Name: defuzzyCOG
     * Goal: computes a deffuzyfication by Center of Gravity Method
     * @param rule_activation
     * @param out_var_values
     * @return double: the value of the defuzzyfied system
     */
    private double[] defuzzyCOG(double[][][] rule_activation){
        System.out.print("Not yet implemented!");
        return null;
    }
    
    /**
     * Name: defuzzyMOM
     * Goal: computes a deffuzyfication by Mean of Maxima Method
     * @param rule_activation
     * @param out_var_values
     * @return double: the value of the defuzzyfied system
     */
    private double[] defuzzyMOM(double[][][] rule_activation){
        System.out.print("Not yet implemented!");
        return null;
    }
    
    /**
     * Name: defuzzySingleton
     * Goal: computes a deffuzyfication by Singleton Method
     * @param rule_activation
     * @param out_var_values
     * @return double: the value of the defuzzyfied system
     */
    private double[] defuzzySingleton(double[][][] rule_activation){
        double[] mu_sum = new double[rule_activation[0].length];
        double[] z_mu_sum = new double[rule_activation[0].length];
        for(int i = 0; i < rule_activation.length; i++){
            for(int j = 0; j < rule_activation[i].length; j++){
                mu_sum[j] += rule_activation[i][j][0];
                z_mu_sum[j] += rule_activation[i][j][0]*rule_activation[i][j][1];
            }
        }
        double[] result = new double[rule_activation[0].length];
        for(int i = 0; i < result.length; i++){
            result[i] = mu_sum[i]==0?0:z_mu_sum[i]/mu_sum[i];
        }
        return result;
    }
    
    /**
     * Name: classifyInstances
     * Goal: classifies the data instances given as parameter
     * @param test_dataset: the weka instances to be classified
     * @return double[]: the most likely class for each instance of the dataset
     */
    public double[] classifyInstances(Instances test_dataset){
        double[][] distrib = new double[test_dataset.numInstances()][test_dataset.classAttribute().numValues()];
        double[] result = new double[test_dataset.numInstances()];
        for(int i = 0; i < test_dataset.numInstances(); i++){
            distrib[i] = distributionForInstance(test_dataset.instance(i));
        }
        double max = 0;
        for(int i = 0; i < distrib.length; i++){
            for(int j = 0; j < distrib[i].length;j++){
                if(distrib[i][j] > max){
                    max = distrib[i][j];
                    result[i] = j;
                }
            }
            max = 0;
        }
        return result;
    }/*end classifyInstances*/
    
    public double[][] preclassifyInstances(Instances test_dataset){
        double[][] distrib = distributionForInstances(test_dataset);
        for(int i = 0; i < distrib.length; i++)
            for(int j = 0; j < distrib[i].length; j++)
                distrib[i][j] = Math.round(distrib[i][j]);
        return distrib;
    }
    
    /**
     * Name: distributionForInstances
     * Goal: computes the probability of membership for each available output class
     * @param test_dataset: the dataset to classify
     * @return double[][]: for each instance, the probability of membership to each output class
     */
    public double[][] distributionForInstances(Instances test_dataset){
        double[][] distrib = new double[test_dataset.numInstances()][test_dataset.classAttribute().numValues()];
        for(int i = 0; i < test_dataset.numInstances(); i++){
            distrib[i] = distributionForInstance(test_dataset.instance(i));
        }
        
        return distrib;
    }
    
    /**
     * Name: distributionForInstance
     * Goal: computes the most likely class for a Weka data instance
     * @param inst: the instance to be classified
     * @return double: the most likely class computed by the fuzzy system
     */
    public double[] distributionForInstance(Instance inst){
        double[][][] activation = computeRulesActivation(inst);
        double[] defuzzy = defuzzy(activation);
        return defuzzy;
    }/*end distributionForInstance*/
    
    
    
    private double[][][] computeRulesActivation(Instance inst){
        double[][][] activation = new double[rules.size()][inst.classAttribute().numValues()][2];
        for(int i = 0; i < rules.size(); i++){
            activation[i] = rules.get(i).computeActivation(inst);
        }
        //default rule
        double default_acti = defaultActivation(activation);
        for(int i = 0; i < inst.classAttribute().numValues(); i++)
            activation[rules.size()-1][i][0] = default_acti;
        return activation;
    }
    
    private double defaultActivation(double[][][] acti){
        double max = acti[0][0][0];
        for(int i = 0; i < acti.length-1; i++){
            for(int j = 0; j < acti[i].length; j++){
                if(acti[i][j][0] > max)
                    max = acti[i][j][0];
            }
        }
        return 1-max;
    }

    public LinkedList<Variable> getInVars() {
        return in_vars;
    }

    public LinkedList<Variable> getOutVar() {
        return out_var;
    }

    public LinkedList<Rule> getRules() {
        return rules;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public static void main(String[] args){
        Instances train_data = DataLoader.loadData(null, null, "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_train.arff", true);
        Instances test_data = DataLoader.loadData(null, null, "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_full.arff", true);
        double[] sl_pts = {2.786348372437899, 3.1024771475077357, 6.629080682469279};//{5.68, 6.45, 7.10};
        double[] sw_pts = {2.525875497462685, 5.686439333534521, 7.639375581236261};//{3.16, 3.16, 3.45};
        double[] pl_pts = {0.4061060402582523, 0.6785615867206264, 1.7329389317637574};//{1.19, 1.77, 6.03};
        double[] pw_pts = {0.34383391837783756, 1.9033914109772292, 5.555980366928549};//{1.55, 1.65, 1.74};
        double[] out_pts = {4.577467025943407, 4.7273122297408685, 5.361837096580746};//{0, 1, 2};
        MembershipFunction sl_mf = new MembershipFunction(null, 0, 20, sl_pts);
        MembershipFunction sw_mf = new MembershipFunction(null, 0, 20, sw_pts);
        MembershipFunction pl_mf = new MembershipFunction(null, 0, 20, pl_pts);
        MembershipFunction pw_mf = new MembershipFunction(null, 0, 20, pw_pts);
        MembershipFunction flower_class = new MembershipFunction(null, 0, 20, out_pts);
        
        String[] mf_terms = {"low", "medium", "high"};
        String[] class_terms = {"setosa", "versicolor", "virginica"};
        
        Variable sepal_length = new Variable("sepal length", mf_terms, sl_mf, 0);
        Variable sepal_width = new Variable("sepal width", mf_terms, sw_mf, 1);
        Variable petal_length = new Variable("petal length", mf_terms, pl_mf, 2);
        Variable petal_width = new Variable("petal width", mf_terms, pw_mf, 3);
        Variable output = new Variable("flower", class_terms, flower_class, 4);
        
        sl_mf.setVar(sepal_length);
        sw_mf.setVar(sepal_width);
        pl_mf.setVar(petal_length);
        pw_mf.setVar(petal_width);
        flower_class.setVar(output);
        
        LinkedList<Variable> in_vars = new LinkedList<Variable>();
        in_vars.add(sepal_length);
        in_vars.add(sepal_width);
        in_vars.add(petal_length);
        in_vars.add(petal_width);
        
        /*RuleTerm ante1 = new RuleTerm(petal_length, "high", 2);
        RuleTerm ante2 = new RuleTerm(sepal_width, "low", 0);
        RuleTerm ante3 = new RuleTerm(petal_width, "low", 0);
        RuleTerm ante4 = new RuleTerm(sepal_length, "medium", 1);
        RuleTerm ante5 = new RuleTerm(petal_width, "medium", 1);*/
        RuleTerm cons1 = new RuleTerm(output, "virginica", 2);
        RuleTerm cons2 = new RuleTerm(output, "setosa", 0);
        RuleTerm cons3 = new RuleTerm(output, "versicolor", 1);
        
        RuleTerm ante1 = new RuleTerm(sepal_width, "medium", 1);
        RuleTerm ante2 = new RuleTerm(sepal_width, "high", 2);
        RuleTerm ante3 = new RuleTerm(petal_width, "low", 0);
        
        LinkedList<RuleTerm> ante_rule1 = new LinkedList<RuleTerm>();
        ante_rule1.add(ante1);
        LinkedList<RuleTerm> ante_rule2 = new LinkedList<RuleTerm>();
        ante_rule2.add(ante2);
        //ante_rule2.add(ante3);
        LinkedList<RuleTerm> ante_rule3 = new LinkedList<RuleTerm>();
        ante_rule3.add(ante3);
        //ante_rule3.add(ante4);
        //ante_rule3.add(ante5);
  
       
        
        /*Rule r1 = new Rule(ante_rule1, cons1, Rule.AND);
        Rule r2 = new Rule(ante_rule2, cons2, Rule.AND);
        Rule r3 = new Rule(ante_rule3, cons2, Rule.AND);
        Rule default_rule = new Rule(null, cons2, Rule.AND);*/
        //Rule r1 = new Rule(ante_rule1, cons3, Rule.AND);
        //Rule r2 = new Rule(ante_rule2, cons2, Rule.AND);
        //Rule r3 = new Rule(ante_rule3, cons2, Rule.AND);
        //Rule default_rule = new Rule(null, cons1, Rule.AND);
        LinkedList<Rule> rules = new LinkedList<Rule>();
        //rules.add(r1);
        //rules.add(r2);
        //rules.add(r3);
        //rules.add(default_rule);
        
        try {
            //FuzzySystem sys = new FuzzySystem(in_vars, output, rules, FuzzySystem.DEFUZZY_SINGLETON, train_data);
            /*System.out.println(sys);
            //sys.classifyInstances(train_data);
            Gene[] genes = GeneTranslator.fuzzyToGene(sys);
            System.out.println("mfs:");
            for(int i = 0; i < genes[0].getAdn().length; i++){
                for(int j = 0; j < genes[0].getAdn()[0].length; j++){
                    System.out.print(genes[0].getAdn()[i][j][0]+" ");
                }
                System.out.println();
            }
            System.out.println("rules:");
            for(int i = 0; i < genes[1].getAdn().length; i++){
                System.out.print("IF ");
                for(int j = 0; j < genes[1].getAdn()[0].length; j++){
                    System.out.print(genes[1].getAdn()[i][j][0]+" IS ");
                    System.out.print(genes[1].getAdn()[i][j][1]+" ");
                }
                System.out.println();
            }
            
            FuzzySystem fs2 = GeneTranslator.geneToFuzzy((MFGene)genes[0], (RuleGene)genes[1], train_data, true, 0.0, 7.7);
            System.out.println("system: "+fs2);*/
            //double[] result = sys.classifyInstances(test_data);
            /*double[] result2 = new double[test_data.numInstances()];
            for(int i = 0; i < result.length; i++){
                result2[i] = Math.round(result[i]);
                System.out.println(test_data.instance(i).classValue()+", "+result[i]+", "+result2[i]);
            }*/
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public String toString(){
        String str = "Fuzzy System\n";
        str += "======================================================\n";
        str += "Input Variables:\n";
        for(Variable v: in_vars){
            str += "------------------------------------------------------\n";
            str +=v+"\n";
            str += "------------------------------------------------------\n";
        }
        str += "======================================================\n";
        str += "Output Variable:\n";
        for(Variable v: out_var){
            str += "------------------------------------------------------\n";
            str +=v+"\n";
            str += "------------------------------------------------------\n";
        }
        str += "Defuzzy method: "+defuzzy_method+"\n";
        str += "======================================================\n";
        str += "Rules:\n";
        for(Rule r: rules)
            str += r+"\n";
        str += "======================================================\n";
        return str;
    }
    
}/*end FuzzySystem*/
