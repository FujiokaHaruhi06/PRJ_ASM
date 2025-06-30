/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import Entity.Division;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class DivisionDBContext extends DBContext {
    
    public List<Division> getAll() {
        EntityManager em = createEntityManager();
        try {
            String sql = "SELECT d FROM Division d";
            TypedQuery<Division> query = em.createQuery(sql, Division.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Division get(int id) {
        EntityManager em = createEntityManager();
        try {
            return em.find(Division.class, id);
        } finally {
            em.close();
        }
    }
} 