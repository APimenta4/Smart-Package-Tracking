package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class Logistician extends User implements Serializable {
    public Logistician(String code, String name, String email, String password) {
        super(code, name, email, password);
    }

    public Logistician() {}
}
