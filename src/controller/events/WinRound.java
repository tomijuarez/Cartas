package controller.events;

import model.Jugador;
import model.MazoJugador;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class WinRound implements GameEventAcceptor {
    Jugador winner;

    public WinRound(Jugador winner, MazoJugador accumulator) {
        winner.agregarPosoGanador(accumulator);
        this.winner = winner;
    }

    public Jugador getWinner() {
        return this.winner;
    }

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
}
