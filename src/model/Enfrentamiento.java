package model;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Guillermo
 */
public interface Enfrentamiento {
    
    public Jugador obtenerGanadorDuelo(List<Jugador> participantes, List<Jugador> empatados, String atrib);
}
