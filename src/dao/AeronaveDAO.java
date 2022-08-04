/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import entidades.Aeronave;
import java.util.List;
import jpaControles.AeronaveJpaController;
import jpaControles.exceptions.NonexistentEntityException;


public class AeronaveDAO extends ModeloDAO<Aeronave, AeronaveJpaController>{
    public AeronaveDAO() {
        objetoJPA = new AeronaveJpaController(getEmf());
    }

    @Override
    public void inserir(Aeronave objeto) throws Exception {
        objetoJPA.create(objeto);
    }

    @Override
    public void editar(Aeronave objeto) throws Exception {
        try {
            objetoJPA.edit(objeto);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + objeto);
        }
    }

    public void excluir(Integer id) throws Exception {
        try {
            objetoJPA.destroy(id);
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco: " + id);
        }
    }

    @Override
    public void excluir(Aeronave objeto) throws Exception {
        try {
            objetoJPA.destroy(objeto.getCodigo());
        } catch (NonexistentEntityException ex) {
            throw new Exception("Não existe esta aeronave no banco:: " + objeto);
        }
    }

    public Aeronave consultar(Integer id) throws Exception {
        Aeronave aeronave = null;
        try {
            aeronave = objetoJPA.findAeronave(id);
            if (aeronave == null) {
                throw new NonexistentEntityException("Não existe esta aeronave no banco: " + id);
            }

        } catch (NonexistentEntityException ex) {
            throw new Exception(ex.getMessage());
        } finally {
            return aeronave;
        }
    }
    
    public List<Aeronave> consultar() {
       return objetoJPA.findAeronaveEntities();
   }
}
