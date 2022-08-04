/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package jpaControles;

import entidades.Passageiro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.PassageirosVoo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControles.exceptions.IllegalOrphanException;
import jpaControles.exceptions.NonexistentEntityException;
import jpaControles.exceptions.PreexistingEntityException;


public class PassageiroJpaController implements Serializable {

    public PassageiroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Passageiro passageiro) throws PreexistingEntityException, Exception {
        if (passageiro.getPassageirosVooCollection() == null) {
            passageiro.setPassageirosVooCollection(new ArrayList<PassageirosVoo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PassageirosVoo> attachedPassageirosVooCollection = new ArrayList<PassageirosVoo>();
            for (PassageirosVoo passageirosVooCollectionPassageirosVooToAttach : passageiro.getPassageirosVooCollection()) {
                passageirosVooCollectionPassageirosVooToAttach = em.getReference(passageirosVooCollectionPassageirosVooToAttach.getClass(), passageirosVooCollectionPassageirosVooToAttach.getPassageirosVooPK());
                attachedPassageirosVooCollection.add(passageirosVooCollectionPassageirosVooToAttach);
            }
            passageiro.setPassageirosVooCollection(attachedPassageirosVooCollection);
            em.persist(passageiro);
            for (PassageirosVoo passageirosVooCollectionPassageirosVoo : passageiro.getPassageirosVooCollection()) {
                Passageiro oldPassageiroOfPassageirosVooCollectionPassageirosVoo = passageirosVooCollectionPassageirosVoo.getPassageiro();
                passageirosVooCollectionPassageirosVoo.setPassageiro(passageiro);
                passageirosVooCollectionPassageirosVoo = em.merge(passageirosVooCollectionPassageirosVoo);
                if (oldPassageiroOfPassageirosVooCollectionPassageirosVoo != null) {
                    oldPassageiroOfPassageirosVooCollectionPassageirosVoo.getPassageirosVooCollection().remove(passageirosVooCollectionPassageirosVoo);
                    oldPassageiroOfPassageirosVooCollectionPassageirosVoo = em.merge(oldPassageiroOfPassageirosVooCollectionPassageirosVoo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPassageiro(passageiro.getNumDocumento()) != null) {
                throw new PreexistingEntityException("Passageiro " + passageiro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Passageiro passageiro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Passageiro persistentPassageiro = em.find(Passageiro.class, passageiro.getNumDocumento());
            Collection<PassageirosVoo> passageirosVooCollectionOld = persistentPassageiro.getPassageirosVooCollection();
            Collection<PassageirosVoo> passageirosVooCollectionNew = passageiro.getPassageirosVooCollection();
            List<String> illegalOrphanMessages = null;
            for (PassageirosVoo passageirosVooCollectionOldPassageirosVoo : passageirosVooCollectionOld) {
                if (!passageirosVooCollectionNew.contains(passageirosVooCollectionOldPassageirosVoo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PassageirosVoo " + passageirosVooCollectionOldPassageirosVoo + " since its passageiro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PassageirosVoo> attachedPassageirosVooCollectionNew = new ArrayList<PassageirosVoo>();
            for (PassageirosVoo passageirosVooCollectionNewPassageirosVooToAttach : passageirosVooCollectionNew) {
                passageirosVooCollectionNewPassageirosVooToAttach = em.getReference(passageirosVooCollectionNewPassageirosVooToAttach.getClass(), passageirosVooCollectionNewPassageirosVooToAttach.getPassageirosVooPK());
                attachedPassageirosVooCollectionNew.add(passageirosVooCollectionNewPassageirosVooToAttach);
            }
            passageirosVooCollectionNew = attachedPassageirosVooCollectionNew;
            passageiro.setPassageirosVooCollection(passageirosVooCollectionNew);
            passageiro = em.merge(passageiro);
            for (PassageirosVoo passageirosVooCollectionNewPassageirosVoo : passageirosVooCollectionNew) {
                if (!passageirosVooCollectionOld.contains(passageirosVooCollectionNewPassageirosVoo)) {
                    Passageiro oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo = passageirosVooCollectionNewPassageirosVoo.getPassageiro();
                    passageirosVooCollectionNewPassageirosVoo.setPassageiro(passageiro);
                    passageirosVooCollectionNewPassageirosVoo = em.merge(passageirosVooCollectionNewPassageirosVoo);
                    if (oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo != null && !oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo.equals(passageiro)) {
                        oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo.getPassageirosVooCollection().remove(passageirosVooCollectionNewPassageirosVoo);
                        oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo = em.merge(oldPassageiroOfPassageirosVooCollectionNewPassageirosVoo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = passageiro.getNumDocumento();
                if (findPassageiro(id) == null) {
                    throw new NonexistentEntityException("The passageiro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Passageiro passageiro;
            try {
                passageiro = em.getReference(Passageiro.class, id);
                passageiro.getNumDocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The passageiro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PassageirosVoo> passageirosVooCollectionOrphanCheck = passageiro.getPassageirosVooCollection();
            for (PassageirosVoo passageirosVooCollectionOrphanCheckPassageirosVoo : passageirosVooCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Passageiro (" + passageiro + ") cannot be destroyed since the PassageirosVoo " + passageirosVooCollectionOrphanCheckPassageirosVoo + " in its passageirosVooCollection field has a non-nullable passageiro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(passageiro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Passageiro> findPassageiroEntities() {
        return findPassageiroEntities(true, -1, -1);
    }

    public List<Passageiro> findPassageiroEntities(int maxResults, int firstResult) {
        return findPassageiroEntities(false, maxResults, firstResult);
    }

    private List<Passageiro> findPassageiroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Passageiro.class));
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

    public Passageiro findPassageiro(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Passageiro.class, id);
        } finally {
            em.close();
        }
    }

    public int getPassageiroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Passageiro> rt = cq.from(Passageiro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
