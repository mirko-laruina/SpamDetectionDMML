package Mail;

import java.util.ArrayList;

public interface EmailClient {
    ArrayList<Email> retrieveMails(int numMails);
}
