/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import Exceptions.NotVerticeException;
import facade.Facade;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Aeroporto;
import model.Companhia;
import thread.ThreadCliente;

/**
 *
 * @author matheusnascimento
 */
public class Server {
    private static Facade f = Facade.getInstance();

    public static void main(String[] args) {

        HashMap data_base = new HashMap<String, Object>();
        
        try{
            importarArquivo("Cidades.txt");

        }catch(NotVerticeException | IOException e){
            
        }
        
        
        
        ServerSocket serv = null;
        try {
            System.out.println("Incializando o servidor...");
            //Iniciliza o servidor
            serv = new ServerSocket(8000);
            //serv = new ServerSocket(Integer.valueOf(System.getenv("PORT")));

            System.out.println("Servidor iniciado, ouvindo a porta " + serv.getLocalPort());
            System.out.println("Host: " + serv.toString());
       
            
            while (true) {
                Socket clie = serv.accept();
                System.out.println(clie.getInetAddress().getHostAddress());
                //Inicia thread do cliente

                new ThreadCliente(clie, data_base).start();
            }
        } catch (IOException e) {
            try {
                serv.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                serv.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    
    /**
     * Método para importação de um grafo a partir de um arquivo, em que contém
     * o número de cidades, cada linha contém o nome do cidade.
     *
     * @param nomeArquivo - nome do arquivo
     * @throws IOException - exceção de arquivo
     * @throws Exceptions.NotVerticeException
     */
    public static void importarArquivo(String nomeArquivo) throws IOException, NotVerticeException {
        BufferedReader ler = null;
        Facade f = Facade.getInstance();

        int contador = 0;
        try {
            ler = new BufferedReader(new FileReader(nomeArquivo));
            contador = Integer.parseInt(ler.readLine());
            while (contador > 0) {//enquanto contador for maior que 0
                String linha = ler.readLine();//lê a linha
                String nome = linha; //contém o nome do cidade
                f.cadastrarAeroporto(nome);
                contador--;//decrementa no contador
            }
            importarArquivoCompanhia("Azul.txt"); //importa o arquivo da companhia
            System.out.println("Here");
        } catch (FileNotFoundException exception) {
            throw new IOException();
        } finally {
            if (ler != null) {
                ler.close(); //fecha o arquivo
            }
        }
    }
    
    /**
     * Importa o arquivo com as rotas de uma companhia. A primeira linha
     * contém o nome da companhia aérea. Cada linha contém o nome da cidade.
     * Após isso, cada uma linha com o nome da cidade e a seguinte com a cidade 
     * adjacente e o peso da ligação(de voo) separada por espaço.
     *
     * @param nomeArquivo - nome do arquivo
     * @throws IOException
     */
    public static void importarArquivoCompanhia(String nomeArquivo) throws IOException, NotVerticeException{
        try (BufferedReader ler = new BufferedReader(new FileReader(nomeArquivo)) //fecha o arquivo
        ) {
            String nomeAresta, linha;
            Aeroporto origem1;
            nomeAresta = ler.readLine(); //lê a linha e armazena na string
            Companhia companhia = f.cadastrarCompanhia(nomeAresta);

            linha = ler.readLine();//lê a proxima linha
            while (linha != null) { //enquanto não for o fim do arquivo
                origem1 = f.getAeroporto(linha); //a linha lida é o cidade
                linha = ler.readLine();//lê a proxima linha
                String[] adjacencia = linha.split(" ");//separa em partes
                for (int i = 0; i < adjacencia.length; i = i + 4) {//Até o fim do vetor
                    Aeroporto destino2 = f.getAeroporto(adjacencia[i]); //indica o segundo cidade
                    double tempo = Double.parseDouble(adjacencia[i + 1]); //converte para int a string
                    double preco = Double.parseDouble(adjacencia[i + 2]); //converte para int a string
                    int passageiros = Integer.parseInt(adjacencia[i + 3]); //converte para int a string
                    
                    f.cadastrarVoo(origem1, destino2, passageiros, preco, companhia, tempo); //adiciona a aresta no grafo.
                }
                linha = ler.readLine();
            }
        } catch (FileNotFoundException exception) {
            throw new IOException();
        }
    }
}
