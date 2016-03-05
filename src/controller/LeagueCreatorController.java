package controller;

import controller.utils.AlertUtils;
import controller.utils.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.AbstractCharacter;
import model.Card;
import model.Character;
import view.model.CardView;
import view.model.CharacterView;
import view.model.ViewPackage;

import java.io.File;
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
    @FXML
    private Button imageSelectorButton;
    @FXML
    private ImageView leagueImageView;

    private TextFieldUtils textFieldUtils = new TextFieldUtils();
    private AlertUtils alertUtils = new AlertUtils();

    private List<Character> characters;
    private Map<String, CharacterView> selectedCharacters = new HashMap<>();

    private ViewPackage imageManager = new ViewPackage();

    private File selectedImage;

    public LeagueCreatorController(List<Character> characters) {
        this.characters = characters;
    }

    private List<CharacterView> getCharactersView() {
        List<CharacterView> characterView = new Vector<>();
        for (Character character: this.characters) {
            characterView.add(new CharacterView(character));
        }
        return characterView;
    }

    private List<Character> getSelectedCharacters() {
        List<Character> characters = new Vector<>();
        for (Map.Entry<String, CharacterView> character: this.selectedCharacters.entrySet()) {
            characters.add(character.getValue().getCharacter());
        }
        return characters;
    }

    private void setCharactersEvents(List<CharacterView> characters) {
        for(CharacterView character: characters) {
            character.getResult().setOnMouseClicked((event)-> {
                if (!this.selectedCharacters.containsKey(character.getCharacter().getFictitiousName())) {
                    character.select();
                    this.selectedCharacters.put(character.getCharacter().getFictitiousName(), character);
                }
                else {
                    character.unselect();
                    this.selectedCharacters.remove(character.getCharacter().getFictitiousName());
                }
            });
        }
    }

    private void showCharacters(List<CharacterView> characters) {
        for (int i =0; i < characters.size(); i++) {
            this.charactersContainer.getChildren().add(characters.get(i).getResult());
        }
    }

    private void showImage(File selectedFile) {
        if(selectedFile != null) {
            Image image = new Image(this.imageManager.getImageAbsolutePath(selectedFile.getPath()));
            this.leagueImageView.setImage(image);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<CharacterView> charactersView = this.getCharactersView();

        this.textFieldUtils.setTextFieldEvent(this.leagueName);

        this.setCharactersEvents(charactersView);
        this.showCharacters(charactersView);


        this.imageSelectorButton.setOnAction((event)->{
            FileChooser imageSelector = new FileChooser();
            imageSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images","*.png"));
            imageSelector.setTitle("Elegir foto de la liga.");
            this.selectedImage = imageSelector.showOpenDialog(this.context.getStage());
            this.showImage(this.selectedImage);
        });

        continueButton.setOnAction((event)-> {
            if ( ! this.textFieldUtils.stringIsEmpty(this.leagueName))
                if (this.selectedCharacters.size() >= 2 )
                    if (selectedImage != null) {
                        this.imageManager.copyImage(this.selectedImage, this.leagueName.getText());
                        this.mediator.setSelectedCharacters(this.leagueName.getText(), this.getSelectedCharacters());
                        this.context.close();
                    }
                    else
                        this.alertUtils.throwUIError("Debes elegir una foto para la liga");
                else
                    this.alertUtils.throwUIError("No es posible seleccionar menos de dos personajes para las ligas.");
            else
                this.alertUtils.throwUIError("El nombre de la liga no puede ser vacío.");
        });

    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (LeagueCreatorMediator) mediator;
    }


}
