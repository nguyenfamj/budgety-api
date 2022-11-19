package com.example.budget.features.role;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum type;

    public Role() {
    }

    public long getId() {
        return id;
    }

    public Role(RoleEnum type) {
        this.type = type;
    }

    public RoleEnum getType() {
        return this.type;
    }

    public void setType(RoleEnum type) {
        this.type = type;
    }
}
