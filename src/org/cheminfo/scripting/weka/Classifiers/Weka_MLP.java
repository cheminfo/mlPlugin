/**
 * Name: Weka_MLP
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
package org.cheminfo.scripting.weka.Classifiers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;

public class Weka_MLP extends Function{
    
    private MultilayerPerceptron classifier;
    
    /**
     * Name: createClassifier
     * Goal: creates, set options and trains a Multi-layer Perceptron
     * @param options: the options to be set for the MLP
     * @param train_data: The data used for training. May be the path to the
     *                    file containing the data, the file, or the data 
     *                    instances
     * @return boolean: indicates whether creation was successful or not
     */
    public boolean createClassifier(String options, Object train_data){
        classifier = new MultilayerPerceptron();
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            classifier.setOptions(opts);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }

        Instances data = null;
        if(train_data instanceof String)
            data = DataLoader.loadData(null, null, (String)train_data, true);
        else if(train_data instanceof Instances)
            data = (Instances)train_data;
        else {
            System.out.println("train data not supported!");
            this.appendError(this.getClass().getName(), "Training data format not supported");
            return false;
        }
        
        try {
            classifier.buildClassifier(data);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Classifier could not be created: "+e.getMessage());
            System.out.println("classifieur non cree: ");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Classifier was successfully created and trained");
        return true;
    }/*end createClassifier*/
    
    
    /**
     * Name: distributionForInstance
     * Goal: indicates, for each available class, the proportion of membership
     *       of a data instance
     * @param c: the classifier to be used
     * @param i: the instance to be classified
     * @return double[]: the proportions of membership to each class for given
     *                   instance
     */
    public double[] distributionForInstance(Instance i){
        try {
            return classifier.distributionForInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Distribution could not be computed: "+e.getMessage());
            System.out.println("l'instance n'a pas pu etre classee: ");
            System.out.println(e.getMessage());
            return null;
        }
    }/*end distributionForInstance*/
    
    
    /**
     * Name: setOptions
     * Goal: modifies the options of the MLP
     * @param c: the classifier to be modified
     * @param options: the options to be modified
     * @return boolean: indicates whether the modification was successful or not
     */
    public boolean setOptions(String[] options){
        try {
            classifier.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/
    
}/*end Weka_MLP*/