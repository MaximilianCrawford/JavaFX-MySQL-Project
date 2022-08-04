/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public abstract class ModeloDAO<To, Tj> {

    protected EntityManagerFactory emf;
    protected Tj objetoJPA;

    public ModeloDAO() {
        emf = Persistence.createEntityManagerFactory("Persistence");
    }

    public abstract void inserir(To objeto)throws Exception;
//        objetoJPA.create(objeto);

    public abstract void editar(To objeto) throws Exception;
//        try {
//            objetoJPA.edit(objeto);
//        } catch (NonexistentEntityException ex) {
//            throw new Exception("Não existe esta venda no banco: " + objeto);
//        }

//    public abstract void excluir(var id) throws Exception;
//        try {
//            objetoJPA.destroy(id);
//        } catch (NonexistentEntityException ex) {
//            throw new Exception("Não existe esta venda no banco: " + id);
//        }
//    }

    public abstract void excluir(To objeto) throws Exception;
//        try {
//            objetoJPA.destroy(objeto.getIdVenda());
//        } catch (NonexistentEntityException ex) {
//            throw new Exception("Não existe esta venda no banco: " + objeto);
//        }
//    }

//    public abstract To consultar(Integer id) throws Exception;

    /**
     * @return the emf
     */
    public EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * @param emf the emf to set
     */
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

}
