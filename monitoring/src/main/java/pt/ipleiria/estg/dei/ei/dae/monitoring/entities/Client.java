package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Client extends User implements Serializable {
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    public Client(String code, String name, String email, String password) {
        super(code, name, email, password);
    }

    public Client() {}

    @Transient
    public List<Volume> getVolumes() {
        return orders.stream()
                .flatMap(order -> order.getVolumes().stream())
                .collect(Collectors.toList());
    }

    @Transient
    public List<Reading> getReadings() {
        return orders.stream()
                .flatMap(order -> order.getVolumes().stream())
                .flatMap(volume -> volume.getSensors().stream())
                .flatMap(sensor -> sensor.getReadings().stream())
                .collect(Collectors.toList());
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
