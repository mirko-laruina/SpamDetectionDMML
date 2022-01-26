package Application;

import Application.UI.EmailListCell;
import Application.UI.MainContent;
import Mail.DummyEmailClient;
import Mail.Email;
import javafx.application.Application;
import Mail.EmailClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.cli.*;
import Mail.EmailClientImpl;

public class UIApplication extends Application {

    public final int NUM_MAILS = 25;
    boolean dummyData = false;
    static EmailClient emailClient;
    private final ListView<Email> emailsListView = new ListView<>();
    private ObservableList<Email> emails;
    private MainContent mainContent;


    @Override
    public void start(Stage stage) throws Exception {
        initMailClient();


        BorderPane borderPane = new BorderPane();
        emails = FXCollections.observableArrayList();
        emailsListView.setItems(emails);

        Button refreshButton = new Button();
        refreshButton.setText("Refresh");


        ScrollPane spEmail = new ScrollPane();
        spEmail.setContent(emailsListView);
        spEmail.setFitToWidth(true);
        spEmail.setFitToHeight(true);
        borderPane.setLeft(spEmail);
        borderPane.setTop(refreshButton);

        emailsListView.setCellFactory(emailListView -> new EmailListCell());


        mainContent = new MainContent();
        ScrollPane sp = new ScrollPane(mainContent);
        sp.setFitToWidth(true);
        borderPane.setCenter(sp);

        emailsListView.setOnMouseClicked(mouseEvent -> {
            ObservableList<Email> email = emailsListView.getSelectionModel().getSelectedItems();
            mainContent.setMail(email.get(0));
        });
        refreshButton.setOnAction(actionEvent -> onRefresh());

        onRefresh();
        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setTitle("SpamDetection Mail Client");
        stage.setScene(scene);
        stage.show();
        mainContent.setMail(null);

    }

    private void onRefresh() {
        mainContent.setMail(null);
        emails.clear();
        emails.addAll(emailClient.retrieveMails(NUM_MAILS));
    }

    private void initMailClient() {

        Options options = new Options();
        Option dummyOpt =  new Option("d", "dummy", true,
                "Dummy data file path (arff file)");
        dummyOpt.setRequired(false);


        Option hostOpt = new Option("h", "host", true, "The host to connect to");
        hostOpt.setRequired(false);
        Option portOpt = new Option("port", "port", true, "The port to connect to");
        portOpt.setRequired(false);
        Option protocolOpt = new Option("protocol", "protocol", true, "The protocol to use");
        protocolOpt.setRequired(false);
        Option userOpt = new Option("u", "user", true, "The username credential");
        userOpt.setRequired(false);
        Option passwordOpt = new Option("p", "password", true, "The password for the given user");
        passwordOpt.setRequired(false);

        options.addOption(dummyOpt);
        options.addOption(hostOpt);
        options.addOption(portOpt);
        options.addOption(protocolOpt);
        options.addOption(userOpt);
        options.addOption(passwordOpt);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            Parameters parameters = getParameters();
            String[] args = parameters.getRaw().toArray(String[]::new);
            CommandLine cmd = parser.parse(options, args);
            String dummyValue = cmd.getOptionValue(dummyOpt);
            String hostValue = cmd.getOptionValue(hostOpt);
            if ((dummyValue == null || hostValue != null) && (dummyValue != null || hostValue == null) ) {
                throw new ParseException("Use dummy data or set up the connection. Mutually exclusive");
            }

            String host = cmd.getOptionValue(hostOpt);
            String protocol = cmd.getOptionValue(protocolOpt);
            String user = cmd.getOptionValue(userOpt);
            String pw = cmd.getOptionValue(passwordOpt);
            String portString = cmd.getOptionValue(portOpt);
            int port = -1;
            if(portString != null) {
                port = Integer.parseInt(portString);
            }

            if(dummyValue == null && (
                    host == null
                    || protocol == null
                    || user == null
                    || pw == null
                    || port != -1
                    )
            ){
                throw new ParseException("If dummy value is not set, all the remaining options should be set");
            }

            if(dummyValue != null){
                dummyData = true;
            }


            if(!dummyData) {
                emailClient = new EmailClientImpl(host, port, protocol, user, pw);
            } else {
                emailClient = new DummyEmailClient(dummyValue);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("SpamDetectionClient", options);
            Platform.exit();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
