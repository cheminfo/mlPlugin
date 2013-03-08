/**
 * @object: Weka_MLP
 * Goal: Allows the creation and usage of a multi-layer perceptron (neural network)
 * Methods: - createClassifier: creates and trains a MLP
 *          - distributionForInstance: indicates, for each class, the proportion
 *            of membership for an instance of data
 *          - setOptions: modifies the options of the MLP
 * Usage: First create and train the classifier. Then classify new data instances
 *        with distributionForInstance. This will indicate the proportion of
 *        membership of the instance to each available class. Options can be
 *        modified at any time. However, this is NOT recommended.
 * @author: Numa Trezzini
 */
var Weka_MLP = {
    
    
    /**
     * @name: createClassifier
     * Goal: creates, parametrizes and trains a Multi-layer Perceptron
     * @param options: Options to be set. available options are:
     *            -L <learning rate>
     *             Learning Rate for the backpropagation algorithm.
     *             (Value should be between 0 - 1, Default = 0.3).
     *
     *            -M <momentum>
     *             Momentum Rate for the backpropagation algorithm.
     *             (Value should be between 0 - 1, Default = 0.2).
     *
     *            -N <number of epochs>
     *             Number of epochs to train through.
     *             (Default = 500).
     *
     *            -V <percentage size of validation set>
     *             Percentage size of validation set to use to terminate
     *             training (if this is non zero it can pre-empt num of epochs.
     *             (Value should be between 0 - 100, Default = 0).
     *
     *            -S <seed>
     *             The value used to seed the random number generator
     *             (Value should be >= 0 and and a long, Default = 0).
     *
     *            -E <threshold for number of consequetive errors>
     *             The consequetive number of errors allowed for validation
     *             testing before the netwrok terminates.
     *             (Value should be > 0, Default = 20).
     *
     *            -G
     *             GUI will be opened.
     *             (Use this to bring up a GUI).
     *
     *            -A
     *             Autocreation of the network connections will NOT be done.
     *             (This will be ignored if -G is NOT set)
     *
     *            -B
     *             A NominalToBinary filter will NOT automatically be used.
     *             (Set this to not use a NominalToBinary filter).
     *
     *            -H <comma seperated numbers for nodes on each layer>
     *             The hidden layers to be created for the network.
     *             (Value should be a list of comma separated Natural 
     *             numbers or the letters 'a' = (attribs + classes) / 2, 
     *             'i' = attribs, 'o' = classes, 't' = attribs .+ classes)
     *             for wildcard values, Default = a).
     *
     *            -C
     *             Normalizing a numeric class will NOT be done.
     *             (Set this to not normalize the class if it's numeric).
     *
     *            -I
     *             Normalizing the attributes will NOT be done.
     *             (Set this to not normalize the attributes).
     *
     *            -R
     *             Reseting the network will NOT be allowed.
     *             (Set this to not allow the network to reset).
     *
     *            -D
     *             Learning rate decay will occur.
     *             (Set this to cause the learning rate to decay).
     *             
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return classifier: returns the created classifier or null if unsuccessful
     */
    createClassifier: function(options, train_data){
        return Weka_Java_MLP.createClassifier(options, train_data);
    },
    
    
    /**
     * @name: distributionForInstance
     * Goal: indicates, for each available class, the proportion of membership
     *       of the data instance
     * @param instance: the instance to be classified
     * @returns double[]: indicates the proportion of membership to each class
     *                    of given instance
     */
    distributionForInstance: function(instance){
        return Weka_Java_MLP.distributionForInstance(instance);
    },
    
    
    /**
     * @name: setOptions
     * Goal: modifies the options of the classifier
     * @param options: the options to be set. see createClassifier for available
     *                 options
     * @returns boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_MLP.setOptions(options);
    }
    
    
}