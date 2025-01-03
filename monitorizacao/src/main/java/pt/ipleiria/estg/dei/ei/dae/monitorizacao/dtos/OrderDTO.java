package pt.ipleiria.estg.dei.ei.dae.monitorizacao.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Order;
import pt.ipleiria.estg.dei.ei.dae.monitorizacao.entities.Volume;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private String code;
    private String clientCode;
    private List<Volume> volumes;

    public OrderDTO() {
        this.volumes = new ArrayList<>();
    }

    public OrderDTO(String code, String clientCode) {
        this.code = code;
        this.clientCode = clientCode;
        this.volumes = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    // Converts an entity Order to a DTO Order class
    public static OrderDTO from(Order order) {
        return new OrderDTO(
                order.getCode(),
                order.getClient().getCode()
        );
    }
    // converts an entire list of entities into a list of DTOs
    public static List<OrderDTO> from(List<Order> order) {
        return order.stream().map(OrderDTO::from).collect(Collectors.toList());
    }
}
