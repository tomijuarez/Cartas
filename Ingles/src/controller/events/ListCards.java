package controller.events;

import model.Card;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ListCards implements ListArtifactsEventAcceptor {
    List<Card> cards;

    public ListCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void accept(ListArtifactsEventVisitor visitor) {
        visitor.visit(this);
    }
}
