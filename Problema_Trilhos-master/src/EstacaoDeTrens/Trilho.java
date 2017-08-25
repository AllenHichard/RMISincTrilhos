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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

/**
 * A classe Trilho representa um trilho no sistema de compartilhamento de trilhos.
 * É utilizado como referência para a classe trem e opera como um objeto remoto,
 * sendo um intermédio entre o Trem e a Estacao desta aplicação com as demais.
 * 
 * @author Allen Hichard e Caique Trindade
 */
public class Trilho extends UnicastRemoteObject implements TrilhoRemoto {
    
    private static final long serialVersionUID = 1L;
    private int largura, altura;
    private Trem trem;
    private Estacao estacao;
    
    
    /**
     * O construtor da classe Trilho inicializa suas variáveis com caracteristicas
     * comportamentais dentro do sistema, como por exemplo: Largura, altura,
     * seu Trem característico e sua estação.
     * 
     * @param largura Int com a largura do trilho.
     * @param altura Int com a altura do trilho.
     * @param x_inicial Int com a coordenada X inicial do seu trem.
     * @param y_inicial Int com a coordenada Y inicial do seu trem.
     * @param contadorInicial Int com o contador inicial do seu trem.
     * @param areaCompartilhada Int com o tamanho da área compartilhada.
     * @param perimetroTotal Int com o valor do perímetro total do trilho.
     * @param estacao Estacao da aplicação.
     * @param porta Int com a porta que irá operar o objeto remoto.
     * 
     * @throws RemoteException Exceção é lançada caso ocorra alguma falha relacionada ao objeto remoto.
     */
    protected Trilho (int largura, int altura, int x_inicial, int y_inicial, int contadorInicial, int areaCompartilhada, int perimetroTotal , Estacao estacao, int porta)throws RemoteException {
    	
        super(porta, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
        
        //Settings
        /**
         * Logo inicialmente é definido as propriedades do sistema para garantir a segurança
         * da aplicação por meio do Protocolo SSL. A String pass é a chave primária para a criptografia
         * e autenticação.
         */
        String pass = "kappa123";
        System.setProperty("javax.net.ssl.debug", "all");
        System.setProperty("javax.net.ssl.keyStore", "ssl\\keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", pass);
        System.setProperty("javax.net.ssl.trustStore", "ssl\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", pass);
        
        this.largura = largura;
        this.altura = altura;
        this.estacao = estacao;
        trem = new Trem(x_inicial, y_inicial, contadorInicial, areaCompartilhada, perimetroTotal, this);
    }
    
    /**
     * Retorna a largura o trilho.
     * 
     * @return Int com a largura.
     */
    public int getLargura() {
        
        return largura;
    }
    
    /**
     * Retorna a altura do trilho.
     * 
     * @return Int com a altura.
     */
    public int getAltura() {
        
        return altura;
    }
    
    /**
     * Solicita para a Estacao a atualização da coordenada atual de seu trem.
     * 
     * @param x Int com a coordenada X.
     * @param y Int com a coordenada Y.
     */
    public void atualizarCoordenada(int x, int y) {
        
        estacao.atualizarCoordenada(x, y);
    }
    
    /**
     * Método utilizado para gerenciar a velocidade de todos os trens na estação
     * verificando e calculando as velocidades limite de cada trem. O método é
     * chamado quando o seu trem entrar no trilho compartilhado.
     */
    public void gerenciarVelocidade() {
        
        estacao.gerenciarVelocidade();
    }
    
    /**
     * Método utilizado quando o trem chega na zona compartilhada para pedir a
     * Estacao para solicitar a sua entrada na fila de prioridades.
     */
    public void solicitarPermissao() {
        
        estacao.solicitarPermissao();
    }
    
    /**
     * Método utilizado quando o trem sai da zona compartilhada para pedir a Estacao
     * para removê-lo da fila de prioridades.
     */
    public void sairAreaCompartilhada() {
        
        estacao.removerPermissao();
    }
    
    /**
     * Método utilizado para pedir permissão verificando se ele pode entrar.
     * Esse método só retorna true se o trem que pediu a permissão for o primeiro
     * da fila, o que significa que ele pode entrar na zona compartilhada.
     * 
     * @return True, se afirmativo. False, caso contrário.
     */
    public boolean getPermissao() {
        
        return estacao.isFirst();
    }
    
    /**
     * Método que propaga a atualização nos valores de velocidade do seu trem.
     * 
     * @param velocidade Int com a velocidade.
     * @param velocidadeLimite Int com a velocidade limite.
     */
    
    public void attVelocidade(int velocidade, int velocidadeLimite) {
        
        estacao.attVelocidade(velocidade, velocidadeLimite);
    }
    
    /**
     * Retorna uma String contendo a coordenada x e y do trem.
     * 
     * @return String com a coordenada (X,Y) do trem.
     */
    @Override
    public String getCoordenada() throws RemoteException {
        
        return trem.getX()+" "+trem.getY();
    }
   
    /**
     * Retorna a velocidade atual do seu trem.
     * 
     * @return Int com a velocidade.
     */
    @Override
    public int getVelocidade() throws RemoteException {
        
        return trem.getVelocidade();
    }
    
    /**
     * Altera a velocidade do seu trem.
     * 
     * @param velocidade Int com a velocidade.
     * 
     * @return True, se alterada com sucesso. False, caso contrário.
     */
    
    @Override
    public boolean setVelocidade(int velocidade) throws RemoteException {
        
        return trem.setVelocidade(velocidade);
    }
    
    /**
     * Retorna a velocidade limite do seu trem.
     * 
     * @return Int com a velocidade limite.
     */
    @Override
    public int getVelocidadeLimite() throws RemoteException {
        
        return trem.getVelocidadeLimite();
    }
    
    /**
     * Altera a velocidade limite do seu trem.
     * 
     * @param velocidade Int com a velocidade
     */
    @Override
    public void setVelocidadeLimite(int velocidade) throws RemoteException {
        
        trem.setVelocidadeLimite(velocidade);
    }
    
    /**
     * Método utilizado para atualizar os valores de velocidade de um dado trem
     * apresentados na interface.
     * 
     * @param numeroTrilho Int com o ID do trem.
     * @param velocidade Int com a velocidade atual do trem.
     * @param velocidadeLimite Int com a velocidade limite atual do trem.
     */
    @Override
    public void atualizarVelocidade(int numeroTrilho, int velocidade, int velocidadeLimite) throws RemoteException {
        
        estacao.atualizarVelocidade(numeroTrilho, velocidade, velocidadeLimite);
    }
    
    /**
     * Altera a coordenada de um dado trem na aplicação.
     * 
     * @param trilho ID do trem.
     * @param x Int com a coordenada X do trem.
     * @param y Int com a coordenada Y do trem.
     */
    @Override
    public void alterarCoordenada(int trilho, int x, int y) throws RemoteException {
        
        estacao.alterarCoordenada(trilho, x, y);
    }
    
    /**
     * Método utilizado para inicializar o sistema indicando que o trem já pode circular
     * no trilho, se os 3 trens já tenham sinalizados, o sistema inicia, caso o contrário será necessário
     * os trens restantes dar seus starts.
     */
    @Override
    public void start() throws RemoteException {
        
        if (!estacao.isComecou()) {
            estacao.setComecou(true);
            trem.start();
        }
    }

    /**
     * Retorna a distância que falta para um trem entrar na zona compartilhada.
     * 
     * @return Int com a distância.
     */
    @Override
    public int getDistanciaPontoCompartilhado() throws RemoteException {
        
        return trem.getDistanciaPontoCompartilhado();
    }
    /**
     * Adiciona o ID de um dado trem na fila de prioridades.
     * 
     * @param numeroTrilho Int com o ID do trem.
     */
    @Override
    public void addPermissao(int numeroTrilho) throws RemoteException {
        
        estacao.addPermissao(numeroTrilho);
    }
    
    /**
     * Remove a permissão do trem, após ter saído do trilho compartilhado.
     * 
     * @param numeroTrilho Int com o ID do trem.
     */
    @Override
    public void remPermissao(int numeroTrilho) throws RemoteException {
        
        estacao.remPermissao(numeroTrilho);
    }
    
}
