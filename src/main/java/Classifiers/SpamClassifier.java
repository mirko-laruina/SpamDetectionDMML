package Classifiers;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public abstract class SpamClassifier {
    protected AbstractClassifier baseClassifier;

    public void train(Instances trainSet) throws Exception {
        baseClassifier.buildClassifier(trainSet);
    }

    public ClassificationStats test(Instances testSet) throws Exception {
        ClassificationStats stats = new ClassificationStats();
        stats.test(baseClassifier, testSet);
        return stats;
    }

    public boolean classify(Instance sample) throws Exception {
        sample.setClassMissing();
        double classificationResult = baseClassifier.classifyInstance(sample);
        return classificationResult == 1.0;
    }

    public Classifier getClassifier(){
        return baseClassifier;
    }

    public void setClassifier(AbstractClassifier newClassifier){
        baseClassifier = newClassifier;
    }

    public void duplicate(SpamClassifier targetClassifier) throws Exception {
        targetClassifier.setClassifier((AbstractClassifier) AbstractClassifier.makeCopy(baseClassifier));
    }
}