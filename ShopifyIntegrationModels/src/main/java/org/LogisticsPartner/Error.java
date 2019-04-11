package org.LogisticsPartner;

import java.util.List;
import lombok.Data;

@Data
public class Error {
	
	private int code;
	private String message;
	private List<String> errorMessages;
}
