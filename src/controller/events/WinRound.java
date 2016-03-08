package controller.events;


import model.DeckPlayer;
import model.Player;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class WinRound implements GameEventAcceptor {
    Player winner;

    public WinRound(Player winner) {
        this.winner = winner;
    }

    public Player getWinner() {
        return this.winner;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
}
