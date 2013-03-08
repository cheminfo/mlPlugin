/**
 * Name: Weka_NBayesianTree
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
package org.cheminfo.scripting.weka.Classifiers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.classifiers.trees.NBTree;
import weka.core.Instance;
import weka.core.Instances;


public class Weka_NBayesianTree extends Function{
    
    private NBTree classifier;
    
    /**
     * Name: createClassifier
     * @param options: the options used for constructing the classifier
     * @param train_data: the data used for training. May be the path to the
     *                    file containing the train data, the file or data
     *                    instances
     * @return Classifier: Returns the created classifier or null if unsuccessful
     */
    public boolean createClassifier(String options, Object train_data){
        classifier = new NBTree();
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            classifier.setOptions(opts);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        
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
        
        try {
            classifier.buildClassifier(data);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Classifier could not be created: "+e.getMessage());
            System.out.println("le classificateur n'a pas ete cree:");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Classifier was successfully created and trained");
        return true;
    }/*end createClassifier*/
    
    /**
     * Name: classifyInstance
     * Classifies an instance of data in one of the available classes
     * @param c: the classifier to be used
     * @param i: the instance of data to be classified
     * @return double: the class in which the instance is classified
     */
    public double classifyInstance(Instance i){
        try {
            return classifier.classifyInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Instance could not be classified: "+e.getMessage());
            System.out.println("l'instance n'a pas pu etre classee:");
            System.out.println(e.getMessage());
            return -1;
        }
    }/*end classifyInstance*/
    
    
    /**
     * Name: distributionForInstance
     * Goal: indicates the proportion of membership to each class for a data 
     *       instance
     * @param c: the classifier to be used
     * @param i: the data instance to be classified
     * @return double[]: indicates, for each class, the porportion of membership
     */
    public double[] distributionForInstance(Instance i){
        try {
            return classifier.distributionForInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Distribution could not be computed: "+e.getMessage());
            System.out.println("l'instance n'a pas pu etre classee:");
            System.out.println(e.getMessage());
            return null;
        }
    }/*end distributionForInstance*/
    

    /**
     * Name: setOptions
     * Goal: allows modification of the classifiers options
     * @param c: the classifier to be modified
     * @param options: the options to be set
     * @return boolean: indicates whether the modification was successful or not
     */
    public boolean setOptions(String[] options){
        try {
            classifier.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/
    
}/*end Weka_NBayesianTree*/
