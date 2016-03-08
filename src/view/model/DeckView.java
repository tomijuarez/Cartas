package view.model;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Deck;

/**
 * Created by Gandalf on 13/2/2016.
 */
public class DeckView extends ViewPackage {
    private Deck deck;

    private Pane getBackground() {
        Pane imageDeckView = new Pane();
        imageDeckView.setPrefSize(150,200);
        imageDeckView.getStyleClass().add("playerDeck");
        return imageDeckView;
    }

    public DeckView(Deck deck) {
        this.deck = deck;
        String name = deck.getName();

        this.container = new Pane();

        Text deckName = new Text();

        VBox verticalDeckElements = new VBox();
        BorderPane positionalPane = new BorderPane();

        deckName.setText(name.toUpperCase());

        verticalDeckElements.setPrefSize(150,200);

        verticalDeckElements.getChildren().addAll(this.getBackground(), deckName);

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
