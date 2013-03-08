/**
 * Name: Weka_HierarchicalClusterer
 * Goal: allows creation and usage of a hierarchical clusterer
 * Methods: - cerateCluster: creates, parametrizes and trains a hierarchical
 *            clusterer
 *          - clusterInstance: attributes a data instance to a cluster
 *          - distributionForInstance: computes the proportion of membership
 *            to each cluster for the given instance
 *          - setOptions: modifies the clusterer's options
 * Usage: First, create, parametrize and train the clusterer with createCluster
 *        Then, either compute most likely cluster or proportions of membership
 *        to each cluster for a data instance, with clusterInstance or 
 *        distributionForInstance, respectively. Option may be set at any time,
 *        but this is NOT recommended
 * @author: Numa Trezzini
 */

package org.cheminfo.scripting.weka.Clusterers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;
import weka.core.Instances;


public class Weka_HierarchicalClusterer extends Function{

    private HierarchicalClusterer cluster;
    
    /**
     * Name: createCluster
     * @param options: the options the clusterer may take. The list of options
     *                 is available in the JS or Weka doc
     * @param train_data: the data used for training. May be the file containing
     *                    the training data, its path or the data instances
     * @return Clusterer: returns the created clusterer or null if unsuccessful
     */
    public boolean createCluster(String options, Object train_data){
        cluster = new HierarchicalClusterer();
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            cluster.setOptions(opts);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("Option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }
        
        Instances data = null;
        if(train_data instanceof String)
            data = DataLoader.loadData(null, null, (String)train_data, false);
        else if(train_data instanceof Instances)
            data = (Instances)train_data;
        else {
            System.out.println("train data not supported!");
            this.appendError(this.getClass().getName(), "Training data format not supported");
            return false;
        }
        
        try {
            cluster.buildClusterer(data);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Clusterer could not be created: "+e.getMessage());
            System.out.println("Cluster non initialise: ");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Clusterer was successfully created and trained");
        return true;
    }/*end createCluster*/
    
    /**
     * Name: clusterInstance
     * Adds an instance of data to the clusters following the KMeans algorithm
     * @param c: the clusterer to be used
     * @param i: the data instance to cluster
     * @return int: the number of the cluster the instance was added to
     */
    public int clusterInstance(Instance i){
        try {
            return cluster.clusterInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Instance could not be clustered: "+e.getMessage());
            System.out.println("les donnees n'ont pas pu etre traitees: ");
            System.out.println(e.getMessage());
            return -1;
        }
    }/*end clusterInstance*/
    
    /**
     * Name: distributionForInstance
     * Indicates, for each cluster, the proportion of membership of the instance
     * This method also adds the instance to the clusters
     * @param i: the instance to be computed
     * @return double[]: for each cluster, the probability of membership of the
     *                   instance
     */
    public double[] distributionForInstance(Instance i){
        try {
            return cluster.distributionForInstance(i);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Distribution could not be computed: "+e.getMessage());
            System.out.println("Les donnees n'ont pas pu etre traitees: ");
            System.out.println(e.getMessage());
            return null;
        }
    }/*end distributionForInstance*/
    
    /**
     * Name: setOptions
     * Allows the modification of the clusterer's options
     * @param c: the clusterer to be modified
     * @param options: the options to be modified
     * @return boolean: indicates if the modification was successful
     */
    public boolean setOptions(String[] options){
        try {
            cluster.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("Option non reconnue: ");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/

}/*end Weka_HierarchicalClusterer*/

