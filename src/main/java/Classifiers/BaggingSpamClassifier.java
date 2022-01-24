package Classifiers;

import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Vote;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

import java.util.ArrayList;

public class BaggingSpamClassifier extends SpamClassifier {

    public BaggingSpamClassifier(ArrayList<SpamClassifier> classifiers){
        if(classifiers.size() % 2 == 0){
            throw new IllegalArgumentException("The number of classifier has to be odd to break classification ties");
        }

        Vote vote = new Vote();
        for(SpamClassifier spamClassifier: classifiers){
            vote.addPreBuiltClassifier(spamClassifier.getClassifier());
        }
        vote.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES));
        baseClassifier = vote;
    }
}
