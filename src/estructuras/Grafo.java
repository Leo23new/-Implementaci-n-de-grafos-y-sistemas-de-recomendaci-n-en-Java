package estructuras;

import modelo.NodoAdyacencia;
import modelo.NodoVertice;

public class Grafo {
    private NodoVertice cabeza;      // lista enlazada de vértices, ordenada por id
    private int cantidadVertices;
    private int cantidadAristas;

    public Grafo() {
        this.cabeza = null;
        this.cantidadVertices = 0;
        this.cantidadAristas = 0;
    }

    public NodoVertice getCabeza() { return cabeza; }
    public int getCantidadVertices() { return cantidadVertices; }
    public int getCantidadAristas() { return cantidadAristas; }

    // ---------- Vértices ----------

    /** RF01: registra un vértice sin duplicados. Inserta ordenado por id. */
    public boolean registrarVertice(int id, String nombre, String especialidad) {
        if (id <= 0 || nombre == null || nombre.trim().isEmpty()
                || especialidad == null || especialidad.trim().isEmpty()) {
            return false;
        }
        if (buscarVertice(id) != null) {
            return false; // ya existe
        }
        NodoVertice nuevo = new NodoVertice(id, nombre.trim(), especialidad.trim());
        if (cabeza == null || id < cabeza.getId()) {
            nuevo.setSiguiente(cabeza);
            cabeza = nuevo;
        } else {
            NodoVertice actual = cabeza;
            while (actual.getSiguiente() != null && actual.getSiguiente().getId() < id) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        cantidadVertices++;
        return true;
    }

    /** Busca un vértice por id. Devuelve null si no existe. */
    public NodoVertice buscarVertice(int id) {
        NodoVertice actual = cabeza;
        while (actual != null && actual.getId() <= id) {
            if (actual.getId() == id) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // ---------- Aristas ----------

    public boolean existeArista(int idA, int idB) {
        NodoVertice a = buscarVertice(idA);
        if (a == null || buscarVertice(idB) == null) {
            return false;
        }
        return tieneAdyacente(a, idB);
    }

    /** RF02: crea una relación bidireccional válida (sin bucles ni duplicados). */
    public boolean crearArista(int idA, int idB) {
        if (idA == idB) {
            return false;
        }
        NodoVertice a = buscarVertice(idA);
        NodoVertice b = buscarVertice(idB);
        if (a == null || b == null) {
            return false;
        }
        if (tieneAdyacente(a, idB)) {
            return false;
        }
        insertarAdyacencia(a, b);
        insertarAdyacencia(b, a);
        cantidadAristas++;
        return true;
    }

    private boolean tieneAdyacente(NodoVertice origen, int idDestino) {
        NodoAdyacencia actual = origen.getCabezaAdyacencia();
        while (actual != null) {
            if (actual.getDestino().getId() == idDestino) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /** Inserta 'destino' en la lista de 'origen', manteniendo orden ascendente por id. */
    private void insertarAdyacencia(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia nuevo = new NodoAdyacencia(destino);
        NodoAdyacencia cab = origen.getCabezaAdyacencia();
        if (cab == null || destino.getId() < cab.getDestino().getId()) {
            nuevo.setSiguiente(cab);
            origen.setCabezaAdyacencia(nuevo);
        } else {
            NodoAdyacencia actual = cab;
            while (actual.getSiguiente() != null
                    && actual.getSiguiente().getDestino().getId() < destino.getId()) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
    }
        // ---------- Visualización ----------

    /** RF05: muestra las listas de adyacencia. */
    public void mostrarGrafo() {
        if (cabeza == null) {
            System.out.println("El grafo está vacío.");
            return;
        }
        NodoVertice v = cabeza;
        while (v != null) {
            System.out.print(v.getId() + ":" + v.getNombre() + " (" + v.getEspecialidad() + ") -> ");
            NodoAdyacencia ady = v.getCabezaAdyacencia();
            if (ady == null) {
                System.out.print("(sin conexiones)");
            }
            while (ady != null) {
                NodoVertice d = ady.getDestino();
                System.out.print(d.getId() + ":" + d.getNombre());
                if (ady.getSiguiente() != null) {
                    System.out.print(" | ");
                }
                ady = ady.getSiguiente();
            }
            System.out.println();
            v = v.getSiguiente();
        }
    }
}
