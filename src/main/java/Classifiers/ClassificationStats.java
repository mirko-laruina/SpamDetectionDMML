package Classifiers;

import weka.classifiers.AbstractClassifier;
import weka.core.Instances;

public class ClassificationStats {

    int[][] confusionMatrix;

    public ClassificationStats(){
        confusionMatrix = new int[2][2];
        initConfusionMatrix();
    }

    public void test(AbstractClassifier classifier, Instances testSet) throws Exception {
        for (int testI = 0; testI < testSet.numInstances(); testI++) {
            double expected = testSet.instance(testI).classValue();
            testSet.instance(testI).setClassMissing();
            double predicted = classifier.classifyInstance(testSet.instance(testI));
            add((int) expected, (int) predicted);
        }

    }

    private void initConfusionMatrix(){
        for(int i = 0; i < confusionMatrix.length; i++){
            for(int j = 0; j<confusionMatrix[0].length; j++){
                confusionMatrix[i][j] = 0;
            }
        }
    }

    public int getFP(){
        return confusionMatrix[1][0];
    }

    public int getTP(){
        return confusionMatrix[1][1];
    }

    public int getFN(){
        return confusionMatrix[0][1];
    }

    public int getTN(){
        return confusionMatrix[0][0];
    }

    public int getTotal(){
        return getTP() + getTN() + getFP() + getFN();
    }

    public float getAccuracy(){
        return (float) 100 * (getTP()+getTN())/getTotal();
    }

    public float getPrecision(){
        return (float) 100 * getTP() / (getTP() + getFP());
    }

    public float getRecall(){
        return (float) 100 * getTP() / (getTP() + getFN());
    }

    public float getFMeasure(){
        return (float) 2 / (1/getPrecision() + 1/getRecall());
    }


    public void add(int predicted, int expected){
        confusionMatrix[predicted][expected] += 1;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("-------------");
        sb.append("\n|");
        sb.append(String.format("%5d", getTP()));
        sb.append("|");
        sb.append(String.format("%5d", getFP()));
        sb.append("|\n");
        sb.append("-------------");
        sb.append("\n|");
        sb.append(String.format("%5d", getFN()));
        sb.append("|");
        sb.append(String.format("%5d", getTN()));
        sb.append("|\n");
        sb.append("-------------\n");
        float accuracy = getAccuracy();
        sb.append(String.format("Accuracy: %5f\n", accuracy));
        float precision = getPrecision();
        float recall = getRecall();
        float fMeasure = getFMeasure();
        sb.append(String.format("Precision: %5f\n", precision));
        sb.append(String.format("Recall: %5f\n", recall));
        sb.append(String.format("F-Measure: %5f\n", fMeasure));
        return sb.toString();
    }


}
