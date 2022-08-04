/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class PassageirosVooPK implements Serializable {

    //chave primária composta de PassageirosVoo//
    @Basic(optional = false)
    @Column(name = "numAssento")
    private int numAssento;
    @Basic(optional = false)
    @Column(name = "codigoVoo")
    private int codigoVoo;
    @Basic(optional = false)
    @Column(name = "numDocumentoPassageiro")
    private String numDocumentoPassageiro;

    public PassageirosVooPK() {
    }

    public PassageirosVooPK(int numAssento, int codigoVoo, String numDocumentoPassageiro) {
        this.numAssento = numAssento;
        this.codigoVoo = codigoVoo;
        this.numDocumentoPassageiro = numDocumentoPassageiro;
    }

    public int getNumAssento() {
        return numAssento;
    }

    public void setNumAssento(int numAssento) {
        this.numAssento = numAssento;
    }

    public int getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(int codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public String getNumDocumentoPassageiro() {
        return numDocumentoPassageiro;
    }

    public void setNumDocumentoPassageiro(String numDocumentoPassageiro) {
        this.numDocumentoPassageiro = numDocumentoPassageiro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numAssento;
        hash += (int) codigoVoo;
        hash += (numDocumentoPassageiro != null ? numDocumentoPassageiro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassageirosVooPK)) {
            return false;
        }
        PassageirosVooPK other = (PassageirosVooPK) object;
        if (this.numAssento != other.numAssento) {
            return false;
        }
        if (this.codigoVoo != other.codigoVoo) {
            return false;
        }
        if ((this.numDocumentoPassageiro == null && other.numDocumentoPassageiro != null) || (this.numDocumentoPassageiro != null && !this.numDocumentoPassageiro.equals(other.numDocumentoPassageiro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numAssento + "." + numDocumentoPassageiro  + "." + codigoVoo;
    }

}
