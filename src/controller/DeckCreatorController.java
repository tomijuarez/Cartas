package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import model.Card;
import model.Deck;
import view.model.CardView;
import view.model.DeckView;

import java.net.URL;
import java.util.*;

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


    List<Card> cards;
    Map<String,Boolean> attributes;

    List<CardView> selectedCards = new Vector<>();

    public DeckCreatorController(List<Card> cards) {
        this.cards = cards;
        this.attributes = new Hashtable<>();
    }

    private List<CardView> getCardsView() {
        List<CardView> cardsView = new Vector<>();
        for (Card card: this.cards) {
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

        continueButton.setOnAction((event)-> {
           this.mediator.createDeck(this.cards,this.deckName.getText(),this.attributes);
        });

    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (DeckCreatorMediator) mediator;
    }


}
