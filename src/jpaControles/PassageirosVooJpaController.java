/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package jpaControles;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Passageiro;
import entidades.PassageirosVoo;
import entidades.PassageirosVooPK;
import entidades.Voo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControles.exceptions.NonexistentEntityException;
import jpaControles.exceptions.PreexistingEntityException;


public class PassageirosVooJpaController implements Serializable {

    public PassageirosVooJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PassageirosVoo passageirosVoo) throws PreexistingEntityException, Exception {
        if (passageirosVoo.getPassageirosVooPK() == null) {
            passageirosVoo.setPassageirosVooPK(new PassageirosVooPK());
        }
        passageirosVoo.getPassageirosVooPK().setCodigoVoo(passageirosVoo.getVoo().getCodigo());
        passageirosVoo.getPassageirosVooPK().setNumDocumentoPassageiro(passageirosVoo.getPassageiro().getNumDocumento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Passageiro passageiro = passageirosVoo.getPassageiro();
            if (passageiro != null) {
                passageiro = em.getReference(passageiro.getClass(), passageiro.getNumDocumento());
                passageirosVoo.setPassageiro(passageiro);
            }
            Voo voo = passageirosVoo.getVoo();
            if (voo != null) {
                voo = em.getReference(voo.getClass(), voo.getCodigo());
                passageirosVoo.setVoo(voo);
            }
            em.persist(passageirosVoo);
            if (passageiro != null) {
                passageiro.getPassageirosVooCollection().add(passageirosVoo);
                passageiro = em.merge(passageiro);
            }
            if (voo != null) {
                voo.getPassageirosVooCollection().add(passageirosVoo);
                voo = em.merge(voo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPassageirosVoo(passageirosVoo.getPassageirosVooPK()) != null) {
                throw new PreexistingEntityException("PassageirosVoo " + passageirosVoo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PassageirosVoo passageirosVoo) throws NonexistentEntityException, Exception {
        passageirosVoo.getPassageirosVooPK().setCodigoVoo(passageirosVoo.getVoo().getCodigo());
        passageirosVoo.getPassageirosVooPK().setNumDocumentoPassageiro(passageirosVoo.getPassageiro().getNumDocumento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PassageirosVoo persistentPassageirosVoo = em.find(PassageirosVoo.class, passageirosVoo.getPassageirosVooPK());
            Passageiro passageiroOld = persistentPassageirosVoo.getPassageiro();
            Passageiro passageiroNew = passageirosVoo.getPassageiro();
            Voo vooOld = persistentPassageirosVoo.getVoo();
            Voo vooNew = passageirosVoo.getVoo();
            if (passageiroNew != null) {
                passageiroNew = em.getReference(passageiroNew.getClass(), passageiroNew.getNumDocumento());
                passageirosVoo.setPassageiro(passageiroNew);
            }
            if (vooNew != null) {
                vooNew = em.getReference(vooNew.getClass(), vooNew.getCodigo());
                passageirosVoo.setVoo(vooNew);
            }
            passageirosVoo = em.merge(passageirosVoo);
            if (passageiroOld != null && !passageiroOld.equals(passageiroNew)) {
                passageiroOld.getPassageirosVooCollection().remove(passageirosVoo);
                passageiroOld = em.merge(passageiroOld);
            }
            if (passageiroNew != null && !passageiroNew.equals(passageiroOld)) {
                passageiroNew.getPassageirosVooCollection().add(passageirosVoo);
                passageiroNew = em.merge(passageiroNew);
            }
            if (vooOld != null && !vooOld.equals(vooNew)) {
                vooOld.getPassageirosVooCollection().remove(passageirosVoo);
                vooOld = em.merge(vooOld);
            }
            if (vooNew != null && !vooNew.equals(vooOld)) {
                vooNew.getPassageirosVooCollection().add(passageirosVoo);
                vooNew = em.merge(vooNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PassageirosVooPK id = passageirosVoo.getPassageirosVooPK();
                if (findPassageirosVoo(id) == null) {
                    throw new NonexistentEntityException("The passageirosVoo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PassageirosVooPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PassageirosVoo passageirosVoo;
            try {
                passageirosVoo = em.getReference(PassageirosVoo.class, id);
                passageirosVoo.getPassageirosVooPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The passageirosVoo with id " + id + " no longer exists.", enfe);
            }
            Passageiro passageiro = passageirosVoo.getPassageiro();
            if (passageiro != null) {
                passageiro.getPassageirosVooCollection().remove(passageirosVoo);
                passageiro = em.merge(passageiro);
            }
            Voo voo = passageirosVoo.getVoo();
            if (voo != null) {
                voo.getPassageirosVooCollection().remove(passageirosVoo);
                voo = em.merge(voo);
            }
            em.remove(passageirosVoo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PassageirosVoo> findPassageirosVooEntities() {
        return findPassageirosVooEntities(true, -1, -1);
    }

    public List<PassageirosVoo> findPassageirosVooEntities(int maxResults, int firstResult) {
        return findPassageirosVooEntities(false, maxResults, firstResult);
    }

    private List<PassageirosVoo> findPassageirosVooEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PassageirosVoo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PassageirosVoo findPassageirosVoo(PassageirosVooPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PassageirosVoo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPassageirosVooCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PassageirosVoo> rt = cq.from(PassageirosVoo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
