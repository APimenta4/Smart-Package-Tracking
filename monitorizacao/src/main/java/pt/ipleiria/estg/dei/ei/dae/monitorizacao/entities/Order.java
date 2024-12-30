package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "order")
    private List<Volume> volumes;

    public Order(long code, Client client) {
        this.code = code;
        this.client = client;
        this.volumes = new ArrayList<>();
    }

    public Order() {
        this.volumes = new ArrayList<>();
    }

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

    public List<Volume> getVolumes() {
        return new ArrayList<>(volumes);
    }

    public void addVolume(Volume volume) {
        volumes.add(volume);
    }

    public void removeVolume(Volume volume) {
        volumes.remove(volume);
    }

}