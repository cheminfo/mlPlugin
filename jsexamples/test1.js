//var train = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_train.json"));
//var test = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_test.json"));

//var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];

var train = "data/iris_train.arff"
var test = "data/iris_test.arff"

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
                   hidden:"5,3",
                   noNumericNormalization:false,
                   noReset:true,
                   decay:true};//,
                   //dataAttributes: [[-1, "nominal", "country", country_list]]};
var mlp_result = Weka.classifyMLP(train, test, mlp_options);
jexport("MLP classification rate", mlp_result.classificationRate);
jexport("MLP distribution", mlp_result.distributionForInstance);