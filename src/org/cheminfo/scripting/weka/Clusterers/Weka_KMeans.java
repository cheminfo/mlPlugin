/**
 * Name: Weka_KMeans
 * Goal: allows creation and usage of a clusterer based on KMeans algorithm
 * Methods: - createCluster: creates, parametrizes and trains a KMeans clusterer
 *          - clusterInstance: adds a data instance to the cluster, indicating
 *            to which it was assigned
 *          - getClusterCentroids: indicates the instances representing the
 *            centroids for each cluster
 *          - getClusterFrequencyCounts: indicates, for each attribute, the
 *            number of occurrences of each variant
 *          - getClusterSizes: indicates the number of instances assigned to each
 *            cluster
 *          - setOptions: modifies the options the clusterer
 * Usage: first, create, parametrize and train the clusterer with createCluster.
 *        Then, add data instances to the clusterer with clusterInstance.
 *        Information on the clusters can be retireved with the get* methods.
 *        Options may be modified at any time, but this is NOT recommended
 * @author: Numa Trezzini
 */
package org.cheminfo.scripting.weka.Clusterers;

import org.cheminfo.function.Function;
import org.cheminfo.scripting.Utils.DataLoader;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;

public class Weka_KMeans extends Function{

    private SimpleKMeans cluster;
    
    /**
     * Name: createCluster
     * @param options: the options the clusterer may take. The list of options
     *                 is available in the JS or Weka doc
     * @param train_data: the data to be used for training. May be the path to
     *                    the file containing the data, the file or the data
     *                    instances
     * @return Clusterer: returns the trained clusterer or null if unsuccessful
     */
    public boolean createCluster(String options, Object train_data){
        cluster = new SimpleKMeans();
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
     * Name: getClusterCentroids
     * @param c: the clusterer to ne used
     * @return Instances: returns the clustered instances representig the
     *                    centroids of each cluster
     */
    public Instances getClusterCentroids(){
        return cluster.getClusterCentroids();
    }/*end getClusterCentroids*/
    
    /**
     * Name: getClusterFrequencyCounts
     * Indicates, for each data attribute, the occurence count of each variant
     * @param c: the clusterer to be used
     * @return int[][][]: the occurence count for each variant of the attributes
     *                    form the data
     */
    public int[][][] getClusterFrequencyCounts(){
        return cluster.getClusterNominalCounts();
    }/*end getClusterFrequencyCounts*/
    
    /**
     * Name: getClusterSizes
     * Indicates the size (number of instances) of each cluster
     * @param c: the clusterer to be used
     * @return int[]: indicates the number of instances in each cluster
     */
    public int[] getClusterSizes(){
        return cluster.getClusterSizes();
    }/*end getClusterSizes*/
    
    /**
     * Name: setOptions
     * Goal: allows modification of the clusterer options
     * @param c: the clusterer to be modified
     * @param options: the options to be set
     * @return boolean: indicates whether the modification was successful or not
     */
    public boolean setOptions(Clusterer c, String[] options){
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
    
}/*end Weka_KMeans*/