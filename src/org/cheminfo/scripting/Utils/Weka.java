package org.cheminfo.scripting.Utils;


import org.cheminfo.function.Function;
import org.cheminfo.scripting.weka.Classifiers.*;
import org.cheminfo.scripting.weka.Clusterers.*;
import org.cheminfo.scripting.weka.Filters.*;

public class Weka extends Function{
    
    public Weka_J48 createJ48(){
        return new Weka_J48();
    }
    
    public Weka_KNN createKNN(){
        return new Weka_KNN();
    }
    
    public Weka_MLP createMLP(){
        return new Weka_MLP();
    }
    
    public Weka_NBayesianTree createNBTree(){
        return new Weka_NBayesianTree();
    }
    
    public Weka_SVM createSVM(){
        return new Weka_SVM();
    }
    
    public Weka_HierarchicalClusterer createHierarchicalClusterer(){
        return new Weka_HierarchicalClusterer();
    }
    
    public Weka_KMeans createKMeans(){
        return new Weka_KMeans();
    }
    
    public Weka_PCA createPCA(){
        return new Weka_PCA();
    }
    
}
