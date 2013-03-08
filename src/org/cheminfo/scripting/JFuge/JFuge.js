/**
 * @object JFuge
 * Allows creation of a fuzzy system from a dataset given by the user.
 * The system is specialized in classifying data instances
 * The system is created through genetic evolution. The system depends on its ability 
 * to classify data, the precision of the predictions, and the complexity of the system 
 * (the number of rules and the mean number of variables per rule). 
 * The genetic algorithm may be parametrized by the user.
 * @author Numa Trezzini
 */

var JFuge = {
    
    createJFuge: function(){
        return JFuge_Java.createJFuge();
    },
    
    /**
     * @function classifyJFuge(train_data, test_data, options);
     * Creates, parametrizes and trains (evolves) a fuzzy system based on the dataset's information. The system may classify new instances.
     * The function returns an object containing the classification of the test data
	 *        - classifyInstances: for each instance, the most likely class
	 *        - distributionForInstances: for each instance, the probability of membership for each class
	 *        - system: may be true, false or absent. Returns the chosen system.    
	 * @param train_data: The train dataset
	 * @param test_data: The test dataset
	 * @option mutationRate: changes the rate at which genes mutate.
	 *                      default value: 0.1. MUST be between 0-1
	 * @option crossoverRate: changes the rate at which genes cross.
	 *                       default value: 0.2. MUST be between 0-1
	 * @option  selectionRate: changes the rate at which genes are chosen
	 *                       for reproduction. default: 0.3. MUST be between 0-1
	 * @option  numGenerations: sets the number of generations the genetic
	 *                        algorithm will run. default: 200. MUST be positive
	 * @option  selectionAlgorithm: sets the algorithm used for
	 *                            selecting parents. default:
	 *                            roulette selection. Available values:
	 *                            "roulette", "tournament" and "random"
	 * @option  elitismRate: sets the rate of elite genes to keep into the next
	 *                     generation. default value: 0. MUST be between 0-1
	 * @option  tournamentSize: sets the number of participants in the tournament
	 *                        selection. If this option is set by the user,
	 *                        selection algorithm option (-A) will be ignored
	 * @option  populationSize: sets the number of individuals the genetic
	 *                        algorithm evolves. default value: 300
	 * @option  errorAlgorithm: sets the algorithm used to compute prediction error
	 *                        Default value is RMSE. Available options are:
	 *                        "rmse", "mse", "rrse" and "rae"
	 * @option  classificationWeight: sets the weight given to the classification rate
	 *                              during fitness computation. default value: 1
	 *                              MUST be between 0-1
	 * @option  errorWeight: sets the weight given to the prediction error during
	 *                     fitness computation. Default value: 1
	 *                     MUST be between 0-1
	 * @option  ruleNumberWeight: sets the weight given to number of rules during
	 *                          fitness computation. Default value: 1
	 *                          MUST be between 0-1
	 * @option  varPerRuleNumberWeight: sets the weight given to number of variables
	 *                                per rule during fitness computation.
	 *                                Default value: 1. MUST be between 0-1
	 * @option  ruleNumber: set the maximum number of rules a system may take.
	 * @option  dataAttributes: if data is an array or a JSON, use this options
	 *                        to give names, types, and, if needed, values to
	 *                        attributes.
	 *                        a modified attribute MUST be presented like this:
	 *                        [index, "attribute type", "name"] => only for string and numeric attributes
	 *                        [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
	 *                        index MAY be a range INCLUDING first and last value: [start_index, end_index]
	 * @option  datasetName: string used to set the dataset name. Option will not be used if data is a file.
	 * @option  classIndex: the index of the attribute representing target class (default: -1 => last attribute)
	 * @option  classifyInstance: use this option to compute most likely class
	 */
    classifyJFuge: function(train_data, test_data, options){
        //parsing of options
        var opts = JFuge.parseJFugeOptions(options);
        
        //loading of training data
        var train_instances;
        if(train_data instanceof Array)
            train_instances = DataLoader.loadDataFromArray(train_data,
                              opts[1],
                              opts[2],
                              true);
        else if(train_data instanceof Object)
            train_instances = DataLoader.loadDataFromJSONObject(train_data,
                              opts[1],
                              opts[2],
                              true);
        else
            train_instances = DataLoader.loadData(train_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            train_instances.setClassIndex(options.classIndex);
        }
        
        //loading of test_data
        var test_instances;
        if(test_data instanceof Array)
            test_instances = DataLoader.loadDataFromArray(test_data, opts[1], opts[2], true);
        else if(test_data instanceof Object)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }
        
        //creation and training of classifier
        var classifier = JFuge_Java.createJFuge();
        classifier.createClassifier(train_instances, opts[0]);
        
        //classification of instances
        //most likely class
        var classify_instance = [];
        if(options.classifyInstance)
            classify_instance = classifier.classifyInstances(test_instances);
        
        //distribution for each instance
        //var distribution = [];
        //for(var i = 0; i < test_instances.numInstances(); i++)
        var distribution = classifier.distributionForInstances(test_instances);
        
        //returning of result
        var result = new Object();
        result.distributionForInstance = distribution;
        if(classify_instance.length != 0)
            result.classifyInstance = classify_instance;
        if(options.system)
            result.system = classifier.getFuzzySystem();
        
        result.percent = JFuge.computeResults(test_instances, classify_instance);
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
        
    },
    
    computeResults: function(test_instances, classification){
        var count = 0;
        for(var i = 0; i<test_instances.numInstances(); i++){ 
            if(classification[i] == test_instances.instance(i).classValue())
                count++;
        }
        return 100*count/test_instances.numInstances();
    },
    
    parseJFugeOptions: function(options){
         var opts = [];
         //method options
         opts[0]=[];
         if(options.mutationRate!=undefined)
             opts[0] += [" -M "]+[options.mutationRate];
         if(options.crossoverRate!=undefined)
             opts[0] += [" -C "]+[options.crossoverRate];
         if(options.selectionRate!=undefined)
             opts[0] += [" -S "]+[options.selectionRate];
         if(options.numGenerations!=undefined)
             opts[0] += [" -G "]+[options.numGenerations];
         if(options.selectionAlgorithm!=undefined)
             opts[0] += [" -A "]+[options.selectionAlgorithm];
         if(options.elitismRate!=undefined)
             opts[0] += [" -E "]+[options.elitismRate];
         if(options.tournamentSize!=undefined)
             opts[0] += [" -T "]+[options.tournamentSize];
         if(options.populationSize!=undefined)
             opts[0] += [" -P "]+[options.populationSize];
         if(options.errorAlgorithm!=undefined)
             opts[0] += [" -R "]+[options.errorAlgorithm];
         if(options.classificationWeight!=undefined)
             opts[0] += [" -W "]+[options.classificationWeight];
         if(options.errorWeight!=undefined)
             opts[0] += [" -X "]+[options.errorWeight];
         if(options.ruleNumberWeight!=undefined)
             opts[0] += [" -Y "]+[options.ruleNumberWeight];
         if(options.varPerRuleNumberWeight!=undefined)
             opts[0] += [" -Z "]+[options.varPerRuleNumberWeight];
         if(options.ruleNumber!=undefined)
             opts[0] += [" -N "]+[options.ruleNumber];
         //data attribute options
         if(options.dataAttributes!=undefined)
             opts[1] = options.dataAttributes;
         else
             opts[1] = null;
         if(options.datasetName!=undefined)
             opts[2] = options.datasetName;
         else
             opts[2] = null;
         
         return opts;
    }
       
}


