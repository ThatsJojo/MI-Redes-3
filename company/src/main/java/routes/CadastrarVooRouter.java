/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

import Exceptions.NotPassException;
import Exceptions.NotVerticeException;
import com.google.gson.Gson;
import controller.RouterController;
import facade.Facade;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.RespostaHTTP;
import view.Server;

/**
 * Rota para cadastrar Voos de outras companhias no servidor atual.
 *
 * @author Cleyton
 */
public class CadastrarVooRouter implements Router {
    private String url;
    
    @Override
    public Object[] GET(Object body, RespostaHTTP data_base) {
        Facade f = Facade.getInstance();
        Gson gson = new Gson();
        HashMap<String, String> entries = gson.fromJson((String) body, HashMap.class);
        if(entries.get("resend").equals("true")){
            for (Map.Entry<String, String> set : Server.knownServer.entrySet()) {
                try {
                    String params = "?resend="+"false"+"&origem="+entries.get("origem")+"&destino="+entries.get("destino")
                            +"&nPassageiros="+entries.get("nPassageiros")
                            +"&precoBase="+entries.get("precoBase")
                            +"&companhia="+entries.get("companhia")
                            +"&tempoBase="+entries.get("tempoBase");
                   
                    URL url = new URL(set.getValue() + "/addVoo"+params);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.addRequestProperty("Accept", "*/*");
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
                    connection.addRequestProperty("Content-Length", "" + 0);
                    connection.addRequestProperty("Logic-Counter", "" + RouterController.contadorLamport);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    String line;
                    int responseCode = connection.getResponseCode();
                    if (responseCode != 200) {
                        throw new Exception();
                    }
                }  catch (Exception ex) {
                    Object[] response = {"500", "ERRO", ex.getMessage()};
                    return response;
                } 
            }
            Object[] response = {"200", "OK", "Solicitaçao de cadastrado enviado com sucesso."};
            return response;
        }else{
            try {
                f.cadastrarVoo(f.getAeroporto(entries.get("origem")),
                        f.getAeroporto(entries.get("destino")),
                        Integer.parseInt(entries.get("nPassageiros")),
                        Double.parseDouble(entries.get("precoBase")),
                        f.getCompanhia(Integer.parseInt(entries.get("companhia"))),
                        Double.parseDouble(entries.get("tempoBase")));
                Object[] response = {"200", "OK", "Voo cadastrado com sucesso."};
                return response;
            } 
            catch (NotVerticeException ex) {
                Object[] response = {"500", "ERRO", ex.getMessage()};
                return response;
            }
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
