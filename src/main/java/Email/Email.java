package Email;

public class Email {
    public String from;
    public String to;
    public String subject;
    public String body;

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
