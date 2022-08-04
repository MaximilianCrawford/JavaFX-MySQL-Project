/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "destino")
@NamedQueries({
    @NamedQuery(name = "Destino.findAll", query = "SELECT d FROM Destino d"),
    @NamedQuery(name = "Destino.findByCodAeroporto", query = "SELECT d FROM Destino d WHERE d.codAeroporto = :codAeroporto"),
    @NamedQuery(name = "Destino.findByNomeAeroporto", query = "SELECT d FROM Destino d WHERE d.nomeAeroporto = :nomeAeroporto"),
    @NamedQuery(name = "Destino.findByTaxaEmbarque", query = "SELECT d FROM Destino d WHERE d.taxaEmbarque = :taxaEmbarque")})
public class Destino implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codAeroporto")
    private String codAeroporto;
    @Column(name = "nomeAeroporto")
    private String nomeAeroporto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxaEmbarque")
    private Double taxaEmbarque;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codAeroportoDestino")
    private Collection<Voo> vooCollection;

    public Destino() {
    }

    public Destino(String codAeroporto) {
        this.codAeroporto = codAeroporto;
    }

    public String getCodAeroporto() {
        return codAeroporto;
    }

    public void setCodAeroporto(String codAeroporto) {
        this.codAeroporto = codAeroporto;
    }

    public String getNomeAeroporto() {
        return nomeAeroporto;
    }

    public void setNomeAeroporto(String nomeAeroporto) {
        this.nomeAeroporto = nomeAeroporto;
    }

    public Double getTaxaEmbarque() {
        return taxaEmbarque;
    }

    public void setTaxaEmbarque(Double taxaEmbarque) {
        this.taxaEmbarque = taxaEmbarque;
    }

    public Collection<Voo> getVooCollection() {
        return vooCollection;
    }

    public void setVooCollection(Collection<Voo> vooCollection) {
        this.vooCollection = vooCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAeroporto != null ? codAeroporto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Destino)) {
            return false;
        }
        Destino other = (Destino) object;
        if ((this.codAeroporto == null && other.codAeroporto != null) || (this.codAeroporto != null && !this.codAeroporto.equals(other.codAeroporto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codAeroporto;
    }

}
