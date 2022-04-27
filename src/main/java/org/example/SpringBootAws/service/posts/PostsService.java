package org.example.SpringBootAws.service.posts;

import lombok.RequiredArgsConstructor;
import org.example.SpringBootAws.domain.posts.Posts;
import org.example.SpringBootAws.domain.posts.PostsRepository;
import org.example.SpringBootAws.web.dto.PostsListResponseDto;
import org.example.SpringBootAws.web.dto.PostsResponseDto;
import org.example.SpringBootAws.web.dto.PostsSaveRequestDto;
import org.example.SpringBootAws.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.ToEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto  requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        // JPA의 영속성 컨텍스트에 의해 트랜젝션 안에서 데이터 베이스 데이터를 가져오면 그 데이터는 영속성 컨택스트가 유지된다.
        // 이 때 데이터 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영하기 때문에 db에  update 쿼리를 날릴 필요가 없다.(더티체킹)
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당게시글이 없습니다. id=" + id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);	//	{1}
    }


}
