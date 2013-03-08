var train = JSON.parse(load("data/coffee_train.json"));
var test = JSON.parse(load("data/coffee_test.json"));

var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];


var pca_options = {dontNormalize:false,
                   retain:0.99,
                   maxAttributes:-1,
                   maxRetain:-1,
                   dataAttributes: [[-1, "nominal", "country", country_list]],
                   datasetName:"example dataset"};
var pca_result = Weka.filterPCA(train, pca_options);
jexport("PCA filtered data", pca_result.filteredData);