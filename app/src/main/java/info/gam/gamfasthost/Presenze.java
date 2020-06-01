package info.gam.gamfasthost;

public class Presenze{
    private String id_presenza;
    private String nominativo_visitatore;
    private String azienda;
    private String nominativo_dipendente;
    private String entrata;
    private String stato_presenza;

    public Presenze(String id_presenza, String nominativo_visitatore, String azienda, String nominativo_dipendente, String entrata, String stato_presenza) {
        setIdPresenza(id_presenza);
        setNominativoVisitatore(nominativo_visitatore);
        setAzienda(azienda);
        setNominativoDipendente(nominativo_dipendente);
        setEntrata(entrata);
        setStato_presenza( stato_presenza );
    }
    public void setIdPresenza(String id_presenza) {
        this.id_presenza = id_presenza;
    }
    public void setNominativoVisitatore(String nominativo_visitatore) {
        this.nominativo_visitatore = nominativo_visitatore;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public void setNominativoDipendente(String nominativo_dipendente) {
        this.nominativo_dipendente = nominativo_dipendente;
    }

    public void setEntrata(String entrata) {
        this.entrata = entrata;
    }

    public void setStato_presenza(String stato_presenza) { this.stato_presenza = stato_presenza; }

    public String getId_presenza() {
        return this.id_presenza;
    }
    public String getNominativoVisitatore() {
        return this.nominativo_visitatore;
    }

    public String getAzienda() {
        return this.azienda;
    }

    public String getNominativoDipendente() {
        return this.nominativo_dipendente;
    }

    public String getEntrata() {
        return this.entrata;
    }

    public String getStato_presenza() { return this.stato_presenza; }
}
