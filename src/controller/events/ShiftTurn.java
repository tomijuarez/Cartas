package controller.events;

import model.Jugador;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ShiftTurn implements GameEventAcceptor {

    private Jugador currentPlayer;

    public ShiftTurn(Jugador currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Jugador getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
}
