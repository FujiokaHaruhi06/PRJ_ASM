package dal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBContext {
    protected EntityManagerFactory emf;

    public DBContext() {
        try {
            emf = Persistence.createEntityManagerFactory("ASM_PERSISTENCE_UNIT");
        } catch (Exception e) {
            // Log the exception (e.g., using a logging framework)
            e.printStackTrace(); // For basic debugging
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
} 