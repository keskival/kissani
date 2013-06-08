package fi.neter.kissani.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
    private static PersistenceManagerFactory pmfInstance = null;

    private PMF() {}

    public static synchronized PersistenceManagerFactory get() {
        if(pmfInstance == null)  {
        	pmfInstance = JDOHelper.getPersistenceManagerFactory(
                  "transactions-optional");
        }
        
        return pmfInstance;
    }
}
