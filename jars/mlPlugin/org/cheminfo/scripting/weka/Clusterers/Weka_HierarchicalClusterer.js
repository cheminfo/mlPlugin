/**
 * @object: Weka_HierarchicalClusterer
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

var Weka_HierarchicalClusterer = {
    
    /**
     * @name: createCluster
     * Goal: creates, parametrizes and trains a hierarchical clusterer
     * @param options: the options to be set. available options are:
     *                -N
     *                number of clusters
     *
     *                -L
     *                 Link type (Single, Complete, Average, Mean, Centroid, Ward, Adjusted complete, Neighbor Joining)
     *                 [SINGLE|COMPLETE|AVERAGE|MEAN|CENTROID|WARD|ADJCOMLPETE|NEIGHBOR_JOINING]
     *
     *                -A
     *                Distance function to use. (default: weka.core.EuclideanDistance)
     *
     *                -P
     *                Print hierarchy in Newick format, which can be used for display in other programs.
     *
     *                -D
     *                If set, classifier is run in debug mode and may output additional info to the console.
     *
     *                -B
     *                \If set, distance is interpreted as branch length, otherwise it is node height.
     * @param train_data: the file containing the training data, its path or the
     *                    Weka data instances
     * @return Clusterer: returns the created clusterer or null if unsuccessful
     */
    createCluster: function(options, train_data){
        return Weka_Java_HC.createCluster(options, train_data);
    },
    
    /**
     * @name: clusterInstance
     * Goal: adds a data instance and allocates it a cluster
     * @param instance: the instance to be clustered
     * @return int: the index of the allocated cluster
     */
    clusterInstance: function(instance){
        return Weka_Java_HC.clusterInstance(instance);
    },
    
    
    /**
     * @name: distributionForInstance
     * Goal: indicates the proportion of membership to each cluster for the
     *       given data instance
     * @param instance: the instance to be clustered
     * @return double[]: for each cluster, the proportion of membership of the
     *                   data instance
     */
    distributionForInstance: function(instance){
        return Weka_Java_HC.distributionForInstance(instance);
    },
    
    
    /**
     * @name: setOptions
     * Goal: modifies the clusterer's options
     * @param options: the options to be modified. available options are listed
     *                 in the createCluster doc
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_HC.setOptions(options);
    }
    
}