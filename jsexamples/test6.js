var train = JSON.parse(load("data/coffee_train.json"));
var test = JSON.parse(load("data/coffee_test.json"));

var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];


//Hierarchical clusterer
var hc_options = {numClusters:5,
                  linkType:"MEAN",
                  distanceFunction:"ChebyshevDistance",
                  print:false,
                  debug:false,
                  branchLength:false,
                  clusterInstance:true,
                  dataAttributes: [[-1, "nominal", "country", country_list]]};
var hc_result = Weka.clusterHierarchicalClusterer(train, test, hc_options);
jexport("HC distribution", hc_result.distributionForInstance);
jexport("HC cluster instance", hc_result.clusterInstance);