package app;

import algoritmos.Recomendador;
import algoritmos.Recorridos;
import estructuras.Grafo;
import modelo.NodoVertice;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Grafo grafo = new Grafo();

    public static void main(String[] args) {
                if (leerSiNo("¿Cargar datos de prueba? (s/n): ")) {
            cargarDatosPrueba();
                }
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            ejecutar(opcion);
        } while (opcion != 0);
        System.out.println("Hasta pronto.");
    }
    
    // ---------- Menú ----------

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== RED PROFESIONAL DE EGRESADOS =====");
        System.out.println("1. Registrar usuario o entidad");
        System.out.println("2. Crear conexión");
        System.out.println("3. Eliminar conexión");
        System.out.println("4. Eliminar usuario o entidad");
        System.out.println("5. Visualizar grafo");
        System.out.println("6. Recorrer mediante BFS");
        System.out.println("7. Recorrer mediante DFS");
        System.out.println("8. Mostrar vecinos comunes");
        System.out.println("9. Recomendar conexiones");
        System.out.println("10. Mostrar estadísticas");
        System.out.println("0. Salir");
    }

    private static void ejecutar(int opcion) {
        System.out.println();
        switch (opcion) {
            case 1: registrarUsuario(); break;
            case 2: crearConexion(); break;
            case 3: eliminarConexion(); break;
            case 4: eliminarUsuario(); break;
            case 5: grafo.mostrarGrafo(); break;
            case 6: recorrerBFS(); break;
            case 7: recorrerDFS(); break;
            case 8: mostrarVecinosComunes(); break;
            case 9: recomendarConexiones(); break;
            case 10: mostrarEstadisticas(); break;
            default: break;
        }
    }

    // ---------- Acciones ----------

    private static void registrarUsuario() {
        int id = leerEntero("ID (entero positivo): ");
        String nombre = leerTexto("Nombre: ");
        String especialidad = leerTexto("Especialidad: ");
        if (grafo.registrarVertice(id, nombre, especialidad)) {
            System.out.println("Usuario registrado.");
        } else {
            System.out.println("Error: ya existe un usuario con el ID " + id + ".");
        }
    }

    private static void crearConexion() {
        NodoVertice a = pedirVertice("ID del primer usuario: ");
        if (a == null) return;
        NodoVertice b = pedirVertice("ID del segundo usuario: ");
        if (b == null) return;
        if (a.getId() == b.getId()) {
            System.out.println("Error: un usuario no puede conectarse consigo mismo.");
        } else if (grafo.existeArista(a.getId(), b.getId())) {
            System.out.println("Error: esa conexión ya existe.");
        } else {
            grafo.crearArista(a.getId(), b.getId());
            System.out.println("Conexión creada: " + a.getNombre() + " <-> " + b.getNombre());
        }
    }

    private static void eliminarConexion() {
        NodoVertice a = pedirVertice("ID del primer usuario: ");
        if (a == null) return;
        NodoVertice b = pedirVertice("ID del segundo usuario: ");
        if (b == null) return;
        if (grafo.eliminarArista(a.getId(), b.getId())) {
            System.out.println("Conexión eliminada.");
        } else {
            System.out.println("Error: esos usuarios no están conectados.");
        }
    }

    private static void eliminarUsuario() {
        NodoVertice v = pedirVertice("ID del usuario a eliminar: ");
        if (v == null) return;
        if (leerSiNo("¿Eliminar a " + v.getNombre() + " y todas sus conexiones? (s/n): ")) {
            grafo.eliminarVertice(v.getId());
            System.out.println("Usuario eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void recorrerBFS() {
        NodoVertice inicio = pedirVertice("ID del vértice inicial: ");
        if (inicio == null) return;
        ArrayList<Integer> niveles = new ArrayList<>();
        ArrayList<NodoVertice> orden = Recorridos.bfsConNiveles(grafo, inicio.getId(), niveles);
        System.out.println("BFS desde " + inicio.getNombre() + " (vecinos en orden ascendente de ID):");
        int nivelActual = -1;
        for (int i = 0; i < orden.size(); i++) {
            if (niveles.get(i) != nivelActual) {
                nivelActual = niveles.get(i);
                System.out.print("\nNivel " + nivelActual + ": ");
            }
            System.out.print(orden.get(i).getNombre() + "(" + orden.get(i).getId() + ") ");
        }
        System.out.println();
        System.out.println("Vértices alcanzados: " + orden.size() + " de " + grafo.getCantidadVertices());
    }

    private static void recorrerDFS() {
        NodoVertice inicio = pedirVertice("ID del vértice inicial: ");
        if (inicio == null) return;
        ArrayList<NodoVertice> orden = Recorridos.dfs(grafo, inicio.getId());
        System.out.println("DFS desde " + inicio.getNombre() + " (vecinos en orden ascendente de ID):");
        for (int i = 0; i < orden.size(); i++) {
            System.out.print(orden.get(i).getNombre() + "(" + orden.get(i).getId() + ")");
            if (i < orden.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Vértices alcanzados: " + orden.size() + " de " + grafo.getCantidadVertices());
    }

    private static void mostrarVecinosComunes() {
        NodoVertice a = pedirVertice("ID del primer usuario: ");
        if (a == null) return;
        NodoVertice b = pedirVertice("ID del segundo usuario: ");
        if (b == null) return;
        if (a.getId() == b.getId()) {
            System.out.println("Error: seleccione dos usuarios distintos.");
            return;
        }
        ArrayList<NodoVertice> comunes = Recomendador.vecinosComunes(grafo, a.getId(), b.getId());
        System.out.println("Vecinos comunes entre " + a.getNombre() + " y " + b.getNombre() + ": " + comunes.size());
        for (int i = 0; i < comunes.size(); i++) {
            System.out.println(" - " + comunes.get(i));
        }
    }

    private static void recomendarConexiones() {
        NodoVertice u = pedirVertice("ID del usuario: ");
        if (u == null) return;
        ArrayList<Recomendador.Candidato> top = Recomendador.recomendar(grafo, u.getId(), 3);
        if (top.isEmpty()) {
            System.out.println("No hay candidatos con contactos en común para " + u.getNombre() + ".");
            return;
        }
        System.out.println("Recomendaciones para " + u.getNombre() + ":");
        for (int i = 0; i < top.size(); i++) {
            Recomendador.Candidato c = top.get(i);
            NodoVertice cv = c.getVertice();
            System.out.println((i + 1) + ". " + cv.getNombre() + " (ID " + cv.getId() + ", " + cv.getEspecialidad() + ")");
            System.out.println("   Puntaje: " + c.getPuntaje() + " = " + c.getComunes() + " comunes x "
                    + Recomendador.PESO_VECINO_COMUN + " + " + c.getBonus() + " por especialidad");
            ArrayList<NodoVertice> comunes = Recomendador.vecinosComunes(grafo, u.getId(), cv.getId());
            System.out.print("   Contactos en común: ");
            for (int j = 0; j < comunes.size(); j++) {
                System.out.print(comunes.get(j).getNombre());
                if (j < comunes.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }

    private static void mostrarEstadisticas() {
        grafo.mostrarEstadisticas();
        System.out.println("Integridad de la estructura: " + (grafo.validarIntegridad() ? "CORRECTA" : "CON ERRORES"));
    }

    // ---------- Lectura validada ----------

    private static NodoVertice pedirVertice(String mensaje) {
        int id = leerEntero(mensaje);
        NodoVertice v = grafo.buscarVertice(id);
        if (v == null) {
            System.out.println("Error: no existe un usuario con ID " + id + ".");
        }
        return v;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor > 0) {
                    return valor;
                }
                System.out.println("Error: ingrese un entero positivo.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número entero válido.");
            }
        }
    }

    private static int leerOpcion() {
        while (true) {
            System.out.print("Seleccione una opción (0-10): ");
            String linea = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= 0 && valor <= 10) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // se repite la pregunta
            }
            System.out.println("Error: opción inválida.");
        }
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (texto.matches("[\\p{L}][\\p{L} .'-]*") && texto.length() <= 40) {
                return texto;
            }
            System.out.println("Error: use solo letras y espacios (máx. 40 caracteres).");
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String r = sc.nextLine().trim();
            if (r.equalsIgnoreCase("s")) return true;
            if (r.equalsIgnoreCase("n")) return false;
            System.out.println("Error: responda 's' o 'n'.");
        }
    }
        // ---------- Datos de prueba: 14 egresados y 18 conexiones ----------

    private static void cargarDatosPrueba() {
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        String[] nombres = {"José Ortiz", "Ana Torres", "María Paredes", "Pedro Salazar", "Carla Vega",
                "Diego Ramos", "Elena Cruz", "Jorge León", "Sofía Núñez", "Marco Silva",
                "Paula Ortiz", "Raúl Andrade", "Isabel Mena", "Lucía Paz"};
        String[] especialidades = {"Software", "Software", "Datos", "Software", "Redes",
                "Datos", "Software", "Seguridad", "Datos", "Redes",
                "Seguridad", "Software", "Redes", "Datos"};
        for (int i = 0; i < ids.length; i++) {
            grafo.registrarVertice(ids[i], nombres[i], especialidades[i]);
        }
        // Componente principal (ids 1-10), componente aparte (11, 12, 14) y un aislado (13)
        int[][] aristas = {
                {1, 2}, {1, 3}, {2, 4}, {3, 4}, {2, 5}, {3, 6}, {5, 6}, {2, 7}, {3, 7},
                {4, 8}, {5, 8}, {6, 9}, {7, 9}, {8, 9}, {5, 10},
                {11, 12}, {12, 14}, {11, 14}
        };
        for (int[] a : aristas) {
            grafo.crearArista(a[0], a[1]);
        }
        System.out.println("Datos cargados: " + grafo.getCantidadVertices() + " usuarios y "
                + grafo.getCantidadAristas() + " conexiones.");
    }
}