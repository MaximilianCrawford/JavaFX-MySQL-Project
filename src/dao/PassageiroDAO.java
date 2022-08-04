/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entidades.Passageiro;
import java.util.List;
import jpaControles.PassageiroJpaController;
import jpaControles.exceptions.NonexistentEntityException;

public class PassageiroDAO extends ModeloDAO<Passageiro, PassageiroJpaController> {

    public PassageiroDAO() {
        objetoJPA = new PassageiroJpaController(getEmf());
    }

    @Override
    public void inserir(Passageiro objeto) throws Exception {
        objetoJPA.create(objeto);
    }

    @Override
    public void editar(Passageiro objeto) throws Exception {
        try {
            objetoJPA.edit(objeto);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + objeto);
        }
    }

    public void excluir(String id) throws Exception {
        try {
            objetoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + id);
        }
    }

    @Override
    public void excluir(Passageiro objeto) throws Exception {
        try {
            objetoJPA.destroy(objeto.getNumDocumento());
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco:: " + objeto);
        }
    }

    public Passageiro consultar(String id) throws Exception {
        Passageiro passageiro = null;
        try {
            passageiro = objetoJPA.findPassageiro(id);
            if (passageiro == null) {
                throw new NonexistentEntityException("Não existe esta aeronave no banco: " + id);
            }

        } catch (NonexistentEntityException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            return passageiro;
        }
    }

    public List<Passageiro> consultar() {
        return objetoJPA.findPassageiroEntities();
    }
}
