package controller.events;

import model.Player;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ShiftTurn implements GameEventAcceptor {

    private Player currentPlayer;

    public ShiftTurn(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
}
