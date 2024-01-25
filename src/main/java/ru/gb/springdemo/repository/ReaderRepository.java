package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Reader;

//@NoRepositoryBean
@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
}
