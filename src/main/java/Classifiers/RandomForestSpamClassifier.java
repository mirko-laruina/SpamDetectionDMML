package Classifiers;

import Filters.InfoGainSelectionFilter;
import Filters.STWFilter;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.MultiFilter;

public class RandomForestSpamClassifier extends SpamClassifier {

    public RandomForestSpamClassifier(){
        FilteredClassifier filteredClassifier = new FilteredClassifier();

        MultiFilter multiFilter = new MultiFilter();
        STWFilter stwFilter = new STWFilter();
        InfoGainSelectionFilter infoGainSelectionFilter = new InfoGainSelectionFilter();
        multiFilter.setFilters(new Filter[] { stwFilter, infoGainSelectionFilter } );
        filteredClassifier.setFilter(multiFilter);

        RandomForest randomForest = new RandomForest();

        // 0 means auto-detect core num
        randomForest.setNumExecutionSlots(0);

        filteredClassifier.setClassifier(randomForest);
        baseClassifier = filteredClassifier;
    }
}
