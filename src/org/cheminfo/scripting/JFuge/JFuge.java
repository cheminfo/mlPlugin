/**
 * Name: JFuge
 * Goal: Allows creation of a fuzzy system from a dataset given by the user.
 *       The system is specialized in classifing data instances
 *       The system is created through genetic evolution. The system depends on
 *       its ability to classify data, the precision of the predictions, and the
 *       complexity of the system (the number of rules and the mean number of
 *       variables per rule). The genetic algorithm may be parametrized by the user
 * Methods:
 *      - createClassifier: creates a classifier with given options. Runs the
 *                          evolution and selects the best available fuzzy system
 *                          for further classification
 *      - classifyInstances: classifies a Weka dataset according the system
 *                           previously evolved
 *      - distributionForInstances: computes the distribution of each instance
 *                                  in a Weka dataset
 *      - getFuzzySystem: returns the fittest evolved fuzzy system
 *      - setOptions: changes the options of the evolutionary algorithm. This
 *                    method will have no effect after system has been evolved
 *                    (this means after use of createClassifier)
 * Options: Available options are:
 *        option name (command line option, default value)
 *      - mutation_rate (-M, 0.1): changes the rate at which genes mutate.
 *                                 default: 0.1. MUST be between 0-1
 *      - crossover_rate (-C, 0.2): changes the rate at which genes cross.
 *                                  default: 0.2. MUST be between 0-1
 *      - selection_rate (-S, 0.3): changes the rate at which genes are chosen
 *                                  for reproduction. default: 0.3. MUST be between 0-1
 *      - num_generations (-G, 200): sets the number of generations the genetic
 *                                   algorithm will run. default: 200. MUST be positive
 *      - selection algorithm (-A, ROULETTE): sets the algorithm used for
 *                                            selecting parents. default:
 *                                            roulette selection. Available values:
 *                                            "roulette", "tournament" and "random"
 *      - elitism rate (-E, 0): sets the rate of elite genes to keep into the next
 *                              generation. default value: 0. MUST be between 0-1
 *      - tournament size (-T, 10): sets the number of participants in the tournament
 *                                  selection. If this option is set by the user,
 *                                  selection algorithm option (-A) will be ignored
 *      - population size (-P, 300): sets the number of individuals the genetic
 *                                   algorithm evolves. default value: 300
 *      - error algorithm (-R, RMSE): sets the algorithm used to compute prediction error
 *                                    Default value is RMSE. Available options are:
 *                                    "rmse", "mse", "rrse" and "rae"
 *      - classification weight (-W, 1): sets the weight given to the classification rate
 *                                       during fitness computation. default value: 1
 *                                       MUST be between 0-1
 *      - error weight (-X, 1): sets the weight given to the prediction error during
 *                              fitness computation. Default value: 1
 *                              MUST be between 0-1
 *      - rule number weight (-Y, 1): sets the weight given to number of rules during
 *                                    fitness computation. Default value: 1
 *                                    MUST be between 0-1
 *      - var per rule number (-Z, 1): sets the weight given to number of variables
 *                                     per rule during fitness computation.
 *                                     Default value: 1. MUST be between 0-1
 * 
 * Usage: First, parametrize, create and train the fuzzy system with
 *        createClassifier. Then, classify new data with classifyInstances or
 *        distributionForInstances. Options setting after use of createClassifier
 *        will have NO effect.
 *        
 * @author Numa Trezzini
 */
package org.cheminfo.scripting.JFuge;

import java.util.LinkedList;
import org.cheminfo.function.Function;
import org.cheminfo.scripting.JFuge.CoEvolution.Coevolution;
import org.cheminfo.scripting.JFuge.FuzzyLogic.FuzzySystem;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.core.Instances;
import weka.core.Utils;

public class JFuge extends Function{
    
    //available selection algorithms
    public static final String ROULETTE_SELECTION = "roulette";
    
    public static final String TOURNAMENT_SELECTION = "tournament";
    
    public static final String RANDOM_SELECTION = "random";
    
    //available error computation algorithms
    public static final String ERROR_RMSE = "rmse";
    
    public static final String ERROR_MSE = "mse";
    
    public static final String ERROR_RRSE = "rrse";
    
    public static final String ERROR_RAE = "rae";
    
    //mutation rate of genes. option to set: -M
    private double mutation_rate = 0.1;
    
    //crossover rate of genes. option to set: -C
    private double crossover_rate = 0.2;
    
    //selection rate of genes. option to set: -S
    private double selection_rate = 0.3;
    
    //number of generation to evolve. option to set: -G
    private int num_generations = 200;
    
    //algorithm used for selecting parent genes. option to set: -A
    private String selection_algorithm = ROULETTE_SELECTION;
    
    //rate of elite to keep into next generation. option to set: -E
    private double elitism_rate = 0;
    
    //in case of tournament selection, size of tournament. option to set: -T
    private int tournament_size = 10;
    
    //number of individuals in population. option to set: -P
    private int pop_size = 100;
    
    //algorithm used for computing classification error. option to set: -R
    private String error_algorithm = ERROR_RMSE;
    
    //weight given to classification rate. option to set: -W
    private double classification_weight = 1;
    
    //weight given to error in classification. option to set: -X
    private double error_weight = 1;
    
    //weight given to number of rules in system. option to set: -Y
    private double rule_number_weight = 1;
    
    //weight given to number of variables per rule. option to set: -Z
    private double var_per_rule_weight = 1;
    
    private int rule_count = 6;
    
    //the system the evolution has come up with
    private FuzzySystem fs;
    
    /**
     * Name: createJFuge
     * Goal: returns a new, empty, JFuge object for further use. Required for JS
     *       interface
     * @return JFuge: a new fuzzy system evolver for further use.
     */
    public JFuge createJFuge(){
        return new JFuge();
    }
    
    /**
     * Name: createClassifier
     * Goal: Evolves a fuzzy system according to given options and dataset
     * @param train_data: the data used to compute performance of a system and
     *                    provide data information
     * @param options: the options used to control the genetic algorithm
     * @return boolean: indicates if creation was successful or not
     */
    public boolean createClassifier(Object train_data, String options){
        try {
            this.setOptions(weka.core.Utils.splitOptions(options));
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        
        //construction du classificateur
        //les options sont separee pour plus de lisibilite en cas d'erreur
        Instances data = null;
        if(train_data instanceof String)
            data = DataLoader.loadData(null, null, (String)train_data, true);
        else if(train_data instanceof Instances)
            data = (Instances)train_data;
        else{
            System.out.println("train data not supported!");
            this.appendError(this.getClass().getName(), "Training data format not supported");
            return false;
        }
        
        Coevolution ce = new Coevolution(data, mutation_rate, crossover_rate,
                                         selection_rate, pop_size,
                                         num_generations, false,
                                         selection_algorithm, error_algorithm,
                                         elitism_rate, tournament_size, 
                                         classification_weight, error_weight,
                                         rule_number_weight, var_per_rule_weight, rule_count);
        fs = ce.evolveSystem();
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Classifier was successfully created and trained");
        return true;
    }/*end createClassifier*/
    
    /**
     * Name: classifyInstances
     * Goal: classifies each instance of the given dataset according to the
     *       predictions made by the fuzzy system
     * @param test_data: the data to classify
     * @return double[]: the classification for each instance
     */
    public double[] classifyInstances(Instances test_data){
        double[] result = fs.classifyInstances(test_data);
        System.out.println(fs);
        return result;
    }/*end distributionForInstance*/
    
    /**
     * Name: distributionForInstances
     * @param test_data: the data to classify
     * @return double[][]: the distribution for each instance
     */
    public double[][] distributionForInstances(Instances test_data){
        return fs.distributionForInstances(test_data);
    }

    public FuzzySystem getFuzzySystem() {
        return fs;
    }/*end distributionForInstance*/
    
    /**
     * Name: setOptions
     * Goal: modifies the options according to the users wishes
     * @param opts: the options to set, in weka format
     * @throws Exception: in case of a wrong option or combination of options
     */
    public void setOptions(String[] opts) throws Exception{
        //set mutation rate
        String mut_rate_str = Utils.getOption('M', opts);
        if (mut_rate_str.length() != 0)
            this.mutation_rate = Double.parseDouble(mut_rate_str);
        if(this.mutation_rate < 0 || this.mutation_rate > 1)
            throw new Exception("Mutation rate muste be between 0 and 1: "+this.mutation_rate);
        
        //set crossover rate
        String x_rate_str = Utils.getOption('C', opts);
        if (x_rate_str.length() != 0)
            this.crossover_rate = Double.parseDouble(x_rate_str);
        if(this.crossover_rate < 0 || this.crossover_rate > 1)
            throw new Exception("Crossover rate must be between 0 and 1: "+this.crossover_rate);
        
        //set selection rate
        String sel_rate_str = Utils.getOption('S', opts);
        if (sel_rate_str.length() != 0)
            this.selection_rate = Double.parseDouble(sel_rate_str);
        if(this.selection_rate < 0 || this.selection_rate > 1)
            throw new Exception("Selection rate must be between 0 and 1: "+this.selection_rate);
       
        //set number of generations
        String num_gen_str = Utils.getOption('G', opts);
        if (num_gen_str.length() != 0)
            this.num_generations = Integer.parseInt(num_gen_str);
        if(this.num_generations <= 0)
            throw new Exception("Number of generations must be between positive: "+this.num_generations);
        
        //set selection algorithm and tournament size if needed
        String sel_algo_str = Utils.getOption('A', opts);
        String tourn_size_str = Utils.getOption('T', opts);
        if (tourn_size_str.length() != 0){
            this.tournament_size = Integer.parseInt(tourn_size_str);
            if(this.tournament_size <= 0)
                throw new Exception("Tournament size must be between positive: "+this.tournament_size);
            this.selection_algorithm = TOURNAMENT_SELECTION;
        }
        else{
            if(sel_algo_str.equals(ROULETTE_SELECTION) || sel_algo_str.equals(RANDOM_SELECTION))
                this.selection_algorithm = sel_algo_str;
            else if(sel_algo_str.equals(TOURNAMENT_SELECTION))
                throw new Exception("Tournament selection requires a tournament size");
            else
                throw new Exception("Selection algorithm not supported: "+sel_algo_str);
        }
        
        //set elitism rate
        String elite_rate_str = Utils.getOption('E', opts);
        if (elite_rate_str.length() != 0)
            this.elitism_rate = Double.parseDouble(elite_rate_str);
        if(this.elitism_rate < 0 || this.elitism_rate > 1)
            throw new Exception("Elitism rate muste be between 0 and 1: "+this.elitism_rate);
        
        //set population size
        String poop_size_str = Utils.getOption('P', opts);
        if (poop_size_str.length() != 0)
            this.pop_size = Integer.parseInt(poop_size_str);
        if(this.pop_size <= 0)
            throw new Exception("crossover rate muste be between positive: "+this.pop_size);
        
        
        //set error algorithm
        String error_algo_str = Utils.getOption('R', opts);
        if(error_algo_str.equals(ERROR_MSE) || error_algo_str.equals(ERROR_RMSE) || error_algo_str.equals(ERROR_RRSE) || error_algo_str.equals(ERROR_RAE))
            this.error_algorithm = error_algo_str;
        else
            throw new Exception("Error algorithm not supported: "+error_algo_str);
        
        //set classification rate weight
        String class_weight_str = Utils.getOption('W', opts);
        if(class_weight_str.length() != 0)
            this.classification_weight = Double.parseDouble(class_weight_str);
        if(this.classification_weight < 0)
            throw new Exception("Classification weight must be positive: "+class_weight_str);
        
        //set classification error weight
        String error_weight_str = Utils.getOption('X', opts);
        if(error_weight_str.length() != 0)
            this.error_weight = Double.parseDouble(error_weight_str);
        if(this.error_weight < 0)
            throw new Exception("Error weight must be positive: "+error_weight_str);
        
        //set rule count weight
        String rule_nb_weight_str = Utils.getOption('Y', opts);
        if(rule_nb_weight_str.length() != 0)
            this.rule_number_weight = Double.parseDouble(rule_nb_weight_str);
        if(this.rule_number_weight < 0)
            throw new Exception("Rule number weight must be positive: "+rule_nb_weight_str);
        
        //set variable per rule number weight
        String vpr_weight_str = Utils.getOption('Z', opts);
        if(vpr_weight_str.length() != 0)
            this.var_per_rule_weight = Double.parseDouble(vpr_weight_str);
        if(this.var_per_rule_weight < 0)
            throw new Exception("Variable per rule weight must be positive: "+vpr_weight_str);
        
        String rule_count_str = Utils.getOption('N', opts);
        if(rule_count_str.length() != 0)
            this.rule_count = Integer.parseInt(rule_count_str);
        if(this.rule_count < 0)
            throw new Exception("Number of rules must be positive: "+rule_count_str);
    }/*end setOptions*/
    
    /**
     * Name: main
     * Goal: example of JFuge's usage
     * @param args: not used
     */
    public static void main(String[] args){
        Instances train_data = DataLoader.loadData(null, null, "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_train.arff", true);
        Instances test_data = DataLoader.loadData(null, null, "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_test.arff", true);
        double mutation_rate = 0.1;
        double crossover_rate = 0.2;
        double selection_rate = -1;
        int num_generations = 100;
        String selection_algorithm = TOURNAMENT_SELECTION;
        String error_algorithm = ERROR_MSE;
        double elitism_rate = 0.1;
        int tournament_size = 10;
        int pop = 200;
        double classification_weight = 1;
        double error_weight = 0;
        double rule_number_weight = 0;
        double var_per_rule_weight = 0.1;
        int rule_count = 5;
        FuzzySystem fs = null;
        double max = 0;
        for(int i = 0; i < 1; i++){
            Coevolution ce = new Coevolution(train_data, mutation_rate, crossover_rate, selection_rate, pop, num_generations, false, selection_algorithm, error_algorithm, elitism_rate, tournament_size, 
                                             classification_weight, error_weight,
                                             rule_number_weight, var_per_rule_weight, rule_count);
            FuzzySystem best = ce.evolveSystem();
            System.out.println(best);
            if(best.getFitness() > max){
                fs = best;
                max = best.getFitness();
            }
            //System.out.println(best);
            System.out.println(i+": "+best.getFitness());
        }
        System.out.println(fs);
        System.out.println(max);
        double[] classif = fs.classifyInstances(test_data);
        double[][] distrib = fs.distributionForInstances(test_data);
        for(int i = 0; i < classif.length; i++){
            System.out.println(classif[i]);
        }
        
        for(int i = 0; i < distrib.length; i++){
            for(int j = 0; j < distrib[i].length; j++){
                System.out.print(distrib[i][j]+" ");
            }
            System.out.println();
        }
    }/*end main*/
    
}/*end JFuge*/
