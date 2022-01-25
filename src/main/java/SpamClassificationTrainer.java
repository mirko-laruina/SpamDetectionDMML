import Classifiers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;

public class SpamClassificationTrainer {

    public static int FOLD_NUMBER = 10;
    public static String BASE_MODELS_FOLDER = "models";

    public static void main (String[] args) throws Exception {

        Instances data = loadDataset("dataset_spamassassin.arff");

        NaiveBayesSpamClassifier naiveBayes = new NaiveBayesSpamClassifier();
        RandomForestSpamClassifier randomForest= new RandomForestSpamClassifier();
        SMOSpamClassifier smo = new SMOSpamClassifier();
        SpamClassifier[] classifiers = new SpamClassifier[]{ naiveBayes, randomForest, smo };
        ArrayList<SpamClassifier> bestClassifiers = new ArrayList<SpamClassifier>();

        for(SpamClassifier classifier : classifiers) {
            SpamClassifier bestClassifier = getBestClassifierKFold(classifier, data);
            bestClassifiers.add(bestClassifier);
            persistModel(bestClassifier);

        }

        BaggingSpamClassifier baggingSpamClassifier = new BaggingSpamClassifier(bestClassifiers);
        baggingSpamClassifier.test(data);
        persistModel(baggingSpamClassifier);
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
        System.out.println("Loading dataset...");
        DataSource source = new DataSource(SpamClassificationTrainer.class.getClassLoader().getResourceAsStream(sourceFile));
        Instances data = source.getDataSet();
        data.randomize(new Random());
        extractHtmlText(data);
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("Loaded");
        return data;
    }

    private static void persistModel(SpamClassifier classifier) throws Exception {
        new File(BASE_MODELS_FOLDER + "/").mkdirs();
        SerializationHelper.write(
                BASE_MODELS_FOLDER + "/" + classifier.getClassifier().getClass().getSimpleName() + "_" + System.currentTimeMillis(),
                classifier.getClassifier()
        );
    }

    private static void extractHtmlText(Instances dataset){
        Attribute htmlAttribute = dataset.attribute("html");
        Attribute bodyAttribute = dataset.attribute("body");
        for(var i = 0; i<dataset.size(); i++){
            Instance instance = dataset.get(i);
            if(instance.value(htmlAttribute) == 1.0){
                Document doc = Jsoup.parse(instance.stringValue(bodyAttribute));
                instance.setValue(bodyAttribute, doc.text());
            }
        }

        dataset.deleteAttributeAt(htmlAttribute.index());
    }
}
