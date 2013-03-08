/**
 * @object: Weka_NBayesianTree
 * Goal: allows the creation and usage of a classifier based on "naive baysian
 *       trees"
 * Methods: - createClassifier: creates, parametrizes and trains a baysian tree
 *            classifier
 *          - classifyInstance: computes the most likely class to which a data
 *            instance is a member
 *          - distributionForInstance: computes the probabiliy of membership to
 *            each class for a data instance
 *          - setOptions: modifies the options of the classifier. This in NOT
 *            recommended after creation
 * Usage: First, create, parametrize and train the classifier with createClassifier
 *        Then, classify instances with either classifyInstance or 
 *        distributionForInstance, depending on the results the user wants
 *        (just the most likely class or the proportion of membership to each
 *        class for an instance, respectively). Options may be changed at any
 *        time, but this is NOT recommended.
 * @author: Numa Trezzini
 */

var Weka_NBayesianTree = {
    
    /**
     * @name: createClassifier
     * Goal: creates, parametrizes and trains a naive baysian tree classifier
     * @param options: the options to be set for the classifier. available 
     *                 options are:
     *                  -D
     *                  If set, classifier is run in debug mode and
     *                  may output additional info to the console
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return classifier: returns the created classifier or null if unsuccessful
     */
    createClassifier: function(options, train_data){
        return Weka_Java_NBTree.createClassifier(options, train_data);
    },
    
    /**
     * @name: classifyInstance
     * Goal: computes the most likely class for a given instance
     * @param instance: the data instance to be classified
     * @return double: the most likely class attributed to the instance
     */
    classifyInstance: function(instance){
        return Weka_Java_NBTree.classifyInstance(instance);
    },
    
    
    /**
     * @name: distributionForInstance
     * Goal: computes, for each class, the proportion of membership of the instance
     * @param instance: the instance to be classified
     * @return double[]: for each class, the proportion of membership of the instance
     */
    distributionForInstance: function(instance){
        return Weka_Java_NBTree.distributionForInstance(instance);
    },
    
    /**
     * @name: setOptions
     * Goal: modifies the options of the classifier
     * @param options: the options to be modified. see createClassifier for 
     *                 available options
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_NBTree.setOptions(options);
    }
    
}