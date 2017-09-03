package fr.kavalier.von.androloc;

/**
 * Created by Tom on 03/09/2017.
 */

public class Ville {
    private String nom;
    private double latitude;
    private double longitude;

    public Ville(String nom, double latitude, double longitude) {
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNom() {
        return nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return nom;
    }
}
