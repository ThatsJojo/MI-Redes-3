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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RespostaHTTP {

    private String protocolo;
    private String codigoResposta;
    private String mensagem;
    private byte[] conteudoResposta;
    private Map<String, List> cabecalhos;
    private OutputStream saida;

    public RespostaHTTP() {

    }

    public RespostaHTTP(String protocolo, String codigoResposta, String mensagem) {
        this.protocolo = protocolo;
        this.codigoResposta = codigoResposta;
        this.mensagem = mensagem;
    }

    /**
     * Envia os dados da resposta ao cliente.
     *
     * @throws IOException
     */
    public void enviar() throws IOException {
        //escreve o headers em bytes
        saida.write(montaCabecalho());
        if(conteudoResposta != null)
            saida.write(conteudoResposta);          //escreve o conteudo em bytes
        //encerra a resposta
        saida.flush();
    }

    /**
     * Insere um item de cabeÃ§alho no mapa
     *
     * @param chave
     * @param valores lista com um ou mais valores para esta chave
     */
    public void setCabecalho(String chave, String... valores) {
        if (cabecalhos == null) {
            cabecalhos = new TreeMap();
        }
        cabecalhos.put(chave, Arrays.asList(valores));
    }

    /**
     * pega o tamanho da resposta em bytes
     *
     * @return retorna o valor em bytes do tamanho do conteudo da resposta
     * convertido em string
     */
    public String getTamanhoResposta() {
        if(conteudoResposta != null)
            return getConteudoResposta().length + "";
        else
            return "0";
    }

    /**
     * converte o cabecalho em string.
     *
     * @return retorna o cabecalho em bytes
     */
    private byte[] montaCabecalho() {
        return this.toString().getBytes();
    }

    /**
     * Método que retorna o conteúdo da resposta
     * @return byte[] - conteudo
     */
    public byte[] getConteudoResposta() {
        return conteudoResposta;
    }
    
    /**
     * Método que altera o conteúdo da resposta.
     * @param conteudoResposta - novo conteúdo
     */
    public void setConteudoResposta(byte[] conteudoResposta) {
        this.conteudoResposta = conteudoResposta;
    }
    
    /**
     * Método que retorna o protoloco da resposta
     * @return String - protocolo
     */
    public String getProtocolo() {
        return protocolo;
    }

    /**
     * Método que altera o protocolo da resposta
     * @param protocolo - novo protocolo
     */
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    /** 
     * Método que retorna o código da resposta
     * @return String - codigo
     */
    public String getCodigoResposta() {
        return codigoResposta;
    }

    /**
     * Método que altera o codigo de resposta
     * @param codigoResposta - novo codigo
     */
    public void setCodigoResposta(String codigoResposta) {
        this.codigoResposta = codigoResposta;
    }

    /**
     * Método que obtém a mensagem da resposta
     * @return String - mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Método que altera a mensagem da resposta
     * @param mensagem - nova mensagem
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Método que retorna a lista de cabeçalhos da resposta
     * @return Map - lista
     */
    public Map<String, List> getCabecalhos() {
        return cabecalhos;
    }

    /**
     * Método que altera a lista de cabeçalhos da resposta
     * @param cabecalhos - nova lista
     */
    public void setCabecalhos(Map<String, List> cabecalhos) {
        this.cabecalhos = cabecalhos;
    }

    /**
     * Método que obtém o buffer de saída
     * @return OutputStream
     */
    public OutputStream getSaida() {
        return saida;
    }

    /**
     * Método que altera o buffer de saída
     * @param saida - novo buffer
     */
    public void setSaida(OutputStream saida) {
        this.saida = saida;
    }
    
    /**
     * Método que retorna os dados da reposta HTTP
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(protocolo).append(" ").append(codigoResposta).append(" ").append(mensagem).append("\r\n");
        for (Map.Entry<String, List> entry : cabecalhos.entrySet()) {
            str.append(entry.getKey());
            String stringCorrigida = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
            str.append(": ").append(stringCorrigida).append("\r\n");
        }
        str.append("\r\n");
        return str.toString();
    }
    
}
