package kr.co.tbase.searchad.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDto {

    private Long id;
    private String url;
    private String title;
    private int keyword_cnt;

    @Builder
    public SearchResponseDto(Long id, String url, String title, int keyword_cnt) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.keyword_cnt = keyword_cnt;
    }

}
