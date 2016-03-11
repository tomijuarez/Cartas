package controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import controller.MediableController;
import controller.Mediator;
import controller.RegisterMediator;
import controller.events.ListArtifactsEventVisitor;
import controller.events.ListCards;
import controller.events.ListDecks;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import model.Character;
import view.Context;
import view.GameWindow;
import view.Modal;
import view.model.DeckView;

import java.net.URL;
import java.util.*;

/**
 * Created by Tomás on 30/12/2015.
 */
public class FirstController extends MediableController implements Initializable, Observer, ListArtifactsEventVisitor {
    @FXML
    private Button initGameButton;
    @FXML
    private Button createCharacterButton;
    @FXML
    private Button createDeckButton;
    @FXML
    private Button createCardButton;
    @FXML
    private Button createLeagueButton;

    private Mediator mediator;

    private Strategy selectedStrategy;
    private List<String> playerNames;
    private List<Boolean> managedManually;
    private MainDeck deck;

    private Game game;

    public FirstController(Game game) {
        game.addObserver(this);
        this.game = game;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGameButton.setOnAction((event)-> {
            Modal subWindow = new Modal("layouts/configurationView.fxml", new ConfigurationController(), new Stage(), this.context);
            this.context.setChild(subWindow, new ConfigurationMediator());
        });

        createDeckButton.setOnAction((event)->{
            Modal deckCreatorWindow = new Modal("layouts/deckCreatorView.fxml", new DeckCreatorController(this.game.getAttributes(), this.game.getCards()), new Stage(), this.context);
            this.context.setChild(deckCreatorWindow, new DeckCreatorMediator());
        });

        createCharacterButton.setOnAction((event)->{
            Modal characterCreatorWindow = new Modal("layouts/characterCreatorView.fxml", new CharacterCreatorController(this.game, this.game.getAttributes()), new Stage(), this.context);
            this.context.setChild(characterCreatorWindow, new CharacterCreatorMediator());
        });

        createCardButton.setOnAction((event)->{
            Modal cardCreatorWindow = new Modal("layouts/cardsCreatorView.fxml", new CardCreatorController(this.game.getCharacters()), new Stage(), this.context);
            this.context.setChild(cardCreatorWindow, new CardCreatorMediator());
        });

        createLeagueButton.setOnAction((event)->{
            Modal leagueCreatorWindow = new Modal("layouts/leagueCreatorView.fxml", new LeagueCreatorController(this.game.getCharacters()), new Stage(), this.context);
            this.context.setChild(leagueCreatorWindow, new LeagueCreatorMediator());
        });

    }

    public void initDeckSelectorUI() {
        Modal malletSelector = new Modal("layouts/deckSelector.fxml", new DeckSelectorController(this.game.getDecks()), new Stage(), this.context);
        this.context.setChild(malletSelector, new DeckSelectorMediator());
    }

    private List<Strategy> createStrategies(GameController controller) {
        List<Strategy> strategies = new Vector<>();
        for(Boolean manual: this.managedManually) {
            Strategy strategy = (manual) ? new ManualStrategy(controller) : this.selectedStrategy;
            strategies.add(strategy);
        }
        return strategies;
    }

    public void initGameUI() {
        GameController gameController = new GameController(this.game);
        this.game.createPlayers(this.playerNames, this.createStrategies(gameController), this.deck);
        Modal subWindow = new Modal("layouts/GameView.fxml", new GameController(this.game), new Stage(), this.context);
        this.context.setChild(subWindow, new GameMediator());
    }

    public void setGameArtifacts(List<String> playerNames, List<Boolean> managedManually, Strategy selectedStrategy) {
        this.playerNames = playerNames;
        this.managedManually = managedManually;
        this.selectedStrategy = selectedStrategy;
        this.initDeckSelectorUI();
    }

    public void setGameDeck(MainDeck deck) {
        this.deck = deck;
        this.initGameUI();
    }

    public void createCard(AbstractCharacter character, List<String> attributes) {
        this.game.createCard(character, attributes);
    }

    public void createCharacter(String characterName, String realName, Map<String, Double> attributes) {
        this.game.createCharacter(characterName, realName, attributes);
    }

    public void createLeague(String leagueName, List<AbstractCharacter> league) {
        this.game.createLeague(leagueName, league);
    }

    public void createDeck(List<Card> cards, String name,Map<String,Boolean> attributes){
        this.game.createDeck(cards,name,attributes);
    }

    @Override
    public void update(Observable object, Object src) {

    }

    @Override
    public void visit(ListDecks event) {

    }

    @Override
    public void visit(ListCards event) {

    }
}