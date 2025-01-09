package pt.ipleiria.estg.dei.ei.dae.monitorizacao.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.CategoryType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.PackingType;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.enums.SensorType;

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
    @EJB
    private OrderBean orderBean;
    @EJB
    private VolumeBean volumeBean;
    @EJB
    private ProductBean productBean;
    @EJB
    private SensorBean sensorBean;
    @EJB
    private LinesOfSaleBean linesOfSaleBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB()
    {
        logger.info("Starting to populate DB");

        logger.info("Creating users");
        try {
            clientBean.create("1c","client", "client@mail.pt","123");
            logisticianBean.create("2","logistician", "logistician@mail.pt","123");
            managerBean.create("3","manager", "manager@mail.pt","123");
        } catch (Exception ignore) {}
        logger.info("Users created");

        logger.info("Creating products");
        try {
            productBean.create("1p", CategoryType.EXAMPLE,"product one");
            productBean.create("2p", CategoryType.EXAMPLE,"product two");
            productBean.create("3p", CategoryType.EXAMPLE,"product three");
        } catch (Exception ignore) {}
        logger.info("Products created");

        logger.info("Creating order");
        try {
            orderBean.create("1o","1c");
            volumeBean.create("1v", "1o", PackingType.EXAMPLE);
            sensorBean.create("1s","1v",SensorType.TEMPERATURE);
            sensorBean.create("2s","1v",SensorType.ACCELERATION);
            linesOfSaleBean.create("1v","1p",10);
            linesOfSaleBean.create("1v","2p",20);
        } catch (Exception ignore) {}
        logger.info("Order created");

        logger.info("DB populated");
    }
}



