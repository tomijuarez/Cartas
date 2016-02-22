package controller.events;

import model.Carta;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ListCards implements ListArtifactsEventAcceptor {
    List<Carta> cards;

    public ListCards(List<Carta> cards) {
        this.cards = cards;
    }

    @Override
    public void accept(ListArtifactsEventVisitor visitor) {
        visitor.visit(this);
    }
}
