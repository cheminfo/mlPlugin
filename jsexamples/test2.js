//var train = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_train.json"));
//var test = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_test.json"));

//var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];

var train = "data/iris_train.arff"
var test = "data/iris_test.arff"


// J48
var j48_options = {unpruned:false,
                   pruningConfidence:0.5,
                   minInstances:3,
                   binarySplits:false,
                   noRaising:true,
                   noCleanup:false,
                   probaSmoothing:true,
                   seed:4322132,
                   classIndex:-1,
                   classifyInstance:true};//,
                   //dataAttributes: [[-1, "nominal", "country", country_list]]};
var j48_result = Weka.classifyJ48(train, test, j48_options);
jexport("J48 classification rate", j48_result.classificationRate);
jexport("J48 distribution", j48_result.distributionForInstance);
jexport("J48 classify instance", j48_result.classifyInstance);