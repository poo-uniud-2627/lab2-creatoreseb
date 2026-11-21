// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
package pong;

/** Una racchetta: sta a un'altezza fissa in orizzontale e si muove solo in verticale. */
public class Racchetta {
    static final int LARGHEZZA = 12;
    static final int ALTEZZA = 80;
    static final int DISTANZA_BORDO = 30;

    /** Quanti pixel si sposta la racchetta a ogni fotogramma, quando si muove. */
    static final int VELOCITA = 6;

    private final Bordo bordo;
    private int x; // non è final perchè si può muovere in orizzontale
    private double y;

    Racchetta(int x, double y) {
        this.bordo = Bordo.SINISTRA;
        this.x = x;
        this.y = y;
    }

    public Racchetta(Bordo bordo, Campo campo){
        if (bordo == null || campo == null){
            throw new IllegalArgumentException("Gli argomenti non possono essere null");
        }

        this.bordo = bordo;

        if (bordo == Bordo.SINISTRA){
            this.x = DISTANZA_BORDO;
            this.y = (campo.getAltezza() - ALTEZZA) / 2.0;
        } else if (bordo == Bordo.DESTRA){
            this.x = campo.getLarghezza() - DISTANZA_BORDO - LARGHEZZA;
            this.y = (campo.getAltezza() - ALTEZZA) / 2.0;
        } else if (bordo == Bordo.ALTO){
            this.x = (campo.getLarghezza() - ALTEZZA) / 2;
            this.y = DISTANZA_BORDO;
        } else {
            this.x = (campo.getLarghezza() - ALTEZZA) / 2;
            this.y = (campo.getAltezza() - DISTANZA_BORDO - LARGHEZZA);
        }
    }

    // ---- quello che chiunque può chiedere alla racchetta
    public Bordo getBordo(){
        return bordo;
    }

    public int getX() { 
        return x; 
    }

    public double getY() { 
        return y; 
    }

    public double getCentroX() {
        return x + getLarghezza() / 2.0; 
    }

    public double getCentroY() {
        return y + getAltezza() / 2.0; 
    }

    public int getLarghezza() {
        if (bordo.orizzontale()) return ALTEZZA;
        return LARGHEZZA; 
    }

            
    public int getAltezza() {
        if (bordo.orizzontale()) return LARGHEZZA;
        return ALTEZZA; 
    }

    // ---- quello che può chiedere solo il codice del pacchetto pong (la Partita)

    /**
     * Esegue una mossa.
     * Pretende: campo non è null ed è alto almeno quanto la racchetta; la racchetta è dentro il campo.
     * Promette: con SU sale di VELOCITA pixel, con GIU scende, con FERMO (o null) resta dov'è;
     *           alla fine sta tutta dentro il campo.
     */
    void muovi(Mossa mossa, Campo campo) {
        if (bordo.orizzontale()){
            if (mossa == Mossa.SU) x -= VELOCITA;
            if (mossa == Mossa.GIU) x += VELOCITA;
            if (x < 0) x = 0;
            if (x > campo.getLarghezza() - getLarghezza()) x = campo.getLarghezza() - getLarghezza();

        } else {
            if (mossa == Mossa.SU) y -= VELOCITA;
            if (mossa == Mossa.GIU) y += VELOCITA;
            if (y < 0) y = 0;
            if (y > campo.getAltezza() - getAltezza()) y = campo.getAltezza() - getAltezza();
        }
    }
}
