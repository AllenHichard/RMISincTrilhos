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

/**
 * A classe Trem representa um trem da sistema de compartilhamento de trilhos.
 * É responsável por gerenciar parâmetros como localização, velocidade, deslocamento
 * e de alterar os estados do sistema.
 * 
 * @author Allen Hichard e Caique Trindade
 */
public class Trem {
    
    private int x, y, contador, velocidade, velocidadeMPS, velocidadeLimite, perimetro, areaCompartilhada;
    private boolean parado, controle = true;
    private Trilho trilho;
    public static final int SEGUNDO = 1000, VELOCIDADE_PADRAO = 100, VELOCIDADE_MAXIMA = 200;
    
    /**
     * O construtor da classe Trem inicializa suas variáveis com caracteristicas
     * comportamentais dentro do sistema, como por exemplo: posição x e y,
     * contador (que representa a distância até o ponto inicial da área 
     * compartilhada), tamanho da área compartilhada, o perímetro do seu percurso
     * e referencia o trilho que o mesmo pertence.
     * 
     * @param x
     * @param y
     * @param contadorInicial
     * @param areaCompartilhada
     * @param perimetroTotal
     * @param trilho 
     */
    public Trem(int x, int y, int contadorInicial, int areaCompartilhada, int perimetroTotal, Trilho trilho) {
        
        this.x = x;
        this.y = y;
        this.velocidadeMPS = VELOCIDADE_PADRAO;
        this.velocidade = SEGUNDO/velocidadeMPS;
        this.velocidadeLimite = VELOCIDADE_MAXIMA;
        this.contador = contadorInicial;
        this.areaCompartilhada = areaCompartilhada;
        this.perimetro = perimetroTotal;
        this.trilho = trilho;
        this.parado = true;
    }

    /**
     * Retorna a posição x do trem.
     * 
     * @return Int com a coordenada X.
     */
    public int getX() {
        
        return x;
    }
    
    /**
     * Retorna a posição y do trem.
     * 
     * @return Int com a coordenada Y.
     */
    public int getY() {
        
        return y;
    }
    
    /**
     * Retorna a distancia até a entrada na zona compartilhada.
     * 
     * @return Int com o contador.
     */
    public int getDistanciaPontoCompartilhado() {
        
        return contador;
    }
    
    /**
     * Retorna o estado do trem.
     * 
     * @return True, se parado. False, caso contrário.
     */
    public boolean isParado() {
        
        return parado;
    }
    
    /**
     * Método responsável por iniciar o funcionamento do trem. Desse modo,
     * cria a Thread responsável por fazer o andar do trem.
     */
    public void start() {
        
        parado = false;
        new Thread(new MotorTrem()).start();
    }
    
    /**
     * Retorna a velocidade do trem.
     * 
     * @return Int com a velocidade atual do trem.
     */
    public int getVelocidade() {
        
        return velocidadeMPS;
    }
    
    /**
     * Retorna a velocidade limite do trem.
     * 
     * @return Int com a velocidade limite atual do trem.
     */
    public int getVelocidadeLimite() {
        
        return velocidadeLimite;
    }
    
    /**
     * Altera a velocidade limite do trem, caso a alteração seja necessária e válida.
     * Exemplo: se um trem está a 100 e altera a velocidade limite para 110, não 
     * é preciso alterar a velocidade atual, já que a mesma está abaixo. Caso a
     * velocidade limite seja alterada para um valor menor, tipo 90, a velocidade
     * é mudada para 90, nesse caso.
     * 
     * @param velocidadeLimite Int com a velocidade limite.
     */
    public void setVelocidadeLimite(int velocidadeLimite) {
        
        if (velocidadeLimite <= VELOCIDADE_MAXIMA)
            this.velocidadeLimite = velocidadeLimite;
        else
            this.velocidadeLimite = VELOCIDADE_MAXIMA;
        
        if (velocidadeLimite < velocidadeMPS)
            this.setVelocidade(velocidadeLimite);
        trilho.attVelocidade(velocidadeMPS, this.velocidadeLimite);
    }

    /**
     * Altera a velocidade de um trem, verificando se a velocidade que será
     * setada é menor ou igual a velocidade limite, caso for maior não é possível
     * fazer a alteração por segurança do sistema. Só poderá ser alterado a
     * velocidade se for menor ou igual que a velocidade limite. A velocidade
     * minima de um trem é 10.
     * 
     * @param velocidade Int com a velocidade.
     * 
     * @return True, caso efetuada com sucesso. False, caso contrário.
     */
    public boolean setVelocidade(int velocidade) {
        
        if (velocidade > velocidadeLimite)
            return false;
        
        if (velocidade != velocidadeMPS) {
            if (velocidade < 10)
                this.velocidadeMPS = 10;
            else
                this.velocidadeMPS = velocidade;
            this.velocidade = SEGUNDO/this.velocidadeMPS;
        }
        trilho.attVelocidade(velocidadeMPS, this.velocidadeLimite);
        return true;
    }
    
    /**
     * Classe MotorTrem é rodada por uma thread que é executada enquanto o trem
     * estiver circulando em seu trilho, sempre verificando se o trem está
     * entrando ou saindo da zona compartilhada e tomando as medidas necessárias
     * para cada uma das situações.
     */
    private class MotorTrem implements Runnable {

        @Override
        public void run() {

            while(true) {
                if (contador == 1) {
                    contador = perimetro;
                    trilho.solicitarPermissao();
                    while(!trilho.getPermissao()) {
                        try {
                            Thread.sleep((long)velocidade);
                        } catch (InterruptedException ex) {}
                    }
                    trilho.gerenciarVelocidade();
                    setVelocidadeLimite(VELOCIDADE_MAXIMA);
                    setVelocidade(VELOCIDADE_MAXIMA);
                }
                else if (contador == areaCompartilhada) {
                    setVelocidadeLimite(VELOCIDADE_MAXIMA);
                    setVelocidade(VELOCIDADE_PADRAO);
                    trilho.sairAreaCompartilhada();
                }
                
                if (x < trilho.getLargura() && y == 0)
                    x+=1;
                else if (x == trilho.getLargura() && y < trilho.getAltura())
                    y+=1;
                else if (y == trilho.getAltura() && x > 0)
                    x-=1;
                else if (x == 0 && y > 0)
                    y-=1;
                
                contador--;

                if (contador % 2 == 0)
                    trilho.atualizarCoordenada(x, y);
            
                try {
                    Thread.sleep((long)velocidade);
                } catch (InterruptedException ex) {};
            }
        }
        
    }
    
}
