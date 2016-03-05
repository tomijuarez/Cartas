package view.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Card;

/**
 * Created by Gandalf on 13/2/2016.
 */
public class CardView extends ViewPackage {

    private Card card;

    /**
     *
     * @param card
     */

    public CardView(Card card) {
        this.card = card;

        String nick = card.getNick();

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

    public Card getCard() {
        return this.card;
    }
}
