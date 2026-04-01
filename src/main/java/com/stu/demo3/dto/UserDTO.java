package com.stu.demo3.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;

    // Getter + Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
