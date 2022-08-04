/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entidades.PassageirosVoo;
import entidades.PassageirosVooPK;
import java.util.List;
import jpaControles.PassageirosVooJpaController;
import jpaControles.exceptions.NonexistentEntityException;

public class PassageirosVooDAO extends ModeloDAO<PassageirosVoo, PassageirosVooJpaController>

    {
    

    public PassageirosVooDAO() {
        objetoJPA = new PassageirosVooJpaController(getEmf());
    }

    @Override
    public void inserir(PassageirosVoo objeto) throws Exception {
        objetoJPA.create(objeto);
    }

    @Override
    public void editar(PassageirosVoo objeto) throws Exception {
        try {
            objetoJPA.edit(objeto);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + objeto);
        }
    }

    public void excluir(PassageirosVooPK id) throws Exception {
        try {
            objetoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + id);
        }
    }

    @Override
    public void excluir(PassageirosVoo objeto) throws Exception {
        try {
            objetoJPA.destroy(objeto.getPassageirosVooPK());
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco:: " + objeto);
        }
    }

    public PassageirosVoo consultar(PassageirosVooPK id) throws Exception {
        PassageirosVoo pasVoo = null;
        try {
            pasVoo = objetoJPA.findPassageirosVoo(id);
            if (pasVoo == null) {
                throw new NonexistentEntityException("Não existe esta aeronave no banco: " + id);
            }

        } catch (NonexistentEntityException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            return pasVoo;
        }
    }

    public List<PassageirosVoo> consultar() {
        return objetoJPA.findPassageirosVooEntities();
    }
}

