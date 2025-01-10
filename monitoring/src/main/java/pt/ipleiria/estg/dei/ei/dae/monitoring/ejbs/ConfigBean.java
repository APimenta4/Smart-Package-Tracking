package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingAcceleration;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.ReadingTemperature;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;

import java.util.List;
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
    private LineOfSaleBean lineOfSaleBean;

    @EJB
    private ReadingAccelerationBean readingAccelerationBean;

    @EJB
    private ReadingTemperatureBean readingTemperatureBean;


    @EJB
    private ReadingLocationBean readingLocationBean;

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB()
    {
        logger.info("Starting to populate DB");

        logger.info("Creating users");
        try {
            clientBean.create("C1","client", "client@mail.pt","123");
            logisticianBean.create("M2","logistician", "logistician@mail.pt","123");
            managerBean.create("L3","manager", "manager@mail.pt","123");
        } catch (Exception ignore) {}
        logger.info("Users created");

        logger.info("Creating products");
        try {
            productBean.create("P01", PackageType.NONE,"Banana");
            productBean.create("P02", PackageType.FRAGILE,"TV");
            productBean.create("P03", PackageType.ISOTERMIC,"product three");
        } catch (Exception ignore) {}
        logger.info("Products created");

        logger.info("Creating order");
        try {
            orderBean.create("O1","C1");
            volumeBean.create("V1", "O1", PackageType.ISOTERMIC_GEOLOCATION);
            sensorBean.create("S1","V1",SensorType.TEMPERATURE);
            sensorBean.create("S2","V1",SensorType.LOCATION);
            lineOfSaleBean.create("V1","P01",10L);
            lineOfSaleBean.create("V1","P02",20L);
            sensorBean.create("S3","V1",SensorType.ACCELERATION);

            volumeBean.create("V2", "O1", PackageType.ISOTERMIC_GEOLOCATION);
            sensorBean.create("S4","V2",SensorType.TEMPERATURE);
            sensorBean.create("S5","V2",SensorType.LOCATION);
            lineOfSaleBean.create("V2","P01",1L);
            lineOfSaleBean.create("V2","P02",2L);
            sensorBean.create("S6","V2",SensorType.ACCELERATION);

            volumeBean.updateStatus("V2", VolumeStatus.CANCELLED);

            readingTemperatureBean.create("S1",24D);
            readingLocationBean.create("S2",-50.61D, 165.97D);
            readingAccelerationBean.create("S3",1.5D);

            readingTemperatureBean.create("S4",24.2D);
            readingLocationBean.create("S5",-50.61D, 165.97D);
            readingAccelerationBean.create("S6",1.5D);
        } catch (Exception ignore) {}
        logger.info("Order created");

        logger.info("DB populated");
    }
}



