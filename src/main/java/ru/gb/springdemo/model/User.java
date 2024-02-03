package ru.gb.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="users_roles",
//            joinColumns=  @JoinColumn(name="users_id", referencedColumnName="id"),
//            inverseJoinColumns= @JoinColumn(name="roles_id", referencedColumnName="id") )
    private List<Role> roles= new ArrayList<>();

    public User(){}

    public User(String name, String password, List<Role> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }
}
