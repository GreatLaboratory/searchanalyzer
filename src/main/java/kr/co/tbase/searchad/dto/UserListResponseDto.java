package kr.co.tbase.searchad.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListResponseDto {
    private Long id;
    private String uid;
    private String name;

    @Builder
    public UserListResponseDto(Long id, String uid, String name) {
        this.id = id;
        this.uid = uid;
        this.name = name;
    }
}
