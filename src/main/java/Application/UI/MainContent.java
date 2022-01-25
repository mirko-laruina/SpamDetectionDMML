package Application.UI;

import Email.Email;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainContent extends GridPane {
    MailInfo mailInfo;
    Text body;
    TextFlow bodyPane;

    public MainContent(){
        super();
        body = new Text();
        bodyPane = new TextFlow();
        bodyPane.getChildren().add(body);
        bodyPane.setPadding(new Insets(20));
        mailInfo = new MailInfo();
        mailInfo.prefWidthProperty().bind(prefWidthProperty());
        add(mailInfo, 0, 0);
        add(bodyPane, 0, 1);
        setHgrow(bodyPane, Priority.ALWAYS);
        setHgrow(mailInfo, Priority.ALWAYS);
        setSpam(false);
    }

    public void setMail(Email email){
        mailInfo.setMail(email);
        if(email != null){
            body.setText(email.body);
        } else {
            body.setText("No email selected...");
        }
    }

    public void setSpam(boolean isSpam){
        Color color = isSpam ? Color.PINK : Color.rgb(191, 255, 195);
        bodyPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
