package controller;

import controller.utils.AlertUtils;
import controller.utils.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML
    private ComboBox attributeSelector1;
    @FXML
    private ComboBox attributeSelector2;
    @FXML
    private ComboBox attributeSelector3;
    @FXML
    private ComboBox attributeSelector4;
    @FXML
    private ComboBox attributeSelector5;
    @FXML
    private ToggleButton attributeComparison1;
    @FXML
    private ToggleButton attributeComparison2;
    @FXML
    private ToggleButton attributeComparison3;
    @FXML
    private ToggleButton attributeComparison4;
    @FXML
    private ToggleButton attributeComparison5;

    private List<Card> cards;
    private List<String> attributes;

    private Map<String, CardView> selectedCards = new HashMap<>();

    private List<ComboBox> attributeSelectors = new ArrayList<>();
    private List<ToggleButton> comparisonSelectors = new ArrayList<>();

    private AlertUtils alertUtils = new AlertUtils();
    private TextFieldUtils textFieldUtils = new TextFieldUtils();

    private static int NUM_ATTR  = 5;
    private static int NUM_CARDS = 48;

    List<String> filter = new ArrayList<>();

    public DeckCreatorController(List<String> attributes, List<Card> cards) {
        this.cards = cards;
        this.attributes = attributes;
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
                if (!this.selectedCards.containsKey(card.getCard().getNick())) {
                    card.select();
                    this.selectedCards.put(card.getCard().getNick(), card);
                }
                else {
                    card.unselect();
                    this.selectedCards.remove(card.getCard().getNick());
                }
            });
        }
    }

    private void showCards(List<CardView> cards) {
        for (int i =0; i < cards.size(); i++) {
            this.cardsContainer.getChildren().add(cards.get(i).getResult());
        }
    }

    private Map<String, Boolean> getSelectedAttributes() {
        Map<String, Boolean> selectedAttributes = new HashMap<>();

        for (int i = 0; i < this.attributeSelectors.size(); i++) {
            selectedAttributes.put(
                    (String) this.attributeSelectors.get(i).getSelectionModel().getSelectedItem(),
                    !this.comparisonSelectors.get(i).isPressed()
            );
        }

        return selectedAttributes;
    }


    private void listAttributeSelectors() {
        this.attributeSelectors.add(this.attributeSelector1);
        this.attributeSelectors.add(this.attributeSelector2);
        this.attributeSelectors.add(this.attributeSelector3);
        this.attributeSelectors.add(this.attributeSelector4);
        this.attributeSelectors.add(this.attributeSelector5);
    }

    private void listComparisonSelectors() {
        this.comparisonSelectors.add(this.attributeComparison1);
        this.comparisonSelectors.add(this.attributeComparison2);
        this.comparisonSelectors.add(this.attributeComparison3);
        this.comparisonSelectors.add(this.attributeComparison4);
        this.comparisonSelectors.add(this.attributeComparison5);
    }

    private void showAttributes() {
        this.attributeSelector1.setItems(FXCollections.observableArrayList(this.attributes));
        this.attributeSelector2.setItems(FXCollections.observableArrayList(this.attributes));
        this.attributeSelector3.setItems(FXCollections.observableArrayList(this.attributes));
        this.attributeSelector4.setItems(FXCollections.observableArrayList(this.attributes));
        this.attributeSelector5.setItems(FXCollections.observableArrayList(this.attributes));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<CardView> cardsView = this.getCardsView();

        this.showAttributes();

        this.setCardsEvents(cardsView);
        this.showCards(cardsView);

        this.listAttributeSelectors();
        this.listComparisonSelectors();

        this.setAttributeSelectorEvents(this.attributeSelectors);
        this.setToggleButtonEvents(this.comparisonSelectors);

        continueButton.setOnAction((event)-> {
            if(!this.textFieldUtils.stringIsEmpty(this.deckName))
                if (this.selectedCards.size() == NUM_CARDS )
                    if (this.getSelectedAttributes().size() == NUM_ATTR)
                        this.mediator.createDeck(this.getSelectedCards(), this.deckName.getText(),this.getSelectedAttributes());
                    else
                        this.alertUtils.throwUIError("Deben seleccionarse 5 atributos.");
                else
                    this.alertUtils.throwUIError("Deben seleccionarse exactamente 48 cartas. Actualmente hay " + this.selectedCards.size() + " cartas seleccionadas.");
            else
                this.alertUtils.throwUIError("Debe ingresarse un nombre para el mazo.");
        });

    }

    private boolean attributeExists(String selectedAttribute) {
        for (String attribute: this.attributes)
            if(attribute.equals(selectedAttribute))
                return true;

        return false;
    }

    private void setAttributeSelectorEvents(List<ComboBox> attributeSelectors) {
        for (ComboBox attributeSelector : attributeSelectors) {
            attributeSelector.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
                if( !this.filter.contains((String)newValue) ) {
                    this.filter.add((String) newValue);
                    attributeSelector.setDisable(true);
                }
                else
                    this.alertUtils.throwUIError("No se puede elegir el mismo atributo dos veces.");
            });
        }
    }

    private void setToggleButtonEvents(List<ToggleButton> comparisonSelectors) {
        for (ToggleButton comparisonSelector: comparisonSelectors)
            comparisonSelector.setOnAction((event)-> {
                comparisonSelector.setText("<");
            });
    }

    private List<Card> getSelectedCards() {
        List<Card> selected = new ArrayList<>();
        for (Map.Entry<String, CardView> view: this.selectedCards.entrySet())
            selected.add(view.getValue().getCard());
        return selected;
    }


    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (DeckCreatorMediator) mediator;
    }


}
