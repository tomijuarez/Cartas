package view.model;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
/**
 * Created by Gandalf on 13/2/2016.
 */
public class DeckView extends ViewPackage {

    private final String deckImageName = "deckBackground.png";

    public DeckView(String name) {
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

        Pane test = new Pane();
        test.setPrefSize(250,200);
        test.setStyle("-fx-background-color: blue;");
        verticalDeckElements.getChildren().addAll(deckImageView, deckName);

        this.container.getChildren().add(verticalDeckElements);
    }

}
