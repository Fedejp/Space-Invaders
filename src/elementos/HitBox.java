package elementos;

import valueobject.HitBoxVO;

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
	private boolean impactada;

	public HitBox(int an, int al, int x, int y) {
		this.ancho = an;
		this.alto = al;
		this.posicionX = x;
		this.posicionY = y;
		setImpactada(false);
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



	public boolean isImpactada() {
		return impactada;
	}



	public void setImpactada(boolean impactada) {
		this.impactada = impactada;
	}

	public boolean estaTocando(int posx, int posy) {
		if((this.posicionX<=posx&&this.posicionX+this.ancho>=posx) && (this.posicionY<=posy&&this.posicionY+this.alto>=posy)) {
			return true;
		} else return false;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}
	
	public HitBoxVO getHBVO() {
	
		return(new HitBoxVO(this.ancho,this.alto,this.posicionX,this.posicionY));
	}
	
	
}
