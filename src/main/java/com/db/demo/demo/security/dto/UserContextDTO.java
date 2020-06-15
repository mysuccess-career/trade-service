package com.db.demo.demo.security.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Savitha
 */

@Builder
public class UserContextDTO implements Serializable {

    private static final long serialVersionUID = -8744778282525639808L;

    public UserContextDTO(String userName, UUID password) {
        this.userName = userName;
        this.password = password;
    }

    private String userName;
    private UUID password;


}
