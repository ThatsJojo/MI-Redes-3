package util;

import Exceptions.NotVerticeException;
import Exceptions.NotPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Grafo<V> {

    private final ArrayList<V> vertices;
    private final ArrayList<Aresta<V>> arestas;
    private final HashMap<V, ArrayList<Aresta<V>>> bucketArestas;
    private final HashMap<V, ArrayList<Aresta<V>>> bucketArestasUnique;

    public Grafo() {
        vertices = new ArrayList();
        arestas = new ArrayList();
        bucketArestas = new HashMap();
        bucketArestasUnique = new HashMap();
    }

    public void addVertice(V vertice) throws NotVerticeException {
        if (vertice == null) {
            throw new NotVerticeException();
        }
        if (vertices.contains(vertice)) {
            return;
        }
        vertices.add(vertice);
        bucketArestas.put(vertice, new ArrayList());
        bucketArestasUnique.put(vertice, new ArrayList());
    }

    public boolean containsVertice(V vertice) {
        return vertices.contains(vertice);
    }

    public ArrayList<V> vertices() {
        ArrayList<V> ret = new ArrayList();
        vertices.forEach((v) -> {
            ret.add(v);
        });
        return ret.isEmpty() ? null : ret;
    }

    //Metodo para adicionar arrestas na lista de arestas
    public void addAresta(Aresta<V> aresta) throws NotVerticeException {
        V origem = aresta.getOrigem();
        V destino = aresta.getDestino();
        int indexOrigem = vertices.indexOf(origem);
        int indexDestino = vertices.indexOf(destino);
        if (indexOrigem < 0 || indexDestino < 0) {
            throw new NotVerticeException();
        }
        arestas.add(aresta);
        bucketArestas.get(origem).add(aresta);
        ArrayList<Aresta<V>> bcUniqueEntry = bucketArestasUnique.get(origem);
        for(Aresta a: bcUniqueEntry)
            if(a.getDestino().equals(aresta.getDestino()))
                return;
        bcUniqueEntry.add(aresta);
    }

    //Metodo que ir?? retornar uma lista de adjacencias apartir de uma origem
    public ArrayList<V> adjacentesDe(V origem) throws NotVerticeException {
        int indexOrigem = vertices.indexOf(origem);
        if (indexOrigem < 0) {
            throw new NotVerticeException();
        }
        ArrayList<V> ret = new ArrayList();
        for (Aresta<V> a : bucketArestas.get(origem)) {
            ret.add(a.getDestino());
        }
        return ret.isEmpty() ? null : ret;
    }

    public ArrayList<Aresta<V>> arestasEntre(V origem, V destino) throws NotVerticeException {
        ArrayList<Aresta<V>> ret = new ArrayList();
        int indexOrigem = vertices.indexOf(origem);
        int indexDestino = vertices.indexOf(destino);
        if (indexOrigem < 0 || indexDestino < 0) {
            throw new NotVerticeException();
        }
        bucketArestas.get(origem).forEach((a) -> {
            if (a.getDestino().equals(destino)) {
                ret.add(a);
            }
        });
        return ret.isEmpty() ? null : ret;
    }

    //M??todo que verifica se extiste uma aresta entre os vertices
    public boolean existeAresta(V origem, V destino) throws NotVerticeException {
        int indexOrigem = vertices.indexOf(origem);
        int indexDestino = vertices.indexOf(destino);
        if (indexOrigem < 0 || indexDestino < 0) {
            throw new NotVerticeException();
        }
        for (Aresta<V> a : bucketArestas.get(origem)) {
            if (a.getOrigem().equals(origem) && a.getDestino().equals(destino)) {
                return true;
            }
        }
        return false;
    }

    public int grauDoVertice(V vertice) throws NotVerticeException {
        int indexOrigem = vertices.indexOf(vertice);
        if (indexOrigem < 0) {
            throw new NotVerticeException();
        }
        int num = 0;
        for (Aresta<V> a : arestas) {
            if (a.getDestino().equals(vertice)) {
                num++;
            }
        }
        return num;
    }

    public int numeroDeArestas() {
        return arestas.size();
    }

    public int numeroDeVertices() {
        return vertices.size();
    }

    public ArrayList<Aresta<V>> menorCaminho(V origem, V destino) throws NotPathException {
        if (!this.containsVertice(origem) || !this.containsVertice(destino)) {
            return null;
        }
        HashMap<V, Double> distanceMap = new HashMap<>();
        HashSet<V> passados = new HashSet();
        HashSet<V> naoPassados = new HashSet();
        distanceMap.put(origem, new Double(0));
        for (V vertice : vertices) {
            naoPassados.add(vertice);
            if (!vertice.equals(origem)) {
                distanceMap.put(vertice, Double.MAX_VALUE);
            }
        }

        HashMap<V, Aresta> caminhoNegado = new HashMap<>();

        while (!naoPassados.isEmpty()) {
            V maisProximo = verticeMaisProximo(naoPassados, distanceMap);
            passados.add(maisProximo);
            naoPassados.remove(maisProximo);
            calcularDistancias(maisProximo, passados, distanceMap, caminhoNegado);

        }
        ArrayList<Aresta<V>> ret = new ArrayList<>();
        V o = destino;
        while (!o.equals(origem)) {
            Aresta<V> a = caminhoNegado.get(o);
            if (a == null) {
                throw new NotPathException();
            }
            ret.add(a);
            o = a.getOrigem();
        }
        return ret;
    }

    private V verticeMaisProximo(HashSet<V> naoPassados, HashMap<V, Double> distanceMap) {
        V ret = null;
        Iterator i = naoPassados.iterator();
        while (i.hasNext()) {
            if (ret == null) {
                ret = (V) i.next();
            } else {
                V vertice = (V) i.next();
                if (distanceMap.get(vertice) < distanceMap.get(ret)) {
                    ret = vertice;
                }
            }
        }
        return ret;
    }

    private void calcularDistancias(V vertice, HashSet<V> passados, HashMap<V, Double> distanceMap, HashMap<V, Aresta> caminhoNegado) {
        ArrayList<Aresta<V>> arestasVizinhos = bucketArestas.get(vertice);
        HashSet<V> passadosAgora = new HashSet<>();
        Double distanciaBase = distanceMap.get(vertice);
        for (Aresta<V> a : arestasVizinhos) {
            if (!passados.contains(a.getDestino())) {
                if (distanceMap.get(a.getDestino()) > a.getPeso() + distanciaBase) {
                    distanceMap.replace(a.getDestino(), distanciaBase + a.getPeso());
                    caminhoNegado.put(a.getDestino(), a);
                }
                //System.out.println("Vim de "+a.getOrigem()+" "+vertice+" e Passei em: "+ a.getDestino()+ " Dist??ncia: " +(a.getPeso()+distanciaBase) +" Dist??nciaNoMapa: "+ distanceMap.get(a.getDestino()));

            }
        }
    }

    private Aresta arestaMaisProxima(V origem, V destino) {
        if (origem == null) {
            return null;
        }
        ArrayList<Aresta<V>> candidatas = bucketArestas.get(origem);
        Aresta<V> closer = null;
        for (Aresta<V> a : candidatas) {
            if (a.getDestino().equals(destino)) {
                if (closer == null) {
                    closer = a;
                } else if (a.compareTo(closer) < 0) {
                    closer = a;
                }
            }
        }
        return closer;
    }

    public ArrayList<ArrayList<V>> getRotas(V origem, V destino) throws NotVerticeException {
        if (!vertices.contains(origem) || !vertices.contains(destino)) {
            throw new NotVerticeException();
        }
        HashMap<Double, ArrayList<V>> caminhos = new HashMap();
        HashSet<V> passados = new HashSet();
        HashSet<V> naoPassados = new HashSet();
        vertices.forEach((vertice) -> {
            naoPassados.add(vertice);
        });

        ArrayList<Aresta<V>> adjacenciasOrigem = bucketArestasUnique.get(origem);
        passados.add(origem);
        for (int i = 0; i < adjacenciasOrigem.size(); i++) {
            Aresta<V> aresta = adjacenciasOrigem.get(i);
            V adjacente = aresta.getDestino();
            ArrayList<V> caminhoAtual = new ArrayList();
            caminhoAtual.add(origem);
            buscaRecursiva(adjacente, destino, passados, naoPassados, caminhos, aresta.getPeso(), caminhoAtual);
        }
        ArrayList<ArrayList<V>> ret = new ArrayList();
        ArrayList<ComparablePath> retPaths = new ArrayList(caminhos.size());
        System.out.println("Size:" + caminhos.size());

        caminhos.forEach((Double u, ArrayList<V> t) -> {
            //System.out.println("Peso: "+u);
//            retPaths.add(new ComparablePath(t, u));
            ret.add(t);
        });

//        sort(retPaths);
//        for (int i = 0; i < retPaths.size(); i++) {
//            ret.add(retPaths.get(i).V);
//        }
        return ret;
    }

    private void sort(ArrayList<ComparablePath> array) {
        for (int i = 1; i < array.size(); i++) {
            ComparablePath current = array.get(i);
            int j = i - 1;
            while (j >= 0 && current.compareTo(array.get(j)) < 0) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, current);
        }
    }

    private class ComparablePath implements Comparable {

        public ComparablePath(ArrayList<V> V, Double u) {
            this.V = V;
            this.u = u;
        }

        ArrayList<V> V;
        Double u;

        @Override
        public int compareTo(Object t) {
            return u.intValue() - ((ComparablePath) t).u.intValue();
        }
    }

    private ArrayList<V> copiaDe(ArrayList<V> caminhoAtual) {
        ArrayList<V> ret = new ArrayList();
        for (int i = 0; i < caminhoAtual.size(); i++) {
            ret.add(caminhoAtual.get(i));
        }
        return ret;
    }

    public void buscaRecursiva(V origem, V destino, HashSet<V> passados, HashSet<V> naoPassados, HashMap<Double, ArrayList<V>> caminhos, Double tamanho, ArrayList<V> caminhoAtual) {
        if (origem.equals(destino)) {
            ArrayList<V> ret = copiaDe(caminhoAtual);
            ret.add(origem);
            while (caminhos.containsKey(tamanho)) {
                tamanho += 0.001;
            }
            caminhos.put(tamanho, ret);
            return;
        }
        caminhoAtual.add(origem);
        ArrayList<Aresta<V>> adj = bucketArestas.get(origem);
        passados.add(origem);
        for (int i = 0; i < adj.size(); i++) {
            V prox = adj.get(i).getDestino();
            if (!passados.contains(prox)) {
                buscaRecursiva(prox, destino, passados, naoPassados, caminhos, tamanho + adj.get(i).getPeso(), copiaDe(caminhoAtual));
            }
        }
        passados.remove(origem);

    }

}
