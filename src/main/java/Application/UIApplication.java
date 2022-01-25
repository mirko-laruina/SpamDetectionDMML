package Application;

import Application.UI.EmailListCell;
import Application.UI.MainContent;
import Email.Email;
import javafx.application.Application;
import Email.EmailClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class UIApplication extends Application {

    static EmailClient emailClient;
    private final ListView<Email> emails = new ListView<>();


    @Override
    public void start(Stage stage) throws Exception {
        initMailClient();


        BorderPane borderPane = new BorderPane();
        ObservableList<Email> observedEmails = FXCollections.observableArrayList();
        emails.setItems(observedEmails);

        Button refreshButton = new Button();
        refreshButton.setText("Refresh");


        ScrollPane spEmail = new ScrollPane();
        spEmail.setContent(emails);
        spEmail.setFitToWidth(true);
        spEmail.setFitToHeight(true);
        borderPane.setLeft(spEmail);
        borderPane.setTop(refreshButton);

        observedEmails.addAll(emailClient.retrieveMails(10));
        
        emails.setCellFactory(emailListView -> new EmailListCell());


        MainContent mainContent = new MainContent();
        ScrollPane sp = new ScrollPane(mainContent);
        sp.setFitToWidth(true);
        borderPane.setCenter(sp);

        emails.setOnMouseClicked(mouseEvent -> {
            ObservableList<Email> email = emails.getSelectionModel().getSelectedItems();
            mainContent.setMail(email.get(0));
        });
        refreshButton.setOnAction(actionEvent -> {
            System.out.println("Refresh requested");
            mainContent.setMail(null);
        });

        Scene scene = new Scene(borderPane, 1024, 768);
        stage.setTitle("SpamDetection Mail Client");
        stage.setScene(scene);
        stage.show();
        mainContent.setMail(null);

    }

    private void initMailClient() {
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();
        if(args.size() < 5){
            System.out.println("Usage, arguments: host port protocol user password");
        }

        String host = args.get(0);
        int port = Integer.parseInt(args.get(1));
        String protocol = args.get(2);
        String user = args.get(3);
        String pw = args.get(4);

        emailClient = new EmailClient(host, port, protocol, user, pw);
    }
}
