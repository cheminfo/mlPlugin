var train = JSON.parse(load("data/coffee_train.json"));
var test = JSON.parse(load("data/coffee_test.json"));

var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];


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
                      getClusterSizes:true,
                      dataAttributes: [[-1, "nominal", "country", country_list]]};
var kmeans_result = Weka.clusterKMeans(train, test, kmeans_options);
jexport("KMeans cluster instance", kmeans_result.clusterInstance);
jexport("KMeans centroids", kmeans_result.centroids);
jexport("KMeans frequencyCounts", kmeans_result.frequencyCounts);
jexport("KMeans cluster sizes", kmeans_result.clusterSizes);
