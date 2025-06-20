package com.MoleLaw_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String password;
    private String nickname;
}
