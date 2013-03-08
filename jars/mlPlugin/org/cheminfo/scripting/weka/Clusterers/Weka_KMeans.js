/**
 * @object: Weka_KMeans
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
var Weka_KMeans = {
    
    
    /**
     * @name: createCluster
     * Goal: create, parametrizes and trains a K-Means clusterer
     * @param options: the options to be set. Available options are:
     *       -N <num>
     *         number of clusters.
     *         (default 2).
     *
     *        -V
     *         Display std. deviations for centroids.
     *
     *
     *        -M
     *         Replace missing values with mean/mode.
     *
     *
     *       -S <num>
     *         Random number seed.
     *         (default 10)
     *
     *        -A <classname and options>
     *         Distance function to be used for instance comparison
     *         (default weka.core.EuclidianDistance)
     *
     *        -I <num>
     *         Maximum number of iterations. 
     *
     *        -O 
     *         Preserve order of instances.
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return Clusterer: returns the created clusterer or null if unsuccessful
     */
    createCluster: function(options, train_data){
        return Weka_Java_KMeans.createCluster(options, train_data);
    },
    

    /**
     * @name: clusterInstance
     * Goal: adds an instance to the clusterer and indicates to which one it was
     *       allocated
     * @param instance: the data instance to cluster
     * @return int: index of the allocated cluster
     */
    clusterInstance: function(instance){
        return Weka_Java_KMeans.clusterInstance(instance);
    },
    
    /**
     * @name: getClusterCentroids
     * Goal: returns the data instances representing the cluster centroids
     * @return Instances: data instance collection of the instances representing
     *                    cluster centroids
     */
    getClusterCentroids: function(){
        return Weka_Java_KMeans.getClusterCentroids();
    },
    

    /**
     * @name: getClusterFrequencyCounts
     * Goal: Indicates, in each cluster, for each data attribute, the number of 
     *       occurrences of each attribute variant
     * @return int[][][]: for each cluster, the occurrence count of each variant
     *                    of each data attribute
     */
    getClusterFrequencyCounts: function(){
        return Weka_Java_KMeans.getClusterFrequencyCounts();
    },


    /**
     * @name: getClusterSizes
     * Goal: indicates the number of data instances allocated to each cluster
     * @return int[]: for each cluster, the number of data instances
     */
    getClusterSizes: function(){
        return Weka_Java_KMeans.getClusterSizes();
    },
    

    /**
     * @name: setOptions
     * Goal: modifies the clusterer's options
     * @param options: the options to be modified. see createCluster for 
     *                 available options
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_KMeans.setOptions(options);
    }
    
    
}