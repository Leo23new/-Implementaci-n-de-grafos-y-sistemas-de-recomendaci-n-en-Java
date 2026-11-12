package algoritmos;
import estructuras.Grafo;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;
import java.util.ArrayList;
public class Recorridos {
 private static void reiniciarMarcas(Grafo grafo) {
        NodoVertice v = grafo.getCabeza();
        while (v != null) {
            v.setVisitado(false);
            v = v.getSiguiente();
        }
    }

    /** RF06: BFS. Devuelve el orden de visita, o null si el vértice no existe. */
    public static ArrayList<NodoVertice> bfs(Grafo grafo, int idInicio) {
        return bfsConNiveles(grafo, idInicio, null);
    }

    /** BFS que además guarda el nivel de cada vértice (mismo orden que el resultado). */
    public static ArrayList<NodoVertice> bfsConNiveles(Grafo grafo, int idInicio, ArrayList<Integer> niveles) {
        NodoVertice inicio = grafo.buscarVertice(idInicio);
        if (inicio == null) {
            return null;
        }
        reiniciarMarcas(grafo);
        ArrayList<NodoVertice> orden = new ArrayList<>();

        // Cola propia con arreglos: cada vértice entra a lo sumo una vez
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
}