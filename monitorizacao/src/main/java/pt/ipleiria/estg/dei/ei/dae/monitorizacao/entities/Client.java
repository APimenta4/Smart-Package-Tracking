package pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllClients",
                query = "SELECT c FROM Client c"
        )
})
public class Client extends User implements Serializable {
    @OneToMany(mappedBy = "client")
    private List<Order> orders;

    public Client() {
        this.orders = new ArrayList<>();
    }

    public Client(String code, String name, String email, String password) {
        super(code, name, email, password);
        this.orders = new ArrayList<>();
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
