package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import model.Carta;
import model.Mazo;
import view.model.CardView;
import view.model.DeckView;

import javax.smartcardio.Card;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Gandalf on 23/2/2016.
 */
public class DeckCreatorController extends MediableController implements Initializable {

    private DeckCreatorMediator mediator;

    @FXML
    private TextField deckName;
    @FXML
    private TilePane cardsContainer;
    @FXML
    private Button continueButton;

    List<Carta> cards;

    List<CardView> selectedCards = new Vector<>();

    public DeckCreatorController(List<Carta> cards) {
        this.cards = cards;
    }

    private List<CardView> getCardsView() {
        List<CardView> cardsView = new Vector<>();
        for (Carta card: this.cards) {
            cardsView.add(new CardView(card));
        }
        return cardsView;
    }

    private void setCardsEvents(List<CardView> cards) {
        for(CardView card: cards) {
            card.getResult().setOnMouseClicked((event)-> {
                card.select();
                this.selectedCards.add(card);
            });
        }
    }

    private void showCards(List<CardView> cards) {
        for (int i =0; i < cards.size(); i++) {
            System.out.println(cards.get(i));
            this.cardsContainer.getChildren().add(cards.get(i).getResult());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<CardView> cardsView = this.getCardsView();

        this.setCardsEvents(cardsView);
        this.showCards(cardsView);

    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (DeckCreatorMediator) mediator;
    }


}
