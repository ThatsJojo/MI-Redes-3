/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import Exceptions.NotPassException;
import com.google.gson.Gson;
import facade.Facade;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.RespostaHTTP;

/**
 * Rota para realizar a compra de passagens previamente reservadas.
 *
 * @author Cleyton
 */
public class EffectiveBuyRouter implements Router {

    @Override
    public Object[] GET(Object body, RespostaHTTP data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();
        HashMap<String, String> entries = gson.fromJson((String) body, HashMap.class);
        try {
            f.comprarPorID(Integer.parseInt(entries.get("vooID")), entries.get("nome"));
            Object[] response = {"200", "OK", "Passagem comprada sucesso."};
            return response;
        } catch (NotPassException ex) {
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
