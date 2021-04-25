package kr.co.tbase.searchad.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class Host extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int cnt;

    @Column(nullable = false)
    private int acc_cnt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Keyword keyword;

    public void accumulate() {
        this.acc_cnt += this.cnt;
    }

    public void plus() {
        this.acc_cnt += 1;
        this.cnt += 1;
    }

    @Builder
    public Host(String name, int cnt, int acc_cnt, Keyword keyword) {
        this.name = name;
        this.cnt = cnt;
        this.acc_cnt = acc_cnt;
        this.keyword = keyword;
    }

}
