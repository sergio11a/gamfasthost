package info.gam.gamfasthost;
public class Persone{
    private String id_persona;
    private String nominativo_dipendente;


    public Persone(String id_persona, String nominativo_dipendente) {
        setId(id_persona);
        setNominativoDipendente(nominativo_dipendente);
    }
    public void setId(String id_persona) {
        this.id_persona = id_persona;
    }
    public void setNominativoDipendente(String nominativo_dipendente) {
        this.nominativo_dipendente = nominativo_dipendente;
    }


    public String getId() {
        return this.id_persona;
    }
    public String getNominativoDipendente() {
        return this.nominativo_dipendente;
    }

}
