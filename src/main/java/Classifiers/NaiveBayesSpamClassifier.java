package Classifiers;

import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.AlphabeticTokenizer;

public class NaiveBayesSpamClassifier extends SpamClassifier {

    public NaiveBayesSpamClassifier() throws Exception {
        String[] options = weka.core.Utils.splitOptions("-W -P 0 -M 2.0");
        Rainbow rainbowStopwords = new Rainbow();
        AlphabeticTokenizer alphabeticTokenizer = new AlphabeticTokenizer();
        SnowballStemmer snowballStemmer = new SnowballStemmer();
        // Set english stemmer ( https://snowballstem.org/algorithms/ )
        snowballStemmer.setStemmer("porter");

        NaiveBayesMultinomialText naiveBayesMultinomialText = new NaiveBayesMultinomialText();
        naiveBayesMultinomialText.setNorm(1);
        naiveBayesMultinomialText.setLNorm(2);
        naiveBayesMultinomialText.setTokenizer(alphabeticTokenizer);
        naiveBayesMultinomialText.setStopwordsHandler(rainbowStopwords);
        naiveBayesMultinomialText.setStemmer(snowballStemmer);
        naiveBayesMultinomialText.setOptions(options);
        baseClassifier = naiveBayesMultinomialText;
    }
}
