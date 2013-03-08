var test = JSON.parse(load("data/coffeeNMRSpectra.json"));
var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];
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
//Convert the weka output to class names
var distribution = j48_result.distributionForInstance;
var prediction = j48_result.classifyInstance;
for(var i=0;i<prediction.length;i++)
	prediction[i]=[test.data[i][512],country_list[prediction[i]]];
jexport("J48 classify instance", prediction);
jexport("J48 distribution", distribution);