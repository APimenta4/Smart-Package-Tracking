package pt.ipleiria.estg.dei.ei.dae.monitoring;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationApi extends Application {
    public ApplicationApi() {
        System.setProperty("resteasy.preferJacksonOverJsonB", "false");
    }
}