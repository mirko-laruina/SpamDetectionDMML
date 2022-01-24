package Filters;

import weka.core.Range;
import weka.core.SelectedTag;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class STWFilter extends StringToWordVector {
    public final int DICTIONARY_SIZE = 1000;
    public final float PERIODIC_PRUNING_RATE = 0.2F;

    public STWFilter(){
        super();

        // This adds a prefix to the attributes generate internally by StringToWordVector
        // It is done to avoid conflicts with the existing attribute names
        // Avoids error like: "Attribute names are not unique!"
        this.setAttributeNamePrefix("STW_");

        // Set how large the dictionary should be
        this.setWordsToKeep(DICTIONARY_SIZE);

        // Prune every 20% of dataset to DICTIONARY_SIZE words (otherwise it prunes only at the end)
        this.setPeriodicPruning(-PERIODIC_PRUNING_RATE);

        // Use TF-IDF
        this.setIDFTransform(true);

        // Keep document length / do not normalize (it may be important)
        this.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NONE, StringToWordVector.TAGS_FILTER));

        // Use lowercase
        this.setLowerCaseTokens(true);

        // This should not be needed because of dict size, but let's ignore the terms appearing once (should be typos)
        this.setMinTermFreq(2);


        Rainbow rainbowStopwords = new Rainbow();
        AlphabeticTokenizer alphabeticTokenizer = new AlphabeticTokenizer();
        SnowballStemmer snowballStemmer = new SnowballStemmer();

        // Set english stemmer ( https://snowballstem.org/algorithms/ )
        snowballStemmer.setStemmer("porter");

        this.setStopwordsHandler(rainbowStopwords);
        this.setTokenizer(alphabeticTokenizer);
        this.setStemmer(snowballStemmer);
    }
}
