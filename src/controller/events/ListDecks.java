package controller.events;

import model.Mazo;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ListDecks implements ListArtifactsEventAcceptor {

    List<Mazo> decks;


    public ListDecks(List<Mazo> decks) {
        this.decks = decks;
    }

    @Override
    public void accept(ListArtifactsEventVisitor visitor) {
        visitor.visit(this);
    }
}
