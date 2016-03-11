package view.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.AbstractCharacter;
import model.Character;

/**
 * Created by Gandalf on 3/3/2016.
 */
public class CharacterView extends ViewPackage {

    private AbstractCharacter character;


    public CharacterView(AbstractCharacter character) {
        this.character = character;

        String nick = character.getFictitiousName();

        this.container = new Pane();

        Text cardName = new Text();
        ImageView cardImageView = new ImageView();

        VBox verticalCardElements = new VBox();
        BorderPane positionalPane = new BorderPane();

        cardImageView.setFitWidth(150);
        cardImageView.setFitHeight(150);
        cardImageView.setImage(new Image(this.getImagePath(nick)));

        cardName.setText(nick.toUpperCase());

        verticalCardElements.setPrefSize(150,170);

        verticalCardElements.getChildren().addAll(cardImageView, cardName);

        this.container.getChildren().add(verticalCardElements);
    }

    public void select() {
        this.container.setStyle("-fx-border-width: 2px; -fx-border-color: green;");
    }

    public void unselect() {
        this.container.setStyle("-fx-border-width: 2px; -fx-border-color: transparent;");
    }

    public AbstractCharacter getCharacter() {
        return this.character;
    }
}
