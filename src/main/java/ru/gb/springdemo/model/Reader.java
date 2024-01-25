package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="readers")
@Data
public class Reader {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @Column(name="name")
  private String name;

  public Reader() {}

  public Reader(String name) {
    this.name = name;
  }

}
