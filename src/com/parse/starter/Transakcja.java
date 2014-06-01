package com.parse.starter;


public class Transakcja {
	private String stworzony;
	private String nazwa; 
	private double koszt;
	private byte[] zdjecie;
	private String tag;
    /*	
    public Transakcja(String stworzony, String nazwa, double koszt, String tag) {
    	super();
    	this.stworzony = stworzony;
        this.nazwa = nazwa;
        this.koszt = koszt;
        this.tag = tag;
    }
    */
    public Transakcja(String stworzony, String nazwa, double koszt, byte[] zdjecie, String tag) {
    	super();
    	this.stworzony = stworzony;
        this.nazwa = nazwa;
        this.koszt = koszt;
        this.zdjecie = zdjecie;
        this.tag = tag;
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
    
    public byte[] getZdjecie() {
    	return zdjecie;
    }
    
    public String getTag() {
    	return tag;
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
	
	public void setZdjecie(byte[] zdjecie) {
		this.zdjecie = zdjecie;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
}