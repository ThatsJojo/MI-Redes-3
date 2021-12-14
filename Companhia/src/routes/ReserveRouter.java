/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import Exceptions.NotPassException;
import com.google.gson.Gson;
import facade.Facade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rota para receber as reservas de passagen requisitadas por outros servidores.
 *
 * @author Cleyton
 */
public class ReserveRouter implements Router {

    private Integer REQLogicCounter;
    private static HashMap<Integer, Integer> mapaRecursos;
    private int waitTimeConcurrentREQ = 50;
    //identifica o último contador lógico utilizado para um dado recurso (chave = recurso, valor = contador).

    public void setREQLogicCounter(Integer REQLogicCounter) {
        this.REQLogicCounter = REQLogicCounter;

    }

    @Override
    public Object[] GET(Object body, HashMap data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();
        HashMap<String, String> entries = gson.fromJson((String) body, HashMap.class);
        int tamanho = Integer.parseInt(entries.get("tamanho"));
        boolean comprar = entries.get("comprar").equals("1");
        ArrayList<Integer> voosID = new ArrayList();
        for (int i = 0; i < tamanho; i++) {
            voosID.set(i, Integer.parseInt(entries.get("voo" + i)));
        }
        Object[] response = {"500", "ERRO!", "N?o foi possível realizar a reserva dos acentos."};
        if (comprar) {
            try {
                comprar(voosID, f, entries.get("nome"));
                Object[] r1 = {"200", "OK", "Compra de passagem para os trechos indicados realizada com sucesso!"};
                response = r1;

            } catch (NotPassException ex) {
                Object[] r1 = {"500", "ERRO!", "N?o foi possível realizar a compra das passagens." + ex.getMessage()};
                response = r1;
            }
        } else if (REQLogicCounter != null) {
            boolean autorizado = autorizarCompra(voosID, f);
            if (autorizado) {
                Object[] r1 = {"200", "OK", "Compra de passagem para os trechos indicados realizada com sucesso!"};
                response = r1;
            } else {
                Object[] r1 = {"500", "ERRO!", "Reserva n?o autorizada."};
                response = r1;
            }

        }
        return response;
    }

    @Override
    public Object[] POST(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] PUT(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] DELETE(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void comprar(ArrayList<Integer> voosID, Facade f, String nome) throws NotPassException {
        for (Integer i : voosID) {
            f.comprarPorID(i, nome);
        }
    }

    private boolean autorizarCompra(ArrayList<Integer> voosID, Facade f) {
        for (Integer i : voosID) {
            attMapaRecursos(i, REQLogicCounter);//Atualiza o mapa de recursos.
            try {
                Thread.sleep(waitTimeConcurrentREQ);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReserveRouter.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (f.isVooCheio(i) || recursoReservado(i, REQLogicCounter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Garante que as requisiç?es que chegaram nos últimos "waitTimeConcurrentREQ" mS
     * Concorram pelo recurso de acordo com seu Contador lógico.
     * @param i
     * @param REQCounter 
     */
    static synchronized private void attMapaRecursos(Integer i, int REQCounter) {
        Integer ultimoContador = mapaRecursos.get(i);
        if (ultimoContador == null || ultimoContador.compareTo(REQCounter) < 0) {
            mapaRecursos.put(i, REQCounter);
        }
    }

    static synchronized private boolean recursoReservado(Integer i, int REQCounter) {
        Integer ultimoContador = mapaRecursos.get(i);
        if (ultimoContador.compareTo(REQCounter) == 0) {
            return true;
        } else {
            return false;
        }
    }

}
