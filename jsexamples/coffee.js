var dataset = JSON.parse(load("data/coffeeNMRSpectra.json"));
var train = {"relation":dataset.relation,"attributes":dataset.attributes};
var test = {"relation":dataset.relation,"attributes":dataset.attributes};
var data = dataset.data;
train.data = [];
test.data = [];
for(var i=0;i<data.length;i++){
	if(i%3==0||i%3==1)
		train.data.push(data[i]);
	else
		test.data.push(data[i]);
}

var country_list = ["Brazil","Camerun","China","Colombia","Costa de Marfil","CostaRica","Ecuador","Guatemala","Hawaii","Honduras","India","Indonesia","Mexico","Nicaragua","Panama","Peru","RepublicaDominicana","Salvador","Tanzania","Togo","Uganda","unknown","Vietnam"];
var j48_options = {unpruned:false,
                pruningConfidence:0.5,
                minInstances:1,
                reducedError:true,
                numFolds:5,
                binarySplits:false,
                noRaising:true,
                noCleanup:false,
                probaSmoothing:true,
                seed:672132,
                classIndex:-1,
                classifyInstance:true,
                dataAttributes: [[512, "nominal", "country", country_list]]};

var j48_result = Weka.classifyJ48(train, test, j48_options);
//Convert the weka output to class names
var distribution = j48_result.distributionForInstance;
var prediction = j48_result.classifyInstance;
var ok=0;
for(var i=0;i<prediction.length;i++){
	if(test.data[i][512]==country_list[prediction[i]])
		ok++;
	prediction[i]=[test.data[i][512],country_list[prediction[i]]];
	
}
jexport('ratiotest',ok/prediction.length);
jexport("J48 classify instance", prediction);
jexport("J48 distribution", distribution);