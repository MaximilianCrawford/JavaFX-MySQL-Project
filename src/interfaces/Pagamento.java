/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

public interface Pagamento {

    //interface para exibição de tipo de pagamento através do valor selecionado no//
    //grupo de radio buttons na tela de vendas//
    public enum TipoPagamento {
        CARTAO_CREDITO("Cartão de Crédito", 1),
        DINHEIRO("Dinheiro", 2),
        BOLETO("Boleto", 3),
        DEPOSITO("Deposito", 4),
        CONVENIO("Convenio", 5);

        String nome;
        int tipo;

        TipoPagamento(String nome, int valor) {
            this.nome = nome;
            this.tipo = valor;
        }

        public int getTipo() {
            return tipo;
        }

        public String getNome() {
            return nome;
        }
    }

    public TipoPagamento selecionarPagamento();
}
