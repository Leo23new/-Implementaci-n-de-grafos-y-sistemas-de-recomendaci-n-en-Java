package algoritmos;
import estructuras.Grafo;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;
import java.util.ArrayList;


/**
 * Recorridos del grafo (BFS y DFS).
 *
 * Orden de exploracion documentado:
 *  - Los vecinos de cada vertice se guardan ordenados por ID ascendente,
 *    por lo tanto siempre se exploran del ID menor al mayor
 *  - BFS: usa una cola visita por niveles primero el inicio, luego sus vecinos
 *    directos, luego los vecinos de estos y asi sucesivamente
 *  - DFS: usa una pila: avanza por el primer vecino no visitado hasta no poder
 *    seguir y entonces retrocede (backtracking)
 *  - Solo se visitan los vertices alcanzables desde el inicio: en un grafo
 *    desconectado los demas componentes no aparecen
 *  - Complejidad de ambos: O(V + E).
 */


public class Recorridos {
 private static void reiniciarMarcas(Grafo grafo) {
        NodoVertice v = grafo.getCabeza();
        while (v != null) {
            v.setVisitado(false);
            v = v.getSiguiente();
        }
    }
    /** BFS Devuelve el orden de visita, o null si el vertice no existe */

    
    public static ArrayList<NodoVertice> bfs(Grafo grafo, int idInicio) {
        return bfsConNiveles(grafo, idInicio, null);
    }

/** BFS que guarda el nivel de cada vertice mismo orden que el resultado */
    public static ArrayList<NodoVertice> bfsConNiveles(Grafo grafo, int idInicio, ArrayList<Integer> niveles) {
        NodoVertice inicio = grafo.buscarVertice(idInicio);
        if (inicio == null) {
            return null;
        }
        reiniciarMarcas(grafo);
        ArrayList<NodoVertice> orden = new ArrayList<>();

        NodoVertice[] cola = new NodoVertice[grafo.getCantidadVertices()];
        int[] nivelCola = new int[grafo.getCantidadVertices()];
        int frente = 0;
        int fin = 0;

        inicio.setVisitado(true);
        cola[fin] = inicio;
        nivelCola[fin] = 0;
        fin++;

        while (frente < fin) {
            NodoVertice actual = cola[frente];
            int nivel = nivelCola[frente];
            frente++;

            orden.add(actual);
            if (niveles != null) {
                niveles.add(nivel);
            }

            NodoAdyacencia ady = actual.getCabezaAdyacencia();
            while (ady != null) {
                NodoVertice vecino = ady.getDestino();
                if (!vecino.isVisitado()) {
                    vecino.setVisitado(true);
                    cola[fin] = vecino;
                    nivelCola[fin] = nivel + 1;
                    fin++;
                }
                ady = ady.getSiguiente();
            }
        }
        return orden;
    }

        /** DFS con pila propia. Devuelve el orden de visita, o null si no existe el vertice */
    public static ArrayList<NodoVertice> dfs(Grafo grafo, int idInicio) {
        NodoVertice inicio = grafo.buscarVertice(idInicio);
        if (inicio == null) {
            return null;
        }
        reiniciarMarcas(grafo);
        ArrayList<NodoVertice> orden = new ArrayList<>();

        // Pila de vertices y en paralelo, el siguiente vecino por explorar de cada uno
        NodoVertice[] pila = new NodoVertice[grafo.getCantidadVertices()];
        NodoAdyacencia[] siguienteVecino = new NodoAdyacencia[grafo.getCantidadVertices()];
        int tope = -1;

        inicio.setVisitado(true);
        orden.add(inicio);
        tope++;
        pila[tope] = inicio;
        siguienteVecino[tope] = inicio.getCabezaAdyacencia();

        while (tope >= 0) {
            NodoAdyacencia ady = siguienteVecino[tope];
            if (ady == null) {
                tope--; // ya no hay mas vecinos: retroceder
            } else {
                siguienteVecino[tope] = ady.getSiguiente();
                NodoVertice vecino = ady.getDestino();
                if (!vecino.isVisitado()) {
                    vecino.setVisitado(true);
                    orden.add(vecino);
                    tope++;
                    pila[tope] = vecino;
                    siguienteVecino[tope] = vecino.getCabezaAdyacencia();
                }
            }
        }
        return orden;
    }
}