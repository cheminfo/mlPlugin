//var train = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_train.json"));
//var test = JSON.parse(load("src/org/cheminfo/scripting/data/coffee_test.json"));

//var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];

var train = "data/iris_train.arff"
var test = "data/iris_test.arff"


//SVM
var svm_options = {debug:true,
                   complexity:1.5,
                   normalize:1,
                   tolerance:0.05,
                   roundOff:1.0e-10,
                   fit:true,
                   validationFolds:-1,
                   seed:579843,
                   kernel:"Puk"};//,
                   //dataAttributes: [[-1, "nominal", "country", country_list]]};
var svm_result = Weka.classifySVM(train, test, svm_options);
out.println(svm_result.classificationRate);
jexport("SVM classification rate", svm_result.classificationRate);
jexport("SVM distribution", svm_result.distributionForInstance);
