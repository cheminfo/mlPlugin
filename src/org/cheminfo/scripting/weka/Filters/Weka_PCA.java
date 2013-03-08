/**
 * Name: Weka_PCA
 * Goal: allows creation and usage of a Principal Component Analysis filter (PCA)
 * Methods: - createFilter: creates and parametrizes a PCA filter
 *          - useFilter: Filters the given data instances using PCA algorithm
 *          - setOptions: modifies the options of the filter
 * Usage: First, create the filter with createFilter. Then, filter data instances
 *        to reduce information with useFilter. options may be modified at any
 *        time, but this is NOT recommended
 *        For technical reasons, data may NOT be filtered one instance at a time
 *        yet
 * @author: Numa Trezzini
 */

package org.cheminfo.scripting.weka.Filters;


import org.cheminfo.function.Function;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

public class Weka_PCA extends Function{

    private PrincipalComponents filter;
    
    /**
     * Name: createFilter
     * Goal: creates and parametrizes a PCA filter
     * @param options: the options to be set on the filter
     * @return Filter: returns the created filter or null if unsuccessful
     */
    public boolean createFilter(String options){

        //creation du filtre
        filter = new PrincipalComponents();
        
        //modification des options
        try {
            String[] opts = weka.core.Utils.splitOptions(options);
            filter.setOptions(opts);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        if(getLogLevel()>2)
            this.appendInfo(this.getClass().getName(), "Clusterer was successfully created and trained");
        return true;
    }/*end createFilter*/
    
    /**
     * Name: useFilter
     * Goal: filters a collection of data instances to reduce volume of data
     * @param f: the filter to be used
     * @param dataset: the dataset to be filtered
     * @return Instances: the filtered data
     */
    public Instances useFilter(Instances dataset){
        try {
            filter.setInputFormat(dataset);
            return Filter.useFilter(dataset, filter);
        }
        catch(Exception e){
            this.appendError(this.getClass().getName(), "Filter could not be used: "+e.getMessage());
            System.out.println("le filtre n'a pas pu être utilisé correctement: ");
            System.out.println(e.getMessage());
            return null;
        }
    }/*end useFilter*/
    
    
    /**
     * Name: setOptions
     * Goal: allows modification of the filter options
     * @param f: the filter to be modified
     * @param options: the options to be set
     * @return boolean: indicates whether the modification was successful or not
     */
    public boolean setOptions(String[] options){
        try {
            filter.setOptions(options);
        } catch (Exception e) {
            this.appendError(this.getClass().getName(), "Option not recognised: "+e.getMessage());
            System.out.println("option non reconnue:");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }/*end setOptions*/

}/*end Weka_PCA*/