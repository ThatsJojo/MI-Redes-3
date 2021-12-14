/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import Exceptions.NotVerticeException;
import com.google.gson.Gson;
import facade.Facade;
import java.util.HashMap;
import util.RespostaHTTP;

/**
 * Rota para cadastrar Voos de outras companhias no servidor atual.
 *
 * @author Cleyton
 */
public class CadastrarVooRouter implements Router {

    @Override
    public Object[] GET(Object body, RespostaHTTP data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();
        HashMap<String, String> entries = gson.fromJson((String) body, HashMap.class);
        try {
            f.cadastrarVoo(f.getAeroporto(entries.get("origem")),
                    f.getAeroporto(entries.get("destino")),
                    Integer.parseInt(entries.get("nPassageiros")),
                    Double.parseDouble(entries.get("precoBase")),
                    f.getCompanhia(Integer.parseInt(entries.get("companhia"))),
                    Double.parseDouble(entries.get("tempoBase")));
            Object[] response = {"200", "OK", "Voo cadastrado com sucesso."};
            return response;
        } catch (NotVerticeException ex) {
            Object[] response = {"500", "ERRO", ex.getMessage()};
            return response;
        }
    }

    @Override
    public Object[] POST(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] PUT(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] DELETE(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
