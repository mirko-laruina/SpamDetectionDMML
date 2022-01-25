package Application.UI;

import Email.Email;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MailInfo extends VBox {
    Text sender = new Text();
    Text subject = new Text();

    public MailInfo() {
        super();
        TextFlow senderFlow = new TextFlow();
        senderFlow.getChildren().add(new Text("Sender: "));
        senderFlow.getChildren().add(sender);

        TextFlow subjectFlow = new TextFlow();
        subjectFlow.getChildren().add(new Text("Subject: "));
        subjectFlow.getChildren().add(subject);

        sender.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        subject.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        sender.setText("...");
        subject.setText("...");
        getChildren().add(senderFlow);
        getChildren().add(subjectFlow);
        setSpacing(5);
        setPadding(new Insets(20));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setMail(Email email){
        if(email != null) {
            sender.setText(email.from);
            subject.setText(email.subject);
        } else {
            sender.setText("...");
            subject.setText("...");
        }
    }
}
