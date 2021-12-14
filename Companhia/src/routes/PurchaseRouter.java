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
import view.Server;

/**
 * Rota para receber a solicitaç?o de compra feita pelo cliente em sua
 * interface.
 *
 * @author Cleyton
 */
public class PurchaseRouter implements Router {

    @Override
    public Object[] GET(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void requisitarCompra(ArrayList<Double> voos, boolean comprar, String nome) throws NotPassException {
        Object[] test = {"200", "OK", "Hehehe"};
                
        for (Map.Entry<String, String> set : Server.knownServer.entrySet()) {
            try {
                URL url = new URL(set.getValue() + "/reserve");
                String params = "?comprar="+comprar+"&nome="+ nome +"&tamanho=" + voos.size();
                int vid;
                for (int i = 0; i < voos.size(); i++) {
                    vid = (int) voos.get(i).byteValue();
                    params += "&voo" + vid + "=" + vid;
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Accept", "*/*");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
                connection.addRequestProperty("Content-Length", "" + test.toString().length());
                connection.addRequestProperty("Logic-Counter", "" + RouterController.contadorLamport);
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("client read " + line);
                }
                reader.close();
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
    public Object[] POST(Object body, HashMap data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();

        HashMap<String, Object> param = gson.fromJson((String) body, HashMap.class);
        ArrayList<Double> ids = (ArrayList<Double>) param.get("voos");
        String nome = (String) param.get("nome");
        try {
            requisitarCompra(ids, false, nome);
            requisitarCompra(ids, true, nome);
            f.reservarVoo(ids, (String) param.get("nome"));
        } catch (NotPassException ex) {
            Object[] response = {"500", "ERRO", ex.getMessage()};
            return response;
        }

        Object[] response = {"200", "OK", "Passagem comprada com sucesso."};
        return response;
    }

    @Override
    public Object[] PUT(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] DELETE(Object body, HashMap data_base) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
