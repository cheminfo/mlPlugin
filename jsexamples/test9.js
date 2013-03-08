/**
 * Demo script for JFuge
 */
//data (replace path on server)
//iris dataset
//var train_iris = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_train.arff";
//var test_iris = "/Users/numa/NUMA/Dossier Ecole/HEIG/Semestre 7/PDB/code/PDB_Trezzini/src/org/cheminfo/scripting/data/iris_test.arff";




var train=JSON.parse(load("data/coffee_train.json"));
var test = JSON.parse(load("/data/coffee_test.json"));

var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil",
                    "CostaRica","Ecuador","Guatemala","Hawaii","Honduras",
                    "India","Indonesia","Mexico","Nicaragua","Panama","Peru",
                    "RepublicaDominicana","Salvador","Tanzania","Togo","Uganda",
                    "unknown","Vietnam"];
// JFuge
var jfuge_options = {mutationRate:0.1,
                     selectionRate:0.3,
                     crossoverRate:0.4,
                     selectionAlgorithm:"tournament",
                     tournamentSize:5,
                     elitismRate:0.1,
                     populationSize:50,
                     numGenerations:10,
                     errorAlgorithm:"rmse",
                     errorWeight:1,
                     classificationWeight:1,
                     ruleNumberWeight:0.5,
                     varPerRuleNumberWeight:0.5,
                     ruleNumber:8,
                     system:false,
                     classifyInstance:true,
                     dataAttributes: [[-1,"nominal", "country", country_list]]};
var jfuge_result = JFuge.classifyJFuge(train, test, jfuge_options);

jexport("system", jfuge_result.system);
jexport("JFuge classify instance", jfuge_result.classifyInstance);