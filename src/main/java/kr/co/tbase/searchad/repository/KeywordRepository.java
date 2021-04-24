package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByName(String keyword);

    List<Keyword> findByNameContaining(String keyword);
}
