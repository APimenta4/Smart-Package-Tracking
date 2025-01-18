package pt.ipleiria.estg.dei.ei.dae.monitoring.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.PackageType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.SensorType;
import pt.ipleiria.estg.dei.ei.dae.monitoring.enums.VolumeStatus;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.monitoring.exceptions.CustomEntityNotFoundException;

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
    public void populateDB(){
        logger.info("Starting to populate DB");

        // Creating Users
        logger.info("Creating users");
        try {
            clientBean.create("client", "Client", "client@mail.pt", "123");
            clientBean.create("client2", "Client 2", "client2@mail.pt", "123");
            logisticianBean.create("logistician", "Logistician", "logistician@mail.pt", "123");
            logisticianBean.create("logistician2", "Logistician 2", "logistician2@mail.pt", "123");
            managerBean.create("manager", "Manager", "manager@mail.pt", "123");
        } catch (Exception e) {
            logger.warning("An error occurred while creating users: " + e.getMessage());
        }

        // Creating Products
        logger.info("Creating products");
        try {
            productBean.create("P01", PackageType.NONE, "T-shirt");
            productBean.create("P02", PackageType.FRAGILE, "TV");
            productBean.create("P03", PackageType.ISOTERMIC, "Special box Ice Cream");
            productBean.create("P04", PackageType.GEOLOCATION, "Parcel with Important Legal Documents");
            productBean.create("P05", PackageType.FRAGILE_ISOTERMIC, "Specialty Chocolates");
            productBean.create("P06", PackageType.FRAGILE_GEOLOCATION, "Crystal Chandelier");
            productBean.create("P07", PackageType.ISOTERMIC_GEOLOCATION, "Frozen Gourmet Meal");
            productBean.create("P08", PackageType.FRAGILE_ISOTERMIC_GEOLOCATION, "Temperature-Controlled Fine Jewelry");
        } catch (Exception e) {
            logger.warning("An error occurred while creating products: " + e.getMessage());
        }

        logger.info("DB populated successfully");
    }
}



