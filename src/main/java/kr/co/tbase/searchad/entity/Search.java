package kr.co.tbase.searchad.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private int keyword_cnt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Keyword keyword;

    @Builder
    public Search(String url, String host, String title, String content, int keyword_cnt, Keyword keyword) {
        this.url = url;
        this.host = host;
        this.title = title;
        this.content = content;
        this.keyword_cnt = keyword_cnt;
        this.keyword = keyword;
    }

}
