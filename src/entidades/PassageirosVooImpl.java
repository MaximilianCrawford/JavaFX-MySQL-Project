/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package entidades;

import java.util.Date;

public class PassageirosVooImpl {
    
    //campos com nomes referentes a classe PassageirosVoo para melhor transferencia de dados//
    //classe utilizada para população de tabelas com fonte composta//
    private PassageirosVooPK codigoVoo;
    private String dataPartida;
    private int numAssento;
    private int codigoAeronave;
    private String passageiro;
    
    public PassageirosVooImpl() {
        
    }
    
     public PassageirosVooImpl(String dataPartida, int numAssento, int codigoAeronave, String passageiro) {
        this.dataPartida = dataPartida;
        this.numAssento = numAssento;
        this.codigoAeronave = codigoAeronave;
        this.passageiro = passageiro;
    }

    public PassageirosVooImpl(PassageirosVooPK codigoVoo, String dataPartida, int numAssento, int codigoAeronave, String passageiro) {
        this.codigoVoo = codigoVoo;
        this.dataPartida = dataPartida;
        this.numAssento = numAssento;
        this.codigoAeronave = codigoAeronave;
        this.passageiro = passageiro;
    }
    
    

    public PassageirosVooPK getCodigoVoo() {
        return codigoVoo;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public int getNumAssento() {
        return numAssento;
    }

    public int getCodigoAeronave() {
        return codigoAeronave;
    }

    public String getPassageiro() {
        return passageiro;
    }

    public void setCodigoVoo(PassageirosVooPK codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public void setDataPartida(String dataPartida) {
        this.dataPartida = dataPartida;
    }

    public void setNumAssento(int numAssento) {
        this.numAssento = numAssento;
    }

    public void setCodigoAeronave(int codigoAeronave) {
        this.codigoAeronave = codigoAeronave;
    }

    public void setPassageiro(String passageiro) {
        this.passageiro = passageiro;
    }

    

    @Override
    public String toString() {
        return dataPartida + " " + passageiro;
    }

}
