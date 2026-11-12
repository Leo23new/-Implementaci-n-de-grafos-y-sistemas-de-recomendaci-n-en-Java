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
    /** RF03: elimina la relación en ambos extremos. */
    public boolean eliminarArista(int idA, int idB) {
        NodoVertice a = buscarVertice(idA);
        NodoVertice b = buscarVertice(idB);
        if (a == null || b == null) {
            return false;
        }
        if (!tieneAdyacente(a, idB)) {
            return false;
        }
        quitarAdyacencia(a, idB);
        quitarAdyacencia(b, idA);
        cantidadAristas--;
        return true;
    }

    /** Quita de la lista de 'origen' el nodo que apunta a idDestino. */
    private boolean quitarAdyacencia(NodoVertice origen, int idDestino) {
        NodoAdyacencia actual = origen.getCabezaAdyacencia();
        NodoAdyacencia anterior = null;
        while (actual != null) {
            if (actual.getDestino().getId() == idDestino) {
                if (anterior == null) {
                    origen.setCabezaAdyacencia(actual.getSiguiente());
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }
    
    /** RF04: elimina el vértice y todas sus conexiones. */
    public boolean eliminarVertice(int id) {
        NodoVertice v = buscarVertice(id);
        if (v == null) {
            return false;
        }
        // 1) Quitar todas las aristas que lo tocan (en ambos extremos)
        while (v.getCabezaAdyacencia() != null) {
            int idVecino = v.getCabezaAdyacencia().getDestino().getId();
            if (!eliminarArista(id, idVecino)) {
                quitarAdyacencia(v, idVecino); 
            }
        }
        // 2) Quitarlo de la lista de vértices
        if (cabeza == v) {
            cabeza = v.getSiguiente();
        } else {
            NodoVertice actual = cabeza;
            while (actual.getSiguiente() != v) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(v.getSiguiente());
        }
        cantidadVertices--;
        return true;
    }
        // ---------- Eliminación ----------

    /** RF03: elimina la relación en ambos extremos. */
    public boolean eliminarArista(int idA, int idB) {
        NodoVertice a = buscarVertice(idA);
        NodoVertice b = buscarVertice(idB);
        if (a == null || b == null) {
            return false;
        }
        if (!tieneAdyacente(a, idB)) {
            return false;
        }
        quitarAdyacencia(a, idB);
        quitarAdyacencia(b, idA);
        cantidadAristas--;
        return true;
    }

    /** Quita de la lista de 'origen' el nodo que apunta a idDestino. */
    private boolean quitarAdyacencia(NodoVertice origen, int idDestino) {
        NodoAdyacencia actual = origen.getCabezaAdyacencia();
        NodoAdyacencia anterior = null;
        while (actual != null) {
            if (actual.getDestino().getId() == idDestino) {
                if (anterior == null) {
                    origen.setCabezaAdyacencia(actual.getSiguiente());
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }
        /** RF04: elimina el vértice y todas sus conexiones. */
    public boolean eliminarVertice(int id) {
        NodoVertice v = buscarVertice(id);
        if (v == null) {
            return false;
        }
        // 1) Quitar todas las aristas que lo tocan (en ambos extremos)
        while (v.getCabezaAdyacencia() != null) {
            int idVecino = v.getCabezaAdyacencia().getDestino().getId();
            if (!eliminarArista(id, idVecino)) {
                quitarAdyacencia(v, idVecino); // seguridad ante estructura inconsistente
            }
        }
        // 2) Quitarlo de la lista de vértices
        if (cabeza == v) {
            cabeza = v.getSiguiente();
        } else {
            NodoVertice actual = cabeza;
            while (actual.getSiguiente() != v) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(v.getSiguiente());
        }
        cantidadVertices--;
        return true;
    }
        // ---------- Integridad y estadísticas ----------

    /** RF10: verifica que no haya duplicados, bucles, referencias inválidas ni asimetrías. */
    public boolean validarIntegridad() {
        boolean ok = true;
        int contadorVertices = 0;
        int sumaGrados = 0;

        NodoVertice v = cabeza;
        while (v != null) {
            contadorVertices++;
            if (v.getSiguiente() != null && v.getSiguiente().getId() <= v.getId()) {
                System.out.println("ERROR: ID duplicado o desordenado en el vértice " + v.getId());
                ok = false;
            }
            NodoAdyacencia ady = v.getCabezaAdyacencia();
            while (ady != null) {
                sumaGrados++;
                NodoVertice d = ady.getDestino();
                if (d == null || buscarVertice(d.getId()) != d) {
                    System.out.println("ERROR: referencia inválida en el vértice " + v.getId());
                    ok = false;
                } else {
                    if (d.getId() == v.getId()) {
                        System.out.println("ERROR: autoconexión en el vértice " + v.getId());
                        ok = false;
                    }
                    if (!tieneAdyacente(d, v.getId())) {
                        System.out.println("ERROR: conexión no bidireccional " + v.getId() + " -> " + d.getId());
                        ok = false;
                    }
                    NodoAdyacencia sig = ady.getSiguiente();
                    if (sig != null && sig.getDestino() != null && sig.getDestino().getId() <= d.getId()) {
                        System.out.println("ERROR: conexión duplicada o desordenada en el vértice " + v.getId());
                        ok = false;
                    }
                }
                ady = ady.getSiguiente();
            }
            v = v.getSiguiente();
        }
        if (contadorVertices != cantidadVertices) {
            System.out.println("ERROR: el contador de vértices no coincide con la lista.");
            ok = false;
        }
        if (sumaGrados != 2 * cantidadAristas) {
            System.out.println("ERROR: el contador de aristas no coincide con las listas.");
            ok = false;
        }
        return ok;
    }

    public void mostrarEstadisticas() {
        System.out.println("Vértices: " + cantidadVertices);
        System.out.println("Aristas: " + cantidadAristas);
        if (cantidadVertices == 0) {
            return;
        }
        int sumaGrados = 0;
        int aislados = 0;
        int maxGrado = -1;
        NodoVertice masConectado = null;
        NodoVertice v = cabeza;
        while (v != null) {
            int grado = v.getGrado();
            sumaGrados += grado;
            if (grado == 0) {
                aislados++;
            }
            if (grado > maxGrado) {
                maxGrado = grado;
                masConectado = v;
            }
            v = v.getSiguiente();
        }
        System.out.println("Grado promedio: " + String.format("%.2f", (double) sumaGrados / cantidadVertices));
        System.out.println("Más conectado: " + masConectado.getNombre() + " (" + maxGrado + " conexiones)");
        System.out.println("Usuarios aislados: " + aislados);
    }
}
