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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A classe Estação é responsável gerenciar os trilhos do sistema assim como
 * representar a disposição dos mesmos de forma gráfica, utilizando java 2D.
 * 
 * @author Allen Hichard e Caique Trindade
 */
public class Estacao extends JPanel {

    private JFrame frame;
    private JLabel label1, label2, label3;
    private TrilhoRemoto trilho1, trilho2, trilho3;
    private boolean comecou, trilho1_isOn, trilho2_isOn, trilho3_isOn;
    private int numeroTrilho, x1, y1, x2, y2, x3, y3;
    public static final int RAIO_TREM = 10;
    private LinkedList filaPrioridade;
    private JTextField velo;
    
    /**
     * O construtor da classe estação é responsável por inicializar o sistema.
     * O sistema inicia após conseguir conectar-se com os 3 trilhos que o compõe.
     * Desta forma, o ponto de partida dos trens é dado e a interface gráfica da
     * aplicação é disposta.
     * 
     * @throws RemoteException Exceção é lançada caso a conexão com algum trilho (objeto remoto) seja perdida.
     * @throws MalformedURLException Exceção é lançada caso a URL do objeto remoto estiver com sintaxe errada.
     * @throws NotBoundException 
     */

    public Estacao() throws RemoteException, MalformedURLException, NotBoundException {

        super(null);
       
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

        /**
         * Fila de prioridade para atender a solicitações de entrada dos trens.
         */
        filaPrioridade = new LinkedList();

        /**
         * Caixa de dialogo que permite ao usuário informa as opções de cada trilho.
         * Exemplo: como são 3 trilhos o usuário pode informa de um a três para iniciar
         * o sistema, não podendo repetir o número.
         */
        this.numeroTrilho = Integer.parseInt(JOptionPane.showInputDialog("Digite o número do trem."));
        boolean pronto = false;
        switch (numeroTrilho) {
            case 1:
                
                /**
                 * Inicialização do trilho 1.
                 */
                trilho1 = new Trilho(500, 250, 500, 0, 245, 995, 1500, this, 5551);
                
                /**
                 * Cria o registro para utilização do RMI, utilizando a porta e os serviçoes de 
                 * segurança do RMI. Após utiliza o registro para abrir conexão em um par de 
                 * Ip/Porta utilizando um Objeto remoto.
                 */
                Registry registry = LocateRegistry.createRegistry(5551, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
           
                registry.rebind("rmi://localhost:5551/TrilhoRemoto1", trilho1);
                do {
                    try {
                        /**
                         * Conexão para clientes utilizarem o Objeto remoto deste servidor.
                         */
                    	Registry registry0 = LocateRegistry.getRegistry("127.0.0.1", 5552, new SslRMIClientSocketFactory());
                        trilho2 = (TrilhoRemoto) registry0.lookup("rmi://localhost:5552/TrilhoRemoto2");
                        Registry registry1 = LocateRegistry.getRegistry("127.0.0.1", 5553, new SslRMIClientSocketFactory());
                        trilho3 = (TrilhoRemoto) registry1.lookup("rmi://localhost:5553/TrilhoRemoto3");
                        pronto = true;
                    } catch (Exception ex) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex1) {};
                    }
                } while (!pronto);
                break;
            case 2:
                
                 /**
                 * Inicialização do trilho 2.
                 */
                trilho2 = new Trilho(500, 500, 0, 500, 745, 1245, 2000, this,5552);
               
                /**
                 * Cria o registro para utilização do RMI, utilizando a porta e os serviçoes de 
                 * segurança do RMI. Após utiliza o registro para abrir conexão em um par de 
                 * Ip/Porta utilizando um Objeto remoto.
                 */
                
                Registry registry1 = LocateRegistry.createRegistry(5552, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
                registry1.rebind("rmi://localhost:5552/TrilhoRemoto2", trilho2);
                do {
                    try {
                        /**
                         * Conexão para clientes utilizarem o Objeto remoto deste servidor.
                         */
                    	Registry registry10 = LocateRegistry.getRegistry("127.0.0.1", 5551, new SslRMIClientSocketFactory());
                        trilho1 = (TrilhoRemoto) registry10.lookup("rmi://localhost:5551/TrilhoRemoto1");
                        Registry registry11 = LocateRegistry.getRegistry("127.0.0.1", 5553, new SslRMIClientSocketFactory());
                        trilho3 = (TrilhoRemoto) registry11.lookup("rmi://localhost:5553/TrilhoRemoto3");
                        pronto = true;
                    } catch (Exception ex) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex1) {};
                    }
                } while (!pronto);
                break;
            case 3:
                
                 /**
                 * Inicialização do trilho 3.
                 */
                trilho3 = new Trilho(500, 500, 500, 0, 995, 1245, 2000, this, 5553);
                  /**
                 * Cria o registro para utilização do RMI, utilizando a porta e os serviçoes de 
                 * segurança do RMI. Após utiliza o registro para abrir conexão em um par de 
                 * Ip/Porta utilizando um Objeto remoto.
                 */
                
                Registry registry2 = LocateRegistry.createRegistry(5553, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
                registry2.rebind("rmi://localhost:5553/TrilhoRemoto3", trilho3);
                do {
                    try {
                        /**
                         * Conexão para clientes utilizarem o Objeto remoto deste servidor.
                         */
                    	Registry registry20 = LocateRegistry.getRegistry("127.0.0.1", 5551, new SslRMIClientSocketFactory());
                        trilho1 = (TrilhoRemoto) registry20.lookup("rmi://localhost:5551/TrilhoRemoto1");
                        Registry registry21 = LocateRegistry.getRegistry("127.0.0.1", 5552, new SslRMIClientSocketFactory());
                        trilho2 = (TrilhoRemoto) registry21.lookup("rmi://localhost:5552/TrilhoRemoto2");
                        pronto = true;
                    } catch (Exception ex) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex1) {};
                    }
                } while (!pronto);
                break;
        };
        
        
        trilho1_isOn = trilho2_isOn = trilho3_isOn = true;
        
        System.out.println("Todos os trilhos foram conectados.");
        System.out.println("O sistema iniciará em breve...");

        // Partes da interface
        this.setBounds(0, 0, 511, 386 + 30);
        /**
         * Montagem da Parte Gráfica da interface.
         */
        label1 = new JLabel("", JLabel.CENTER);
        label1.setBounds(130, 5, 250, 125);
        label2 = new JLabel("", JLabel.CENTER);
        label2.setBounds(5, 130, 250, 250);
        label3 = new JLabel("", JLabel.CENTER);
        label3.setBounds(255, 130, 250, 250);
        
        /**
         * Parte da interface que permite ser alterado a velocidade  de um trêm.
         */
        JLabel velocimetro = new JLabel("Alterar a velocidade do Trem "+numeroTrilho+":", JLabel.CENTER);
        velo = new JTextField();
        velo.setBounds(511-5-90-90, 386+5, 90, 20);
        velocimetro.setBounds(511-5-90-90-200, 386+5, 200, 20);
        JButton botao = new JButton("Enviar");
        botao.setBounds(511-5-80, 386+5, 80, 20);
        botao.addActionListener(new ActionListener(){

            /**
             * Método utilizado para alterar a velocidade dos trens. Primeiramente
             * Esse método que um evento disparado ao pressionar um botão, tendo como 
             * principais condições verificar se o trilho que pressionou o botão
             * foi um 1 , 2 ou 3.
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (numeroTrilho == 1) {
                    try {
                        trilho1.setVelocidade(Integer.parseInt(velo.getText()));
                        velo.setText("");
                    } catch (RemoteException ex) {}
                }
                else if (numeroTrilho == 2) {
                    try {
                        trilho2.setVelocidade(Integer.parseInt(velo.getText()));
                        velo.setText("");
                    } catch (RemoteException ex) {}
                }
                else {
                    try {
                        trilho3.setVelocidade(Integer.parseInt(velo.getText()));
                        velo.setText("");
                    } catch (RemoteException ex) {}
                }
            }
            
        });
        
        /**
         * Adiciona os componentes de visualização de velocidades na tela de cada trem.
         */
        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(velo);
        this.add(velocimetro);
        this.add(botao);
        
        iniciarPontos();
        
        /**
         * Criação do Frame aonde será visualizado a circulação dos trens
         */
        frame = new JFrame("Painel do Trilho "+numeroTrilho);
        frame.setContentPane(this);
        frame.setSize(this.getWidth()+6, this.getHeight()+29);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((tela.width-frame.getWidth())/2, (tela.height-frame.getHeight())/2);
        this.repaint();
        frame.setVisible(true);
        
        comecou = false;
        new Thread(new InicializarTrens()).start();
    }
    
    /**
     * Verifica se esta aplicação foi inicializada no sistema.
     * 
     * @return True, se já iniciada, ou false, caso contrário.
     */
    public boolean isComecou() {
        
        return comecou;
    }
    
    /**
     * Muda o estado de inicialização da aplicação, alterado para true quando o
     * sistema é iniciado.
     * 
     * @param estado Boolean com o estado.
     */
    public void setComecou(boolean estado) {
        
        this.comecou = estado;
    }

    /**
     * Método responsável por desenhar a disposição dos trilhos e dos trens na
     * interface gráfica, possibilitando assim o monitoramento e acompanhamento
     * do sistema.
     * 
     * @param g Graphics.
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        Rectangle trilhoUm = new Rectangle(130, 5, 250, 125);
        Rectangle trilhoDois = new Rectangle(5, 130, 250, 250);
        Rectangle trilhoTres = new Rectangle(255, 130, 250, 250);

        g2.draw(trilhoUm);
        g2.draw(trilhoDois);
        g2.draw(trilhoTres);
        g2.setColor(Color.red);
        g2.drawLine(130, 130, 380, 130);
        g2.drawLine(255, 130, 255, 380);
        
        g2.setColor(Color.blue);
        if (trilho1_isOn)
            g2.fillOval(x1/2 + 125, y1/2, RAIO_TREM, RAIO_TREM);
        else
            g2.drawOval(x1/2 + 125, y1/2, RAIO_TREM, RAIO_TREM);
        
        g2.setColor(Color.magenta);
        if (trilho2_isOn)
            g2.fillOval(x2/2, y2/2 + 125, RAIO_TREM, RAIO_TREM);
        else
            g2.drawOval(x2/2, y2/2 + 125, RAIO_TREM, RAIO_TREM);
        
        g2.setColor(new Color(0x3ea20d));
        if (trilho3_isOn)
            g2.fillOval(x3/2 + 250, y3/2 + 125, RAIO_TREM, RAIO_TREM);
        else
            g2.drawOval(x3/2 + 250, y3/2 + 125, RAIO_TREM, RAIO_TREM);
    }
    
    /**
     * Inicia as coordenadas de cada trem em seu determinado trilho e exibe
     * as informações de velocidade de cada trem no instante inicial.
     */
    public void iniciarPontos() {
        
        String posicaoXY[];
        try {
            if (trilho1_isOn) {
                posicaoXY = trilho1.getCoordenada().split(" ");
                x1 = Integer.parseInt(posicaoXY[0]);
                y1 = Integer.parseInt(posicaoXY[1]);
                label1.setText("<html><center>Trem 1<br>Velocidade: 100m/s<br>Vel. Limite: 200m/s</center></html>");
            }
        } catch (RemoteException ex) {
            trilho1_isOn = false;
            System.out.println("A conexão com o trilho 1 foi perdida!");
        }
        try {
            if (trilho2_isOn) {
                posicaoXY = trilho2.getCoordenada().split(" ");
                x2 = Integer.parseInt(posicaoXY[0]);
                y2 = Integer.parseInt(posicaoXY[1]);
                label2.setText("<html><center>Trem 2<br>Velocidade: 100m/s<br>Vel. Limite: 200m/s</center></html>");
            }
        } catch (RemoteException ex) {
            trilho2_isOn = false;
            System.out.println("A conexão com o trilho 2 foi perdida!");
        }
        try {
            if (trilho3_isOn) {
                posicaoXY = trilho3.getCoordenada().split(" ");
                x3 = Integer.parseInt(posicaoXY[0]);
                y3 = Integer.parseInt(posicaoXY[1]);
                label3.setText("<html><center>Trem 3<br>Velocidade: 100m/s<br>Vel. Limite: 200m/s</center></html>");
            }
        } catch (RemoteException ex) {
            trilho3_isOn = false;
            System.out.println("A conexão com o trilho 3 foi perdida!");
        }
        repaint();
    }

    /**
     * O método permite que o trem desta aplicação solicite permissão para entrar
     * na zona compartilhada. Quando chamado, o trem é inserido na fila de prioridade
     * de todos os trens que compõe o sistema.
     */
    public void solicitarPermissao() {

        if (trilho1_isOn) {
            try {
                trilho1.addPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho1_isOn = false;
                System.out.println("A conexão com o trilho 1 foi perdida!");
            }
        }
        if (trilho2_isOn) {
            try {
                trilho2.addPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho2_isOn = false;
                System.out.println("A conexão com o trilho 2 foi perdida!");
            }
        }
        if (trilho3_isOn) {
            try {
                trilho3.addPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho3_isOn = false;
                System.out.println("A conexão com o trilho 3 foi perdida!");
            }
        }
    }

    /**
     * Método utilizado para atender uma requisição de permissão, adicionando o
     * ID de um dado trem na fila de prioridades.
     * 
     * @param numeroTrilho Int com o ID do trem.
     */
    public synchronized void addPermissao(int numeroTrilho) {

        if(!filaPrioridade.contains(numeroTrilho))
            filaPrioridade.addLast(numeroTrilho);
    }

    /**
     * Ao passar pelo trilho compartilhado, o trem deve ser removido da lista de
     * prioridades. O método então é responsável por solicitar a remoção do trem
     * nas demais aplicações.
     */
    public void removerPermissao() {
        
        if (trilho1_isOn) {
            try {
                trilho1.remPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho1_isOn = false;
                System.out.println("A conexão com o trilho 1 foi perdida!");
            }
        }
        if (trilho2_isOn) {
            try {
                trilho2.remPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho2_isOn = false;
                System.out.println("A conexão com o trilho 2 foi perdida!");
            }
        }
        if (trilho3_isOn) {
            try {
                trilho3.remPermissao(numeroTrilho);
            } catch (RemoteException ex) {
                trilho3_isOn = false;
                System.out.println("A conexão com o trilho 3 foi perdida!");
            }
        }
    }

    /**
     * Método utilizado para atender uma remoção de permissão, removendo o
     * ID de um dado trem da fila de prioridades.
     * 
     * @param numeroTrilho Int com o ID de um dado trem.
     */
    public void remPermissao(int numeroTrilho) {
        
        try {
            if ((int) filaPrioridade.getFirst() == numeroTrilho) {
                filaPrioridade.removeFirst();
            }
        } catch (NoSuchElementException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
     * Método que verifica se o primeiro da fila é o trem desta aplicação. Caso 
     * o trem seja o primeiro da lista é autorizado a entrada na zona compartilhada.
     * 
     * @return True, se verdadeiro ou false, caso contrário.
     */
    public boolean isFirst() {
        
        try {
            return (int)filaPrioridade.getFirst() == numeroTrilho;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Método utilizado para calcular o limite de velocidade de cada trem,
     * baseando em variáveis como o tempo que o trem com permissão irá levar na
     * zona compartilhada e distancia dos demais trens até a entrada do trilho
     * compartilhado.
     */
    public void gerenciarVelocidade() {
        
        int contador1 = 2000, contador2 = 2000, contador3 = 2000, velocidade1 = 0, velocidade2 = 0, velocidade3 = 0;
        switch(numeroTrilho) {
            case 1:
                try {
                    if (trilho2_isOn)
                        contador2 = trilho2.getDistanciaPontoCompartilhado();
                    if (trilho3_isOn)
                        contador3 = trilho3.getDistanciaPontoCompartilhado();
                    if (contador3 < contador2) {
                        velocidade3 = (int) contador3/3;
                        if (velocidade3 < 10)
                            velocidade3 = 10;
                        if (trilho3_isOn)
                            trilho3.setVelocidadeLimite(velocidade3);
                        velocidade2 = (int) contador2/((contador3/velocidade3)+4);
                        if (velocidade2 < 10)
                            velocidade2 = 10;
                        if (trilho2_isOn)
                            trilho2.setVelocidadeLimite(velocidade2);
                    }
                    else {
                        velocidade2 = (int) contador2/3;
                        if (velocidade2 < 10)
                            velocidade2 = 10;
                        if (trilho2_isOn)
                            trilho2.setVelocidadeLimite(velocidade2);
                        velocidade3 = (int) contador3/((contador2/velocidade2)+4);
                        if (velocidade3 < 10)
                            velocidade3 = 10;
                        if (trilho3_isOn)
                            trilho3.setVelocidadeLimite(velocidade3);
                    }
                    
                    
                } catch (RemoteException ex) {
                    System.out.println("Trilho");
                }
                break;
            case 2:
                try {
                    if (trilho1_isOn)
                        contador1 = trilho1.getDistanciaPontoCompartilhado();
                    if (trilho3_isOn)
                        contador3 = trilho3.getDistanciaPontoCompartilhado();
                    if (contador3 < contador1) {
                        velocidade3 = (int) contador3/4;
                        if (velocidade3 < 10)
                            velocidade3 = 10;
                        if (trilho3_isOn)
                            trilho3.setVelocidadeLimite(velocidade3);
                        velocidade1 = (int) contador1/((contador3/velocidade3)+4);
                        if (velocidade1 < 10)
                            velocidade1 = 10;
                        if (trilho1_isOn)
                            trilho1.setVelocidadeLimite(velocidade1);
                    }
                    else {
                        velocidade1 = (int) contador1/4;
                        if (velocidade1 < 10)
                            velocidade1 = 10;
                        if (trilho1_isOn)
                            trilho1.setVelocidadeLimite(velocidade1);
                        velocidade3 = (int) contador3/((contador1/velocidade1)+3);
                        if (velocidade3 < 10)
                            velocidade3 = 10;
                        if (trilho3_isOn)
                            trilho3.setVelocidadeLimite(velocidade3);
                    }
                } catch (RemoteException ex) {
                    System.out.println("Trilho");
                }
                break;
            case 3:
                try {
                    if (trilho1_isOn)
                        contador1 = trilho1.getDistanciaPontoCompartilhado();
                    if (trilho2_isOn)
                        contador2 = trilho2.getDistanciaPontoCompartilhado();
                    if (contador2 < contador1) {
                        velocidade2 = (int) contador2/4;
                        if (velocidade2 < 10)
                            velocidade2 = 10;
                        if (trilho2_isOn)
                            trilho2.setVelocidadeLimite(velocidade2);
                        velocidade1 = (int) contador1/((contador2/velocidade2)+4);
                        if (velocidade1 < 10)
                            velocidade1 = 10;
                        if (trilho1_isOn)
                            trilho1.setVelocidadeLimite(velocidade1);
                    }
                    else {
                        velocidade1 = (int) contador1/4;
                        if (velocidade1 < 10)
                            velocidade1 = 10;
                        if (trilho1_isOn)
                            trilho1.setVelocidadeLimite(velocidade1);
                        velocidade2 = (int) contador2/((contador1/velocidade1)+3);
                        if (velocidade2 < 10)
                            velocidade2 = 10;
                        if (trilho2_isOn)
                            trilho2.setVelocidadeLimite(velocidade2);
                    }
                } catch (RemoteException ex) {
                    System.err.println(ex.toString());
                }
                break;
        }
        
    }
    
    /**
     * Método que propaga a nova coordenada do trem desta aplicação para as demais
     * aplicações envolvidas.
     * 
     * @param x Int com a coordenada X do trem.
     * @param y Int com a coordenada Y do trem.
     */
    public void atualizarCoordenada(int x, int y) {
        
        if (trilho1_isOn) {
            try {
                trilho1.alterarCoordenada(numeroTrilho, x, y);
            } catch (RemoteException ex) {
                trilho1_isOn = false;
                System.out.println("A conexão com o trilho 1 foi perdida!");
            }
        }
        if (trilho2_isOn) {
            try {
                trilho2.alterarCoordenada(numeroTrilho, x, y);
            } catch (RemoteException ex) {
                trilho2_isOn = false;
                System.out.println("A conexão com o trilho 2 foi perdida!");
            }
        }
        if (trilho3_isOn) {
            try {
                trilho3.alterarCoordenada(numeroTrilho, x, y);
            } catch (RemoteException ex) {
                trilho3_isOn = false;
                System.out.println("A conexão com o trilho 3 foi perdida!");
            }
        }
    }

    /**
     * Método que altera a coordenada de um dado trem e atualiza o ponto do
     * trem na interface gráfica da aplicação, através da chamada ao método
     * repaint.
     * 
     * @param trilho Int com o número do trilho em que a coordenada foi alterada.
     * @param x Int com a coordenada X do trem.
     * @param y Int com a coordenada Y do trem.
     */
    public void alterarCoordenada(int trilho, int x, int y) {
        
        if (trilho == 1) {
            x1 = x;
            y1 = y;
            trilho1_isOn = true;
        }
        else if (trilho == 2) {
            x2 = x;
            y2 = y;
            trilho2_isOn = true;
        }
        else {
            x3 = x;
            y3 = y;
            trilho3_isOn = true;
        }
        repaint();
    }
    
    /**
     * Método responsável por atualizar as informações de velocidade do trem desta
     * aplicação para as demais.
     * 
     * @param velocidade Int com a velocidade atual do trem (em metros por segundo).
     * @param velocidadeLimite Int com a atual velocidade limite do trem (em metros por segundo).
     */
    public void attVelocidade(int velocidade, int velocidadeLimite) {
        
        if (trilho1_isOn) {
            try {
                trilho1.atualizarVelocidade(numeroTrilho, velocidade, velocidadeLimite);
            } catch (RemoteException ex) {
                trilho1_isOn = false;
                System.out.println("A conexão com o trilho 1 foi perdida!");
            }
        }
        if (trilho2_isOn) {
            try {
                trilho2.atualizarVelocidade(numeroTrilho, velocidade, velocidadeLimite);
            } catch (RemoteException ex) {
                trilho2_isOn = false;
                System.out.println("A conexão com o trilho 2 foi perdida!");
            }
        }
        if (trilho3_isOn) {
            try {
                trilho3.atualizarVelocidade(numeroTrilho, velocidade, velocidadeLimite);
            } catch (RemoteException ex) {
                trilho3_isOn = false;
                System.out.println("A conexão com o trilho 3 foi perdida!");
            }
        }
    }

    /**
     * Método responsável por atualizar as informações de velocidade de um dado
     * trem na interface.
     * 
     * @param numeroTrilho Int com o ID do trem.
     * @param velocidade Int com a velocidade atual do trem (em metros por segundo).
     * @param velocidadeLimite Int com a velocidade limite do trem (em metros por segundo).
     */
    public void atualizarVelocidade(int numeroTrilho, int velocidade, int velocidadeLimite) {
        
        if (numeroTrilho == 1)
            label1.setText("<html><center>Trem 1<br>Velocidade: "+velocidade+"m/s<br>Vel. Limite: "+velocidadeLimite+"m/s</center></html>");
        else if (numeroTrilho == 2)
            label2.setText("<html><center>Trem 2<br>Velocidade: "+velocidade+"m/s<br>Vel. Limite: "+velocidadeLimite+"m/s</center></html>");
        else 
            label3.setText("<html><center>Trem 3<br>Velocidade: "+velocidade+"m/s<br>Vel. Limite: "+velocidadeLimite+"m/s</center></html>");
    }

    /**
     * Classe privada que implementa a interface Runnable, utilizada para
     * inicializar todos os trens envolvidos no sistema.
     */
    private class InicializarTrens implements Runnable {
        
        @Override
        public void run() {
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {ex.toString();}
            
            if (!comecou) {
                try {
                    trilho1.start();
                } 
                catch (RemoteException ex) {
                    trilho1_isOn = false;
                    System.out.println("A conexão com o trilho 1 foi perdida!");
                }
                try {
                    trilho2.start();
                } 
                catch (RemoteException ex) {
                    trilho2_isOn = false;
                    System.out.println("A conexão com o trilho 2 foi perdida!");
                }
                try {
                    trilho3.start();
                } 
                catch (RemoteException ex) {
                    trilho3_isOn = false;
                    System.out.println("A conexão com o trilho 3 foi perdida!");
                }
            }
        }
        
    }

}
