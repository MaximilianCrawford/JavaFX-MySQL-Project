/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package jpaControles;

import entidades.Destino;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Voo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControles.exceptions.IllegalOrphanException;
import jpaControles.exceptions.NonexistentEntityException;
import jpaControles.exceptions.PreexistingEntityException;


public class DestinoJpaController implements Serializable {

    public DestinoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Destino destino) throws PreexistingEntityException, Exception {
        if (destino.getVooCollection() == null) {
            destino.setVooCollection(new ArrayList<Voo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Voo> attachedVooCollection = new ArrayList<Voo>();
            for (Voo vooCollectionVooToAttach : destino.getVooCollection()) {
                vooCollectionVooToAttach = em.getReference(vooCollectionVooToAttach.getClass(), vooCollectionVooToAttach.getCodigo());
                attachedVooCollection.add(vooCollectionVooToAttach);
            }
            destino.setVooCollection(attachedVooCollection);
            em.persist(destino);
            for (Voo vooCollectionVoo : destino.getVooCollection()) {
                Destino oldCodAeroportoDestinoOfVooCollectionVoo = vooCollectionVoo.getCodAeroportoDestino();
                vooCollectionVoo.setCodAeroportoDestino(destino);
                vooCollectionVoo = em.merge(vooCollectionVoo);
                if (oldCodAeroportoDestinoOfVooCollectionVoo != null) {
                    oldCodAeroportoDestinoOfVooCollectionVoo.getVooCollection().remove(vooCollectionVoo);
                    oldCodAeroportoDestinoOfVooCollectionVoo = em.merge(oldCodAeroportoDestinoOfVooCollectionVoo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDestino(destino.getCodAeroporto()) != null) {
                throw new PreexistingEntityException("Destino " + destino + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Destino destino) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Destino persistentDestino = em.find(Destino.class, destino.getCodAeroporto());
            Collection<Voo> vooCollectionOld = persistentDestino.getVooCollection();
            Collection<Voo> vooCollectionNew = destino.getVooCollection();
            List<String> illegalOrphanMessages = null;
            for (Voo vooCollectionOldVoo : vooCollectionOld) {
                if (!vooCollectionNew.contains(vooCollectionOldVoo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Voo " + vooCollectionOldVoo + " since its codAeroportoDestino field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Voo> attachedVooCollectionNew = new ArrayList<Voo>();
            for (Voo vooCollectionNewVooToAttach : vooCollectionNew) {
                vooCollectionNewVooToAttach = em.getReference(vooCollectionNewVooToAttach.getClass(), vooCollectionNewVooToAttach.getCodigo());
                attachedVooCollectionNew.add(vooCollectionNewVooToAttach);
            }
            vooCollectionNew = attachedVooCollectionNew;
            destino.setVooCollection(vooCollectionNew);
            destino = em.merge(destino);
            for (Voo vooCollectionNewVoo : vooCollectionNew) {
                if (!vooCollectionOld.contains(vooCollectionNewVoo)) {
                    Destino oldCodAeroportoDestinoOfVooCollectionNewVoo = vooCollectionNewVoo.getCodAeroportoDestino();
                    vooCollectionNewVoo.setCodAeroportoDestino(destino);
                    vooCollectionNewVoo = em.merge(vooCollectionNewVoo);
                    if (oldCodAeroportoDestinoOfVooCollectionNewVoo != null && !oldCodAeroportoDestinoOfVooCollectionNewVoo.equals(destino)) {
                        oldCodAeroportoDestinoOfVooCollectionNewVoo.getVooCollection().remove(vooCollectionNewVoo);
                        oldCodAeroportoDestinoOfVooCollectionNewVoo = em.merge(oldCodAeroportoDestinoOfVooCollectionNewVoo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = destino.getCodAeroporto();
                if (findDestino(id) == null) {
                    throw new NonexistentEntityException("The destino with id " + id + " no longer exists.");
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
            Destino destino;
            try {
                destino = em.getReference(Destino.class, id);
                destino.getCodAeroporto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The destino with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Voo> vooCollectionOrphanCheck = destino.getVooCollection();
            for (Voo vooCollectionOrphanCheckVoo : vooCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Destino (" + destino + ") cannot be destroyed since the Voo " + vooCollectionOrphanCheckVoo + " in its vooCollection field has a non-nullable codAeroportoDestino field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(destino);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Destino> findDestinoEntities() {
        return findDestinoEntities(true, -1, -1);
    }

    public List<Destino> findDestinoEntities(int maxResults, int firstResult) {
        return findDestinoEntities(false, maxResults, firstResult);
    }

    private List<Destino> findDestinoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Destino.class));
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

    public Destino findDestino(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Destino.class, id);
        } finally {
            em.close();
        }
    }

    public int getDestinoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Destino> rt = cq.from(Destino.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
