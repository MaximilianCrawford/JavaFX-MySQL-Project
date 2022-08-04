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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "aeronave")
@NamedQueries({
    @NamedQuery(name = "Aeronave.findAll", query = "SELECT a FROM Aeronave a"),
    @NamedQuery(name = "Aeronave.findByCodigo", query = "SELECT a FROM Aeronave a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "Aeronave.findByModelo", query = "SELECT a FROM Aeronave a WHERE a.modelo = :modelo"),
    @NamedQuery(name = "Aeronave.findByQtdeAssentos", query = "SELECT a FROM Aeronave a WHERE a.qtdeAssentos = :qtdeAssentos"),
    @NamedQuery(name = "Aeronave.findByQtdeAssEspeciais", query = "SELECT a FROM Aeronave a WHERE a.qtdeAssEspeciais = :qtdeAssEspeciais")})
public class Aeronave implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "qtdeAssentos")
    private Integer qtdeAssentos;
    @Column(name = "qtdeAssEspeciais")
    private Integer qtdeAssEspeciais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoAeronave")
    private Collection<Voo> vooCollection;

    public Aeronave() {
    }

    public Aeronave(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getQtdeAssentos() {
        return qtdeAssentos;
    }

    public void setQtdeAssentos(Integer qtdeAssentos) {
        this.qtdeAssentos = qtdeAssentos;
    }

    public Integer getQtdeAssEspeciais() {
        return qtdeAssEspeciais;
    }

    public void setQtdeAssEspeciais(Integer qtdeAssEspeciais) {
        this.qtdeAssEspeciais = qtdeAssEspeciais;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aeronave)) {
            return false;
        }
        Aeronave other = (Aeronave) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.codigo.toString() + " - " + this.modelo;
    }

}
