package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Search, Long> {
}
