package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    List<User> findByNameContaining(String keyword, Pageable pageable);
}
