/**
 * Name: DataLoader
 * Goal: Allows loading of data for usage with Weka. Transforms raw data into
 *       Instances.
 * Methods: - loadData: loads data from files. Supported formats are those from
 *                      Weka (.arff and .csv are a given)
 *          - loadDataFromJSONObject: parses JSON strings to create datasets.
 *                                    See usage and function header for needed
 *                                    JSON format
 *          - loadDataFromArray: parses JS arrays to create datasets. See usage
 *                               and function header for needed format
 * Usage: - loadData verifies file path validity and loads data from weka-recognised
 *          file formats (at least .arff and .csv. other formats need testing)
 *        - loadDataFromJSONObject verifies validity of JSON string. JSON MUST
 *          contain an 'attributes' and 'data' array. Attributes MUST have, for
 *          each attribute, the name, the type (numeric, nominal, string), and
 *          in case of nominal attribute, the possible variants. Order of infos
 *          MUST be respected (name, type (,options)). The data array
 *          MUST be of same type in each column. Each row of the data array MUST
 *          be of same length. Missing values MUST be represented by an empty
 *          slot in the array. See function header for examples. Dataset name
 *          also has to be given as a parameter. Attribute and data arrays MUST
 *          have same row-length.
 *        - loadDataFromArray verifies if parameters are JS arrays. Attributes
 *          are created from given parameter. Array MUST contain, for each
 *          attribute, in order: the name, the type, and, if type is nominal, 
 *          the possible options.
 *          Data array MUST have same row-length as attributes array. Data
 *          columns MUST be of same type. Data rows MUST be of same length
 *          (missing values MUST be empty).
 *          Examples in function header
 *        all functions require a boolean to know if data will be used by a 
 *        classification method (classification data requires some additional
 *        settings).
 * 
 * @author: Numa Trezzini
 */
package org.cheminfo.scripting.Utils;


import java.io.FileNotFoundException;
import java.io.IOException;
import org.cheminfo.function.Function;
import org.cheminfo.function.scripting.SecureFileManager;
import org.json.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.*;


public class DataLoader extends Function{
    
    /**
     * @name: loadData
     * Goal: allows loading of data from files to Weka Instances object
     * Usage: This function requires a path to a file containing data. File types
     *        are those supported by WEKA (at least .arff and .csv)
     *        data meant to be used with a classification method require additional
     *        setup. last parameter (is_classification_data) indicates whether
     *        to do the additional setup or not.
     *        returns a WEKA Instances object.
     * @param base_dir: secure directory for loading files
     * @param base_dir_key: secure directory for loading files
     * @param file_name: path to the file containing the data
     * @param is_classification_data: indicates if data will be used by a
     *                                classification method
     * @return Instances: the data loaded in a Weka readable format
     */
    public static Instances loadData(String base_dir, String base_dir_key, String file_name, boolean is_classification_data){
        DataSource ds;
        Instances data;
        //TODO ajouter le controle de fichier
        file_name = SecureFileManager.getValidatedFilename(base_dir, base_dir_key, file_name);
        //if(full_filename==null){System.out.println("failed file search");return null;}
        try {
            ds = new DataSource(file_name);
            data = ds.getDataSet();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if(is_classification_data)
            data.setClassIndex(data.numAttributes()-1);
        return data;
    }/*end loadData*/
    
    
    /**
     * @name: loadDataFromJSONObject
     * Goal: loads data from a JSON string into a Weka Instances dataset
     * Usage: requires a JSON string, the dataset name and an indication if
     *        dataset is to be used with a classification method (requires data
     *        formatting).
     *        JSON MUST have an "attributes" array and a "data" array.
     *        attributes array MUST have, for each attribute, in order: name,
     *        attribute type (numeric, string or nominal). If attribute is nominal,
     *        an additional array, containing possible values, MUST be added.
     *        data array MUST have same number of elements in each row. missing
     *        values MUST be indicated by an empty slot. Each column of data
     *        MUST have same type
     *        example:
     *        "{attributes:[
     *              [\"att1\",\"Nominal\", [\"val1\", \"val2\", \"val3\"]],
     *              [\"att2\",\"String\"],
     *              [\"att3\",\"Numeric\"]
     *          ],
     *          Data:[[\"val1\", \"a\", 3],
     *                [\"val2\", \"b\", ], => last element is missing
     *                [\"val3\", , 9]]}" => second element is missing
     * @param data: the JSON string, containing attributes and data
     * @param dataset_name: the name of the dataset
     * @param is_classification_data: indicates if data is used with a classifier
     *                                or not
     * @return Instances: the data in a format used by Weka methods
     */
    public static Instances loadDataFromJSONObject(Object train_data, Object att_array, String dataset_name, boolean is_classification_data){
        Function f = new DataLoader();
        JSONObject jdata = f.checkParameter(train_data);
        JSONArray att_row = f.checkJSONArray(att_array);
        Instances inst;
        //checking for default dataset name
        if(dataset_name == null)
            dataset_name = "dataset";
        try {
            JSONArray d = jdata.getJSONArray(Constantes.JSONData);
            FastVector attributes = new FastVector();
            for(int i = 0; i < d.getJSONArray(0).length(); i++)
                attributes.addElement(new Attribute("attribute "+i));

            //read and create attributes
            JSONArray current;
            Attribute att;
            Object index = null;
            for(int i=0; i<att_row.length();i++){
                current = att_row.getJSONArray(i);
                index = current.get(0);
                if(index instanceof JSONArray){
                    for(Double j = (Double)((JSONArray)index).get(0); j <= (Double)((JSONArray)index).get(1);j++){
                        addAttributeFromArray(attributes, current, j);
                    }

                }
                else
                    addAttributeFromArray(attributes, current, (Double)index);
            }
            
            //create dataset with given name and attributes
            inst = new Instances(dataset_name, attributes, 0);
            
            //read data instances from JSON
            JSONArray data_instance;
            Instance in;
            for (int i = 0; i < d.length(); i++){
                data_instance = d.getJSONArray(i);
                
                in = new Instance(data_instance.length());
                in.setDataset(inst);
                for (int j = 0; j < data_instance.length(); j++){
                    if(data_instance.get(j) instanceof Double)
                        in.setValue(j, (Double)data_instance.get(j));
                    else if(data_instance.get(j) == null){
                        in.setMissing(j);
                    }
                    else if(data_instance.get(j) instanceof Integer)
                        in.setValue(j, (Integer)data_instance.get(j));
                    else
                        in.setValue(j, (String)data_instance.get(j));
                }
                inst.add(in);
                
            }
            if(is_classification_data){
                if(((Double)index).intValue()==-1)
                    inst.setClassIndex(inst.numAttributes()-1);
                else
                    inst.setClassIndex(((Double)index).intValue());
            }
            return inst;
        } catch (JSONException ex) {
            System.out.println("Error reading JSON data: "+ex.getMessage());
            return null;
        }
        
        
    }/*end loadDataFromJSONObject*/
    
    private static void addAttributeFromArray(FastVector attributes, JSONArray new_attribute, Double index) throws JSONException{
        Attribute att = null;
        if(new_attribute.get(1).equals(Constantes.Numeric))
            att = new Attribute((String)new_attribute.get(2));
        else if(new_attribute.get(1).equals(Constantes.String))
            att = new Attribute((String)new_attribute.get(2), (FastVector)null);
        else if(new_attribute.get(1).equals(Constantes.Nominal)){
            String name = (String)new_attribute.get(2);
            JSONArray nominal_opts = new_attribute.getJSONArray(3);
            FastVector v = new FastVector();
            for(int j = 0; j < nominal_opts.length(); j++){
                v.addElement(nominal_opts.get(j));
            }
            att = new Attribute(name, v);
        }
        else{
            System.out.println("Attribute format not supported! "+new_attribute.get(1));
            return;
        }
        attributes.setElementAt(att, index.intValue()==-1?attributes.size()-1:index.intValue());
    }/*end addAttributeFromArray*/
    
}/*end DataLoader*/
