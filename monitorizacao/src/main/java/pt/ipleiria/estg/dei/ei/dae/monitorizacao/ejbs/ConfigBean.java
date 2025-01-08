package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    private ClientBean clientBean;
    @EJB
    private LogisticianBean logisticianBean;
    @EJB
    private ManagerBean managerBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB()
    {
        logger.info("Starting to populate DB");

        logger.info("Creating users");
        try {
            clientBean.create("1","client", "client@mail.pt","123");
            logisticianBean.create("2","logistician", "logistician@mail.pt","123");
            managerBean.create("3","manager", "manager@mail.pt","123");
        } catch (Exception ignore) {}
    }
}
