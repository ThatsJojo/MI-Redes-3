/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package routes;

import facade.Facade;
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
        Object[] test = {"200", "OK", list.get(0).toString()};
        return test;
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
