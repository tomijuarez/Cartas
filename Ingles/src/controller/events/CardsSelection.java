package controller.events;

import model.Player;

import java.util.List;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class CardsSelection implements GameEventAcceptor {

    List<Player> players;

    public CardsSelection(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }

}
