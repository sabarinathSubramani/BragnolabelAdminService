package org.LPIntegrator.models;

import lombok.Data;

@Data
public class Tax {

	private String taxType;
	private double value;
	private double rate;
}
