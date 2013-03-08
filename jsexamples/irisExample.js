var train = Weka.load("/data/iris_train.arff");
var test = Weka.load("/data/iris_test.arff");
jexport("nTrain",train.numInstances());
jexport("nTest",test.numInstances());
var param = [];
param[0]="-U";
var model = Weka.train(train, param);
jexport("model",model);
var nInstances = test.numInstances();
var result = [];
for(var i=0;i<nInstances;i++){
    var sample = test.get(i);
	try {
		//
        result[i]={predicted: sample.classValue(), expected:model.classifyInstance(sample)};
        if(sample.classValue()==model.classifyInstance(sample))
          result[i].state="OK";
         else
           result[i].state="FAIL";
	} catch(err) {
       out.println("Error: "+err);
	}
}
jexport("result",result);