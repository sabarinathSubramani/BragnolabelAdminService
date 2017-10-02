package org.LPIntegrator.service.models;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest
{
    private Orderlines[] orderlines;
}