/**
 * Componente Curricular: M�dulo Integrado de Concorr�ncia e Conectividade
 * Autor: Cleyton Almeida da Silva, Est�fane Carmo de Souza e Matheus Nascimento
 * Data: 11/10/2021
 *
 * Declaro que este c�digo foi elaborado por n�s de forma colaborativa e
 * n�o cont�m nenhum trecho de c�digo de outro colega ou de outro autor,
 * tais como provindos de livros e apostilas, e p�ginas ou documentos
 * eletr�nicos da Internet. Qualquer trecho de c�digo de outra autoria que
 * uma cita��o para o  n�o a minha est� destacado com  autor e a fonte do
 * c�digo, e estou ciente que estes trechos n�o ser�o considerados para fins
 * de avalia��o. Alguns trechos do c�digo podem coincidir com de outros
 * colegas pois estes foram discutidos em sess�es tutorias.
 */
package controller;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import routes.CadastrarVooRouter;
import routes.EffectiveBuyRouter;
import routes.PurchaseRouter;
import routes.ReserveRouter;
import routes.Router;
import routes.TicketRouter;
import routes.WayRouter;

public class RouterController {

    private Router route;
    public static int contadorLamport = 0;

    public Object[] router(Map<String, List> cabecalhos, String url, String method, String body, HashMap data_base) {
        System.out.println(url);
        try {
            String[] urlSplit = url.split("\\?");
            String path = urlSplit[0];

            HashMap<String, String> params = new HashMap();
            if (urlSplit.length == 2) {
                urlSplit = urlSplit[1].split("\\&");

                for (int i = 0; i < urlSplit.length; i++) {
                    String[] entry = urlSplit[i].split("=");
                    params.put(entry[0], entry[1]);
                }
            }

            Gson gson = new Gson();
            String queryParams = gson.toJson(params);
            System.out.println(path);
            if (path.equals("/ticket")) {
                route = new TicketRouter();
                if (method.equals("POST")) {
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }
            if (path.equals("/caminho")) {
                route = new WayRouter();
                if (method.equals("POST")) {
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }
            if (path.equals("/reserve")) {
                route = new ReserveRouter();
                int count;
                if(cabecalhos.get("Logic-Counter") == null){
                    count = RouterController.contadorLamport;
                }else{
                    count = Integer.parseInt((String) cabecalhos.get("Logic-Counter").get(0));
                }
                ((ReserveRouter)route).setREQLogicCounter(count);
                if (method.equals("POST")) {
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }
            if (path.equals("/purchase")) {
                route = new PurchaseRouter();
                if (method.equals("POST")) {
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }
            if (path.equals("/buy")) {
                route = new EffectiveBuyRouter();
                if (method.equals("POST")) {
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }
            if (path.equals("/addVoo")) {
                System.out.println(method);
                route = new CadastrarVooRouter();
                if (method.equals("POST")) {
                    System.out.println("POST");
                    return route.POST(body, data_base);
                } else if (method.equals("PUT")) {
                    return route.PUT(body, data_base);
                } else if (method.equals("GET")) {
                    System.out.println("GET");
                    return route.GET(queryParams, data_base);
                } else if (method.equals("DELETE")) {
                    return route.DELETE(queryParams, data_base);
                }
            }

            String[] responseNotFoud = {"404", "Not found", ""};
            return responseNotFoud;
        } catch (Exception e) {
            String[] responseNotFoud = {"500", "Internal ERROR", ""};
            return responseNotFoud;
        }
    }
}
