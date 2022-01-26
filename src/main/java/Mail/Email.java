package Mail;

import Utils.Constants;
import weka.core.Instance;

public class Email {
    public String from;
    public String to;
    public String subject;
    public String body;

    public Email(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Email() {

    }

    public String toString() {
        return "From : " + from +
        "\n" +
        "To : " + to +
        "\n" +
        "Subject : " + subject +
        "\n" +
        "Body : " + body +
        "\n";
    }
}
