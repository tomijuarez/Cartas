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
public class EnfrentamientoHeroes implements Enfrentamiento{

    @Override
    public Jugador obtenerGanadorDuelo(List<Jugador> participantes, List<Jugador> empatados, String atrib){
       Jugador mayor = participantes.remove(0);
       for(Jugador j : participantes){
           if(mayor.obtenerAtributo(atrib) > j.obtenerAtributo(atrib)){
               mayor = j;
           }
        }
       //verifico si hay empate en la partida
       empatados.add(mayor);
       for(Jugador j : participantes){
           if(mayor.obtenerAtributo(atrib) == j.obtenerAtributo(atrib)){
               empatados.add(j);
           }
        }
       if(empatados.size() == 1){
           return mayor;
       }
       else{
           return null;
       }
       
       
    }

   
    
}
