package Exceptions;

import model.Voo;

public class NotPassException extends Exception {
    
    private Voo v;

    public NotPassException(Voo v) {
        this.v = v;
    }
    
    public NotPassException(){
    }
    
    @Override
    public String getMessage(){
        return "N?o h� mais acentos no voo selecionado: "+(v==null?"":v)+". " ;
    }
}
