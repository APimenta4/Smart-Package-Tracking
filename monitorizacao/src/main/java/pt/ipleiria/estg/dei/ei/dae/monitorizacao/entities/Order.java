package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(
                name = "getAllOrders",
                query = "SELECT o FROM Order o"
        )
})
public class Order implements Serializable {
    @Id
    private long code;

    @NotNull
    private Client client;

    @OneToMany(mappedBy = "volumes")
    @NotEmpty
    private List<Sensor> volumes;

    public Order(long code, Client client, List<Sensor> volumes) {
        this.code = code;
        this.client = client;
        this.volumes = volumes;
    }

    public Order() {}

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public @NotNull Client getClient() {
        return client;
    }

    public void setClient(@NotNull Client client) {
        this.client = client;
    }

    public List<Sensor> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Sensor> volumes) {
        this.volumes = volumes;
    }
}