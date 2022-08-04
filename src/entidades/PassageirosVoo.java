/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import interfaces.Pagamento;
import interfaces.Pagamento.TipoPagamento;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "passageirosvoo")
@NamedQueries({
    @NamedQuery(name = "PassageirosVoo.findAll", query = "SELECT p FROM PassageirosVoo p"),
    @NamedQuery(name = "PassageirosVoo.findByNumAssento", query = "SELECT p FROM PassageirosVoo p WHERE p.passageirosVooPK.numAssento = :numAssento"),
    @NamedQuery(name = "PassageirosVoo.findByCodigoVoo", query = "SELECT p FROM PassageirosVoo p WHERE p.passageirosVooPK.codigoVoo = :codigoVoo"),
    @NamedQuery(name = "PassageirosVoo.findByNumDocumentoPassageiro", query = "SELECT p FROM PassageirosVoo p WHERE p.passageirosVooPK.numDocumentoPassageiro = :numDocumentoPassageiro"),
    @NamedQuery(name = "PassageirosVoo.findBySolicitacoes", query = "SELECT p FROM PassageirosVoo p WHERE p.solicitacoes = :solicitacoes"),
    @NamedQuery(name = "PassageirosVoo.findByTipoAssento", query = "SELECT p FROM PassageirosVoo p WHERE p.tipoAssento = :tipoAssento"),
    @NamedQuery(name = "PassageirosVoo.findByFormaPago", query = "SELECT p FROM PassageirosVoo p WHERE p.formaPago = :formaPago"),
    @NamedQuery(name = "PassageirosVoo.findByValorPago", query = "SELECT p FROM PassageirosVoo p WHERE p.valorPago = :valorPago"),
    @NamedQuery(name = "PassageirosVoo.findOcupacoes", query = "SELECT p.passageirosVooPK.codigoVoo, p.voo.dataPartida, p.passageirosVooPK.numAssento, p.voo.codigoAeronave.codigo, p.passageiro.numDocumento FROM PassageirosVoo p")})
public class PassageirosVoo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PassageirosVooPK passageirosVooPK;
    @Column(name = "solicitacoes")
    private String solicitacoes;
    @Column(name = "tipoAssento")
    private Integer tipoAssento;
    @Column(name = "formaPago")
    private Integer formaPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorPago")
    private Double valorPago;
    @JoinColumn(name = "numDocumentoPassageiro", referencedColumnName = "numDocumento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Passageiro passageiro;
    @JoinColumn(name = "codigoVoo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Voo voo;

    public PassageirosVoo() {
    }

    public PassageirosVoo(PassageirosVooPK passageirosVooPK) {
        this.passageirosVooPK = passageirosVooPK;
    }

    public PassageirosVoo(int numAssento, int codigoVoo, String numDocumentoPassageiro) {
        this.passageirosVooPK = new PassageirosVooPK(numAssento, codigoVoo, numDocumentoPassageiro);
    }

    public PassageirosVoo(PassageirosVooPK passageirosVooPK, String solicitacoes, Integer tipoAssento, Integer formaPago, Double valorPago, Passageiro passageiro, Voo voo) {
        this.passageirosVooPK = passageirosVooPK;
        this.solicitacoes = solicitacoes;
        this.tipoAssento = tipoAssento;
        this.formaPago = formaPago;
        this.valorPago = valorPago;
        this.passageiro = passageiro;
        this.voo = voo;
    }

    public PassageirosVooPK getPassageirosVooPK() {
        return passageirosVooPK;
    }

    public void setPassageirosVooPK(PassageirosVooPK passageirosVooPK) {
        this.passageirosVooPK = passageirosVooPK;
    }

    public String getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(String solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public Integer getTipoAssento() {
        return tipoAssento;
    }

    public void setTipoAssento(Integer tipoAssento) {
        this.tipoAssento = tipoAssento;
    }

    public Integer getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(Integer formaPago) {
        this.formaPago = formaPago;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }
    
    public TipoPagamento getTipoPagamento() {
        return Pagamento.TipoPagamento.values()[this.getFormaPago()];
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passageirosVooPK != null ? passageirosVooPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassageirosVoo)) {
            return false;
        }
        PassageirosVoo other = (PassageirosVoo) object;
        if ((this.passageirosVooPK == null && other.passageirosVooPK != null) || (this.passageirosVooPK != null && !this.passageirosVooPK.equals(other.passageirosVooPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return passageirosVooPK.toString();
    }

}
