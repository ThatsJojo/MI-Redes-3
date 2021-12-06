/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routes;

import Exceptions.NotVerticeException;
import Exceptions.NotVooException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Aeroporto;
import view.Server;

/**
 *
 * @author matheusnascimento
 */
public class WayRouter implements Router{

    @Override
    public Object[] GET(Object body, HashMap data_base) {
        Facade f = Facade.getInstance();
        ArrayList<ArrayList<Aeroporto>> list = null;
        Gson gson = new Gson();
        HashMap<String, String> entries = gson.fromJson((String) body, HashMap.class);
        try{
                list = f.getRotas(f.getAeroporto(entries.get("origem")), f.getAeroporto(entries.get("destino")));
                 
        }catch(Exception e){
            
        }
        String result =gson.toJson(list);
        Object[] response = {"200", "OK", result};
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
    
}
