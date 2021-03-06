package controller;

import controller.utils.AlertUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import model.MainDeck;
import view.model.DeckView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Gandalf on 8/2/2016.
 */
public class DeckSelectorController extends MediableController implements Initializable {

    @FXML
    private TilePane container;
    @FXML
    private Button continueButton;

    private List<String> selectedAttributes;

    private List<MainDeck> decks;

    private DeckSelectorMediator mediator;
    private DeckView selectedDeck;
    private List<String> attributes;

    private AlertUtils alertUtils = new AlertUtils();

    public DeckSelectorController(List<MainDeck> decks) {
        this.decks = decks;
    }

    private List<DeckView> getDeckViews() {
        List<DeckView> decksView = new Vector<>();
        for (MainDeck deck: this.decks) {
            decksView.add(new DeckView(deck));
        }
        return decksView;
    }

    private void setDecksEvents(List<DeckView> decksView) {
        for(DeckView view: decksView) {
            view.getResult().setOnMouseClicked((event) -> {
                this.continueButton.setDisable(false);
                if(this.selectedDeck != null)
                    this.selectedDeck.unselect();
                view.select();
                this.selectedDeck = view;
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<DeckView> decksView = this.getDeckViews();
        this.setDecksEvents(decksView);
        this.showDecks(decksView);

        this.continueButton.setOnAction((event)->{
            if (this.selectedDeck != null) {
                this.mediator.setDeck(this.selectedDeck.getDeck());
                this.context.close();
            }
            else
                this.alertUtils.throwUIError("Debes seleccionar al menos un mazo.");
        });
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (DeckSelectorMediator) mediator;
    }

    public void showDecks(List<DeckView> decks) {
        for (int i =0; i < decks.size(); i++) {
            this.container.getChildren().add(decks.get(i).getResult());
        }
    }
}
