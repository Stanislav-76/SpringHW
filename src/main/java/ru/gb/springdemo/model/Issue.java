package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * Запись о факте выдачи книги (в БД)
 */
@Data
// @Entity
public class Issue {

  public static long sequence = 1L;

  private final long id;
  private final long bookId;
  private final long readerId;

  /**
   * Дата выдачи
   */
  private final LocalDateTime issued_at;
  private  LocalDateTime returned_at;

  @JsonCreator
  public Issue(long bookId, long readerId) {
    this.id = sequence++;
    this.bookId = bookId;
    this.readerId = readerId;
    this.issued_at = LocalDateTime.now();
    this.returned_at = null;
  }

}
