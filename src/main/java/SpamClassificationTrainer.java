import Classifiers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import Utils.*;

public class SpamClassificationTrainer {

    public static int FOLD_NUMBER = 10;
    public static String BASE_MODELS_FOLDER = "models";

    public static void main (String[] args) throws Exception {

        Instances data = loadDataset("dataset_full.arff");
        data = balanceDataset(data);
        data.randomize(new Random());
        data.setClassIndex(data.numAttributes() - 1);

        NaiveBayesSpamClassifier naiveBayes = new NaiveBayesSpamClassifier();
        RandomForestSpamClassifier randomForest= new RandomForestSpamClassifier();
        SMOSpamClassifier smo = new SMOSpamClassifier();
        SpamClassifier[] classifiers = new SpamClassifier[]{ naiveBayes, randomForest, smo };
        ArrayList<SpamClassifier> bestClassifiers = new ArrayList<>();

        for(SpamClassifier classifier : classifiers) {
            SpamClassifier bestClassifier = getBestClassifierKFold(classifier, data);
            bestClassifiers.add(bestClassifier);
            persistModel(bestClassifier);

        }

        BaggingSpamClassifier baggingSpamClassifier = new BaggingSpamClassifier(bestClassifiers);
        baggingSpamClassifier.test(data);
        persistModel(baggingSpamClassifier);
    }

    private static Instances balanceDataset(Instances data) throws Exception {
        // Since we have quite a lot of data (huge training time)
        // we can rebalance by deleting entries from the more frequent class
        // until they are equal
        // Note: the instances are randomized
        AttributeStats stats = data.attributeStats(data.numAttributes() - 1);

        System.out.println(stats);

        int minorityClassNum = Math.min(stats.nominalCounts[0], stats.nominalCounts[1]);
        float resamplePercentage = 2.0F * minorityClassNum / (stats.nominalCounts[0] + stats.nominalCounts[1]);

        Resample resampleFilter = new Resample();
        resampleFilter.setInputFormat(data);
        resampleFilter.setNoReplacement(true);
        resampleFilter.setBiasToUniformClass(1);
        resampleFilter.setSampleSizePercent(100 * resamplePercentage);
        Instances resampledData = Filter.useFilter(data, resampleFilter);

        System.out.println("After resampling ( with percentage " + resamplePercentage + ") :");
        System.out.println(resampledData.attributeStats(resampledData.numAttributes() - 1));
        return resampledData;
    }

    private static SpamClassifier getBestClassifierKFold(SpamClassifier classifier, Instances data) throws Exception {
        int bestFold = 0;
        int bestAccuracy = -1;
        for (int foldNumber = 0; foldNumber < FOLD_NUMBER; foldNumber++) {
            System.out.println("Fold " + foldNumber);
            Instances testSet = data.testCV(FOLD_NUMBER, foldNumber);
            Instances trainSet = data.trainCV(FOLD_NUMBER, foldNumber);

            System.out.println("Train set size: " + trainSet.size());
            System.out.println("Test set size: " + testSet.size());


            classifier.train(trainSet);

            ClassificationStats stats = classifier.test(testSet);

            System.out.println(stats);
            if(stats.getAccuracy() > bestAccuracy){
                bestFold = foldNumber;
            }
        }

        // TODO: classifier selection without redoing training
        classifier.train(data.trainCV(FOLD_NUMBER, bestFold));
        return classifier;
    }

    private static Instances loadDataset(String sourceFile) throws Exception {
        System.out.println("Loading dataset from " + sourceFile + "...");
        DataSource source = new DataSource(SpamClassificationTrainer.class.getClassLoader().getResourceAsStream(sourceFile));
        Instances data = source.getDataSet();
        HtmlExtractor.extractHtml(data);
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("Loaded");
        return data;
    }

    private static void persistModel(SpamClassifier classifier) throws Exception {
        boolean mkdirs = new File(BASE_MODELS_FOLDER + "/").mkdirs();
        if(mkdirs) {
            SerializationHelper.write(
                    BASE_MODELS_FOLDER + "/" + classifier.getClassifier().getClass().getSimpleName() + "_" + System.currentTimeMillis(),
                    classifier.getClassifier()
            );
        }
    }
}
