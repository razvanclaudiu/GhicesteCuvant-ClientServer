package app.persistance.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSession {

    private static SessionFactory sessionFactory = null;

    public static synchronized SessionFactory getInstance(){
        if(sessionFactory == null){
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            try{
                sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
            }
            catch(Exception ex){
                System.err.println("Exception: " + ex);
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
        return sessionFactory;
    }

}
