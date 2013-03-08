/**
 * @object: Data_Loader
 * Goal: Allows loading of data for usage with Weka. Transforms raw data into
 *       Instances.
 * Methods: - loadData: loads data from files. Supported formats are those from
 *                      Weka (.arff and .csv are a given)
 *          - loadDataFromJSONObject: parses JSON strings to create datasets.
 *                                    See usage and function header for needed
 *                                    JSON format
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
var DataLoader = {
    
    /**
     * @name: loadData
     * Goal: allows loading of data from files to Weka Instances object
     * Usage: This function requires a path to a file containing data. File types
     *        are those supported by WEKA (at least .arff and .csv)
     *        data meant to be used with a classification method require additional
     *        setup. last parameter (is_classification_data) indicates whether
     *        to do the additional setup or not.
     *        returns a WEKA Instances object.
     * @param file_name: path to the file containing the data
     * @param is_classification_data: indicates if data will be used by a
     *                                classification method
     * @return Instances: the data loaded in a Weka readable format
     */
    loadData: function(file_name, is_classification_data){
        checkGlobal(file_name);
        return Weka_DL.loadData(Global.basedir, Global.basedirkey, file_name, is_classification_data);
    },
    
    /**
     * @name: loadDataFromJSONObject
     * Goal: loads data from a JSON string or object into a Weka Instances dataset
     * Usage: requires a JSON string or object, the dataset name and an indication if
     *        dataset is to be used with a classification method (requires data
     *        formatting).
     *        JSON MUST have an "attributes" array and a "data" array. order of
     *        these arrays is not important. Values names MUST be correct though
     *        attributes array MUST have, for each attribute, in order: name,
     *        attribute type (numeric, string or nominal). If attribute is nominal,
     *        an additional array, containing possible values, MUST be added.
     *        data array MUST have same number of elements in each row. missing
     *        values MUST be indicated by an empty slot. Each column of data
     *        MUST have same type
     *        example:
     *        - JSON string:
     *        var json = "{
     *          attributes:[
     *              [\"att1\",\"Nominal\", [\"val1\", \"val2\", \"val3\"]],
     *              [\"att2\",\"String\"],
     *              [\"att3\",\"Numeric\"]
     *          ],
     *          Data:[
     *              [\"val1\", \"a\", 3],
     *              [\"val2\", \"b\", ], => 3rd value is missing
     *              [\"val3\", , 9] => 2nd value is missing
     *          ]
     *        }";
     *        - JSON object:
     *        var json = {
     *              "Data":[
     *                  [,"a","val1"], => 1st value missing
     *                  [4,,"val2"], => 2nd value missing
     *                  [7,"c",] => 3rd value missing
     *              ],
     *              "attributes":[
     *                  ["att1","Numeric"],
     *                  ["att2","String"],
     *                  ["att3","Nominal", ["val1", "val2", "val3"]]
     *              ]
     *        };
     * @param data_array: the JSON string or object, containing attributes and data
     * @param dataset_name: the name of the dataset
     * @param is_classification_data: indicates if data is used with a classifier
     *                                or not
     * @return Instances: the data in a format used by Weka methods
     */
    loadDataFromJSONObject: function(data_array, attributes_array, dataset_name, is_classification_data){
        if(typeof data_array == 'object')
            data_array = JSON.stringify(data_array);
        return Weka_DL.loadDataFromJSONObject(data_array, attributes_array, dataset_name, is_classification_data);
    }
    
}
