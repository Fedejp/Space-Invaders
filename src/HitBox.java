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

public class HitBox {
	private int ancho;
	private int alto;
	private int posicionX;
	private int posicionY;

	public HitBox(int an, int al, int x, int y) {
		this.ancho = an;
		this.alto = al;
		this.posicionX = x;
		this.posicionY = y;
	}

	public int getPosicionX() {
		return posicionX;
	}

	public void setPosicionX(int posicionX) {
		this.posicionX = posicionX;
	}

	public int getPosicionY() {
		return posicionY;
	}

	public void setPosicionY(int posicionY) {
		this.posicionY = posicionY;
	}
	
	
}
