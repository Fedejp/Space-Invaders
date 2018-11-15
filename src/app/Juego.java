package app;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;

import elementos.CampoDeFuerza;
import elementos.HitBox;
import elementos.Proyectil;
import grafico.ventana;
import naves.Enemigo;
import naves.Jugador;
import valueobject.HitBoxVO;
import valueobject.ProyectilVO;;

//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Trabajo Practico Obligatorio
//  @ File Name : space Invaders v1.0
//  @ Date : 2/10/2018
//  @ Author : Parodi Federico, Salvioli Santiago, Yanzon Carlos Santiago
//
//

public class Juego {
	private int nivel;
	private Jugador jugador;
	private int anchoPantalla;
	private int largoPamtalla;
	private int enemigoSpawnX;
	private int enemigoSpawnY;
	private boolean movimientoInverso;
	private ArrayList<Enemigo> enemigos;
	private ArrayList<CampoDeFuerza> camposDeFuerza;
	private ArrayList<Proyectil> listaProyectiles;
	private int distancia_mov_enem;
	
	private static Juego instancia;
	
	private Juego() {
		this.anchoPantalla = 500 ;
		this.largoPamtalla = 644;
		this.enemigoSpawnX = 4;
		this.enemigoSpawnY = 26;
	}
	
	//Revisar si va aca
	// ###########################
	/*public void movimientoJugador(String input) {
		if("a".equals(input) && estaEnLaPantalla(jugador.getPosicionX()-1, jugador.getPosicionY())) {
			jugador.setPosicionX(jugador.getPosicionX()-1);
		} else if ("s".equals(input) && estaEnLaPantalla(jugador.getPosicionX()+1, jugador.getPosicionY())) {
			jugador.setPosicionX(jugador.getPosicionX()+1);
		}
		System.out.println("[" + jugador.getPosicionX() + ":" + jugador.getPosicionY() + "]");
	}*/
	// ###########################

	public void chequearImpactos() {
			for(Proyectil tiro : listaProyectiles) {
				if(tiro.getPosicionY()<=0|| tiro.getPosicionY()>=644)
					tiro.setImpactada(true);
				if(!tiro.isImpactada()) {
					for(Enemigo enem : enemigos) { //Verifica la lista de enemigos
						if(!tiro.isImpactada() && enem.estaTocando(tiro.getPosicionX(), tiro.getPosicionY())&& tiro.getDireccion()<0){
							enem.setImpactada(true);
							tiro.setImpactada(true);
							enem.darPuntos(this.jugador);
							System.out.println("Enemigo impactado");
						}
					}
				}
			if(!tiro.isImpactada()) {
				for(CampoDeFuerza muro : camposDeFuerza) {
					if(!tiro.isImpactada()&&muro.estaTocando(tiro.getPosicionX(), tiro.getPosicionY())){
						muro.serDanado(10);
						tiro.setImpactada(true);
						System.out.println("Muro Impactado");
    	}
				}
			}
				if(!tiro.isImpactada()&&jugador.estaTocando(tiro.getPosicionX(), tiro.getPosicionY())) {
					jugador.restarVida();
					tiro.setImpactada(true);
					System.out.println("JugadorImpactado");
				}
			}
		}

	public void eliminarImpactados() {
		// TODO Auto-generated method stub
		Iterator<Proyectil> itproy=listaProyectiles.iterator();
		HitBox aux;
		while(itproy.hasNext()){
			aux=(HitBox) itproy.next();
			if(aux.isImpactada()) {
				itproy.remove();
				System.out.println("Proyectil Eliminado");
			}
		}
		Iterator<Enemigo> itenem=enemigos.iterator();
		while (itenem.hasNext()){
			aux= (HitBox) itenem.next();
			if(aux.isImpactada()) {
				itenem.remove();
				System.out.println("Nave enemiga Eliminado");
			}
		}
		Iterator<CampoDeFuerza> itcamp =camposDeFuerza.iterator();
		while(itcamp.hasNext()) {
			aux=(HitBox) itcamp.next();
			if(aux.isImpactada())
				itcamp.remove();
				//System.out.println("Campo Eliminado");
		}
		if(this.jugador.vidasRestantes()<=0) {
			terminarJuego();
		}
	}

	public void nuevoJuego() {
		this.jugador = new Jugador(0, 0);
		enemigos = new ArrayList<Enemigo>();
		camposDeFuerza = new ArrayList<CampoDeFuerza>();
		//listaColisiones = new ArrayList<HitBox>();
		listaProyectiles = new ArrayList<Proyectil>();
		spawnEnemigos();
		spawnCamposDeFuerza();
		jugador.spawn(this.anchoPantalla/2 - 100, this.largoPamtalla - 100);
		this.distancia_mov_enem = 2;
		nivel=1;
		
		ventana v = new ventana();
		
	}

	public void terminarJuego() {
		//Elimina los enemigos y campos de fuerza
		enemigos.clear();
		camposDeFuerza.clear();
	//	listaColisiones.clear();
		listaProyectiles.clear();
		
		System.out.println("Termino el Juego");
		System.out.println("Tu puntaje fue de: " + this.jugador.getPuntaje() + "pts");
	}
	
	/**
 	* @Funcion: siguienteNivel
 	* @Descripcion: pasa al siguiente nivel, respawnea  a los enemigos y campos de fuerza y le da el respectivo puntaje al jugador
 	* @Devuelve:
 	* @Parametros:
 	*/
	public void siguienteNivel() {
		this.nivel++;
		this.distancia_mov_enem+=2;
		spawnEnemigos();
		spawnCamposDeFuerza();
		jugador.recibirPuntos(500);
		listaProyectiles.clear();
	}
	
	public void moverJugadorIzq() {
		jugador.moverseEjeX(-1);
	}

	public void moverJugadorDer() {
		jugador.moverseEjeX(1);
	}
	
	/**
 	* @Funcion: estaEnLaPantalla
 	* @Descripcion: chequea si un objeto esta o no en el campo de juego
 	* @Devuelve: true/false
 	* @Parametros: posicion X e Y
 	*/
	public Boolean estaEnLaPantalla(int x, int y) {
		if(x <= anchoPantalla && x >= 0) {
			if(y <= largoPamtalla && y >= 0) return true;
			else return false;
		} else return false;
	}
	
	/**
 	* @Funcion: spawnEnemigos
 	* @Descripcion: Crea y ubica 15 enemigos en el mapa.
 	* @Devuelve:
 	* @Parametros:
 	*/
	private void spawnEnemigos() {
		int auxX = enemigoSpawnX; //De donde se va a ubicar el primer enemigo en el eje X
		int auxY = enemigoSpawnY; //De donde se va a ubicar el primer enemigo en el eje y
		this.movimientoInverso = false; //Como los spawneamos por primera vez, van a moverse de izquierda a derecha.
		
		//La funcion itera durante 15 veces y cada 5 enemigos, baja un lugar en y para formar filas
		
		for(int i = 0; i < 15; i++) {
			int enemigoSpawnXx = 6;
			int enemigoSpawnYy = 6;
			if(i == 0) {
				auxX = enemigoSpawnXx;
				auxY = enemigoSpawnYy;
			} else if(i == 5) {
				auxX = enemigoSpawnXx;
				auxY = enemigoSpawnYy + 36;
			} else if(i == 10) {
				auxX = enemigoSpawnXx;
				auxY = enemigoSpawnYy + 72;
			}
			Enemigo enemigo = new Enemigo(auxX, auxY);
			enemigos.add(enemigo);
			auxX += 36;
		}		
	}

	/**
 	* @Funcion: SpawnCamposDeFuerza
 	* @Descripcion: Crea y ubica los campos de fuerza
 	* @Devuelve: 
 	* @Parametros:
 	*/
	private void spawnCamposDeFuerza() {
		int auxX = 40;
		int auxY = this.largoPamtalla - 150;
		
		for (int i = 0; i < 7; i++) {
			CampoDeFuerza campo = new CampoDeFuerza(32, 32, auxX, auxY);
			camposDeFuerza.add(campo);
			auxX += 64;
		}
	}

	//###############################################
	public void dispararJugador() {
		listaProyectiles.add(jugador.disparar(-1));
	}
	//##############################################333
	private Enemigo elegirEnemigo() {
		int atr;
		atr= (int) (Math.floor(Math.random() * enemigos.size()));
		Enemigo aux= enemigos.get(atr);
		System.out.println("Hay " +enemigos.size() + "naves ");
		System.out.println("Intenta nave: " +atr);
		for(Enemigo enem : enemigos) {
			if(aux.getPosicionY()<enem.getPosicionY())
				atr++;
		}
		return enemigos.get(atr);
		
	}

	public void dispararEnemigo() {
		Enemigo aux= elegirEnemigo();
		listaProyectiles.add(aux.disparar(1));
		
	}
	
	public boolean hayEnemigos() {
		if(enemigos.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	private void eliminarHitBox() {
		
	}
	
	public static Juego getInstancia() {
		if(instancia == null) instancia = new Juego();
		return instancia;
	}
	
	public List<HitBoxVO> getEnemigos() {
		List<HitBoxVO> aux = new ArrayList<HitBoxVO>();
		for(Enemigo nave : enemigos) {
			aux.add(nave.getHBVO());
		}
		return aux;
	}

	public List<ProyectilVO>getListaProyectiles(){
		List<ProyectilVO>aux=new ArrayList<ProyectilVO>();
		for(Proyectil proyect: listaProyectiles) {
			aux.add(proyect.getPVO());
		}
		return aux;
	}
	
	public Jugador getJugador() {
		return this.jugador;
	}

	public int getAnchoPantalla() {
		return anchoPantalla;
	}

	public int getLargoPamtalla() {
		return largoPamtalla;
	}
	
	public int getDistancia_mov_enem() {
		return distancia_mov_enem;
	}

	public List<CampoDeFuerza> getCampo() {
		// TODO Auto-generated method stub
		return this.camposDeFuerza;
	}

	public void moverEnemigos() {
		boolean chocaPared = false;
		
		//Aca chequeamos si toco con una pared, y si tiene que invertir el movimiento
		for (Enemigo enemigo : enemigos) {
			if (enemigo.getPosicionX() > anchoPantalla -78 && !movimientoInverso) {
				movimientoInverso = true;  
				chocaPared = true;
			} else if (enemigo.getPosicionX() <= 6 && movimientoInverso) {
				movimientoInverso = false; 
				chocaPared = true;
			}
		}
		//Si alguno de los enemigos choco una pared (por ende todos deben moverse) vemos cual y realizamos la accion correspondiente
		for (Enemigo enemigo: enemigos) {
				if(chocaPared) {
					enemigo.setPosicionY(enemigo.getPosicionY() + 36);}
				else if(movimientoInverso) {
					enemigo.setPosicionX(enemigo.getPosicionX() - distancia_mov_enem);			
				} else {
					enemigo.setPosicionX(enemigo.getPosicionX() + distancia_mov_enem);
				}
				if(enemigo.getPosicionY() >= jugador.getPosicionY()) {			//Si algun enemigo llega a la altura del jugador o menos se termina el juego
					terminarJuego();
					break;
				}
			}
	}
	
	public void moverProyectiles() {
		for(Proyectil tiro: listaProyectiles) {
			if(tiro.getPosicionY()>0&&tiro.getPosicionY()<644) {
				tiro.setPosicionY(tiro.getPosicionY()+(tiro.getVelocidad()*tiro.getDireccion()));
				//System.out.println("Hay un proyectil en la posicion X:"+ tiro.getPosicionX() + "Y:" +tiro.getPosicionY());
				}
			}
		
		
	}

	
	public int getNivel() {
		// TODO Auto-generated method stub
		return nivel;
	}

	
}
