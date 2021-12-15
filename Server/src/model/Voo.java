package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import util.Aresta;

public class Voo implements Aresta<Aeroporto>{
    private final Aeroporto origem;
    private final Aeroporto destino;
    private int id; 
    private int numeroDePassageiros;
    private int passaagensVendidas;
    private transient final ArrayList<Passageiro> passageiros;
    private transient final ArrayList<Passagem> passagens;
    private final double precoBase;
    private final double tempoBase;
    private final Companhia companhia;
    private boolean cheio;
    private static int count = 0;
    
    private static int getID(){
        return count++;
    }
    
    public Voo(Aeroporto origem, Aeroporto destino, int numeroDePassageiros, double precoBase, Companhia companhia, double tempoBase) {
            this.origem = origem;
            this.companhia = companhia;
            this.destino = destino;
            this.id = Voo.getID();
            this.numeroDePassageiros = numeroDePassageiros;
            this.passageiros = new ArrayList();
            this.passagens = new ArrayList();
            this.precoBase = precoBase;
            this.tempoBase = tempoBase;
            this.passaagensVendidas = 0;
    }

    @Override
    public Aeroporto getOrigem() {
            return origem;
    }

    @Override
    public Aeroporto getDestino() {
            return destino;
    }

    public Companhia getCompanhia() {
        return companhia;
    }
    
    public boolean isCheio(){
        return numeroDePassageiros<=passaagensVendidas;
    }
    public boolean hasOneTicket(){
        return numeroDePassageiros - passaagensVendidas == 1;
    }
    
    public int getId() {
            return id;
    }

    public void setId(int id) {
            this.id = id;
    }

    public int getNumeroDePassageiros() {
            return numeroDePassageiros;
    }

    public void setNumeroDePassageiros(int numeroDePassageiros) {
            this.numeroDePassageiros = numeroDePassageiros;
    }

    public ArrayList<Passageiro> getPassageiros() {
            return passageiros;
    }

    public void cadastrarPassageiro(Passageiro e){
        passageiros.add(e);
        cheio = passageiros.size()==numeroDePassageiros;
    }

    public ArrayList<Passagem> getPassagens() {
            return passagens;
    }

    public void cadastrarPassagem(Passagem p){
        passagens.add( p);
    }

    public double getPrecoBase() {
            return precoBase;
    }

    @Override
    public Double getPeso() {
        return this.tempoBase;
    }

    @Override
    public int compareTo(Object t) {
        if (! (t instanceof Voo))
            return Integer.MIN_VALUE ;
        return this.id - ((Voo)t).id;
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Voo other = (Voo) obj;
        return this.id == other.id;
    }

    public void removerPassgaem(Passagem p, int idViagemAtual) {
        passageiros.remove(p.getPassageiro().getIdPassageiro());
        passagens.remove(p.getId());
    }

    @Override
    public String toString() {
        return "{" + "origem=" + origem + ", destino=" + destino + ", id=" + id + ", nPassageiros=" + numeroDePassageiros + '}';
    }

    @Override
    public boolean able() {
        return !cheio;
    }

    @Override
    public void up() {
        this.passaagensVendidas--;
    }

    @Override
    public void down() {
        this.passaagensVendidas++;
    }
    
}
