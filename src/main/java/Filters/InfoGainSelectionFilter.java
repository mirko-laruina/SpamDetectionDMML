package Filters;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.filters.supervised.attribute.AttributeSelection;

public class InfoGainSelectionFilter extends AttributeSelection {
    public InfoGainSelectionFilter(){
        super();
        InfoGainAttributeEval infoGainEval = new InfoGainAttributeEval();
        setEvaluator(infoGainEval);

        Ranker ranker = new Ranker();
        // Select one attribute
        ranker.setNumToSelect(1);
        setSearch(ranker);
    }
}
