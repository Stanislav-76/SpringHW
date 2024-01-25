package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.management.ConstructorParameters;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Запись о факте выдачи книги (в БД)
 */
@Entity
@Table(name="Issues")
@Data
// @Entity
public class Issue {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @Column(name="bookId")
  private Long bookId;
  @Column(name="readerId")
  private Long readerId;

  /**
   * Дата выдачи
   */
  @Column(name="issued_at")
  private final LocalDateTime issued_at= LocalDateTime.now();
  @Column(name="returned_at")
  private  LocalDateTime returned_at = null;

  public Issue(){}

  public Issue(long bookId, long readerId) {
    this.bookId = bookId;
    this.readerId = readerId;
  }

}
