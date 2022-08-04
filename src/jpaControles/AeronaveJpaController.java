/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package jpaControles;

import entidades.Aeronave;
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


public class AeronaveJpaController implements Serializable {

    public AeronaveJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aeronave aeronave) {
        if (aeronave.getVooCollection() == null) {
            aeronave.setVooCollection(new ArrayList<Voo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Voo> attachedVooCollection = new ArrayList<Voo>();
            for (Voo vooCollectionVooToAttach : aeronave.getVooCollection()) {
                vooCollectionVooToAttach = em.getReference(vooCollectionVooToAttach.getClass(), vooCollectionVooToAttach.getCodigo());
                attachedVooCollection.add(vooCollectionVooToAttach);
            }
            aeronave.setVooCollection(attachedVooCollection);
            em.persist(aeronave);
            for (Voo vooCollectionVoo : aeronave.getVooCollection()) {
                Aeronave oldCodigoAeronaveOfVooCollectionVoo = vooCollectionVoo.getCodigoAeronave();
                vooCollectionVoo.setCodigoAeronave(aeronave);
                vooCollectionVoo = em.merge(vooCollectionVoo);
                if (oldCodigoAeronaveOfVooCollectionVoo != null) {
                    oldCodigoAeronaveOfVooCollectionVoo.getVooCollection().remove(vooCollectionVoo);
                    oldCodigoAeronaveOfVooCollectionVoo = em.merge(oldCodigoAeronaveOfVooCollectionVoo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aeronave aeronave) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aeronave persistentAeronave = em.find(Aeronave.class, aeronave.getCodigo());
            Collection<Voo> vooCollectionOld = persistentAeronave.getVooCollection();
            Collection<Voo> vooCollectionNew = aeronave.getVooCollection();
            List<String> illegalOrphanMessages = null;
            for (Voo vooCollectionOldVoo : vooCollectionOld) {
                if (!vooCollectionNew.contains(vooCollectionOldVoo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Voo " + vooCollectionOldVoo + " since its codigoAeronave field is not nullable.");
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
            aeronave.setVooCollection(vooCollectionNew);
            aeronave = em.merge(aeronave);
            for (Voo vooCollectionNewVoo : vooCollectionNew) {
                if (!vooCollectionOld.contains(vooCollectionNewVoo)) {
                    Aeronave oldCodigoAeronaveOfVooCollectionNewVoo = vooCollectionNewVoo.getCodigoAeronave();
                    vooCollectionNewVoo.setCodigoAeronave(aeronave);
                    vooCollectionNewVoo = em.merge(vooCollectionNewVoo);
                    if (oldCodigoAeronaveOfVooCollectionNewVoo != null && !oldCodigoAeronaveOfVooCollectionNewVoo.equals(aeronave)) {
                        oldCodigoAeronaveOfVooCollectionNewVoo.getVooCollection().remove(vooCollectionNewVoo);
                        oldCodigoAeronaveOfVooCollectionNewVoo = em.merge(oldCodigoAeronaveOfVooCollectionNewVoo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aeronave.getCodigo();
                if (findAeronave(id) == null) {
                    throw new NonexistentEntityException("The aeronave with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aeronave aeronave;
            try {
                aeronave = em.getReference(Aeronave.class, id);
                aeronave.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aeronave with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Voo> vooCollectionOrphanCheck = aeronave.getVooCollection();
            for (Voo vooCollectionOrphanCheckVoo : vooCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aeronave (" + aeronave + ") cannot be destroyed since the Voo " + vooCollectionOrphanCheckVoo + " in its vooCollection field has a non-nullable codigoAeronave field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(aeronave);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aeronave> findAeronaveEntities() {
        return findAeronaveEntities(true, -1, -1);
    }

    public List<Aeronave> findAeronaveEntities(int maxResults, int firstResult) {
        return findAeronaveEntities(false, maxResults, firstResult);
    }

    private List<Aeronave> findAeronaveEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aeronave.class));
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

    public Aeronave findAeronave(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aeronave.class, id);
        } finally {
            em.close();
        }
    }

    public int getAeronaveCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aeronave> rt = cq.from(Aeronave.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
