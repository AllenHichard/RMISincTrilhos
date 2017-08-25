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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Esta classe é uma interface a ser utilizado como objeto de acesso remoto por 
 * hosts do tipo cliente, caso o cliente queira utilizar alguns destes métodos 
 * é só fazer a chamada pelo objeto remoto desta interface.
 * 
 * @author allen e caique
 */
public interface TrilhoRemoto extends Remote {

    public String getCoordenada() throws RemoteException;
    
    public int getVelocidade() throws RemoteException;
    
    public boolean setVelocidade(int velocidade) throws RemoteException;
    
    public int getVelocidadeLimite() throws RemoteException;
    
    public void setVelocidadeLimite(int velocidade) throws RemoteException;
    
    public void atualizarVelocidade(int numeroTrilho, int velocidade, int velocidadeLimite) throws RemoteException;
    
    public void alterarCoordenada(int trilho, int x, int y) throws RemoteException;
    
    public void start() throws RemoteException;
    
    public int getDistanciaPontoCompartilhado() throws RemoteException;
    
    public void addPermissao(int numeroTrilho) throws RemoteException;
    
    public void remPermissao(int numeroTrilho) throws RemoteException;
    
}
