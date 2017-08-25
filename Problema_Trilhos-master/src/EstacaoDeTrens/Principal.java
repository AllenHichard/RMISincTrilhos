/**
 * Componente Curricular: Módulo Integrador de Concorrência e Conectividade
 * Autor: Allen Hichard Marques dos Santos e Caique dos Santos Trindade
 * Data:  15/05/2016
 *
 * Declaramos que este código foi elaborado por nós em dupla e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o não a nossa está destacado com autor e a fonte do
 * código, e estamos cientes que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */

package EstacaoDeTrens;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Classe principal, responsável por inicializar a aplicação. Ao executar o
 * programa, uma caixa de diálogo é aberta solicitando o ID do trem que será
 * rodado nesta aplicação. Assim, quando todos os trens forem conectados o sistema
 * iniciará.
 * 
 * @author Allen Hichard e Caique Trindade
 */
public class Principal {

    /**
     * Método main da aplicação que instancia a classe Estacao.
     */
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        
        Estacao estacao = new Estacao();
    }
    
}
