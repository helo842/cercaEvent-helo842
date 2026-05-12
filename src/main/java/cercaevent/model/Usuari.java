package cercaevent.model;
import java.sql.Timestamp;
public class Usuari {
    private int id;
    private String usuari;
    private String nom;
    private String cognoms;
    private String email;
    private String passwordHash;
    private Timestamp dataRegistre;
    private String rol;

    public Usuari() {
    }

    public Usuari(String usuari, String nom, String cognoms, String email, String passwordHash, String rol) {
        this.usuari = usuari;
        this.nom = nom;
        this.cognoms = cognoms;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getUsuari() {
        return usuari;
    }

    public String getNom() {
        return nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Timestamp getDataRegistre() {
        return dataRegistre;
    }

    public String getRol() {
        return rol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setDataRegistre(java.sql.Timestamp dataRegistre) {
        this.dataRegistre = dataRegistre;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuari{" +
                "id=" + id +
                ", usuari='" + usuari + '\'' +
                ", nom='" + nom + '\'' +
                ", cognoms='" + cognoms + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", dataRegistre=" + dataRegistre +
                ", rol='" + rol + '\'' +
                '}';
    }

}