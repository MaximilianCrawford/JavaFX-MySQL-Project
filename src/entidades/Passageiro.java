/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "passageiro")
@NamedQueries({
    @NamedQuery(name = "Passageiro.findAll", query = "SELECT p FROM Passageiro p"),
    @NamedQuery(name = "Passageiro.findByNumDocumento", query = "SELECT p FROM Passageiro p WHERE p.numDocumento = :numDocumento"),
    @NamedQuery(name = "Passageiro.findByNome", query = "SELECT p FROM Passageiro p WHERE p.nome = :nome"),
    @NamedQuery(name = "Passageiro.findByDataNascimento", query = "SELECT p FROM Passageiro p WHERE p.dataNascimento = :dataNascimento")})
public class Passageiro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "numDocumento")
    private String numDocumento;
    @Column(name = "nome")
    private String nome;
    @Column(name = "dataNascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "passageiro")
    private Collection<PassageirosVoo> passageirosVooCollection;

    public Passageiro() {
    }

    public Passageiro(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Passageiro(String numDocumento, String nome, Date dataNascimento) {
        this.numDocumento = numDocumento;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
    
    

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(dataNascimento);
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Collection<PassageirosVoo> getPassageirosVooCollection() {
        return passageirosVooCollection;
    }

    public void setPassageirosVooCollection(Collection<PassageirosVoo> passageirosVooCollection) {
        this.passageirosVooCollection = passageirosVooCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numDocumento != null ? numDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Passageiro)) {
            return false;
        }
        Passageiro other = (Passageiro) object;
        if ((this.numDocumento == null && other.numDocumento != null) || (this.numDocumento != null && !this.numDocumento.equals(other.numDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nome + " - " + numDocumento;
    }

}
