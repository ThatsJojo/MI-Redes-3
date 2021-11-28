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
package routes;

import java.util.HashMap;

/**
 * Interface Router que determina o formato das rotas da API
 */
public interface Router {
    
    public Object[] GET(Object body, HashMap data_base); //Rota GET
    public Object[] POST(Object body, HashMap data_base); //Rota POST
    public Object[] PUT(Object body, HashMap data_base); //Rota PUT
    public Object[] DELETE(Object body, HashMap data_base);  //Rota DELETE
    
}
