package kr.co.tbase.searchad.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY)
    private List<Search> searchList = new ArrayList<Search>();

    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY)
    private List<Host> hostList = new ArrayList<Host>();

    public void updateList(List<Search> searchList, List<Host> hostList) {
        this.searchList = searchList;
        this.hostList = hostList;
    }

    @Builder
    public Keyword(String name) {
        this.name = name;
    }
}
