package Application;

import Email.EmailClient;

public class UIApplication {
    public static void main(String[] args) {

        if(args.length < 5){
            System.out.println("Usage, arguments: host port protocol user password");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String protocol = args[2];
        String user = args[3];
        String pw = args[4];

        EmailClient emailClient = new EmailClient(host, port, protocol, user, pw);
        emailClient.retrieveMails(10);
    }
}
