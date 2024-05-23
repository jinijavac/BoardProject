package org.example.boardproject.repository;

import org.example.boardproject.domain.Board;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Board, Long> {
}
