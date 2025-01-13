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
    public void populateDB()
    {
        logger.info("Starting to populate DB");

        logger.info("Creating users");
        try {
            clientBean.create("client","client", "client@mail.pt","123");
            clientBean.create("client2","client2", "client2@mail.pt","123");
            logisticianBean.create("logistician","logistician", "logistician@mail.pt","123");
            managerBean.create("manager","manager", "manager@mail.pt","123");
        } catch (Exception ignore) {}
        logger.info("Users created");

        logger.info("Creating products");
        try {
            productBean.create("P01", PackageType.NONE,"Banana");
            productBean.create("P02", PackageType.FRAGILE,"TV");
            productBean.create("P03", PackageType.ISOTERMIC,"product three");
            productBean.create("P04", PackageType.GEOLOCATION,"Arduino");
            productBean.create("P05", PackageType.FRAGILE_ISOTERMIC,"Chemical equipment");
            productBean.create("P06", PackageType.FRAGILE_ISOTERMIC_GEOLOCATION,"Palm tree");
        } catch (Exception ignore) {}
        logger.info("Products created");

        logger.info("Creating order");
        try {
            orderBean.create("O1","client");
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
            int i = 7;
            i = createVolume("1",i);
            i = createVolume("2",i);
            i = createVolume("3",i);
            i = createVolume("4",i);
            i = createVolume("5",i);

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

    private int createVolume(String volumeCode, int i)
            throws CustomConstraintViolationException, CustomEntityNotFoundException, CustomEntityExistsException {
        volumeBean.create(volumeCode, "O1", PackageType.ISOTERMIC_GEOLOCATION);
        sensorBean.create("S"+i,volumeCode,SensorType.TEMPERATURE);
        sensorBean.create("S"+(i+1),volumeCode,SensorType.LOCATION);
        lineOfSaleBean.create(volumeCode,"P01",1L);
        lineOfSaleBean.create(volumeCode,"P02",2L);
        sensorBean.create("S"+(i+2),volumeCode,SensorType.ACCELERATION);

        volumeBean.updateStatus(volumeCode, VolumeStatus.IN_TRANSIT);
        return i+3;
    }
}



