/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routes;

import com.google.gson.Gson;
import controller.RouterController;
import facade.Facade;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import model.Voo;
import java.util.ArrayList;
import model.Aeroporto;

/**
 *
 * @author matheusnascimento
 */
public class TicketRouter implements Router{

    @Override
    public Object[] GET(Object body, HashMap data_base) {
        Facade f = Facade.getInstance();
        ArrayList<ArrayList<Aeroporto>> list = null;
        try{
           list = f.getRotas(f.getAeroporto("Salvador"), f.getAeroporto("Sao_Luis"));
        }catch(Exception e){
            
        }
        Gson gson = new Gson();
        String result =gson.toJson(list);
        Object[] response = {"200", "OK", result};
        return response;
    }

    @Override
    public Object[] POST(Object body, HashMap data_base) {
        try {
            Object[] test = {"200", "OK", "Hehehe"};
            URL url = new URL("http://127.0.0.1:8000/" + "ticket");
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
            System.out.println("response code=" + responseCode);
            System.out.println("Client exiting");
            return test;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            Object[] test = {"200", "OK", "Hehehe"};
            return test;
        }
    
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
