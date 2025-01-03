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

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB()
    {
        System.out.println("Hello Java EE!");



        // create clients
        logger.info("Creating clients ...");
        try {
            clientBean.create("1","manuel", "manuel@mail.pt","123");
            clientBean.create("2","joao", "joao@mail.pt","123");
            clientBean.create("3","maria", "maria@mail.pt","123");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }


    }
}
