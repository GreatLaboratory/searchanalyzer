package kr.co.tbase.searchad.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;;

@Getter
@Setter
public class UserJoinRequestDto {

    @NotEmpty
    private String uid;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;
}
