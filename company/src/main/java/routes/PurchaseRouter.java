/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import Exceptions.NotPassException;
import com.google.gson.Gson;
import controller.RouterController;
import facade.Facade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.RespostaHTTP;
import view.Server;

/**
 * Rota para receber a solicitaç?o de compra feita pelo cliente em sua
 * interface.
 *
 * @author Cleyton
 */
public class PurchaseRouter implements Router {

    @Override
    public Object[] GET(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void requisitarCompra(ArrayList<Double> voos, boolean comprar, String nome) throws NotPassException {
        Object[] test = {"200", "OK", "Hehehe"};
                
        for (Map.Entry<String, String> set : Server.knownServer.entrySet()) {
            try {
                String params = "?comprar="+comprar+"&nome="+ nome +"&tamanho=" + voos.size();
                int vooId;
                for (int i = 0; i < voos.size(); i++) {
                    vooId = (int) voos.get(i).byteValue();
                    params += "&voo" + i + "=" + vooId;
                }
                URL url = new URL(set.getValue() + "/reserve"+params);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "*/*");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
                connection.addRequestProperty("Content-Length", "" + test.toString().length());
                connection.addRequestProperty("Logic-Counter", "" + RouterController.contadorLamport);
                connection.setRequestMethod("GET");
                connection.connect();
                String line;
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new NotPassException();
                }
            } catch (IOException ex) {
                Logger.getLogger(PurchaseRouter.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    

    @Override
    public Object[] POST(Object body, RespostaHTTP data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();

        HashMap<String, Object> param = gson.fromJson((String) body, HashMap.class);
        ArrayList<Double> ids = (ArrayList<Double>) param.get("voos");
        String nome = (String) param.get("nome");
        try {
            requisitarCompra(ids, false, nome);
            requisitarCompra(ids, true, nome);
        } catch (NotPassException ex) {
            Object[] response = {"500", "ERRO", ex.getMessage()};
            return response;
        }

        Object[] response = {"200", "OK", "Passagem comprada com sucesso."};
        return response;
    }

    @Override
    public Object[] PUT(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] DELETE(Object body, RespostaHTTP data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object[] OPTIONS(String queryParams, RespostaHTTP resposta) {
        Object[] response = {"204", "OK", ""};
        return response;
    }

}
