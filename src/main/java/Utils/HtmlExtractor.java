package Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class HtmlExtractor {
    public static void extractHtml(Instances dataset){
        Attribute htmlAttribute = dataset.attribute(Constants.DATASET_HTML);
        Attribute bodyAttribute = dataset.attribute(Constants.DATASET_BODY);
        for (Instance instance : dataset) {
            if (instance.value(htmlAttribute) == 1.0) {
                Document doc = Jsoup.parse(instance.stringValue(bodyAttribute));
                instance.setValue(bodyAttribute, doc.text());
            }
        }

        dataset.deleteAttributeAt(htmlAttribute.index());
    }
}
