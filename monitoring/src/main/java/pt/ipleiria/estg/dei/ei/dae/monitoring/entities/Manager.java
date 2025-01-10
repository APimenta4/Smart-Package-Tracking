package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class Manager extends User implements Serializable {
    public Manager(String code, String name, String email, String password) {
        super(code, name, email, password);
    }

    public Manager() {}
}
