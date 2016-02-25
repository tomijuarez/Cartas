package controller.events;

import model.Deck;


import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ListDecks implements ListArtifactsEventAcceptor {

    List<Deck> decks;


    public ListDecks(List<Deck> decks) {
        this.decks = decks;
    }

    @Override
    public void accept(ListArtifactsEventVisitor visitor) {
        visitor.visit(this);
    }
}
