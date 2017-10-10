package org.LPIntegrator.service.models;

import lombok.Data;

@Data
public class ShipmentStatusUpdateRequest
{
    private Orderlines[] orderlines;
}