package Exceptions;

import model.Voo;

public class NotPassException extends Exception {
    
    private final Voo v;

    public NotPassException(Voo v) {
        this.v = v;
    }
    
    @Override
    public String getMessage(){
        return "N?o há mais acentos no voo selecionado: "+v+". " ;
    }
}
