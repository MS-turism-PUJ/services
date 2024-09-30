package com.turism.services.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.turism.services.models.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageDTO implements Serializable {
    private String userId;
    private String username;
    private String name;
    private String email;
    private String photo;

    public User toUser() {
        return new User(userId, username, name, email, photo);
    }
}

