/**
 * @object: Weka_SVM
 * Goal: allows creation and usage of a Support Vector Machine classifier.
 *       It works with the Sequential Minimal Optimiation algorithm (SMO)
 * Methods: - createClassifier: creates, parametrizes and trains a classifier
 *          - distributionForInstance: indicates the probability of membership
 *            to each class for an instance
 *          - setOptions: modifies the options of the classifier
 * Usage: First, create, parametrize and train the classifier. then, compute the
 *        probability of membership with distributionForInstance. Option
 *        setting after creation is NOT recommended
 */
 var Weka_SVM = {
    
    
    /**
     * @function: createClassifier
     * Goal: creates, parametrizes and trains a SVM classifier
     * @param options: the options to be set. Available options are:
     *        -D
     *         If set, classifier is run in debug mode and
     *         may output additional info to the console
     *        -no-checks
     *         Turns off all checks - use with caution!
     *         Turning them off assumes that data is purely numeric, doesn't
     *         contain any missing values, and has a nominal class. Turning them
     *         off also means that no header information will be stored if the
     *         machine is linear. Finally, it also assumes that no instance has
     *         a weight equal to 0.
     *         (default: checks on)
     *        -C <double>
     *         The complexity constant C. (default 1)
     *        -N
     *         Whether to 0=normalize/1=standardize/2=neither. (default 0=normalize)
     *        -L <double>
     *         The tolerance parameter. (default 1.0e-3)
     *        -P <double>
     *         The epsilon for round-off error. (default 1.0e-12)
     *        -M
     *         Fit logistic models to SVM outputs. 
     *        -V <double>
     *         The number of folds for the internal
     *         cross-validation. (default -1, use training data)
     *        -W <double>
     *         The random number seed. (default 1)
     *        -K <classname and parameters>
     *         The Kernel to use.
     *         (default: weka.classifiers.functions.supportVector.PolyKernel)
     *
     *        Options specific to kernel weka.classifiers.functions.supportVector.PolyKernel:
     *
     *        -D
     *         Enables debugging output (if available) to be printed.
     *         (default: off)
     *        -no-checks
     *         Turns off all checks - use with caution!
     *         (default: checks on)
     *        -C <num>
     *         The size of the cache (a prime number), 0 for full cache and 
     *         -1 to turn it off.
     *         (default: 250007)
     *        -E <num>
     *         The Exponent to use.
     *         (default: 1.0)
     *        -L
     *         Use lower-order terms.
     *         (default: no)
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return classifier: returns the created classifier or null if unsuccessful
     */
     createClassifier: function(options, train_data){
         return Weka_Java_SVM.createClassifier(options, train_data);
     },
     

    /**
     * @name: distributionForInstance
     * Goal: computes, for each class, the proportion of membership of given instance
     * @param instance: the instance to be classified
     * @return double[]: the proportion of membership to each class for given instance
     */
     distributionForInstance: function(instance){
         return Weka_Java_SVM.distributionForInstance(instance);
     },


    /**
     * @name: setOptions
     * Goal: modifies the options of the classifier
     * @param options: the options to be set. see createClassifier for available
     *                 options
     * @return boolean: indicates whether the modification was successful or not
     */
     setOptions: function(options){
         return Weka_Java_SVM.setOptions(options);
     }
     
 }