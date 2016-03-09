package model;

import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public interface Confrontation {

    public Player getWinnerRound(List<Player> players, List<Player> deadHead, String attrib, Boolean typeConfrontation);
}
