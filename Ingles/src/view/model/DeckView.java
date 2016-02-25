package view.model;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Deck;
import model.Mazo;

/**
 * Created by Gandalf on 13/2/2016.
 */
public class DeckView extends ViewPackage {

    private final String deckImageName = "deckBackground";

    private Deck deck;

    public DeckView(Deck deck) {
        this.deck = deck;
        String name = deck.getName();

        this.container = new Pane();

        Text deckName = new Text();
        ImageView deckImageView = new ImageView();

        VBox verticalDeckElements = new VBox();
        BorderPane positionalPane = new BorderPane();

        deckImageView.setFitWidth(150);
        deckImageView.setFitHeight(200);
        deckImageView.setImage(new Image(this.getImagePath(this.deckImageName)));

        deckName.setText(name.toUpperCase());

        verticalDeckElements.setPrefSize(150,200);

        verticalDeckElements.getChildren().addAll(deckImageView, deckName);

        this.container.getChildren().add(verticalDeckElements);
    }

    public void select () {
        this.container.setStyle("-fx-border-width: 2px; -fx-border-color: blue;");
    }

    public void unselect() {
        this.container.setStyle("-fx-border-color: transparent;");
    }

    public Deck getDeck() {
        return this.deck;
    }

}