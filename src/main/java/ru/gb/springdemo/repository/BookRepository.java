package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Book;

//@NoRepositoryBean
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
