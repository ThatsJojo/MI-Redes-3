/**
 * Componente Curricular: Módulo Integrado de Concorrência e Conectividade
 * Autor: Cleyton Almeida da Silva, Estéfane Carmo de Souza e Matheus Nascimento
 * Data: 11/10/2021
 *
 * Declaro que este código foi elaborado por nós de forma colaborativa e
 * não contém nenhum trecho de código de outro colega ou de outro autor,
 * tais como provindos de livros e apostilas, e páginas ou documentos
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o  não a minha está destacado com  autor e a fonte do
 * código, e estou ciente que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */

package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RequisicaoHTTP {

    private String protocolo;
    private String recurso;
    private String metodo;
    private String body;
    private boolean manterViva = true;
    private long tempoLimite = 3000;
    private Map<String, List> cabecalhos;

    public static RequisicaoHTTP lerRequisicao(InputStream entrada) throws IOException {
        RequisicaoHTTP requisicao = new RequisicaoHTTP();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
        /* LÃª a primeira linha
         contem as informaÃ§oes da requisiÃ§Ã£o
         */
        String linhaRequisicao = buffer.readLine();

        //quebra a string pelo espaÃ§o em branco
        String[] dadosReq = linhaRequisicao.split(" ");
        
        //pega o metodo
        requisicao.setMetodo(dadosReq[0]);
        //paga o caminho do arquivo
        requisicao.setRecurso(dadosReq[1]);
        //pega o protocolo
        requisicao.setProtocolo(dadosReq[2]);
        String dadosHeader = buffer.readLine();
        //Enquanto a linha nao for nula e nao for vazia
        while (dadosHeader != null && !dadosHeader.isEmpty()) {
            String[] linhaCabecalho = dadosHeader.split(":");
            requisicao.setCabecalho(linhaCabecalho[0], linhaCabecalho[1].trim().split(","));
            dadosHeader = buffer.readLine();
        }
        requisicao.body = "";
        int pilha = 0;  //Stack auxiliar
        String line;
        do{
            if(!buffer.ready())
                break;
            line = buffer.readLine();

            if(line == null || line.isEmpty()){
                pilha = 0;
            }
            else if(line.charAt(0) == '{'){
                pilha++;
            }
            else if(line.charAt(0) == '}'){
                pilha--;
            }
            requisicao.body = requisicao.body + line;            
        }while(pilha != 0);
        //se existir a chave Connection no cabeÃ§alho
        if (requisicao.getCabecalhos().containsKey("Connection")) {
            //seta o manter viva a conexao se o connection for keep-alive
            requisicao.setManterViva(requisicao.getCabecalhos().get("Connection").get(0).equals("keep-alive"));
        }
        return requisicao;
    }

    /**
     * Método que altera o cabeçalho da requisicao
     * @param chave - chave 
     * @param valores - valores
     */
    public void setCabecalho(String chave, String... valores) {
        if (cabecalhos == null) {
            cabecalhos = new TreeMap();
        }
        cabecalhos.put(chave, Arrays.asList(valores));
    }

    /**
     * Método que retorna o protocolo da requisição
     * @return String - o protocolo
     */
    public String getProtocolo() {
        return protocolo;
    }

    /**
     * Método que altera o protocolo da requisicao
     * @param protocolo - novo protocolo
     */
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    /**
     * Método que retorna o recurso da requisição
     * @return String - recurso
     */
    public String getRecurso() {
        return recurso;
    }
    
    /**
     * Método que altera o recurso da requisição
     * @param recurso - novo recurso
     */
    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }
    
    /**
     * Método que obtém o método da requisição
     * @return String - método
     */
    public String getMetodo() {
        return metodo;
    }
    
    /**
     * Método que altera o método da requisição
     * @param metodo - novo método
     */
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    /**
     * Método que verifica se deve manter a requisição viva
     * @return boolean
     */
    public boolean isManterViva() {
        return manterViva;
    }

    /**
     * Método que altera se a requisição deve se manter viva
     * @param manterViva 
     */
    public void setManterViva(boolean manterViva) {
        this.manterViva = manterViva;
    }

    /**
     * Método que retorna o tempo limite da requisição
     * @return long - o tempo
     */
    public long getTempoLimite() {
        return tempoLimite;
    }

    /**
     * Método que altera o tempo limite da requisição
     * @param tempoLimite - novo tempo
     */
    public void setTempoLimite(long tempoLimite) {
        this.tempoLimite = tempoLimite;
    }
    
    /**
     * Método que retorna a lista de cabeçalhos
     * @return Map - a lista
     */
    public Map<String, List> getCabecalhos() {
        return cabecalhos;
    }
    /**
     * Método que altera a lista de cabeçalhos
     * @param cabecalhos - nova lista
     */
    public void setCabecalhos(Map<String, List> cabecalhos) {
        this.cabecalhos = cabecalhos;
    }

    /**
     * Método que retorna o corpo da requisição
     * @return String - o corpo
     */
    public String getBody() {
        return body;
    }
    
    /**
     * Método que altera o corpo da requisição
     * @param body - novo corpo
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    
}