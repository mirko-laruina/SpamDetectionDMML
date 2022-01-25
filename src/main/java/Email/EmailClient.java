package Email;

import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Properties;

public class EmailClient {

    Properties properties;
    Authenticator authenticator;

    public EmailClient(String host, int port, String protocol, String user, String password){
        properties = new Properties();
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.port", String.valueOf(port));
        properties.setProperty("mail.transport.protocol", protocol);
        authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };
    }

    public ArrayList<Email> retrieveMails(int numMails){
        Session session = Session.getInstance(properties, authenticator);
        ArrayList<Email> emails = new ArrayList<>();

        try {
            Store store = session.getStore("imaps");
            store.connect();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            System.out.println("Number of mails = " + messages.length);
            for ( Message message : messages ) {
                Email email = new Email();
                email.from = String.valueOf(message.getFrom()[0]);
                email.to = String.valueOf(message.getRecipients(Message.RecipientType.TO)[0]);
                email.subject = message.getSubject();
                email.body = getMessagePlainContent(message);
                emails.add(email);
            }
            inbox.close(true);
            store.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return emails;
    }

    public static String getMessagePlainContent(Message message) throws Exception {
        if(message.isMimeType("text/plain")){
            return message.getContent().toString();
        }

        if(message.isMimeType("multipart/*")){
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            return getMultiPartContent(multipart);
        }
        return "";
    }

    public static String getMultiPartContent(MimeMultipart content) throws Exception {

        int partCount = content.getCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partCount; i++) {
            BodyPart bodyPart = content.getBodyPart(i);
            Object bodyPartContent = bodyPart.getContent();
            if (bodyPart.isMimeType("text/plain")) {
                sb.append(bodyPartContent);
            } else if (bodyPart.isMimeType("text/html")) {
                String htmlPart = (String) bodyPartContent;
                sb.append(Jsoup.parse(htmlPart).text());
            } else if (bodyPartContent instanceof MimeMultipart){
                sb.append(getMultiPartContent((MimeMultipart) bodyPartContent));
            }
        }
        return sb.toString();
    }
}
