/**
 * @object: Weka_J48
 * Goal: Allows creation and usage of a J48 classifier, based on a C4.5 decision
 *       tree
 * Methods: - createClassifier: creates, parametrizes and trains a J48 classifier
 *          - classifyInstance: adds and classifies and instance
 *          - distributionForInstance: indicates the probability of membership
 *            to each class for an instance
 *          - setOptions: modifies the options of the classifier
 * Usage: First, create, parametrize and train the classifier. then, either add
 *        instances by classifing them, or compute the probability of membership
 *        with classifyInstance or distributionForInstance, respectively. Option
 *        setting after creation is NOT recommended
 * @author: Numa Trezzini
 */
var Weka_J48 = {
    
    
    /**
     * @name: createClassifier
     * Goal: creates, parametrizes and trains a J48-tree classifier
     * @param: options: the options for parametrizing the classifier. Available
     *                  options are:
     *        -U
     *         Use unpruned tree.
     *
     *        -C <pruning confidence>
     *         Set confidence threshold for pruning.
     *         (default 0.25)
     *
     *        -M <minimum number of instances>
     *         Set minimum number of instances per leaf.
     *         (default 2)
     *
     *        -R
     *         Use reduced error pruning.
     *
     *        -N <number of folds>
     *         Set number of folds for reduced error
     *         pruning. One fold is used as pruning set.
     *         (default 3)
     *
     *        -B
     *         Use binary splits only.
     *
     *        -S
     *         Don't perform subtree raising.
     *
     *        -L
     *         Do not clean up after the tree has been built.
     *
     *        -A
     *         Laplace smoothing for predicted probabilities.
     *
     *        -Q <seed>
     *         Seed for random data shuffling (default 1).
     * @param train_data: the file containing the train data, its path or the
     *                    Weka data instances
     * @return Classifier: returns the created and trained classifier or null
     *                     if unsuccessful
     */
    createClassifier: function(options, train_data){
        return Weka_Java_J48.createClassifier(options, train_data);
    },


    /**
     * @name: classifyInstance
     * Goal: Classifies the instance into the most likely class
     * @param instance: the data instance to be classified
     * @return double: the most likely class computed by the classifier for the
     *                  given instance
     */
    classifyInstance: function(instance){
        return Weka_Java_J48.classifyInstance(instance);
    },
    
    
    /**
     * @name: distributionForInstance
     * Goal: indicates, for each available class, the proportion of membership of
     *       given instance
     * @param instance: the data instance to be classified
     * @return double[]: the probability of membership to each class for given
     *                   instance
     */
    distributionForInstance: function(instance){
        return Weka_Java_J48.distributionForInstance(instance);
    },


    /**
     * @name: setOptions
     * Goal: modifies the options of the classifier
     * @param options: the options to be modified. see createClassifier for 
     *                 available options
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_J48.setOptions(options);
    }

}
