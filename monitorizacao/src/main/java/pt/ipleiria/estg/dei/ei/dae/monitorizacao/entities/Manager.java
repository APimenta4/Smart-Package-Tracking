package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllManagers",
                query = "SELECT m FROM Manager m"
        )
})
public class Manager extends User implements Serializable {
    public Manager() {
    }

    public Manager(String code, String name, String email, String password) {
        super(code, name, email, password);
    }
}
