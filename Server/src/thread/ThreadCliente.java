/**
 * Componente Curricular: M?dulo Integrado de Concorr?ncia e Conectividade
 * Autor: Cleyton Almeida da Silva, Est?fane Carmo de Souza e Matheus Nascimento
 * Data: 11/10/2021
 *
 * Declaro que este c?digo foi elaborado por n?s de forma colaborativa e
 * n?o cont?m nenhum trecho de c?digo de outro colega ou de outro autor,
 * tais como provindos de livros e apostilas, e p?ginas ou documentos
 * eletr?nicos da Internet. Qualquer trecho de c?digo de outra autoria que
 * uma cita??o para o  n?o a minha est? destacado com  autor e a fonte do
 * c?digo, e estou ciente que estes trechos n?o ser?o considerados para fins
 * de avalia??o. Alguns trechos do c?digo podem coincidir com de outros
 * colegas pois estes foram discutidos em sess?es tutorias.
 */
package thread;

import controller.RouterController;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.RequisicaoHTTP;
import util.RespostaHTTP;

/**
 * Classe que cria o cliente da API
 * 
 */
public class ThreadCliente extends Thread {

    private final Socket socket; //Socket
    private boolean conectado; //Verifica a conex?o com o servidor
    private HashMap data_base_ref; //Base de dados
    private static int qtd_list = 0; //Quantidade de pacientes graves que devem ser listados

    /**
     * M?todo construtor da classe
     * @param cliente - cliente socket
     * @param data_base - base de dados
     */
    public ThreadCliente(Socket cliente, HashMap data_base) {
        this.socket = cliente; //Cria o socket cliente
        this.data_base_ref = data_base; //Salva a base de dados
    }

    /**
     * M?todo que retorna a quantidade de pacientes graves a serem listados
     * @return int - quantidade
     */
    public static int getQtd_list() {
        return qtd_list;
    }

    /**
     * M?todo que altera a quantidade de pacientes graves que devem ser listados
     * @param qtd_list - nova quantidade
     */
    public static void setQtd_list(int qtd_list) {
        ThreadCliente.qtd_list = qtd_list;
    }

    @Override
    public void run() {
        conectado = true;
        //imprime na tela o IP do cliente
        System.out.println(socket.getInetAddress());
        while (conectado) {
            try {
                //cria uma requisicao a partir do InputStream do cliente
                RequisicaoHTTP requisicao = RequisicaoHTTP.lerRequisicao(socket.getInputStream());
                //se a conexao esta marcada para se mantar viva entao seta keepalive e o timeout
                if (requisicao.isManterViva()) {
                    socket.setKeepAlive(true);
                    socket.setSoTimeout((int) requisicao.getTempoLimite());
                } else {
                    //se nao seta um valor menor suficiente para uma requisicao
                    socket.setSoTimeout(300);
                }
                //abre o arquivo pelo caminho
                File arquivo = new File(requisicao.getRecurso().replaceFirst("/", ""));
                RespostaHTTP resposta;
                resposta = new RespostaHTTP(requisicao.getProtocolo(), "100", "OK");
                //se o caminho foi igual a / entao deve pegar o /index.html
                RouterController routerController = new RouterController();
                Object[] res = routerController.router(requisicao.getCabecalhos(), requisicao.getRecurso(), requisicao.getMetodo(), requisicao.getBody(), data_base_ref);
                if (res != null) {
                    System.out.println(res[0].toString());
                    System.out.println(res[1].toString());
                    System.out.println(res[2].toString());
                    resposta.setCodigoResposta((String) res[0]);
                    resposta.setMensagem((String) res[1]);
                    resposta.setConteudoResposta(((String) res[2]).getBytes());
                } else {
                    resposta.setMensagem("Not Found");
                    resposta.setCodigoResposta("404");
                }
                //lê todo o conteudo do arquivo para bytes e gera o conteudo de resposta
                //converte o formato para o GMT espeficicado pelo protocolo HTTP
                String dataFormatada = format.format(new Date());
                //cabecalho padraoo da resposta HTTP/1.1
                resposta.setCabecalho("Date", dataFormatada);
                resposta.setCabecalho("Server", "PBL server/0.1");
                resposta.setCabecalho("Content-Type", "application/json");
                resposta.setCabecalho("Logic-Counter", RouterController.contadorLamport + "");
                resposta.setCabecalho("Content-Length", resposta.getTamanhoResposta());
                resposta.setCabecalho("Access-Control-Allow-Origin", "*");
                //cria o canal de resposta utilizando o outputStream
                resposta.setSaida(socket.getOutputStream());
                resposta.enviar();
            } catch (IOException ex) {
                //quando o tempo limite terminar encerra a thread
                if (ex instanceof SocketTimeoutException) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            } finally {
                try {
                    socket.close(); //fecha o socket
                } catch (IOException ex) {
                    Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    conectado = false; //n?o h? conex?o
                }
            }

        }
    }

    private SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:Ss z");

}
