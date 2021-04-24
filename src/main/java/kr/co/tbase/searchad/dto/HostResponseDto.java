package kr.co.tbase.searchad.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostResponseDto {
    private Long id;
    private String keyword;
    private String host;
    private int acc_cnt;

    @Builder
    public HostResponseDto(Long id, String keyword, String host, int acc_cnt) {
        this.id = id;
        this.keyword = keyword;
        this.host = host;
        this.acc_cnt = acc_cnt;
    }
}
