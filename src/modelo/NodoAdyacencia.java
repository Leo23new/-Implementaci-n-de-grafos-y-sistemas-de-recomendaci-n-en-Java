package modelo;

public class NodoAdyacencia {
    private final NodoVertice destino;
    private NodoAdyacencia siguiente;

    public NodoAdyacencia(NodoVertice destino) {
        this.destino = destino;
    }

    public NodoVertice getDestino() { return destino; }

    public NodoAdyacencia getSiguiente() { return siguiente; }
    public void setSiguiente(NodoAdyacencia siguiente) { this.siguiente = siguiente; }
}