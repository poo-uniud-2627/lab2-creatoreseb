// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 5, variante 3: il Pong su quattro lati. I contratti ci sono, i corpi dei metodi li scrivete voi.
package pong;

/**
 * Su quale bordo del campo gioca un giocatore: è il «dove». Il «chi» resta Lato
 * (SINISTRA è sempre la persona, DESTRA il giocatore automatico, per il Punteggio).
 */
public enum Bordo {
    SINISTRA, DESTRA, ALTO, BASSO;

    /** Vero per ALTO e BASSO: lì la racchetta è orizzontale e si muove lungo x. */
    public boolean orizzontale() {
        throw new UnsupportedOperationException("da scrivere");
    }

    /** Il bordo di fronte: SINISTRA e DESTRA, ALTO e BASSO. */
    public Bordo opposto() {
        throw new UnsupportedOperationException("da scrivere");
    }
}
