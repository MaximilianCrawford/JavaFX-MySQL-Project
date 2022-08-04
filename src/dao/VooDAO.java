/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entidades.Voo;
import java.util.ArrayList;
import java.util.List;
import jpaControles.VooJpaController;
import jpaControles.exceptions.NonexistentEntityException;

public class VooDAO extends ModeloDAO<Voo, VooJpaController> {

    public VooDAO() {
        objetoJPA = new VooJpaController(getEmf());
    }

    @Override
    public void inserir(Voo objeto) throws Exception {
        objetoJPA.create(objeto);
    }

    @Override
    public void editar(Voo objeto) throws Exception {
        try {
            objetoJPA.edit(objeto);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este voo no banco: " + objeto);
        }
    }

    public void excluir(Integer id) throws Exception {
        try {
            objetoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este voo no banco: " + id);
        }
    }

    @Override
    public void excluir(Voo objeto) throws Exception {
        try {
            objetoJPA.destroy(objeto.getCodigo());
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este voo no banco:: " + objeto);
        }
    }

    public Voo consultar(Integer id) throws Exception {
        Voo voo = null;
        try {
            voo = objetoJPA.findVoo(id);
            if (voo == null) {
                throw new NonexistentEntityException("Não existe este voo no banco: " + id);
            }

        } catch (NonexistentEntityException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            return voo;
        }
    }

    public List<Voo> consultar() {
        return objetoJPA.findVooEntities();
    }

    public Voo consultarPorDestino(String id) {
        for(Voo voo : objetoJPA.findVooEntities()) {
            if(voo.consultarVooPorIdDestino(id) != null) {
                return voo;
            }
        }
        return null;
    }

    public List<String> consultarDatas() {
        List<String> datas = new ArrayList<>();
        for (Voo v : this.consultar()) {
            datas.add(v.getDataPartida().toString());
        }
        return datas;
    }
}
