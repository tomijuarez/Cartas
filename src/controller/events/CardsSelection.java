package controller.events;

import model.Jugador;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class CardsSelection implements GameEventAcceptor {

    List<Jugador> players;

    public CardsSelection(List<Jugador> players) {
        this.players = players;
    }

    public List<Jugador> getPlayers() {
        return this.players;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }

}
