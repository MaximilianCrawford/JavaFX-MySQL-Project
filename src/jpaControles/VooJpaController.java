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
import entidades.Aeronave;
import entidades.Destino;
import entidades.PassageirosVoo;
import entidades.Voo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpaControles.exceptions.IllegalOrphanException;
import jpaControles.exceptions.NonexistentEntityException;


public class VooJpaController implements Serializable {

    public VooJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Voo voo) {
        if (voo.getPassageirosVooCollection() == null) {
            voo.setPassageirosVooCollection(new ArrayList<PassageirosVoo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aeronave codigoAeronave = voo.getCodigoAeronave();
            if (codigoAeronave != null) {
                codigoAeronave = em.getReference(codigoAeronave.getClass(), codigoAeronave.getCodigo());
                voo.setCodigoAeronave(codigoAeronave);
            }
            Destino codAeroportoDestino = voo.getCodAeroportoDestino();
            if (codAeroportoDestino != null) {
                codAeroportoDestino = em.getReference(codAeroportoDestino.getClass(), codAeroportoDestino.getCodAeroporto());
                voo.setCodAeroportoDestino(codAeroportoDestino);
            }
            Collection<PassageirosVoo> attachedPassageirosVooCollection = new ArrayList<PassageirosVoo>();
            for (PassageirosVoo passageirosVooCollectionPassageirosVooToAttach : voo.getPassageirosVooCollection()) {
                passageirosVooCollectionPassageirosVooToAttach = em.getReference(passageirosVooCollectionPassageirosVooToAttach.getClass(), passageirosVooCollectionPassageirosVooToAttach.getPassageirosVooPK());
                attachedPassageirosVooCollection.add(passageirosVooCollectionPassageirosVooToAttach);
            }
            voo.setPassageirosVooCollection(attachedPassageirosVooCollection);
            em.persist(voo);
            if (codigoAeronave != null) {
                codigoAeronave.getVooCollection().add(voo);
                codigoAeronave = em.merge(codigoAeronave);
            }
            if (codAeroportoDestino != null) {
                codAeroportoDestino.getVooCollection().add(voo);
                codAeroportoDestino = em.merge(codAeroportoDestino);
            }
            for (PassageirosVoo passageirosVooCollectionPassageirosVoo : voo.getPassageirosVooCollection()) {
                Voo oldVooOfPassageirosVooCollectionPassageirosVoo = passageirosVooCollectionPassageirosVoo.getVoo();
                passageirosVooCollectionPassageirosVoo.setVoo(voo);
                passageirosVooCollectionPassageirosVoo = em.merge(passageirosVooCollectionPassageirosVoo);
                if (oldVooOfPassageirosVooCollectionPassageirosVoo != null) {
                    oldVooOfPassageirosVooCollectionPassageirosVoo.getPassageirosVooCollection().remove(passageirosVooCollectionPassageirosVoo);
                    oldVooOfPassageirosVooCollectionPassageirosVoo = em.merge(oldVooOfPassageirosVooCollectionPassageirosVoo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Voo voo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voo persistentVoo = em.find(Voo.class, voo.getCodigo());
            Aeronave codigoAeronaveOld = persistentVoo.getCodigoAeronave();
            Aeronave codigoAeronaveNew = voo.getCodigoAeronave();
            Destino codAeroportoDestinoOld = persistentVoo.getCodAeroportoDestino();
            Destino codAeroportoDestinoNew = voo.getCodAeroportoDestino();
            Collection<PassageirosVoo> passageirosVooCollectionOld = persistentVoo.getPassageirosVooCollection();
            Collection<PassageirosVoo> passageirosVooCollectionNew = voo.getPassageirosVooCollection();
            List<String> illegalOrphanMessages = null;
            for (PassageirosVoo passageirosVooCollectionOldPassageirosVoo : passageirosVooCollectionOld) {
                if (!passageirosVooCollectionNew.contains(passageirosVooCollectionOldPassageirosVoo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PassageirosVoo " + passageirosVooCollectionOldPassageirosVoo + " since its voo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoAeronaveNew != null) {
                codigoAeronaveNew = em.getReference(codigoAeronaveNew.getClass(), codigoAeronaveNew.getCodigo());
                voo.setCodigoAeronave(codigoAeronaveNew);
            }
            if (codAeroportoDestinoNew != null) {
                codAeroportoDestinoNew = em.getReference(codAeroportoDestinoNew.getClass(), codAeroportoDestinoNew.getCodAeroporto());
                voo.setCodAeroportoDestino(codAeroportoDestinoNew);
            }
            Collection<PassageirosVoo> attachedPassageirosVooCollectionNew = new ArrayList<PassageirosVoo>();
            for (PassageirosVoo passageirosVooCollectionNewPassageirosVooToAttach : passageirosVooCollectionNew) {
                passageirosVooCollectionNewPassageirosVooToAttach = em.getReference(passageirosVooCollectionNewPassageirosVooToAttach.getClass(), passageirosVooCollectionNewPassageirosVooToAttach.getPassageirosVooPK());
                attachedPassageirosVooCollectionNew.add(passageirosVooCollectionNewPassageirosVooToAttach);
            }
            passageirosVooCollectionNew = attachedPassageirosVooCollectionNew;
            voo.setPassageirosVooCollection(passageirosVooCollectionNew);
            voo = em.merge(voo);
            if (codigoAeronaveOld != null && !codigoAeronaveOld.equals(codigoAeronaveNew)) {
                codigoAeronaveOld.getVooCollection().remove(voo);
                codigoAeronaveOld = em.merge(codigoAeronaveOld);
            }
            if (codigoAeronaveNew != null && !codigoAeronaveNew.equals(codigoAeronaveOld)) {
                codigoAeronaveNew.getVooCollection().add(voo);
                codigoAeronaveNew = em.merge(codigoAeronaveNew);
            }
            if (codAeroportoDestinoOld != null && !codAeroportoDestinoOld.equals(codAeroportoDestinoNew)) {
                codAeroportoDestinoOld.getVooCollection().remove(voo);
                codAeroportoDestinoOld = em.merge(codAeroportoDestinoOld);
            }
            if (codAeroportoDestinoNew != null && !codAeroportoDestinoNew.equals(codAeroportoDestinoOld)) {
                codAeroportoDestinoNew.getVooCollection().add(voo);
                codAeroportoDestinoNew = em.merge(codAeroportoDestinoNew);
            }
            for (PassageirosVoo passageirosVooCollectionNewPassageirosVoo : passageirosVooCollectionNew) {
                if (!passageirosVooCollectionOld.contains(passageirosVooCollectionNewPassageirosVoo)) {
                    Voo oldVooOfPassageirosVooCollectionNewPassageirosVoo = passageirosVooCollectionNewPassageirosVoo.getVoo();
                    passageirosVooCollectionNewPassageirosVoo.setVoo(voo);
                    passageirosVooCollectionNewPassageirosVoo = em.merge(passageirosVooCollectionNewPassageirosVoo);
                    if (oldVooOfPassageirosVooCollectionNewPassageirosVoo != null && !oldVooOfPassageirosVooCollectionNewPassageirosVoo.equals(voo)) {
                        oldVooOfPassageirosVooCollectionNewPassageirosVoo.getPassageirosVooCollection().remove(passageirosVooCollectionNewPassageirosVoo);
                        oldVooOfPassageirosVooCollectionNewPassageirosVoo = em.merge(oldVooOfPassageirosVooCollectionNewPassageirosVoo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = voo.getCodigo();
                if (findVoo(id) == null) {
                    throw new NonexistentEntityException("The voo with id " + id + " no longer exists.");
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
            Voo voo;
            try {
                voo = em.getReference(Voo.class, id);
                voo.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The voo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PassageirosVoo> passageirosVooCollectionOrphanCheck = voo.getPassageirosVooCollection();
            for (PassageirosVoo passageirosVooCollectionOrphanCheckPassageirosVoo : passageirosVooCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Voo (" + voo + ") cannot be destroyed since the PassageirosVoo " + passageirosVooCollectionOrphanCheckPassageirosVoo + " in its passageirosVooCollection field has a non-nullable voo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Aeronave codigoAeronave = voo.getCodigoAeronave();
            if (codigoAeronave != null) {
                codigoAeronave.getVooCollection().remove(voo);
                codigoAeronave = em.merge(codigoAeronave);
            }
            Destino codAeroportoDestino = voo.getCodAeroportoDestino();
            if (codAeroportoDestino != null) {
                codAeroportoDestino.getVooCollection().remove(voo);
                codAeroportoDestino = em.merge(codAeroportoDestino);
            }
            em.remove(voo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Voo> findVooEntities() {
        return findVooEntities(true, -1, -1);
    }

    public List<Voo> findVooEntities(int maxResults, int firstResult) {
        return findVooEntities(false, maxResults, firstResult);
    }

    private List<Voo> findVooEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Voo.class));
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

    public Voo findVoo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Voo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVooCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Voo> rt = cq.from(Voo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
