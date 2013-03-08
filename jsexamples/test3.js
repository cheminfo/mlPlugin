//var train = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_train.json"));
//var test = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_test.json"));

//var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];

var train = "data/iris_train.arff"
var test = "data/iris_test.arff"


// KNN
var knn_options = {numReferences:2,
                   numCiters:2,
                   hausdorffRank:3,
                   classIndex:-1,
                   distance:true};
var knn_result = Weka.classifyKNN(train, test, knn_options);
jexport("KNN classification rate", knn_result.classificationRate);
jexport("KNN distribution", knn_result.distributionForInstance);
jexport("KNN distance", knn_result.distance);