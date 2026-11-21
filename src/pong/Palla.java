// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

import geometria.Posizione;
import geometria.Rettangolo;
import java.util.Random;

/** La palla: una posizione, una velocità, un raggio. Sa muoversi e rimbalzare. */
public class Palla {
    /** Di quanto si moltiplica la velocità a ogni rimbalzo su una racchetta. 1.0 = non cambia mai. */
    static final double ACCELERAZIONE_A_OGNI_RIMBALZO = 1.0;

    /** Ogni quanti rimbalzi sulle racchette la palla accelera, e di quanto (1.15 = il 15% più veloce). */
    static final int RIMBALZI_PER_ACCELERARE = 4;
    static final double ACCELERAZIONE_OGNI_TOT_RIMBALZI = 1.15;
    /** La velocità oltre la quale la palla non accelera più (in orizzontale, pixel per fotogramma). */
    static final double VELOCITA_MASSIMA = 14;

    static final int RAGGIO = 8;

    /**
     * Di quanto cambia, al massimo e a caso, la velocità verticale quando la palla prende un ostacolo
     * di lato: senza questo po' di caso, la palla potrebbe girare per sempre fra gli stessi ostacoli.
     */
    static final double DEVIAZIONE_SUGLI_OSTACOLI = 0.3;

    private Posizione posizione;
    private double velocitaX;
    private double velocitaY;
    private int rimbalziDaLancio;   // rimbalzi sulle racchette dall'ultimo lancio
    private final Random caso = new Random();

    Palla(double x, double y) {
        this.posizione = new Posizione(x, y);
    }

    // ---- quello che chiunque può chiedere alla palla

    /** Dove si trova la palla (il suo centro). Posizione è un record: chi la riceve non può spostare la palla. */
    public Posizione getPosizione() { return posizione; }
    public double getVelocitaX() { return velocitaX; }
    public double getVelocitaY() { return velocitaY; }
    public int getRaggio() { return RAGGIO; }

    /** Il quadrato che contiene la palla: serve per sapere quale ostacolo tocca. */
    public Rettangolo getArea() {
        return new Rettangolo(posizione.x() - RAGGIO, posizione.y() - RAGGIO, 2 * RAGGIO, 2 * RAGGIO);
    }

    // ---- quello che può chiedere solo il codice del pacchetto pong (la Partita)

    /**
     * Rimette la palla in un punto e la lancia verso destra o verso sinistra, con un po' di angolo
     * a caso; velocita sono i pixel per fotogramma in orizzontale (la decide il livello).
     */
    void lancia(double x, double y, boolean versoDestra, double velocita) {
        if (versoDestra){
            lancia(x, y, Bordo.DESTRA, velocita);
        } else {
            lancia(x, y, Bordo.SINISTRA, velocita);
        }
        
    }

    void lancia(double x, double y, Bordo verso, double velocita){
        posizione = new Posizione(x, y);
        rimbalziDaLancio = 0;
        double deviazione = (caso.nextDouble() * 2 - 1) * velocita * 0.6;

        if (verso == Bordo.ALTO){
            velocitaY = -velocita;
            velocitaX = deviazione;
        } else if (verso == Bordo.BASSO){
            velocitaY = velocita;
            velocitaX = deviazione;
        } else if (verso == Bordo.SINISTRA){
            velocitaX = -velocita;
            velocitaY = deviazione;
        } else {
            velocitaX = velocita;
            velocitaY = deviazione;
        }
    }

    /** Ferma la palla in un punto (prima di cominciare una partita). */
    void fermaIn(double x, double y) {
        posizione = new Posizione(x, y);
        velocitaX = 0;
        velocitaY = 0;
    }

    void muovi() {
        posizione = posizione.spostata(velocitaX, velocitaY);
    }

    /** Rimbalzo sul bordo alto o basso del campo: si inverte solo la direzione verticale. */
    void rimbalzaVerticale() {
        velocitaY = -velocitaY;
    }


    /** Metodo per il rimbalzo orizzontale: si inverte solo la direzione orizzontale */
    void rimbalzaOrizzontale(){
        velocitaX = - velocitaX;
    }


    /** Vero se la palla, in questo istante, si sovrappone alla racchetta. */
    boolean tocca(Racchetta racchetta) {
        double x = posizione.x();
        double y = posizione.y();
        boolean sovrappostaInOrizzontale = x + RAGGIO >= racchetta.getX()
                && x - RAGGIO <= racchetta.getX() + racchetta.getLarghezza();
        boolean sovrappostaInVerticale = y + RAGGIO >= racchetta.getY()
                && y - RAGGIO <= racchetta.getY() + racchetta.getAltezza();
        return sovrappostaInOrizzontale && sovrappostaInVerticale;
    }

    /**
     * Rimbalzo su una racchetta: la palla torna indietro e prende un angolo che dipende
     * da dove ha colpito la racchetta (al centro va dritta, sui bordi va in diagonale).
     */
    void rimbalzaSu(Racchetta racchetta) {
        double velocitaBase = Math.max (Math.abs(velocitaX), Math.abs(velocitaY));

        double velocita = velocitaBase * ACCELERAZIONE_A_OGNI_RIMBALZO;
        rimbalziDaLancio++;
        if (rimbalziDaLancio % RIMBALZI_PER_ACCELERARE == 0) {
            velocita = Math.min(velocita * ACCELERAZIONE_OGNI_TOT_RIMBALZI, Math.max(VELOCITA_MASSIMA, Math.abs(velocitaBase)));
        }
        if (racchetta.getBordo().orizzontale()) {
            double scostamento = (posizione.x() - racchetta.getCentroX()) / (racchetta.getLarghezza() / 2.0);
            velocitaX = scostamento * velocita;
            if (racchetta.getBordo() == Bordo.ALTO) {
                velocitaY = velocita;
                posizione = new Posizione(posizione.x(), racchetta.getY() + racchetta.getAltezza() + RAGGIO);
            } else {
                velocitaY = -velocita;
                posizione = new Posizione(posizione.x(), racchetta.getY() - RAGGIO);
            }
        } else {
            double scostamento = (posizione.y() - racchetta.getCentroY()) / (racchetta.getAltezza() / 2.0);
            velocitaY = scostamento * velocita;
            if (racchetta.getBordo() == Bordo.SINISTRA) {
                velocitaX = velocita;
                posizione = new Posizione(racchetta.getX() + racchetta.getLarghezza() + RAGGIO, posizione.y());
            } else {
                velocitaX = -velocita;
                posizione = new Posizione(racchetta.getX() - RAGGIO, posizione.y());
            }
        }
    }

    /**
     * Rimbalzo su un ostacolo (il suo rettangolo): se la palla lo ha preso di lato si inverte la
     * direzione orizzontale (e quella verticale cambia un poco, a caso: DEVIAZIONE_SUGLI_OSTACOLI),
     * se lo ha preso sopra o sotto si inverte quella verticale. Poi la palla viene messa appena fuori dall'ostacolo, dal lato da cui è arrivata, così non lo
     * tocca di nuovo al fotogramma dopo.
     */
    void rimbalzaSu(Rettangolo ostacolo) {
        Rettangolo p = getArea();
        // quanto la palla è entrata nell'ostacolo, in orizzontale e in verticale
        double dentroX = Math.min(p.x() + p.larghezza(), ostacolo.x() + ostacolo.larghezza()) - Math.max(p.x(), ostacolo.x());
        double dentroY = Math.min(p.y() + p.altezza(), ostacolo.y() + ostacolo.altezza()) - Math.max(p.y(), ostacolo.y());
        if (dentroX < dentroY) {
            boolean daSinistra = posizione.x() < ostacolo.centro().x();
            velocitaX = daSinistra ? -Math.abs(velocitaX) : Math.abs(velocitaX);
            velocitaY += (caso.nextDouble() * 2 - 1) * DEVIAZIONE_SUGLI_OSTACOLI;
            double x = daSinistra ? ostacolo.x() - RAGGIO - 0.5 : ostacolo.x() + ostacolo.larghezza() + RAGGIO + 0.5;
            posizione = new Posizione(x, posizione.y());
        } else {
            boolean daSopra = posizione.y() < ostacolo.centro().y();
            velocitaY = daSopra ? -Math.abs(velocitaY) : Math.abs(velocitaY);
            double y = daSopra ? ostacolo.y() - RAGGIO - 0.5 : ostacolo.y() + ostacolo.altezza() + RAGGIO + 0.5;
            posizione = new Posizione(posizione.x(), y);
        }
    }
}
