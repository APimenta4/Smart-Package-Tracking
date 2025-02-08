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
    public void populateDB() {
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


        logger.info("Creating order");
        try {
            orderBean.create("OrderTest1", "client");

            volumeBean.create("VolumeTest1", "OrderTest1", PackageType.ISOTERMIC_GEOLOCATION);
            sensorBean.create("A1", "VolumeTest1", SensorType.ACCELERATION);
            sensorBean.create("L1", "VolumeTest1", SensorType.LOCATION);
            sensorBean.create("T1", "VolumeTest1", SensorType.TEMPERATURE);
            lineOfSaleBean.create("VolumeTest1", "P01", 10L);
            lineOfSaleBean.create("VolumeTest1", "P02", 1L);

            volumeBean.create("VolumeTest2", "OrderTest1", PackageType.FRAGILE);
            sensorBean.create("A2", "VolumeTest2", SensorType.ACCELERATION);
            sensorBean.create("L2", "VolumeTest2", SensorType.LOCATION);
            sensorBean.create("T2", "VolumeTest2", SensorType.TEMPERATURE);
            lineOfSaleBean.create("VolumeTest2", "P03", 1L);
            lineOfSaleBean.create("VolumeTest2", "P04", 1L);


            orderBean.create("OrderTest2", "client");

            volumeBean.create("VolumeTest3", "OrderTest2", PackageType.ISOTERMIC_GEOLOCATION);
            sensorBean.create("A3", "VolumeTest2", SensorType.ACCELERATION);
            sensorBean.create("A31", "VolumeTest2", SensorType.ACCELERATION);
            sensorBean.create("T3", "VolumeTest2", SensorType.TEMPERATURE);
            sensorBean.create("L3", "VolumeTest2", SensorType.LOCATION);
            lineOfSaleBean.create("VolumeTest2", "P05", 1L);
            lineOfSaleBean.create("VolumeTest2", "P06", 1L);

            volumeBean.create("VolumeTest4", "OrderTest2", PackageType.NONE);
            sensorBean.create("A4", "VolumeTest2", SensorType.ACCELERATION);
            sensorBean.create("T4", "VolumeTest4", SensorType.TEMPERATURE);
            sensorBean.create("T41", "VolumeTest4", SensorType.TEMPERATURE);
            sensorBean.create("L4", "VolumeTest4", SensorType.LOCATION);
            sensorBean.create("L41", "VolumeTest4", SensorType.LOCATION);
            lineOfSaleBean.create("VolumeTest4", "P07", 1L);
            lineOfSaleBean.create("VolumeTest4", "P08", 1L);



            readingTemperatureBean.create("S1", 24D);
            readingLocationBean.create("S2", -50.61D, 165.97D);
            readingAccelerationBean.create("S3", 1.5D);
            readingTemperatureBean.create("S4", 24.2D);
            readingLocationBean.create("S5", -50.61D, 165.97D);
            readingAccelerationBean.create("S6", 1.5D);
        } catch (Exception e) {
            logger.warning("An error occurred while creating order: " + e.getMessage());
        }
        logger.info("Order created");
        logger.info("DB populated");
    }
}



