/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.DestinoDAO;
import dao.PassageiroDAO;
import dao.PassageirosVooDAO;
import dao.VooDAO;
import entidades.Aeronave;
import entidades.Destino;
import entidades.Passageiro;
import entidades.PassageirosVoo;
import entidades.PassageirosVooPK;
import entidades.Voo;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import operadoresNumericos.OperadorNumerico;

public class TelaVendasController implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane spAviao;
    @FXML
    private GridPane gAviao;
    @FXML
    private ImageView imgFim;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private Text txtEstrutura;
    @FXML
    private TextField txtCPF;
    @FXML
    private TextField txtNome;
    @FXML
    private DatePicker datNas;
    @FXML
    private RadioButton rCartao;
    @FXML
    private ToggleGroup pagamento;
    @FXML
    private RadioButton rDinheiro;
    @FXML
    private RadioButton rBoleto;
    @FXML
    private RadioButton rDeposito;
    @FXML
    private RadioButton rConvenio;
    @FXML
    private TextArea tfSolic;
    @FXML
    private ComboBox<Passageiro> cbPassageiros;
    @FXML
    private ComboBox<String> cbDestino;
    @FXML
    private ComboBox<String> cbData;
    private VooDAO bancoVoo;
    private DestinoDAO bancoDestino;
    private PassageiroDAO bancoPassageiro;
    private PassageirosVooDAO bancoPassageirosVoo;
    private ObservableList<String> lstVoos;
    private ObservableList<String> lstDestinos;
    private ObservableList<Passageiro> lstPassageiro;
    private List<Integer[]> lstAssentos;
    @FXML
    private Text txtValor;
    private Image assento;
    private Image assentoEsp;
    private int pagamentoTipo;
    @FXML
    private Button bAlterar;
    @FXML
    private AnchorPane aPassageiro;
    @FXML
    private Button bNovo;
    @FXML
    private Label txtSelAssentos;
    @FXML
    private Text txtAvisoAssent;
    @FXML
    private Button bRemover;
    private OperadorNumerico campoNumerico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNumerico = new OperadorNumerico();
        lstAssentos = new ArrayList<>();
        txtCPF.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumericoCpf()));
        bancoVoo = new VooDAO();
        bancoDestino = new DestinoDAO();
        bancoPassageiro = new PassageiroDAO();
        bancoPassageirosVoo = new PassageirosVooDAO();
        assento = new Image("/telas/imagens/Assento.png");
        assentoEsp = new Image("/telas/imagens/AssentoEsp.png");
        buscarPassageiros();
        buscarDestinos();
        buscarVoos();
    }

    //altera os dados do passageiro selecionado//
    @FXML
    private void alterar(ActionEvent event) {
        try {
            if (!"".equals(txtNome.getText()) && (datNas.getValue() != null)) {
                Passageiro alvo = new Passageiro(txtCPF.getText(), txtNome.getText(), java.sql.Date.valueOf(datNas.getValue()));
                alvo.setPassageirosVooCollection(bancoPassageiro.consultar(txtCPF.getText()).getPassageirosVooCollection());
                bancoPassageiro.editar(alvo);
                txtEsquerdo.setText("Passageiro " + alvo.getNumDocumento() + " editado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Edite os campos com valores válidos.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
                txtCPF.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Nome muito grande. Por favor diminua o tamanho do campo.", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /*
    efetiva a venda no banco de dados, criando o passageiro caso já não exista e uma instância de PassageirosVoo para 
    cada assento escolhido. Verifica a integridade dos campos e informa o valor total a ser pago
    */
    @FXML
    private void submeter(ActionEvent event) {
        try {
            if (validarCampos()) {
                Double valorTotal = 0.0;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Passageiro passageiro = new Passageiro(txtCPF.getText(), txtNome.getText(), Date.from(datNas.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                Destino des = (Destino) bancoDestino.getEmf().createEntityManager().createNamedQuery("Destino.findByNomeAeroporto")
                        .setParameter("nomeAeroporto", cbDestino.getValue())
                        .getSingleResult();
                Voo voo = (Voo) bancoVoo.getEmf().createEntityManager().createNamedQuery("Voo.findByDataAndDestino")
                        .setParameter("dataPartida", Date.from(formatter.parse(cbData.getValue().substring(0, 10)).toInstant()))
                        .setParameter("codAeroportoDestino", des)
                        .getSingleResult();
                if (bancoPassageiro.consultar(passageiro.getNumDocumento()) == null) {
                    bancoPassageiro.inserir(passageiro);
                }
                for (Integer[] selecionado : lstAssentos) {
                    PassageirosVoo ocupacao = new PassageirosVoo(new PassageirosVooPK(selecionado[0], voo.getCodigo(), passageiro.getNumDocumento()), tfSolic.getText(), selecionado[1], pagamentoTipo, Double.parseDouble(String.format("%.2f", voo.getValorPassagem() + (voo.getValorPassagem() / 100) * des.getTaxaEmbarque()).replace(",", ".")), passageiro, voo);
                    valorTotal += voo.getValorPassagem() + (voo.getValorPassagem() / 100) * des.getTaxaEmbarque();
                    bancoPassageirosVoo.inserir(ocupacao);
                }
                txtValor.setText("Valor total: " + String.format("%.2f", valorTotal) + " Reais.");
                lstAssentos.clear();
                montarAviao(null);
                buscarPassageiros();
                txtEsquerdo.setText("Compra de " + String.format("%.2f", valorTotal) + " reais por " + passageiro.getNome() + " realizada com sucesso.");
            }
        } catch (Exception ex) {
            if (!ex.getMessage().contains("already exists")) {
                JOptionPane.showMessageDialog(null,
                        "Nome e/ou Solicitações muito grande. Por favor diminua o tamanho dos campos." + ex.getMessage(), "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Por favor selecione apenas assentos disponíveis.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
                lstAssentos.clear();
                gAviao.getChildren().forEach((t) -> {
                    t.setOpacity(1);
                });
            }
        }
    }

    //verifica a integridade dos campos e exibe mensagens relevantes// 
    private boolean validarCampos() {
        if (txtCPF.getText().length() == 11) {
            if (!"".equals(txtNome.getText()) && (datNas.getValue() != null) && (pagamento.getSelectedToggle() != null) && (!"".equals(cbData.getValue()) && !"".equals(cbDestino.getValue()))) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Preencha todos os campos.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
                txtCPF.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Complete o campo de CPF.", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
            txtCPF.requestFocus();
        }
        return false;
    }

    private void buscarPassageiros() {
        try {
            lstPassageiro = FXCollections.observableArrayList(bancoPassageiro.consultar());
            cbPassageiros.setItems(lstPassageiro);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void buscarVoos() {
        try {
            lstVoos = FXCollections.observableArrayList(bancoVoo.consultarDatas());
            cbData.setItems(lstVoos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void buscarDestinos() {
        try {
            lstDestinos = FXCollections.observableArrayList(bancoDestino.consultarNomes());
            cbDestino.setItems(lstDestinos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //popula o combo box de datas a partir do destino selecionado//
    @FXML
    private void filtrarData(ActionEvent event) {
        Boolean matches = false;
        lstVoos.clear();
        try {
            for (Voo v : bancoVoo.consultar()) {
                if (v.getCodAeroportoDestino().getNomeAeroporto().equals(cbDestino.getValue())) {
                    matches = true;
                }
                if (matches) {
                    lstVoos.add(v.getDataPartida() + " - " + v.getCodigoAeronave().getModelo() + "-" + v.getCodigoAeronave().getCodigo());
                }
                matches = false;
            }
            cbData.setItems(lstVoos);
            if (lstVoos.isEmpty()) {
                cbData.setDisable(true);
            } else {
                cbData.setDisable(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //encontra a aeronave utilizado no voo e chama métodos para exibição do mesmo//
    @FXML
    private void montarAviao(ActionEvent event) {
        List<Voo> atual = bancoVoo.consultar();
        List<Integer> ocupados = new ArrayList<>();
        Aeronave alvo;
        if (cbData.getValue() != null) {
            for (Voo v : atual) {
                if (v.consultarAeronavePorData(cbData.getValue().substring(0, 10)) != null) {
                    alvo = v.consultarAeronavePorData(cbData.getValue().substring(0, 10));
                    for (PassageirosVoo p : v.getPassageirosVooCollection()) {
                        ocupados.add(p.getPassageirosVooPK().getNumAssento());
                    }
                    prepararEstrutura(alvo, ocupados);
                    break;
                }
            }
        } else {
            spAviao.setVisible(false);
        }
    }

    //prepara o desenho interagivel do avião passado por parametro, utilizando a lista de//
    //Integers também passada por parametro para verificar a disponibilidade dos assentos.//
    //Por fim exibe o desenho e informa o usúario qual aeronave está sendo utilizada//
    private void prepararEstrutura(Aeronave alvo, List<Integer> ocupados) {
        int linhas, assTotais = 0, n, l, assentoAtual = 0;
        gAviao.getChildren().clear();
        linhas = contarLinhas(alvo.getQtdeAssentos());
        for (n = 0; n < linhas; n++) {
            if (!ocupados.contains(assentoAtual)) {
                gAviao.addColumn(0, criarAssento(false, false, assentoAtual));
            } else {
                gAviao.addColumn(0, criarAssento(false, true, assentoAtual));
            }
            assentoAtual++;
            if (assTotais + 8 < alvo.getQtdeAssentos()) {
                for (int t = 1; t < 10; t++) {
                    if ((t != 2) && (t != 7)) {
                        if (!ocupados.contains(assentoAtual)) {
                            gAviao.add(criarAssento(false, false, assentoAtual), t, n);
                        } else {
                            gAviao.add(criarAssento(false, true, assentoAtual), t, n);
                        }
                        assentoAtual++;
                    }
                }
                assTotais += 8;
            }
        }

        for (l = 1; assTotais < alvo.getQtdeAssentos() - 1; l++, assTotais++) {
            if ((l != 2) && (l != 7)) {
                if (!ocupados.contains(assentoAtual)) {
                    gAviao.add(criarAssento(false, false, assentoAtual), l, n - 1);
                } else {
                    gAviao.add(criarAssento(false, true, assentoAtual), l, n - 1);
                }
                assTotais++;
                assentoAtual++;
            }
        }
        linhas = contarLinhas(alvo.getQtdeAssEspeciais());
        assTotais = 0;
        for (l = 0; l < linhas; l++) {
            if (!ocupados.contains(assentoAtual)) {
                gAviao.addColumn(0, criarAssento(true, false, assentoAtual));
            } else {
                gAviao.addColumn(0, criarAssento(true, true, assentoAtual));
            }
            assentoAtual++;
            if (assTotais + 8 < alvo.getQtdeAssEspeciais()) {
                for (int t = 1; t < 10; t++) {
                    if ((t != 2) && (t != 7)) {
                        if (!ocupados.contains(assentoAtual)) {
                            gAviao.add(criarAssento(true, false, assentoAtual), t, l + n);
                        } else {
                            gAviao.add(criarAssento(true, true, assentoAtual), t, l + n);
                        }
                        assentoAtual++;
                    }
                }
                assTotais += 8;
            }
        }
        for (int i = 1; assTotais < alvo.getQtdeAssEspeciais() - 1; i++) {
            if ((i != 2) && (i != 7)) {
                if (!ocupados.contains(assentoAtual)) {
                    gAviao.add(criarAssento(true, false, assentoAtual), i, l + n - 1);
                } else {
                    gAviao.add(criarAssento(true, true, assentoAtual), i, l + n - 1);
                }
                assentoAtual++;
                assTotais++;
            }
        }
        txtEstrutura.setText("Desenho do avião: " + alvo.getModelo() + " - " + alvo.getCodigo());
        spAviao.setVisible(true);
        txtAvisoAssent.setVisible(true);
        txtSelAssentos.setVisible(true);
    }

    //utilizado para instanciar ImageViews como assentos a ser exibidos na aeronave.//
    //define as propriedades do assento a ser devolvido a partir de booleans//
    private ImageView criarAssento(Boolean especial, Boolean ocupado, int id) {
        ImageView view;
        if (especial) {
            view = new ImageView(assentoEsp);
        } else {
            view = new ImageView(assento);
        }
        if (ocupado) {
            view.setOpacity(0.1);
        }
        view.setId(Integer.toString(id));
        return view;
    }

    //utilizado na contagem de linhas na matriz de assentos//
    private int contarLinhas(int a) {
        int l = 0;
        for (int n = 0; n < a; n++) {
            if (n % 8 == 0) {
                l++;
            }
        }
        return l;
    }

    //adiciona o assento selecionado a lista de assentos, ou o remove se esta atualmente selecionado.//
    //altera a imagem a ser exibida pela ImageView do assento para refletir o seu estado//
    @FXML
    private void selecionarAssento(MouseEvent event) {
        if ((event.getButton() == MouseButton.SECONDARY) && (event.getPickResult().getIntersectedNode().getClass() == ImageView.class)) {
            ImageView assent = (ImageView) event.getPickResult().getIntersectedNode();
            Integer[] coordenadas = new Integer[2];
            Image assSel = new Image("/telas/imagens/AssentoSel.png");
            Image assSelEsp = new Image("/telas/imagens/AssentoSelEsp.png");
            coordenadas[0] = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());
            if ((assent.getOpacity() == 1)) {
                if (assent.getImage() == criarAssento(true, false, Integer.parseInt(event.getPickResult().getIntersectedNode().getId())).getImage()) {
                    coordenadas[1] = 1;
                    assent.setImage(assSelEsp);
                } else {
                    coordenadas[1] = 0;
                    assent.setImage(assSel);
                }
                lstAssentos.add(coordenadas);
                assent.setOpacity(0.9);
            } else if (assent.getOpacity() == 0.9) {
                lstAssentos.removeIf((t) -> (t[0] == coordenadas[0]));
                assent.setOpacity(1);
                if (assent.getImage().getUrl().equals(assSelEsp.getUrl())) {
                    assent.setImage(assentoEsp);
                } else {
                    assent.setImage(assento);
                }
            }
        }
    }

    //recebe qual tipo de pagamento esta selecionado e atribui um valor referente//
    //ao seu tipo na Enum presente na Interface Pagamentos para pagamentoTipo//
    @FXML
    private void definirPagamento(ActionEvent event) {
        if (event.getSource().equals(rBoleto)) {
            pagamentoTipo = 3;
        } else if (event.getSource().equals(rCartao)) {
            pagamentoTipo = 1;
        } else if (event.getSource().equals(rConvenio)) {
            pagamentoTipo = 5;
        } else if (event.getSource().equals(rDeposito)) {
            pagamentoTipo = 4;
        } else {
            pagamentoTipo = 2;
        }
    }

    //recebe o passageiro selecionado e preenche os campos com os seus dados, removendo//
    //a possibilidade de alteração do CPF por ser chave primária no banco//
    @FXML
    private void selecionarPassageiro(ActionEvent event) {
        if (cbPassageiros.getValue() != null) {
            Passageiro alvo = cbPassageiros.getValue();
            txtCPF.setText(alvo.getNumDocumento());
            txtNome.setText(alvo.getNome());
            datNas.setValue(LocalDate.parse(alvo.getDataNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            bAlterar.setDisable(false);
            bRemover.setDisable(false);
            txtCPF.setDisable(true);
            aPassageiro.setVisible(true);
        }
    }

    //reseta os campos a serem preenchidos para criar um novo passageiro//
    @FXML
    private void novo(ActionEvent event) {
        txtCPF.clear();
        txtNome.clear();
        datNas.setValue(null);
        bAlterar.setDisable(true);
        bRemover.setDisable(true);
        txtCPF.setDisable(false);
        aPassageiro.setVisible(true);
    }

    //pergunta ao usuário se deseja remover o passageiro selecionado e efetiva a ação se sim//
    @FXML
    private void remover(ActionEvent event) {
        String[] escolhas = {"Sim", "Não"};
        Passageiro alvo = cbPassageiros.getValue();
        int result = JOptionPane.showOptionDialog(
                null,
                "Tem certeza que deseja excluir este passageiro?", "Aviso:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                escolhas,
                "Não"
        );
        if (result == JOptionPane.YES_OPTION) {
            try {
                bancoPassageiro.excluir(alvo);
                buscarPassageiros();
                JOptionPane.showMessageDialog(null,
                        "Passageiro " + alvo.getNumDocumento() + " excluido com sucesso.", "",
                        JOptionPane.PLAIN_MESSAGE);
                txtEsquerdo.setText("Passageiro " + alvo.getNumDocumento() + " excluido com sucesso.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Não é possivel excluir este passageiro pois há ocupações para ele. Por favor remova elas antes de remove-lo.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

    }

}
