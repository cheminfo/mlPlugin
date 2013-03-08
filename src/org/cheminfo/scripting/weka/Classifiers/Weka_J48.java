/**
 * Name: Weka_J48
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
package org.cheminfo.scripting.weka.Classifiers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class Weka_J48 extends Function{
    
    private J48 tree;
    
    
    /**
     * Name: createClassifier
     * Goal: Allows creation, option setting and training of a J48 classifier
     * @param options: the options of the classifier to be set
     * @param train_data: training data for the classifier. May be the path to 
     *                    the file, the file or a set of instances
     * @return Classifier: returns the trained classifier or null if creation 
     *                     was unsuccessful
     */
    public boolean createClassifier(String options, Object train_data){
        tree = new J48();
        this.setLogLevel(3);
        //set des options
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            tree.setOptions(opts);
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
        try {
            
            tree.buildClassifier(data);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Classifier could not be created: "+e.getMessage());
            System.out.println("le classificateur n'a pas pu etre construit:");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Classifier was successfully created and trained");
        return true;
    }/*end createClassifier*/

    
    /**
     * Name: classifyInstance
     * Goal: adds and classifies an instance of data
     * @param c: the classifier to be used for classification
     * @param i: the data instance to be classified
     * @return double: the class attributed to the instance
     */
    public double classifyInstance(Instance i){
        try {
            return tree.classifyInstance(i);
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
            return tree.distributionForInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Distribution could not be computed: "+e.getMessage());
            System.out.println("les probabilites n'ont pas pu etre calculees:");
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
            tree.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/
    
}/*end Weka_J48*/