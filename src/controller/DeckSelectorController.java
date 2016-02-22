package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import model.Mazo;
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

    private Mazo mazo;

    private DeckSelectorMediator mediator;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DeckView deck1 = new DeckView("liga de la justicia");
        DeckView deck2 = new DeckView("liga de la justicia");
        DeckView deck3 = new DeckView("liga de la justicia");
        DeckView deck4 = new DeckView("liga de la justicia");
        DeckView deck5 = new DeckView("liga de la justicia");
        DeckView deck6 = new DeckView("liga de la justicia");
        DeckView deck7 = new DeckView("liga de la justicia");
        DeckView deck8 = new DeckView("liga de la justicia");
        DeckView deck9 = new DeckView("liga de la justicia");
        DeckView deck10 = new DeckView("liga de la justicia");

        List<DeckView> decks = new Vector<>();
        decks.add(deck1);
        decks.add(deck2);
        decks.add(deck3);
        decks.add(deck4);
        decks.add(deck5);
        decks.add(deck6);
        decks.add(deck7);
        decks.add(deck8);
        decks.add(deck9);
        decks.add(deck10);

        this.showMallets(decks);
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (DeckSelectorMediator) mediator;
    }

    public void showMallets(List<DeckView> decks) {
        for (int i =0; i < decks.size(); i++) {
            this.container.getChildren().add(decks.get(i).getResult());
        }
    }
}
