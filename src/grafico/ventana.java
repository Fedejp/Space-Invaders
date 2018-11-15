package grafico;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import app.Juego;
import elementos.CampoDeFuerza;
import valueobject.HitBoxVO;
import valueobject.ProyectilVO;


public class ventana extends JFrame {
	//private JButton btndos, btnuno, btntres;
	//private JTextField txtMensaje;
	
	private JLabel naveEnemiga1;
	private JLabel naveEnemiga2;
	private JLabel naveEnemiga3;
	private JLabel naveJugador;
	private Container c;
	private ArrayList<JLabel> enemigosJL;
	private ArrayList<JLabel> ListMuro;
	private ArrayList<JLabel> ListProy;
	private JLabel puntaje;
	private JLabel vidas;
	private int randomizador=0;
	
	public ventana(){
		c = this.getContentPane();
		eventos();
		c.setBackground(Color.BLACK);
		this.setSize(Juego.getInstancia().getAnchoPantalla(), Juego.getInstancia().getLargoPamtalla());
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Esto no va
		jugar();
	}
	
	public void jugar() {
		int x = Juego.getInstancia().getJugador().getPosicionX();
		int y = Juego.getInstancia().getJugador().getPosicionY();

		ImageIcon nave = new ImageIcon("nave.png");
		this.naveJugador = new JLabel(nave);
		this.naveJugador.setBounds(x, y, 32, 32);
		c.add(naveJugador);
		
		enemigosJL = new ArrayList<JLabel>();
		ImageIcon enemigo1 = new ImageIcon("enemigoA.png");
		for (HitBoxVO enemigo : Juego.getInstancia().getEnemigos()) {
			JLabel enem = new JLabel(enemigo1);
			enem.setBounds(enemigo.getPosicionX(), enemigo.getPosicionY(), 32, 32);
			enemigosJL.add(enem);
			c.add(enem);
		}
		
		ListMuro= new ArrayList<JLabel>();
		
		for(CampoDeFuerza pared: Juego.getInstancia().getCampo()) {
			JLabel muro= new JLabel(new ImageIcon ("muroMedio.png"));
			muro.setBounds(pared.getPosicionX(),pared.getPosicionY(),32,32);
			ListMuro.add(muro);
			muro.setVisible(true);
			c.add(muro);
		}
		
		ListProy= new ArrayList<JLabel>();
		
		puntaje= new JLabel();
		puntaje.setText("Puntos: "+ Juego.getInstancia().getJugador().getPuntaje());
		puntaje.setFont(new Font("OCR A Extended", Font.BOLD,14));
		puntaje.setBounds(350, 570, 150, 30);
		puntaje.setForeground(Color.WHITE);
		puntaje.setVisible(true);
		c.add(puntaje);
		
		vidas=new JLabel();
		vidas.setText("Vidas: "+ Juego.getInstancia().getJugador().vidasRestantes());
		vidas.setFont(new Font("OCR A Extended", Font.BOLD,14));
		vidas.setBounds(30, 570, 100, 30);
		vidas.setForeground(Color.WHITE);
		vidas.setVisible(true);
		c.add(vidas);
		
		Manejo refresco = new Manejo();
		Timer timer = new Timer(20, refresco);
		timer.start();

	}

	class Manejo implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Manejo de colisiones
			Juego.getInstancia().chequearImpactos();					//AcÃƒÂ¡ debe recorrer todas las listas de JLabels de la ventana para eliminar las correspondientes.
			Juego.getInstancia().eliminarImpactados();
			//Manejo de proyectiles, mover y dibujar
			Juego.getInstancia().moverProyectiles();								//Muevo todos los proyectiles en pantalla
			Iterator<JLabel> itproy= ListProy.iterator();							//Debo recorrer en paralelo ambas listas de proyectiles y de JLabels
			for(ProyectilVO tiro : Juego.getInstancia().getListaProyectiles()) {		//Por cada nuevo proyectil
				JLabel aux= (JLabel) itproy.next();									//cambio al JLabel siguiente
				aux.setBounds(tiro.getPosicionX(), tiro.getPosicionY(), 16, 16);
				aux.setVisible(true);
				if(tiro.getDireccion()>0){
					aux.setIcon(new ImageIcon("misil2.png"));//
				}
				else{
					aux.setIcon(new ImageIcon("misil.png"));
				}
			}
			while(itproy.hasNext()){
				itproy.next().setVisible(false);
			}
			//Manejo de enemigos, movimiento y dibujar. Control del nuevo nivel.
			Juego.getInstancia().moverEnemigos();
			if(Juego.getInstancia().hayEnemigos()) {
				Iterator<JLabel> itr = enemigosJL.iterator();
				for (HitBoxVO enemigo : Juego.getInstancia().getEnemigos()) {
					JLabel enemigoLabel = itr.next();
					//System.out.print("- x:" + enemigo.getPosicionX());
					//System.out.println();
					enemigoLabel.setBounds(enemigo.getPosicionX(), enemigo.getPosicionY(), 32, 32);
				}
				while(itr.hasNext()) {
					itr.next().setVisible(false);
				}
			}else if(Juego.getInstancia().getNivel()<3){
				Juego.getInstancia().siguienteNivel();
				JOptionPane.showMessageDialog(null, "Has ganado 500 puntos. Continuas?", "Has pasado de nivel!", JOptionPane.WARNING_MESSAGE);
				Iterator<JLabel> itr = enemigosJL.iterator();
				for (HitBoxVO enemigo : Juego.getInstancia().getEnemigos()) {
					JLabel enemigoLabel = itr.next();
					enemigoLabel.setBounds(enemigo.getPosicionX(), enemigo.getPosicionY(), 32, 32);
					enemigoLabel.setVisible(true);
				}
			}else
				JOptionPane.showMessageDialog(null, "Tu puntaje final es de "+ Juego.getInstancia().getJugador().getPuntaje() + "puntos","Ganaste!", JOptionPane.INFORMATION_MESSAGE);
		
			//Actualización del mensaje de puntaje y de vidas del jugador
			puntaje.setText("Puntos: "+ Juego.getInstancia().getJugador().getPuntaje());
			vidas.setText("Vidas: "+ Juego.getInstancia().getJugador().vidasRestantes());
			
			//Manejo del disparo de los enemigos. Sólo disparan si logran aumentar al randomizador a más de 500.
			if(randomizador > 100) {
				Juego.getInstancia().dispararEnemigo();
				JLabel tiro = new JLabel();
				ListProy.add(tiro);
				c.add(tiro);
				randomizador=0;
			} else {
				randomizador= randomizador + (int) (Math.random()*Juego.getInstancia().getDistancia_mov_enem())+1;
			}
		
		}

	}

	private void eventos() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addKeyListener(new EventoTeclas());
		//this.add
	}
	

	class EventoTeclas implements KeyListener{
		public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
			int tecla = e.getKeyCode();	
			if(tecla == KeyEvent.VK_LEFT) {
				Juego.getInstancia().getJugador().moverseEjeX(-5);
				naveJugador.setBounds(Juego.getInstancia().getJugador().getPosicionX(), Juego.getInstancia().getJugador().getPosicionY(), 32, 32);
				//System.out.println(Juego.getInstancia().getJugador().getPosicionX());
			}
			if (tecla == KeyEvent.VK_RIGHT) {
				Juego.getInstancia().getJugador().moverseEjeX(5);
				naveJugador.setBounds(Juego.getInstancia().getJugador().getPosicionX(), Juego.getInstancia().getJugador().getPosicionY(), 32, 32);
				//System.out.println(Juego.getInstancia().getJugador().getPosicionX());
			} 
			if(tecla==KeyEvent.VK_SPACE) {
				Juego.getInstancia().dispararJugador();
				JLabel misil = new JLabel(new ImageIcon("misil.png"));
				ListProy.add(misil);
				c.add(misil);				
			}
			if (tecla == KeyEvent.VK_ESCAPE) System.out.println("Juego terminado");
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
}
