/**
 * @object Weka
 * Allows instantiation of a new weka mthod for usage in JS code
 * Usage: before training and using a classifier/clusterer/filter, the corresponding
 * function MUST be used to create a new var in JS, unless the classifyMethod function is used
 * @author: Numa Trezzini
 */
var Weka = {
    
    computePercents: function(test_instances, distribution){
        var max = 0;
        var max_index = 0;
        var count = 0;
        for(var j = 0; j < test_instances.numInstances(); j++){
            for(var k = 0; k < distribution[j].length; k++){
                if(distribution[j][k] > max){
                    max = distribution[j][k];
                    max_index = k;
                }    
            }
            if(max_index == test_instances.instance(j).classValue())
                count++;
            max_index = 0;
            max = 0;    
        }
        return count*100/test_instances.numInstances();
    },
    
    createJ48: function(){
        return Weka_Creator.createJ48();
    }, /*end createJ48*/
    
    /**
     * @function classifyJ48(train_data, test_data, options);
     * This function creates, trains and classifies a J48 classifier with the given data.
     * The function uses a J48 decision tree to classify the test data. train and test data 
     * MAY be paths to files, arrays or JSONs.
     * The function returns an object containing the classification of the data
     * 	-distributionForInstance: for each instance, the probability of membership for each class
     * 	-classifyInstance (optional): for each instance, the most likely class
	 * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option unpruned: tree is unpruned. value must be true if used, false or
     *                    absent if not used
     * @option pruningConfidence: sets the pruning confidence threshold
     *                             (default: 0.25)
     * @option minInstances: minimum number of instances per leaf (default: 2)
     * @option reducedError: use reduced error pruning. value must be true if 
     *                    used, false or absent if not used
     * @option numFolds: sets number of folds for reduced error pruning
     *                    (default: 3)
     * @option binarySplits: use binary splits only. value must be true if 
     *                        used, false or absent if not used
     * @option noRaising: don't perform subtree raising. value must be true if 
     *                     used, false or absent if not used
     * @option noCleanup: Do not clean up after the tree has been built.
     *
     * @option probaSmoothing: Laplace smoothing for predicted probabilities.
     *
     * @option seed: Seed for random data shuffling (default 1).
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option classIndex: the index of the attribute representing target class (default: -1 => last attribute)
     * @option classifyInstance: use this option to compute most likely class       
     */
    classifyJ48: function(train_data, test_data, options){
        //parsing of options
        var opts = Weka.parseJ48Options(options);
        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
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
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }
        
        //creation and training of classifier
        var classifier = Weka_Creator.createJ48();
        classifier.createClassifier(opts[0], train_instances);
        
        //classification of instances
        //most likely class
        var classify_instance = [];
        if(options.classifyInstance){
            for(var i = 0; i<test_instances.numInstances(); i++)
                classify_instance[i] = classifier.classifyInstance(test_instances.instance(i));
        }
        //distribution for each instance
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        //returning of result
        var result = new Object();
        result.distributionForInstance = distribution;
        if(classify_instance.length != 0)
            result.classifyInstance = classify_instance;
        
        var percent = Weka.computePercents(test_instances, distribution);
        result.classificationRate = percent;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
            
    },/*classifyJ48*/
    

    parseJ48Options: function(options){
         var opts = [];
         //method options
         opts[0]=[];
         if(options.unpruned)
             opts[0] += [" -U "];
         if(options.pruningConfidence!=undefined)
             opts[0] += [" -C "]+[options.pruningConfidence];
         if(options.minInstances!=undefined)
             opts[0] += [" -M "]+[options.minInstances];
         if(options.reducedError)
             opts[0] += [" -R "];
         if(options.numFolds!=undefined)
             opts[0] += [" -N "]+[options.numFolds];
         if(options.binarySplits)
             opts[0] += [" -B "];
         if(options.noRaising)
             opts[0] += [" -S "];
         if(options.noCleanup)
             opts[0] += [" -L "];
         if(options.probaSmoothing)
             opts[0] += [" -A "];
         if(options.seed!=undefined)
             opts[0] += [" -Q "]+[options.seed];
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
         
    },/*end parseJ48Options*/
    
    createKNN: function(){
        return Weka_Creator.createKNN();
    },/*end createKNN*/
    
    /**
     * @function classifyKNN(train_data, test_data, options);
     * This function creates, trains and uses a KNN classifier with the given data
     * Usage: this function uses a KNN classifier to classify the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
     *  The function returns an object containing the classification of the data
     * 	-distributionForInstance: for each instance, the probability of membership for each class
     * 	-distance (optional): the distance computed between every instance
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option numReferences: number of nearest references (default: 1)
     * @option numCiters: number of nearest citers (default: 1)
     * @option hausdorffRank: rank of the Hausdorff distance (default: 1)
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option classIndex: the index of the attribute representing target class (default: -1 => last attribute)
     * @option distance: use this option to compute distance between every instance.     
     */
    classifyKNN: function(train_data, test_data, options){
        var opts = Weka.parseKNNOptions(options);
        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
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
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }
        
        //creation and training of classifier
        var classifier = Weka_Creator.createKNN();
        classifier.createClassifier(opts[0], train_instances);
        
        //classification of instances
        //most likely class
        var distance = [];
        if(options.distance!=undefined){
            for(var i = 0; i<test_instances.numInstances(); i++){
                for(var j = 0;j<test_instances.numInstances();j++)
                    distance[i*test_instances.numInstances()+j] = classifier.distance(test_instances.instance(i), test_instances.instance(j));
            }
                
        }
        //distribution for each instance
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        //returning of result
        var result = new Object();
        result.distributionForInstance = distribution;
        if(distance.length != 0)
            result.distance = distance;
            
        var percent = Weka.computePercents(test_instances, distribution);
        result.classificationRate = percent;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end classifyKNN*/
    
    parseKNNOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.numReferences!=undefined)
            opts[0] = [" -R "]+[options.numReferences];
        if(options.numCiters!=undefined)
            opts[0] += [" -C "]+[options.numCiters];
        if(options.hausdorffRank!=undefined)
            opts[0] += [" -H "]+[options.hausdorffRank];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*end parseKNNOptions*/
    
    createMLP: function(){
        return Weka_Creator.createMLP();
    },/*end createMLP*/
    
    parseMLPOptions: function(options){
        var opts = [];
        
        opts[0] = [];
        if(options.learningRate!=undefined)
            opts[0] += [" -L "]+[options.learningRate];
        if(options.momentum!=undefined)
            opts[0] += [" -M "]+[options.momentum];
        if(options.numEpochs!=undefined)
            opts[0] += [" -N "]+[options.numEpochs];
        if(options.validationSetSize!=undefined)
            opts[0] += [" -V "]+[options.validationSetSize];
        if(options.seed!=undefined)
            opts[0] += [" -S "]+[options.seed];
        if(options.errorThreshold!=undefined)
            opts[0] += [" -E "]+[options.errorThreshold];
        if(options.gui)
            opts[0] += [" -G "];
        if(options.autocreation)
            opts[0] += [" -A "];
        if(options.nominalToBinary)
            opts[0] += [" -B "];
        if(options.hidden!=undefined)
            opts[0] += [" -H "]+[options.hidden];
        if(options.noNumericNormalization)
            opts[0] += [" -C "];
        if(options.noAttributeNormalization)
            opts[0] += [" -I "];
        if(options.noReset)
            opts[0] += [" -R "];
        if(options.decay)
            opts[0] += [" -D"];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*end parseMLPOptions*/

   /**
     * @function classifyMLP(train_data, test_data, options);
     * This function creates, trains and uses a MLP classifier with the given data
     * Usage: this function uses a MLP classifier to classify the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
     * The function returns an object containing the classification of the data.
     * -distributionForInstance: for each instance, the probability of membership for each class
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option learningRate: learning rate for the backpropagation algorithm.
     *                        MUST be between 0-1. (default: 0.3)
     * @option momentum: momentum rate for the backpropagation algorithm.
     *                    MUST be betwwen 0-1. (default: 0.2)
     * @option numEpochs: number of epochs for training (default: 500)
     * @option validationSetSize: percentage of validation set to use to terminate
     *                             training. MUST be between 0-100 (default: 0)
     * @option seed: value used to seed random generator. MUST be positive or 0,
     *                SHOULD be a long (default: 0)
     * @option errorThreshold: The consequetive number of errors allowed for validation
     *                          testing before the netwrok terminates.
     *                          Value should be > 0 (Default = 20).
     * @option gui: opens a GUI
     * @option autocreation: Autocreation of the network connections will NOT be done.
     *                        (This will be ignored if -G is NOT set)
     * @option nominalToBinary: A NominalToBinary filter will NOT automatically be used.
     *                           (Set this to not use a NominalToBinary filter).
     * @option hidden: The hidden layers to be created for the network.
     *                  (Value should be a list of comma separated Natural 
     *                  numbers or the letters 'a' = (attribs + classes) / 2, 
     *                  'i' = attribs, 'o' = classes, 't' = attribs .+ classes)
     *                  for wildcard values, Default = a).
     * @option noNumericNormalization: Normalizing a numeric class will NOT be done.
     *                                  (Set this to not normalize the class if it's numeric).
     * @option noAttributeNormalization: Normalizing the attributes will NOT be done.
     *                                    (Set this to not normalize the attributes).
     * @option noReset: Reseting the network will NOT be allowed.
     * @option decay: Learning rate decay will occur.
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option classIndex: the index of the attribute representing target class (default: -1 => last attribute)  
     */
    classifyMLP: function(train_data, test_data, options){
        var opts = Weka.parseMLPOptions(options);
        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
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
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }

        //creation and training of classifier
        var classifier = Weka_Creator.createMLP();
        classifier.createClassifier(opts[0], train_instances);
        
        //classification of instances

        //distribution for each instance
        var result = new Object();
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        var percent = Weka.computePercents(test_instances, distribution);
        
        result.distributionForInstance = distribution;
        result.classificationRate = percent;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end classifyMLP*/
    
    
    
    createNBayesianTree: function(){
        return Weka_Creator.createNBTree();
    },/*end createNBayesianTree*/
    
    
    /**
     * @function classifyNBTree(train_data, test_data, options);
     * This function creates, trains and uses a Naive Bayesian Tree classifier with the given data
     * Usage: this function uses a NBayesian Tree classifier to classify the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
     * The function returns an object containing the classification of the data
     * -distributionForInstance: for each instance, the probability of membership for each class
     * -classifyInstance (optional): the most likely class for each instance
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option debug: prints debug info to the console
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option classIndex: the index of the attribute representing target class (default: -1 => last attribute)
     * @option classifyInstance: computes the most likely class for each instance     
     */
    classifyNBTree: function(train_data, test_data, options){
        //parsing of options
        var opts = Weka.parseNBTreeOptions(options);

        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
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
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }
        
        //creation and training of classifier
        var classifier = Weka_Creator.createNBTree();
        classifier.createClassifier(opts[0], train_instances);
        
        //classification of instances
        //most likely class
        var classify_instance = [];
        if(options.classifyInstance!=undefined){
            for(var i = 0; i<test_instances.numInstances(); i++)
                classify_instance[i] = classifier.classifyInstance(test_instances.instance(i));
        }
        //distribution for each instance
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        //returning of result
        var result = new Object();
        result.distributionForInstance = distribution;
        if(classify_instance.length != 0)
            result.classifyInstance = classify_instance;
        
        var percent = Weka.computePercents(test_instances, distribution);
        result.classificationRate = percent;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end classifyNBTree*/
    
    parseNBTreeOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.debug)
            opts[0] += ["-D"];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*end parseNBTreeOptions*/
    
    createSVM: function(){
        return Weka_Creator.createSVM();
    },/*end createSVM*/
    
    /**
     * @function classifySVM(train_data, test_data, options);
     * This function creates, trains and uses a Support Vector Machine classifier with the given data
     * Usage: this function uses a SVM classifier to classify the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
     * The function returns an object containing the classification of the data
     * 	-distributionForInstance: for each instance, the probability of membership for each class.
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option debug: prints debug info to the console
     * @option complexity: complexity constant of the algorithm (default: 1)
     * @option normalize: Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
     * @option tolerance: The tolerance parameter. (default 1.0e-3)
     * @option roundOff: The epsilon for round-off error. (default 1.0e-12)
     * @option fit: Fit logistic models to SVM outputs.
     * @option validationFolds: The number of folds for the internal cross-validation.
     *                           (default -1, use training data)
     * @option seed: The random number seed. (default 1)
     * @option kernel: kernel to be used in SVM algorithm. (default: PolyKernel)
     *                  [PolyKernel, Puk, RBFKernel, StringKernel, RegSMO, NormalizedPolyKernel]
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option classIndex: the index of the attribute representing target class (default: -1 => last attribute)
     */
    classifySVM: function(train_data, test_data, options){
        var opts = Weka.parseSVMOptions(options);
        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
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
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], true);
        else
            test_instances = DataLoader.loadData(test_data, true);
        
        if(options.classIndex!=undefined && options.classIndex != -1){
            test_instances.setClassIndex(options.classIndex);
        }
        
        //creation and training of classifier
        var classifier = Weka_Creator.createSVM();
        classifier.createClassifier(opts[0], train_instances);
        
        //classification of instances

        //distribution for each instance
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        var percent = Weka.computePercents(test_instances, distribution);
        
        var result = {distributionForInstance:distribution,
                      classificationRate:percent};
        
        //result.percent = computeResults(test_instances, distribution);
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end classifySVM*/
    
    parseSVMOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.debug)
            opts[0] = [" -D "];
        if(options.complexity!=undefined)
            opts[0] += [" -C "]+[options.complexity];
        if(options.normalize!=undefined)
            opts[0] += [" -N "]+[options.normalize];
        if(options.tolerance!=undefined)
            opts[0] += [" -L "]+[options.tolerance];
        if(options.roundOff!=undefined)
            opts[0] += [" -P "]+[options.roundOff];
        if(options.fit)
            opts[0] += [" -M "];
        if(options.validationFolds!=undefined)
            opts[0] += [" -V "]+[options.validationFolds];
        if(options.seed!=undefined)
            opts[0] += [" -W "]+[options.seed];
        if(options.kernel!=undefined)
            opts[0] += [" -K "]+["weka.classifiers.functions.supportVector."+options.kernel];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*end parseSVMOptions*/
    
    createHierarchicalClusterer: function(){
        return Weka_Creator.createHierarchicalClusterer();
    },/*end createHierarchicalClusterer*/
    
    /**
     * @function clusterHierarchicalClusterer(train_data, test_data, options);
     * This function creates, trains and uses a Hierarchical Clusterer with the given data
     * Usage: this function uses a Hierarchical Clusterer to cluster the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
	 * The function returns an object containing the classification of the data
     *	-distributionForInstance: for each instance, the probability of membership for each cluster
     *  -clusterInstance (optional): the most likely cluster for each instance
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option numClusters: number of clusters
     * @option linkType: Link type (Single, Complete, Average, Mean, Centroid, Ward, Adjusted complete, Neighbor Joining)
     *                    [SINGLE|COMPLETE|AVERAGE|MEAN|CENTROID|WARD|ADJCOMLPETE|NEIGHBOR_JOINING]
     * @option distanceFunction: Distance function to use. (default: EuclideanDistance)
     *                            [ChebyshevDistance, EuclideanDistance, ManhattanDistance, MinkowskiDistance, NormalizableDistance]
     * @option print: Print hierarchy in Newick format, which can be used for display in other programs.
     * @option debug: Prints debug info to console
     * @option brachLength: If set, distance is interpreted as branch length, otherwise it is node height.
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option clusterInstance: computes the most likely cluster.
     *        
     */
    clusterHierarchicalClusterer: function(train_data, test_data, options){
        //parsing of options
        var opts = Weka.parseHCOptions(options);

        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
            train_instances = DataLoader.loadDataFromJSONObject(train_data,
                              opts[1],
                              opts[2],
                              false);
        else
            train_instances = DataLoader.loadData(train_data, false);
        
        
        //loading of test_data
        var test_instances;
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], false);
        else
            test_instances = DataLoader.loadData(test_data, false);
        
        //creation and training of classifier
        var classifier = Weka_Creator.createHierarchicalClusterer();
        classifier.createCluster(opts[0], train_instances);
        
        //classification of instances
        //most likely class
        var cluster_instance = [];
        if(options.clusterInstance){
            for(var i = 0; i<test_instances.numInstances(); i++)
                cluster_instance[i] = classifier.clusterInstance(test_instances.instance(i));
        }
        //distribution for each instance
        var distribution = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            distribution[i] = classifier.distributionForInstance(test_instances.instance(i));
        
        //returning of result
        var result = new Object();
        result.distributionForInstance = distribution;
        if(cluster_instance.length != 0)
            result.clusterInstance = cluster_instance;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end clusterHierarchicalClusterer*/
    
    parseHCOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.numClusters!=undefined)
            opts[0] = [" -N "]+[options.numClusters];
        if(options.linkType!=undefined)
            opts[0] += [" -L "]+[options.linkType];
        if(options.distanceFunction!=undefined)
            opts[0] += [" -A "]+["weka.core."+options.distanceFunction];
        if(options.print)
            opts[0] += [" -P "];
        if(options.debug)
            opts[0] += [" -D "];
        if(options.branchLength)
            opts[0] += [" -B "];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*parseHCOptions*/
    
    createKMeans: function(){
        return Weka_Creator.createKMeans();
    },/*end createKMeans*/

    /**
     * @function clusterKMeans(train_data, test_data, options)
     * This function creates, trains and uses a KMeans Clusterer with the given data
     * Usage: this function uses a KMeans Clusterer to cluster the test data.
     *        test and train data MAY be paths to files, arrays or JSONs
     * The function returns an object containing the classification of the data
     *	-clusterInstance: the most likely cluster for each instance
     * 	-getCentroids (optional): indicates the instances set as cluster centroids
     * 	-frequencyCounts (optional): indicates, for each attribute, the appearance frequency of each variant
     * 	-getClusterSizes (optional): indicates the size (number of instances) of each cluster
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option numClusters: number of clusters (default: 2)
     * @option stdDeviation: Display std. deviations for centroids.
     * @option missing: Replace missing values with mean/mode
     * @option seed: Random number seed. (default 10)
     * @option distanceFunction: Distance function to use. (default: EuclideanDistance)
     *                            [ChebyshevDistance, EuclideanDistance, ManhattanDistance, MinkowskiDistance, NormalizableDistance]
     * @option maxIter: maximum number of iterations
     * @option order: preserve order of instances
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     * @option getCentroids: indicates the instances set as cluster centroids
     * @option frequencyCounts: indicates, for each attribute, the appearance frequency of each variant
     * @option getClusterSizes: indicates the size (number of instances) of each cluster        
     */
    clusterKMeans: function(train_data, test_data, options){
        //parsing of options
        var opts = Weka.parseKMeansOptions(options);

        
        //loading of training data
        var train_instances;
        if(train_data instanceof Object || train_data instanceof Array)
            train_instances = DataLoader.loadDataFromJSONObject(train_data,
                              opts[1],
                              opts[2],
                              false);
        else
            train_instances = DataLoader.loadData(train_data, false);
        
        
        //loading of test_data
        var test_instances;
        if(test_data instanceof Object || test_data instanceof Array)
            test_instances = DataLoader.loadDataFromJSONObject(test_data, opts[1], opts[2], false);
        else
            test_instances = DataLoader.loadData(test_data, false);
        
        //creation and training of classifier
        var clusterer = Weka_Creator.createKMeans();
        clusterer.createCluster(opts[0], train_instances);
        
        //classification of instances
        //most likely class
        var cluster_instance = [];
        for(var i = 0; i < test_instances.numInstances(); i++)
            cluster_instance[i] = clusterer.clusterInstance(test_instances.instance(i));
        
        var centroids = [];
        if(options.getCentroids)
            centroids = clusterer.getClusterCentroids();
        
        var frequency_counts = [];
        if(options.frequencyCounts)
            frequency_counts = clusterer.getClusterFrequencyCounts();
        
        var cluster_sizes = [];
        if(options.getClusterSizes)
            cluster_sizes = clusterer.getClusterSizes();
        //returning of result
        var result = new Object();
        result.clusterInstance = cluster_instance;
        if(centroids.length != 0)
            result.centroids = centroids;
        if(frequency_counts.length != 0)
            result.frequencyCounts = frequency_counts;
        if(cluster_sizes.length != 0)
            result.clusterSizes = cluster_sizes;
        
        var classes = [];
        for(var j = 0; j < test_instances.numAttributes(); j++)
            classes[j] = train_instances.attribute(j).name();
        result.classes = classes;
        
        return result;
    },/*end clusterKMeans*/
    
    parseKMeansOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.numClusters!=undefined)
            opts[0] = [" -N "]+[options.numClusters];
        if(options.stdDeviation)
            opts[0] += [" -V "];
        if(options.missing)
            opts[0] += [" -M "];
        if(options.seed!=undefined)
            opts[0] += [" -S "]+[options.seed];
        if(options.distanceFunction!=undefined)
            opts[0] += [" -A "]+["weka.core."+options.distanceFunction];
        if(options.maxIter!=undefined)
            opts[0] += [" -I "]+[options.maxIter];
        if(options.order)
            opts[0] += [" -O "];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    },/*end parseKMeansOptions*/
    
    createPCA: function(){
        return Weka_Creator.createPCA();
    },/*end createPCA*/

    /**
     * @function filterPCA(data, options);
     * This function creates, trains and uses a KMeans Clusterer with the given data
     * Usage: this function uses a KMeans Clusterer to cluster the test data.
     *        test and train data MAY be paths to files, arrays or JSONs     
     * The function returns an object containing the filtered data
     * - filteredData: the reduced data after application of PCA filter
     * @param train_data: The train dataset.
	 * @param test_data: The test dataset.
     * @option dontNormalize: don't normalize input data
     * @option retain: Retain enough PC attributes to account for this 
     *                  proportion of variance in the original data.
     *                  (default: 0.95)
     * @option maxAttributes: Maximum number of attributes to include in 
     *                         transformed attribute names.
     *                         (-1 = include all, default: 5)
     * @option maxRetain: Maximum number of PC attributes to retain.
     *                     (-1 = include all, default: -1)
     * @option dataAttributes: if data is an array or a JSON, use this options
     *                          to give names, types, and, if needed, values to
     *                          attributes.
     *                          a modified attribute MUST be presented like this:
     *                          [index, "attribute type", "name"] => only for string and numeric attributes
     *                          [index, "nominal", "name", ["value1", "value2"]] => for nominal attributes
     *                          index MAY be a range INCLUDING first and last value: [start_index, end_index]
     * @option datasetName: string used to set the dataset name. Option will not be used if data is a file.
     */
    filterPCA: function(data, options){
        var opts = Weka.parsePCAOptions(options);
        
        //loading of test_data
        var instances;
        if(data instanceof Object || data instanceof Array)
            instances = DataLoader.loadDataFromJSONObject(data, opts[1], opts[2], false);
        else
            instances = DataLoader.loadData(data, false);
        
        var filter = Weka_Creator.createPCA();
        filter.createFilter(options);
        var result = {filteredData:filter.useFilter(instances)};
        
        var classes = [];
        for(var j = 0; j < instances.numAttributes(); j++)
            classes[j] = instances.attribute(j).name();
        result.classes = classes;
        
        return result;
        
    },/*end filterPCA*/
    
    parsePCAOptions: function(options){
        var opts = [];
        opts[0] = [];
        if(options.dontNormalize)
            opts[0] = [" -D "];
        if(options.retain!=undefined)
            opts[0] += [" -R "]+[options.retain];
        if(options.maxAttributes!=undefined)
            opts[0] += [" -A "]+[options.maxAttributes];
        if(options.maxRetain!=undefined)
            opts[0] += [" -M "]+[options.maxRetain];
        
        if(options.dataAttributes!=undefined)
            opts[1] = options.dataAttributes;
        else
            opts[1] = null;
        if(options.datasetName!=undefined)
            opts[2] = options.datasetName;
        else
            opts[2] = null;
        
        return opts;
    }/*end parsePCAOptions*/
 
}/*end Weka*/
