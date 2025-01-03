package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "logisticians")
@NamedQueries({
        @NamedQuery(
                name = "getAllLogisticians",
                query = "SELECT l FROM Logistician l"
        )
})
public class Logistician extends User implements Serializable {
    public Logistician() {
    }

    public Logistician(long code, String name, String email, String password) {
        super(code, name, email, password);
    }
}
