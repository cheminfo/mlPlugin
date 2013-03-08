/**
 * @object: Weka_PCA
 * Goal: allows creation and usage of a Principal Component Analysis filter (PCA)
 * Methods: - createFilter: creates and parametrizes a PCA filter
 *          - input: not available yet
 *          - output: not available yet
 *          - useFilter: Filters the given data instances using PCA algorithm
 *          - setOptions: modifies the options of the filter
 * Usage: First, create the filter with createFilter. Then, filter data instances
 *        to reduce information with useFilter. options may be modified at any
 *        time, but this is NOT recommended
 *        For technical reasons, data may NOT be filtered one instance at a time
 *        yet
 * @author: Numa Trezzini
 */
var Weka_PCA = {
	

    /**
     * @name: createFilter
     * Goal: create and parametrize a Principal Component Analysis.
     * @param: options: options to be set. Available options are:
     *		   -D -> Don't normalize input data.
     *
     *		   -R <num> -> Retain enough PC attributes to account
     * 					   for this proportion of variance in 
     * 					   the original data.
     * 					   (default: 0.95)
     *
     *		   -A <num> -> Maximum number of attributes to include in 
     * 					   transformed attribute names.
     * 					   (-1 = include all, default: 5)
     *
     *		   -M <num> -> Maximum number of PC attributes to retain.
     *					   (-1 = include all, default: -1)
     * @return Filter: returns the created filter or null if unsuccessful
     */
    createFilter: function(options){
        return Weka_Java_PCA.createFilter(options);
    },
    
    /**
     * @name: useFilter
     * Goal: filters a collection of data instances
     * @param dataset: the instances to be filtered
     * @return Instances: filtered (i.e. reduced) instances
     */
    useFilter: function(dataset){
        return Weka_Java_PCA.useFilter(dataset);
    },


    /**
     * @name: setOptions
     * Goal: modifies the options of the filter
     * @param options: the options to be set. see createFilter for available
     *                 options
     * @return boolean: indicates whether the modification was successful or not
     */
    setOptions: function(options){
        return Weka_Java_PCA.setOptions(options);
    }

	
}
