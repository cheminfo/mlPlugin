//var train = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_train.json"));
//var test = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_test.json"));

//var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];

var train = "data/iris_train.arff"
var test = "data/iris_test.arff"


//NB Tree
var nbtree_options = {debug:false,
                      classifyInstance:true};//,
                      //dataAttributes: [[-1, "nominal", "country", country_list]]};
var nbtree_result = Weka.classifyNBTree(train, test, nbtree_options);
out.println(nbtree_result.classificationRate);
jexport("NB Tree classification rate", nbtree_result.classificationRate);
jexport("NB Tree distribution", nbtree_result.distributionForInstance);
jexport("NB Tree classify instance", nbtree_result.classifyInstance);