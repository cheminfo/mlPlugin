/**
 * @object: Weka_KNN
 * Goal: Creation and usage of a classifier based on K Nearest Neighbours 
 *       algorithm
 * Methods: - createClassifier: creates, parametrizes and trains a KNN classifier
 *          - distance: computes the distance between two instances of data
 *          - distributionForInstance: computes, for each available class, the
 *            proportion of membership of an instance
 *          - setOptions: allows modification of the classifier's options (use
 *            not recommended after creation)
 * Usage: first, create, parametrize and train the classifier with 
 *        createClassifier. Then, either compute distance between instances or
 *        distribution the instance
 * @author: Numa Trezzini
 */
var Weka_KNN = {
    
    
    /**
     * @name: createClassifier
     * Goal: create, parametrize and train a KNN classifier
     * @param options: the options to be set. Available options are:
     *        -R <number of references>
     *             Number of Nearest References (default 1)
     *
     *        -C <number of citers>
     *              Number of Nearest Citers (default 1)
     *
     *        -H <rank>
     *              Rank of the Hausdorff Distance (default 1)
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return Classifier: returns the created classifier or null if unsuccessful
     */
    createClassifier: function(options, train_data){
        return Weka_Java_KNN.createClassifier(options, train_data);
    },
    

    /**
     * @name: distance
     * Goal: computes the distance between two data instances
     * @param first: first instance to be used in the computation
     * @param second: second instance to be used in the computation
     * @return double: distance between the two instances
     */
    distance: function(first, second){
        return Weka_Java_KNN.distance(first, second);
    },
    

    /**
     * @name: distributionsForInstance
     * Goal: indicates the proportion of membership to each class for given instance
     * @param instance: instance to be classified
     * @return double[]: proportion of membership to each class
     */
    distributionForInstance: function(instance){
        return Weka_Java_KNN.distributionForInstance(instance);
    },
    
    /**
     * @name: setOptions
     * Goal: modifies the classifier's options
     * @param options: options to be set. see available options in createClassifier
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_KNN.setOptions(options);
    }
    
}