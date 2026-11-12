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

    // Fórmula: puntaje = (contactos comunes x PESO_VECINO_COMUN) +
    // BONUS_ESPECIALIDAD si coincide
    public static final int PESO_VECINO_COMUN = 2;
    public static final int BONUS_ESPECIALIDAD = 1;

    /** Candidato a recomendar con el detalle de su puntaje. */
    public static class Candidato {
        private final NodoVertice vertice;
        private int comunes;
        private final int bonus;

        public Candidato(NodoVertice vertice, int bonus) {
            this.vertice = vertice;
            this.bonus = bonus;
            this.comunes = 0;
        }

        public NodoVertice getVertice() {
            return vertice;
        }

        public int getComunes() {
            return comunes;
        }

        public int getBonus() {
            return bonus;
        }

        public int getPuntaje() {
            return comunes * PESO_VECINO_COMUN + bonus;
        }

        void incrementarComunes() {
            comunes++;
        }
    }

    /**
     * Candidatos del usuario: amigos de sus amigos, sin el propio usuario,
     * sin conexiones existentes y sin duplicados. Null si el usuario no existe.
     */
    public static ArrayList<Candidato> calcularCandidatos(Grafo grafo, int idUsuario) {
        NodoVertice usuario = grafo.buscarVertice(idUsuario);
        if (usuario == null) {
            return null;
        }
        ArrayList<Candidato> candidatos = new ArrayList<>();

        NodoAdyacencia amigo = usuario.getCabezaAdyacencia();
        while (amigo != null) {
            NodoAdyacencia segundo = amigo.getDestino().getCabezaAdyacencia();
            while (segundo != null) {
                NodoVertice posible = segundo.getDestino();
                boolean esElMismo = posible.getId() == idUsuario;
                if (!esElMismo && !grafo.existeArista(idUsuario, posible.getId())) {
                    Candidato c = buscarCandidato(candidatos, posible.getId());
                    if (c == null) {
                        int bonus = usuario.getEspecialidad().equalsIgnoreCase(posible.getEspecialidad())
                                ? BONUS_ESPECIALIDAD
                                : 0;
                        c = new Candidato(posible, bonus);
                        candidatos.add(c);
                    }
                    c.incrementarComunes(); // un contacto común más
                }
                segundo = segundo.getSiguiente();
            }
            amigo = amigo.getSiguiente();
        }
        return candidatos;
    }

    private static Candidato buscarCandidato(ArrayList<Candidato> lista, int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getVertice().getId() == id) {
                return lista.get(i);
            }
        }
        return null;
    }
}