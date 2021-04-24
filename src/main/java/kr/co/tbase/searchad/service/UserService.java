package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.dto.UserJoinRequestDto;
import kr.co.tbase.searchad.dto.UserListResponseDto;
import kr.co.tbase.searchad.entity.Role;
import kr.co.tbase.searchad.entity.User;
import kr.co.tbase.searchad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        return userRepository.findByUid(uid).orElseThrow(()-> new UsernameNotFoundException(uid));
    }

    @Transactional
    public Long save(UserJoinRequestDto userJoinRequestDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userJoinRequestDto.setPassword(encoder.encode(userJoinRequestDto.getPassword()));

        User user = userRepository.save(User.builder()
                .uid(userJoinRequestDto.getUid())
                .password(userJoinRequestDto.getPassword())
                .name(userJoinRequestDto.getName())
                .role(Role.USER)
                .build());
        return user.getId();
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 아이디가 존재하지 않습니다. id="+id));
        userRepository.delete(user);
    }

    // 사용자 검색 기능
    @Transactional(readOnly = true)
    public List<UserListResponseDto> search(String keyword, Pageable pageable) {
        List<User> userList = userRepository.findByNameContaining(keyword, pageable);
        List<UserListResponseDto> result = new ArrayList<>();
        userList.forEach(user -> {
            UserListResponseDto dto = UserListResponseDto.builder()
                    .id((long) userList.indexOf(user)+ 10L *pageable.getPageNumber()+1L)
                    .uid(user.getUid())
                    .name(user.getName())
                    .build();
            result.add(dto);
        });
        return result;
    }

    @Transactional
    public Page<User> getUserList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public Boolean checkPrevious(Pageable pageable) {
        Page<User> saved = getUserList(pageable);
        return saved.hasPrevious();
    }

    @Transactional
    public Boolean checkNext(Pageable pageable) {
        Page<User> saved = getUserList(pageable);
        return saved.hasNext();
    }
}
