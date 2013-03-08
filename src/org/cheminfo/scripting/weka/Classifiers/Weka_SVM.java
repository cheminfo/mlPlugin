/**
 * Name: Weka_SVM
 * Goal: allows creation and usage of a Support Vector Machine classifier.
 *       It works with the Sequential Minimal Optimiation algorithm (SMO)
 * Methods: - createClassifier: creates, parametrizes and trains a classifier
 *          - distributionForInstance: indicates the probability of membership
 *            to each class for an instance
 *          - setOptions: modifies the options of the classifier
 * Usage: First, create, parametrize and train the classifier. then, compute the
 *        probability of membership with distributionForInstance. Option
 *        setting after creation is NOT recommended
 * @author: Numa Trezzini
 */
package org.cheminfo.scripting.weka.Classifiers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;

public class Weka_SVM extends Function{

    private SMO classifier;

    /**
     * Name: createClassifier
     * Goal: creates, parametrizes and trains a SVM classifier
     * @param options: the options to be set on the classifier
     * @param train_data: the data used for training. May be the path to the 
     *                    file, the file or data instances
     * @return Classifier: returns the created classifier or null if unsuccessful
     */
    public boolean createClassifier(String options, Object train_data){
        classifier = new SMO();
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
        else {
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
            this.appendError(this.getClass().getName(), "Instance could not be classified: "+e.getMessage());
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

}/*end Weka_SVM*/