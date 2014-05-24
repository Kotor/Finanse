package com.parse.starter;

public class Transakcja {
	private String stworzony;
	private String nazwa; 
	private double koszt;
    
    public Transakcja(String stworzony, String nazwa, double koszt) {
    	super();
    	this.stworzony = stworzony;
        this.nazwa = nazwa;
        this.koszt = koszt;
    }

    public String getStworzony() {
        return stworzony;
    }
    
    public String getNazwa() {
        return nazwa;
    }

    public double getKoszt() {
       return koszt;
    }
    
    public void setStworzony(String stworzony) {
    	this.stworzony = stworzony;
    }

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setKoszt(double koszt) {
		this.koszt = koszt;
	}
}