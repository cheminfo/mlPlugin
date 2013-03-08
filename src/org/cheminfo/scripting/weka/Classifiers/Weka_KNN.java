/**
 * Name: Weka_KNN
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

package org.cheminfo.scripting.weka.Classifiers;


import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.classifiers.mi.CitationKNN;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PropositionalToMultiInstance;

public class Weka_KNN extends Function{

    private CitationKNN classifier;
    
    /**
     * Name: createClassifier
     * Goal: Allows creation, option setting and training of a KNN classifier
     * @param options: the classifiers options (available in JS or Weka doc)
     * @param train_data: data used for training. May be the path to the file 
     *                    containing the data, the file or data instances
     * @return classifier: returns the trained classifier or null if unsuccessful
     */
    public boolean createClassifier(String options, Object train_data){
        classifier = new CitationKNN();
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            classifier.setOptions(opts);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("Option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }
        
        Instances data = null;
        if(train_data instanceof String)
            data = DataLoader.loadData(null, null, (String)train_data, true);
        else if(train_data instanceof Instances)
            data = (Instances)train_data;
        else{
            System.out.println("not supported!");
            this.appendError(this.getClass().getName(), "Training data format not supported");
            return false;
        }

        PropositionalToMultiInstance filt = new PropositionalToMultiInstance();
        
        try {
            filt.setInputFormat(data);
            Instances data2 = Filter.useFilter(data, filt);
            data2.setClassIndex(data2.numAttributes()-1);
            classifier.buildClassifier(data2);
            
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Classifier could not be created: "+e.getMessage());
            System.out.println("Classificateur non initialise: ");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Classifier was successfully created and trained");
        return true;
    }/*end createClassifier*/
    
    
    /**
     * Name: distance
     * Goal: computes the distance between two instances
     * @param c: the classifier to be used
     * @param first: the first instance to be used in the calculation of the
     *               distance
     * @param second: the second instance to be used
     * @return double: the computed distance
     */
    public double distance(Instance first, Instance second){
        return classifier.distance(first, second);
    }/*end distance*/
    
    /**
     * Name: distributionForInstance
     * Goal: Computes the probability of membership to each available class for
     *       given instance
     * @param c: the classifier to be used
     * @param i: the instance to be classified
     * @return double[]: the probability of membership for each class of instance
     */
    public double[] distributionForInstance(Instance i){
        try {
            return classifier.distributionForInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Distribution could not be computed: "+e.getMessage());
            System.out.println("L'instance n'a pas pu etre classee: ");
            System.out.println(e.getMessage());
            return null;
        }
    }/*end distributionForInstance*/
    
    /**
     * Name: setOptions
     * Goal: allows modification of the classifier's options
     * @param c: the classifier to be modified
     * @param options: the options to be modified
     * @return boolean: indicates whether the modification was successful
     */
    public boolean setOptions(String[] options){
        try {
            classifier.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("Option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/

}/*end Weka_KNN*/