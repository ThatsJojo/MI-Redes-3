/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routes;

import com.google.gson.Gson;
import facade.Facade;
import java.util.HashMap;
import java.util.ArrayList;
import model.Aeroporto;
import util.RespostaHTTP;

/**
 *
 * @author matheusnascimento
 */
public class WayRouter implements Router{

    @Override
    public Object[] GET(Object body, RespostaHTTP data_base) {
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
