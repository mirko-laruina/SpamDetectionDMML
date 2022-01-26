package Application.UI;

import Mail.Email;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class EmailListCell extends ListCell<Email> {

    Text sender;
    Text subject;
    VBox content;

    public EmailListCell(){
        super();
        sender = new Text();
        subject = new Text();
        content = new VBox(sender, subject);
        sender.setFont(Font.font("Verdana", 14));
        subject.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        content.setSpacing(5);
        content.setPadding(new Insets(5));
    }

    @Override
    protected void updateItem(Email email, boolean empty){
        super.updateItem(email, empty);
        if(email != null && !empty){
            sender.setText(email.from);
            subject.setText(email.subject);
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

    @Override
    public void updateSelected(boolean selected){
        super.updateSelected(selected);
        if(selected){
            sender.setFill(Color.WHITE);
            subject.setFill(Color.WHITE);
        } else {
            sender.setFill(Color.BLACK);
            subject.setFill(Color.BLACK);
        }
    }
}
