package Mail;

import Utils.Constants;
import Utils.HtmlExtractor;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DummyEmailClient implements EmailClient{

    Instances data;

    public DummyEmailClient(String filename) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(DummyEmailClient.class.getClassLoader().getResourceAsStream(filename));
        data = source.getDataSet();
        HtmlExtractor.extractHtml(data);
    }

    @Override
    public ArrayList<Email> retrieveMails(int numMails) {
        Attribute fromAttribute = data.attribute(Constants.DATASET_FROM);
        Attribute toAttribute = data.attribute(Constants.DATASET_TO);
        Attribute bodyAttribute = data.attribute(Constants.DATASET_BODY);
        Attribute subjectAttribute = data.attribute(Constants.DATASET_SUBJECT);
        data.randomize(new Random());

        ArrayList<Email> emails = new ArrayList<>();
        for(int i = 0 ; i < numMails; i++){
            Instance instance = data.get(i);
            Email email = new Email();
            email.from = instance.stringValue(fromAttribute);
            email.to = instance.stringValue(toAttribute);
            email.subject = instance.stringValue(subjectAttribute);
            email.body = instance.stringValue(bodyAttribute);
            emails.add(email);
        }
        return emails;
    }
}
