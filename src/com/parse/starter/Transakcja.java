package com.parse.starter;

public class Transakcja {
	private String nazwa; 
	private double koszt;
    
    public Transakcja(String nazwa, double koszt) {
    	super();
        this.nazwa = nazwa;
        this.koszt = koszt;
    }

    public String getNazwa() {
        return nazwa;
    }

    public double getKoszt() {
       return koszt;
    }

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setKoszt(double koszt) {
		this.koszt = koszt;
	}
}