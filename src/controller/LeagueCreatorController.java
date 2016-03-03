package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import model.Card;
import model.Character;
import view.model.CardView;
import view.model.CharacterView;

import java.net.URL;
import java.util.*;

/**
 * Created by Gandalf on 3/3/2016.
 */
public class LeagueCreatorController extends MediableController implements Initializable {

    private LeagueCreatorMediator mediator;

    @FXML
    private TextField leagueName;
    @FXML
    private TilePane charactersContainer;
    @FXML
    private Button continueButton;


    List<Character> characters;
    List<CharacterView> selectedCharacters = new Vector<>();

    public LeagueCreatorController(List<Character> characters) {
        this.characters = characters;
    }

    private List<CharacterView> getCharactersView() {
        List<CharacterView> cardsView = new Vector<>();
        for (Character character: this.characters) {
            cardsView.add(new CharacterView(character));
        }
        return cardsView;
    }

    private void setCharactersEvents(List<CharacterView> characters) {
        for(CharacterView character: characters) {
            character.getResult().setOnMouseClicked((event)-> {
                character.select();
                this.selectedCharacters.add(character);
            });
        }
    }

    private void showCharacters(List<CharacterView> characters) {
        for (int i =0; i < characters.size(); i++) {
            System.out.println(characters.get(i));
            this.charactersContainer.getChildren().add(characters.get(i).getResult());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<CharacterView> charactersView = this.getCharactersView();

        this.setCharactersEvents(charactersView);
        this.showCharacters(charactersView);

        continueButton.setOnAction((event)-> {
            //this.mediator.c(this.cards,this.deckName.getText(),this.attributes);
        });

    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (LeagueCreatorMediator) mediator;
    }


}
