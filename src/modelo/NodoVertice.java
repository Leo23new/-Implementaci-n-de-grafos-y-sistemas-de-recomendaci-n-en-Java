package modelo;

public class NodoVertice {
    private final int id;
    private String nombre;
    private String especialidad;
    private NodoAdyacencia cabezaAdyacencia; // lista de vecinos de este vértice
    private NodoVertice siguiente;           // siguiente vértice en la lista de vértices
    private boolean visitado;                // marca usada por BFS y DFS

    public NodoVertice(int id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }

    public NodoAdyacencia getCabezaAdyacencia() { return cabezaAdyacencia; }
    public void setCabezaAdyacencia(NodoAdyacencia cabezaAdyacencia) { this.cabezaAdyacencia = cabezaAdyacencia; }

    public NodoVertice getSiguiente() { return siguiente; }
    public void setSiguiente(NodoVertice siguiente) { this.siguiente = siguiente; }

    public boolean isVisitado() { return visitado; }
    public void setVisitado(boolean visitado) { this.visitado = visitado; }

    /** Cantidad de conexiones del vértice (recorre su lista de adyacencia). */
    public int getGrado() {
        int contador = 0;
        NodoAdyacencia actual = cabezaAdyacencia;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + especialidad + ")";
    }
}