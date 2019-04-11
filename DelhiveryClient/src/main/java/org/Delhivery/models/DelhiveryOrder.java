package org.Delhivery.models;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

@Data
@JsonSnakeCase
public class DelhiveryOrder {

    private String orderNumber;

    private SubOrders[] subOrders;

    private Consignee consignee;

    private String orderDate;
 
    @Override
    public String toString()
    {
        return "orderNumber = "+orderNumber+", subOrders = "+subOrders+", consignee = "+consignee+", orderDate = "+orderDate+"]";
    }

}
