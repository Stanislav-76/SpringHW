package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="books")
@Data
public class Book {

//  public static long sequence = 1L;
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;
  @Column(name="name")
  private String name;

  public Book() {}

  public Book(String name) {
    this.name = name;
  }

}
