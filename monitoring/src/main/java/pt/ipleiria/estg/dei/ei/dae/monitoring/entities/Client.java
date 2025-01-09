package pt.ipleiria.estg.dei.ei.dae.monitoring.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends User implements Serializable {
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    public Client(String code, String name, String email, String password) {
        super(code, name, email, password);
    }

    public Client() {}

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
