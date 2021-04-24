package kr.co.tbase.searchad.repository;

import kr.co.tbase.searchad.entity.Host;
import kr.co.tbase.searchad.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    @Query("SELECT h FROM Host h WHERE h.name = :name and h.keyword = :keyword")
    Optional<Host> findByNameAndKeywordId(@Param("name") String name, @Param("keyword") Keyword keyword);
}
