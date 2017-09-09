package com.shopify.api.models;

import lombok.Data;

@Data
public class ClientDetails
{
    private String browser_ip;

    private String session_hash;

    private String accept_language;

    private String user_agent;

    private String browser_height;

    private String browser_width;

    @Override
    public String toString()
    {
        return "ClassPojo [browser_ip = "+browser_ip+", session_hash = "+session_hash+", accept_language = "+accept_language+", user_agent = "+user_agent+", browser_height = "+browser_height+", browser_width = "+browser_width+"]";
    }
}