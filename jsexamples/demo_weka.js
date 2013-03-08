/**
 * Demo script for weka methods
 * - classifiers
 *    - J48 (decision tree)
 *    - KNN (K-Nearest Neighbours)
 *    - MLP (Multi-Layer Perceptron)
 *    - NBayesianTree (Naive Bayesian Tree)
 *    - SVM
 * - clusterers
 *    - Hierarchical Clusterer
 *    - KMeans
 * - filters
 *    - PCA
 */

var test = JSON.parse(load("data/coffeeNMRSpectra.json"));

//jexport("1", test);

//data (replace path on server)
//iris dataset
var train_iris = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_train.arff";
var test_iris = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_test.arff";

//anneal dataset
var anneal = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/anneal.arff";

//ERA dataset
var era = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/ERA.arff";

//ESL dataset
var esl = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/ESL.arff";

//LEV dataset
var lev = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/LEV.arff";

//SWD dataset
var swd = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/SWD.arff";


var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];
// J48
var j48_options = {unpruned:false,
                   pruningConfidence:0.5,
                   minInstances:3,
                   reducedError:true,
                   numFolds:5,
                   binarySplits:false,
                   noRaising:true,
                   noCleanup:false,
                   probaSmoothing:true,
                   seed:4322132,
                   classIndex:-1,
                   classifyInstance:true,
                   dataAttributes: [[512, "nominal", "country", country_list]]};
var j48_result = Weka.classifyJ48(test, test, j48_options);



jexport("J48 distribution", j48_result.distributionForInstance);
jexport("J48 classify instance", j48_result.classifyInstance);


// KNN
/*var knn_options = {numReferences:2,
                   numCiters:2,
                   hausdorffRank:3,
                   classIndex:-1,
                   distance:true};
var knn_result = Weka.classifyKNN(anneal, anneal, knn_options);
jexport("KNN distribution", knn_result.distributionForInstance);
jexport("KNN distance", knn_result.distance);

// MLP
var mlp_options = {learningRate:0.5,
                   momentum:0.3,
                   numEpochs:1000,
                   validationSetSize:10,
                   seed:5438927,
                   errorThreshold:50,
                   gui:false,
                   autocreation:false,
                   nominalToBinary:false,
                   hidden:"5,5,3", //creates 3 hidden layers, with 5, 5 and 3 neurons, respectively
                   noNumericNormalization:false,
                   noReset:true,
                   decay:true};
var mlp_result = Weka.classifyMLP(anneal, anneal, mlp_options);
jexport("MLP distribution", mlp_result.distributionForInstance);

//NB Tree
var nbtree_options = {debug:true,
                      classifyInstance:true};
var nbtree_result = Weka.classifyNBTree(train_iris, test_iris, nbtree_options);
jexport("NB Tree distribution", nbtree_result.distributionForInstance);
jexport("NB Tree classify instance", nbtree_result.classifyInstance);

//SVM
var svm_options = {debug:true,
                   complexity:1.5,
                   normalize:1,
                   tolerance:0.05,
                   roundOff:1.0e-10,
                   fit:true,
                   validationFolds:10,
                   seed:579843,
                   kernel:"Puk"};
var svm_result = Weka.classifySVM(train_iris, test_iris, svm_options);
jexport("SVM distribution", svm_result.distributionForInstance);

//Hierarchical clusterer
var hc_options = {numClusters:5,
                  linkType:"MEAN",
                  distanceFunction:"ChebyshevDistance",
                  print:false,
                  debug:true,
                  branchLength:false,
                  clusterInstance:true};
var hc_result = Weka.clusterHierarchicalClusterer(swd, swd, hc_options);
jexport("HC distribution", hc_result.distributionForInstance);
jexport("HC cluster instance", hc_result.clusterInstance);

//KMeans
var kmeans_options = {numClusters:3,
                      stdDeviation:true,
                      missing:true,
                      seed:5432789,
                      distanceFunction:"ManhattanDistance",
                      maxIter:1000,
                      order:true,
                      getCentroids:true,
                      getFrequencyCounts:false,
                      getClusterSizes:true};
var kmeans_result = Weka.clusterKMeans(era, era, kmeans_options);
jexport("KMeans cluster instance", kmeans_result.clusterInstance);
jexport("KMeans centroids", kmeans_result.centroids);
//jexport("KMeans frequencyCounts", kmeans_result.frequencyCounts);
jexport("KMeans cluster sizes", kmeans_result.clusterSizes);

//PCA
//PCA cannot handle string attributes!
//example of created data:
//data MUST have same row length!
//missing values MUST be indicated with an empty array cell
var data = [[1, 2, 2, "value1"],
            [3, 3, 4, "value2"],
            [ , 4, 5, "value1"],
            [6,    , 7, "value3"],
            [8, 5, 9,         ]];
//Attributes are given in the options
//numeric attributes are detected by default. ANY other type MUST be indicated
//Attributes range from 0 to numAttributes-1
//attributes MUST be an arry with following pattern:
//[index, "type", "name"] in case of string or numeric attribute
//[index, "nominal", "name", ["values"]] in case of nominal attribute, where values are comma separated strings
//index MAY be an array containing a range: [start_index, end_index]. INCLUDES start and end index
var pca_options = {dontNormalize:false,
                   retain:0.99,
                   maxAttributes:-1,
                   maxRetain:-1,
                   dataAttributes:[
                                   //[1, "string", "string attribute"],
                                   [3, "nominal",  "nominal attribute", ["value1", "value2", "value3"]]
                                  ],
                  datasetName:"example dataset"};
var pca_result = Weka.filterPCA(data, pca_options);
jexport("PCA filtered data", pca_result.filteredData);*/

