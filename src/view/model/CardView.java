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

    private static int SMALL_HEIGHT = 150;
    private static int SMALL_WIDTH  = 110;

    private static int BIG_HEIGHT   = 170;
    private static int BIG_WIDTH    = 150;

    /**
     *
     * @param card
     */

    public CardView(Card card, boolean big) {
        this.card = card;

        String nick = card.getNick();

        this.container = new Pane();

        Text cardName = new Text();
        ImageView cardImageView = new ImageView();

        VBox verticalCardElements = new VBox();
        BorderPane positionalPane = new BorderPane();

        cardName.setText(nick.toUpperCase());

        if(big) {
            positionalPane.setPrefSize(BIG_WIDTH, BIG_HEIGHT);
            cardImageView.setFitWidth(BIG_WIDTH);
            cardImageView.setFitHeight(BIG_WIDTH);
            verticalCardElements.setPrefSize(BIG_HEIGHT, BIG_WIDTH);
        }
        else {
            positionalPane.setPrefSize(120, 170);
            cardImageView.setFitWidth(SMALL_WIDTH);
            cardImageView.setFitHeight(SMALL_WIDTH);
            verticalCardElements.setPrefSize(SMALL_WIDTH, SMALL_HEIGHT);
        }
        cardImageView.setImage(new Image(this.getImagePath(nick)));

        verticalCardElements.getChildren().addAll(cardImageView, cardName);
        positionalPane.setCenter(verticalCardElements);
        this.container.getChildren().add(positionalPane);
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
