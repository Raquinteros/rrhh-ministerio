package rrhh.datos;

import java.util.ArrayList;
import java.util.List;
import rrhh.modelo.Agente;

public class RepositorioMemoria {

    private static final List<Agente> agentes = new ArrayList<>();

    private RepositorioMemoria() {
    }

    public static void agregarAgente(Agente agente) {
        agentes.add(agente);
    }

    public static List<Agente> obtenerAgentes() {
        return agentes;
    }

    public static Agente buscarAgentePorDni(String dni) {
        for (Agente agente : agentes) {
            if (agente.getDni().equals(dni)) {
                return agente;
            }
        }
        return null;
    }

    public static boolean existeDni(String dni) {
        return buscarAgentePorDni(dni) != null;
    }
}