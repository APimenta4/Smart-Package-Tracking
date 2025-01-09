package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private String code;
    private String clientCode;
    private List<VolumeDTO> volumes;

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

    public List<VolumeDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeDTO> volumes) {
        this.volumes = volumes;
    }

    public static OrderDTO from(Order order) {
        return new OrderDTO(
                order.getCode(),
                order.getClient().getCode()
        );
    }

    public static List<OrderDTO> from(List<Order> order) {
        return order.stream().map(OrderDTO::from).collect(Collectors.toList());
    }
}
