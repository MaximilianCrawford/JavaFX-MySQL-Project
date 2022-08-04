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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "voo")
@NamedQueries({
    @NamedQuery(name = "Voo.findAll", query = "SELECT v FROM Voo v"),
    @NamedQuery(name = "Voo.findByCodigo", query = "SELECT v FROM Voo v WHERE v.codigo = :codigo"),
    @NamedQuery(name = "Voo.findByDataPartida", query = "SELECT v FROM Voo v WHERE v.dataPartida = :dataPartida"),
    @NamedQuery(name = "Voo.findByValorPassagem", query = "SELECT v FROM Voo v WHERE v.valorPassagem = :valorPassagem"),
    @NamedQuery(name = "Voo.findByDataAndDestino", query = "SELECT v FROM Voo v WHERE v.dataPartida = :dataPartida AND v.codAeroportoDestino = :codAeroportoDestino")})
public class Voo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "dataPartida")
    @Temporal(TemporalType.DATE)
    private Date dataPartida;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorPassagem")
    private Double valorPassagem;
    @JoinColumn(name = "codigoAeronave", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Aeronave codigoAeronave;
    @JoinColumn(name = "codAeroportoDestino", referencedColumnName = "codAeroporto")
    @ManyToOne(optional = false)
    private Destino codAeroportoDestino;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voo")
    private Collection<PassageirosVoo> passageirosVooCollection;

    public Voo() {
    }

    public Voo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDataPartida() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(dataPartida);
    }

    public void setDataPartida(Date dataPartida) {
        this.dataPartida = dataPartida;
    }

    public Double getValorPassagem() {
        return valorPassagem;
    }

    public void setValorPassagem(Double valorPassagem) {
        this.valorPassagem = valorPassagem;
    }

    public Aeronave getCodigoAeronave() {
        return codigoAeronave;
    }

    public void setCodigoAeronave(Aeronave codigoAeronave) {
        this.codigoAeronave = codigoAeronave;
    }

    public Destino getCodAeroportoDestino() {
        return codAeroportoDestino;
    }

    public void setCodAeroportoDestino(Destino codAeroportoDestino) {
        this.codAeroportoDestino = codAeroportoDestino;
    }

    public Collection<PassageirosVoo> getPassageirosVooCollection() {
        return passageirosVooCollection;
    }

    public void setPassageirosVooCollection(Collection<PassageirosVoo> passageirosVooCollection) {
        this.passageirosVooCollection = passageirosVooCollection;
    }
    
    public Aeronave consultarAeronavePorData(String data) {
        if(this.getDataPartida().equals(data)) {
            return this.getCodigoAeronave();
        }
        return null;
    }
    
    public Voo consultarVooPorIdDestino(String id) {
        if(this.getCodAeroportoDestino().equals(id)) {
            return this;
        }
        return null;
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
        if (!(object instanceof Voo)) {
            return false;
        }
        Voo other = (Voo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo.toString();
    }

}
