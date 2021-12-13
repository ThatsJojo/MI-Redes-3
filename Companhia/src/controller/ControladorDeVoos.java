
package controller;

import java.util.ArrayList;
import model.Aeroporto;
import model.Passageiro;
import model.Passagem;
import model.Voo;
import util.Aresta;
import util.Grafo;
import Exceptions.NotPassException;
import Exceptions.NotPathException;
import Exceptions.NotVerticeException;
import Exceptions.NotVooException;
import java.util.HashMap;
import java.util.Iterator;
import model.Companhia;
import util.Semaforo;


public class ControladorDeVoos {
    private static ControladorDeVoos controladorDeVoos;
    private final Grafo<Aeroporto> grafo;
    private final HashMap<Integer, Voo> voos;
    
    private ControladorDeVoos() {
        this.grafo = new Grafo();
        voos = new HashMap();
    }
    
    public static synchronized ControladorDeVoos getInstance(){
        if (controladorDeVoos == null)
            controladorDeVoos = new ControladorDeVoos();
        return controladorDeVoos;  
    }
    
   
    public Aeroporto cadastrarAeroporto(String nome) throws NotVerticeException{
        Aeroporto aeroporto = new Aeroporto(nome);
        grafo.addVertice(aeroporto);
        return aeroporto;
    }
    
    public Aeroporto cadastrarAeroporto(String nome, int id) throws NotVerticeException{
        Aeroporto aeroporto = new Aeroporto(nome, id);
        grafo.addVertice(aeroporto);
        return aeroporto;
    }
    
    public Aeroporto getAeroporto(String nome) throws NotVerticeException{
        for (Aeroporto aeroporto : grafo.vertices()) {
            if(aeroporto.getNome().equals(nome)){
                return aeroporto;
            }
        }
        return null;
    }
    
    public ArrayList<Voo> getVoos(Aeroporto a1, Aeroporto a2) throws NotVerticeException, NotVooException{
        ArrayList<Aresta<Aeroporto>> arestas = grafo.arestasEntre(a1, a2);
        ArrayList<Voo> ret = toVoo(arestas);
        if(ret == null)
            throw new NotVooException();
        return ret;
        
    }
    
    private ArrayList<Voo> toVoo(ArrayList<Aresta<Aeroporto>> arestas){
        ArrayList<Voo> ret = new ArrayList();
        if(arestas == null)
            return null;
        for(int i = arestas.size()-1; i>=0; i--){
            ret.add((Voo) arestas.get(i));
        }
        return ret;
    }

    public Voo cadastrarVoo(Aeroporto a1, Aeroporto a2, int numeroDePassageiros, double precoBase, Companhia companhia, double tempoBase) throws NotVerticeException{
        Voo v = new Voo(a1, a2, numeroDePassageiros, precoBase, companhia, tempoBase);
        grafo.addAresta(v);
        voos.put(v.getId(), v);
        Semaforo.getInstance().cadastrarRecurso(v, v.getNumeroDePassageiros());
        return v;
    }

    public ArrayList<Voo> menorCaminho(Aeroporto origem, Aeroporto destino) throws NotPathException{
        return toVoo(grafo.menorCaminho(origem, destino));
    }
   
    public Passagem criarPassagem(Passageiro p, Voo v) throws NotPassException{
        if(!Semaforo.getInstance().down(v)){
            throw new NotPassException(v);
        }
        Passagem m = new Passagem(v, p);
        v.cadastrarPassageiro(p);
        v.cadastrarPassagem(m);
        return m;
    }

    public ArrayList<ArrayList<Aeroporto>> getRotas(Aeroporto origem, Aeroporto destino) throws NotVerticeException {
        return grafo.getRotas(origem,destino);//return grafo.getRotas(Aeroporto origem, Aeroporto destino);
    }

    public void devolverPassagem(Passagem p, int idViagemAtual) {
        p.getVoo().removerPassgaem(p, idViagemAtual);
        Semaforo.getInstance().up(p.getVoo());
    }
    
    public Voo getVoo(int id){
        return voos.get(id);
    }
}
