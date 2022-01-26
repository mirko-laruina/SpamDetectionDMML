package Application.UI;

import Mail.Email;
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
        Font bold = Font.font("Verdana", FontWeight.BOLD, 14);
        Text prependSender = new Text("Sender: ");
        prependSender.setFont(bold);
        senderFlow.getChildren().add(prependSender);
        senderFlow.getChildren().add(sender);

        TextFlow subjectFlow = new TextFlow();
        Text prependSubject = new Text("Subject: ");
        prependSubject.setFont(bold);
        subjectFlow.getChildren().add(prependSubject);
        subjectFlow.getChildren().add(subject);

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
