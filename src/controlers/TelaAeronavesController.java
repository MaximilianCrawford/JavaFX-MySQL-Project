/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlers;

import dao.AeronaveDAO;
import entidades.Aeronave;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import jpaControles.exceptions.IllegalOrphanException;
import operadoresNumericos.OperadorNumerico;

public class TelaAeronavesController implements Initializable {

    @FXML
    private Button bInsAviao;
    @FXML
    private Button bRemAviao;
    @FXML
    private TextField txtCodAviao;
    @FXML
    private Button bEdiAviao;
    @FXML
    private TableView<Aeronave> tAvioes;
    @FXML
    private TableColumn<Aeronave, Integer> rCodAviao;
    @FXML
    private TableColumn<Aeronave, String> rModeloAviao;
    @FXML
    private TableColumn<Aeronave, Integer> rAssentosAviao;
    @FXML
    private TableColumn<Aeronave, Integer> rAssentosEsAviao;
    private ObservableList<Aeronave> lstAeronaves;
    private AeronaveDAO banco;
    @FXML
    private AnchorPane pAtributosAviao;
    @FXML
    private Text txtAviso;
    @FXML
    private Button bProAviao;
    @FXML
    private Button bNovoAviao;
    @FXML
    private TextField txtAss;
    @FXML
    private TextField txtAssEspeciais;
    @FXML
    private TextField txtModelo;
    @FXML
    private Text txtEstrutura;
    @FXML
    private Text txtInf;
    @FXML
    private Label txtEsquerdo;
    @FXML
    private GridPane gAviao;
    @FXML
    private ScrollPane spAviao;
    @FXML
    private ImageView imgFim;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane aPrinc;
    private OperadorNumerico campoNumerico;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoNumerico = new OperadorNumerico();
        txtCodAviao.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumerico()));
        txtAss.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumerico()));
        txtAssEspeciais.setTextFormatter(new TextFormatter<>(campoNumerico.fazerCampoNumerico()));
        banco = new AeronaveDAO();
        rCodAviao.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        rModeloAviao.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        rAssentosAviao.setCellValueFactory(new PropertyValueFactory<>("qtdeAssentos"));
        rAssentosEsAviao.setCellValueFactory(new PropertyValueFactory<>("qtdeAssEspeciais"));
        buscarAeronaves();
    }

    //prepara uma representação não interativa da estrutura do avião e atualiza a interface para mostra-la//
    private void prepararEstrutura(int ass, int assEsp) {
        int linhas, assTotais = 0, n, l;
        Image assento = new Image("/telas/imagens/Assento.png");
        Image assentoEsp = new Image("/telas/imagens/AssentoEsp.png");
        gAviao.getChildren().clear();
        linhas = contarLinhas(ass);
        for (n = 0; n < linhas; n++) {
            gAviao.addColumn(0, new ImageView(assento));
            if (assTotais + 8 < ass) {
                gAviao.add(new ImageView(assento), 1, n);
                gAviao.add(new ImageView(assento), 3, n);
                gAviao.add(new ImageView(assento), 4, n);
                gAviao.add(new ImageView(assento), 5, n);
                gAviao.add(new ImageView(assento), 6, n);
                gAviao.add(new ImageView(assento), 8, n);
                gAviao.add(new ImageView(assento), 9, n);
                assTotais += 8;
            }
        }
        for (l = 1; assTotais < ass - 1; l++, assTotais++) {
            if ((l != 2) && (l != 7)) {
                gAviao.add(new ImageView(assento), l, n - 1);
                assTotais++;
            }
        }
        linhas = contarLinhas(assEsp);
        assTotais = 0;
        for (l = 0; l < linhas; l++) {
            gAviao.addColumn(0, new ImageView(assentoEsp));
            if (assTotais + 8 < assEsp) {
                gAviao.add(new ImageView(assentoEsp), 1, l + n);
                gAviao.add(new ImageView(assentoEsp), 3, l + n);
                gAviao.add(new ImageView(assentoEsp), 4, l + n);
                gAviao.add(new ImageView(assentoEsp), 5, l + n);
                gAviao.add(new ImageView(assentoEsp), 6, l + n);
                gAviao.add(new ImageView(assentoEsp), 8, l + n);
                gAviao.add(new ImageView(assentoEsp), 9, l + n);
                assTotais += 8;
            }
        }
        for (int i = 1; assTotais < assEsp - 1; i++) {
            if ((i != 2) && (i != 7)) {
                gAviao.add(new ImageView(assentoEsp), i, l + n - 1);
                assTotais++;
            }
        }
        txtEstrutura.setText("Desenho do avião " + txtCodAviao.getText() + " - " + txtModelo.getText());
        spAviao.setVisible(true);
    }

    //chamada pela prepararEstrutura para contagem de linhas da matriz//
    private int contarLinhas(int a) {
        int l = 0;
        for (int n = 0; n < a; n++) {
            if (n % 8 == 0) {
                l++;
            }
        }
        return l;
    }

    private Boolean consultar() {
        if ("".equals(txtCodAviao.getText())) {
            JOptionPane.showMessageDialog(null,
                    "Informe o código do avião", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
            txtCodAviao.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void buscarAeronaves() {
        try {
            lstAeronaves = FXCollections.observableArrayList(banco.consultar());
            tAvioes.setItems(lstAeronaves);
            tAvioes.refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void abrirPainel(Boolean inserir, Boolean remover, Boolean editar) {
        pAtributosAviao.setVisible(true);
        bInsAviao.setVisible(inserir);
        bRemAviao.setVisible(remover);
        bEdiAviao.setVisible(editar);
    }

    //pergunta ao usuário se deseja remover a aeronave selecionada e efetiva a ação se sim//
    @FXML
    private void remover(ActionEvent event) {
        try {
            String[] escolhas = {"Sim", "Não"};
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Tem certeza que deseja excluir este avião?", "Aviso:",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    escolhas,
                    "Não"
            );
            if (result == JOptionPane.YES_OPTION) {
                banco.excluir(Integer.parseInt(txtCodAviao.getText()));
                buscarAeronaves();
                JOptionPane.showMessageDialog(null,
                        "Avião " + txtCodAviao.getText() + " excluido com sucesso.", "",
                        JOptionPane.PLAIN_MESSAGE);
                txtEsquerdo.setText("Avião " + txtCodAviao.getText() + " excluido com sucesso.");
                pAtributosAviao.setVisible(false);
                spAviao.setVisible(false);
                txtEstrutura.setText("Selecione um avião");
                txtAviso.setText("");
            }
        } catch (IllegalOrphanException ex) {
            JOptionPane.showMessageDialog(null,
                    "Aeronave não pode ser removida pois há voos destinados a ela. Por favor remova os voos dependentes para remove-la.", "",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    //edita a aeronave selecionada//
    @FXML
    private void editar(ActionEvent event) {
        try {
            if (verificarCampos()) {
                Aeronave nova = banco.consultar(Integer.parseInt(txtCodAviao.getText()));
                nova.setModelo(txtModelo.getText());
                nova.setQtdeAssEspeciais(Integer.parseInt(txtAssEspeciais.getText()));
                nova.setQtdeAssentos(Integer.parseInt(txtAss.getText()));
                banco.editar(nova);
                txtEsquerdo.setText("Avião " + txtCodAviao.getText() + " editado com sucesso.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            buscarAeronaves();
        }
    }

    //insere uma nova aeronave após verificar a integridade dos campos//
    @FXML
    private void inserir(ActionEvent event) {
        try {
            if (verificarCampos()) {
                Aeronave nova = new Aeronave();
                nova.setModelo(txtModelo.getText());
                nova.setQtdeAssEspeciais(Integer.parseInt(txtAssEspeciais.getText()));
                nova.setQtdeAssentos(Integer.parseInt(txtAss.getText()));
                banco.inserir(nova);
                txtEsquerdo.setText("Avião modelo " + txtModelo.getText() + " inserido com sucesso.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Campo(s) com valor(es) muito grande. Por favor insira um valor menor.", "Erro:",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            buscarAeronaves();
        }
    }

    //verifica se todos os campos relevantes foram preenchidos, com mensagem relevante para cada um caso não//
    private Boolean verificarCampos() {
        if (!"".equals(txtAss.getText()) && !"".equals(txtAssEspeciais.getText()) && !"".equals(txtModelo.getText())) {
            if (!"0".equals(txtAssEspeciais.getText()) || !"0".equals(txtAss.getText())) {
                if (Integer.parseInt(txtAssEspeciais.getText()) + Integer.parseInt(txtAss.getText()) <= 850) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            (Integer.parseInt(txtAssEspeciais.getText()) + Integer.parseInt(txtAss.getText())) + " ultrapassa o limite de assentos. Por favor informe uma quantidade menor que 850.", "Aviso:",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Avião deve ter pelo menos 1 assento.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Todos os campos devem ser preenchidos.", "Aviso:",
                    JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }

    //encontra a aeronave selecionada no banco e preenche os campos de entrada com os dados dela//
    @FXML
    private void procurar(ActionEvent event) {
        if (consultar()) {
            try {
                Aeronave alvo = banco.consultar(Integer.parseInt(txtCodAviao.getText()));
                if (alvo != null) {
                    txtAviso.setText("Avião com código " + txtCodAviao.getText() + " selecionado.");
                    txtInf.setText("Dados do avião:");
                    txtModelo.setText(alvo.getModelo());
                    txtAss.setText(alvo.getQtdeAssentos().toString());
                    txtAssEspeciais.setText(alvo.getQtdeAssEspeciais().toString());
                    abrirPainel(false, true, true);
                    prepararEstrutura(alvo.getQtdeAssentos(), alvo.getQtdeAssEspeciais());
                } else {
                    txtAviso.setText("Avião com código " + txtCodAviao.getText() + " não encontrado.");
                    pAtributosAviao.setVisible(false);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Registro com valor muito grande. Por favor insira um valor menor.", "Aviso:",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    
    @FXML
    private void novo(ActionEvent event) {
        try {
            txtAviso.setText("Inserindo novo avião.");
            txtInf.setText("Informe os dados do avião:");
            txtModelo.clear();
            txtAss.clear();
            txtAssEspeciais.clear();
            abrirPainel(true, false, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "Erro:",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //seleciona a aeronave a partir do item selecionado na tabela//
    @FXML
    private void selecionarAviao(MouseEvent event) {
        txtCodAviao.setText(tAvioes.getSelectionModel().getSelectedItem().getCodigo().toString());
        procurar(null);
    }
}
