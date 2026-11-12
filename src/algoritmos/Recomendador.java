package algoritmos;

import estructuras.Grafo;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;
import java.util.ArrayList;

public class Recomendador {

    /**
     * RF08: contactos que tienen en común los usuarios A y B. Null si alguno no
     * existe.
     */
    public static ArrayList<NodoVertice> vecinosComunes(Grafo grafo, int idA, int idB) {
        NodoVertice a = grafo.buscarVertice(idA);
        NodoVertice b = grafo.buscarVertice(idB);
        if (a == null || b == null) {
            return null;
        }
        ArrayList<NodoVertice> comunes = new ArrayList<>();
        NodoAdyacencia ady = a.getCabezaAdyacencia();
        while (ady != null) {
            NodoVertice vecino = ady.getDestino();
            if (grafo.existeArista(vecino.getId(), idB)) {
                comunes.add(vecino);
            }
            ady = ady.getSiguiente();
        }
        return comunes;
    }
}