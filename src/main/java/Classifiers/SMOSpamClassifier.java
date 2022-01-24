package Classifiers;

import Filters.InfoGainSelectionFilter;
import Filters.STWFilter;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.MultiFilter;

public class SMOSpamClassifier extends SpamClassifier {

    public final float TOLERANCE = 0.001F;
    public final float EPSILON_ROUND_OFF = 1e-12F;


    public SMOSpamClassifier(){
        // we have to apply word2vec, so we start with a filtered classifier
        FilteredClassifier filteredClassifier = new FilteredClassifier();

        MultiFilter multiFilter = new MultiFilter();
        STWFilter stwFilter = new STWFilter();

        InfoGainSelectionFilter infoGainSelectionFilter = new InfoGainSelectionFilter();

        multiFilter.setFilters(new Filter[] { stwFilter, infoGainSelectionFilter } );

        // Add our multifilter to the classifier we are building
        filteredClassifier.setFilter(multiFilter);

        SMO smo = new SMO();
        smo.setToleranceParameter(TOLERANCE);
        smo.setEpsilon(EPSILON_ROUND_OFF);

        PolyKernel polyKernel = new PolyKernel();
        // Default exponent
        polyKernel.setExponent(1);
        smo.setKernel(polyKernel);

        Logistic logisticCalibrator = new Logistic();
        smo.setCalibrator(logisticCalibrator);

        filteredClassifier.setClassifier(smo);
        baseClassifier = filteredClassifier;
    }
}
