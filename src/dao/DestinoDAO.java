/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import entidades.Destino;
import java.util.ArrayList;
import java.util.List;
import jpaControles.DestinoJpaController;
import jpaControles.exceptions.NonexistentEntityException;


public class DestinoDAO extends ModeloDAO<Destino, DestinoJpaController>{
    public DestinoDAO() {
        objetoJPA = new DestinoJpaController(getEmf());
    }

    @Override
    public void inserir(Destino objeto) throws Exception {
        objetoJPA.create(objeto);
    }

    @Override
    public void editar(Destino objeto) throws Exception {
        try {
            objetoJPA.edit(objeto);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este destino no banco: " + objeto);
        }
    }

    public void excluir(String id) throws Exception {
        try {
            objetoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este destino no banco: " + id);
        }
    }

    @Override
    public void excluir(Destino objeto) throws Exception {
        try {
            objetoJPA.destroy(objeto.getCodAeroporto());
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe este destino no banco:: " + objeto);
        }
    }

    public Destino consultar(String id) throws Exception {
        Destino destino = null;
        try {
            destino = objetoJPA.findDestino(id);
            if (destino == null) {
                throw new NonexistentEntityException("Não existe este destino no banco: " + id);
            }

        } catch (NonexistentEntityException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            return destino;
        }
    }
    
    public List<Destino> consultar() {
       return objetoJPA.findDestinoEntities();
   }
    
    public List<String> consultarNomes () {
        List<String> nomes = new ArrayList<>();
        for(Destino d : this.consultar()) {
            nomes.add(d.getNomeAeroporto());
        }
        return nomes;
    }
}
