package kr.co.tbase.searchad.service;

import kr.co.tbase.searchad.dto.HostResponseDto;
import kr.co.tbase.searchad.dto.SearchResponseDto;
import kr.co.tbase.searchad.entity.Host;
import kr.co.tbase.searchad.entity.Keyword;
import kr.co.tbase.searchad.entity.Search;
import kr.co.tbase.searchad.repository.HostRepository;
import kr.co.tbase.searchad.repository.KeywordRepository;
import kr.co.tbase.searchad.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AnalyzeService {

    private final SearchRepository searchRepository;
    private final HostRepository hostRepository;
    private final KeywordRepository keywordRepository;
    private final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=";

    // 크롤링해서 받아오는 url을 깔끔한 url로 만들기
    private static String makeCleanUrl(String url) {
        int idx = url.indexOf(" ");
        if (idx == -1) {
            return url;
        }
        return url.substring(0, idx);
    }

    // url을 host로 만들기
    private static String makeUrlToHost(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }

    // 실시간 검색
    @Transactional
    public List<SearchResponseDto> search(String keyword) {
        // 웹으로 뿌려줄 SearchResponseDto 리스트 초기화
        List<SearchResponseDto> result = new ArrayList<>();

        // 검색한 키워드가 db에 존재하는지 파악
        Optional<Keyword> optionalKeyword = keywordRepository.findByName(keyword);

        optionalKeyword.ifPresentOrElse(keywordEntity -> {
            // 1. 검색한 키워드가 이미 db에 존재할 때

            // 1-1. host db에 누적횟수를 증가시키고
            List<Host> hostList = keywordEntity.getHostList();
            hostList.forEach(Host::accumulate);

            // 1-2. 초기화 해뒀던 SearchResponseDto 변수에 반복문으로 값 add
            List<Search> searchList = keywordEntity.getSearchList();
            AtomicInteger cnt = new AtomicInteger();
            cnt.set(0);
            searchList.forEach(search -> {
                SearchResponseDto dto = SearchResponseDto.builder()
                        .id((long) cnt.incrementAndGet())
                        .url(search.getUrl())
                        .title(search.getTitle())
                        .keyword_cnt(search.getKeyword_cnt())
                        .build();
                result.add(dto);
            });
        }, ()-> {
            // 2. 검색한 키워드가 db에 존재하지 않아서 크롤링해야할 때
            Keyword keywordEntity = Keyword.builder()
                    .name(keyword)
                    .build();
            Keyword savedKeyword = keywordRepository.save(keywordEntity);

            List<String> urlList = new ArrayList<>();
            List<String> host_urlList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            List<String> contentList = new ArrayList<>();


            // 2-1. 크롤링해온 데이터를 Keyword, Search, Host Entity에 새롭게 save
            Connection conn = Jsoup.connect(GOOGLE_SEARCH_URL+keyword);
            try {
                Document doc = conn.get();
                Elements el_class_g_list = doc.getElementsByClass("g");
                el_class_g_list.forEach(el_class_g -> {
                    Elements el_tag_a_list = el_class_g.getElementsByTag("a");
                    el_tag_a_list.forEach(el_tag_a -> {
                        Elements el_tag_url_list = el_tag_a.getElementsByTag("cite");
                        el_tag_url_list.forEach(el_tag_url -> {
                            urlList.add(el_tag_url.text());
                        });
                        Elements el_tag_title_list = el_tag_a.getElementsByTag("h3");
                        el_tag_title_list.forEach(el_tag_title -> {
                            titleList.add(el_tag_title.text());
                        });
                    });
                });
                // VwiC3b yXK7lf MUxGbd yDYNvb lyLwlc
                Elements el_class_content_list = doc.getElementsByClass("IsZvec");
                el_class_content_list.forEach(el_class_content -> {
                    contentList.add(el_class_content.text());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < contentList.size(); i++) {
                Pattern pattern = Pattern.compile(keyword);
                Matcher matcher = pattern.matcher(contentList.get(i));
                int keyword_cnt = 0;
                while(matcher.find()){
                    keyword_cnt++;
                }
                String url = makeCleanUrl(urlList.get(i));
                String host = makeUrlToHost(url);
                host_urlList.add(host);
                Search searchEntity = Search.builder()
                        .url(url)
                        .host(host)
                        .title(titleList.get(i))
                        .content(contentList.get(i))
                        .keyword_cnt(keyword_cnt)
                        .keyword(savedKeyword)
                        .build();
                searchRepository.save(searchEntity);

                // 2-2. 초기화 해뒀던 SearchResponseDto 변수에 반복문으로 값 add
                SearchResponseDto dto = SearchResponseDto.builder()
                        .id((long) (i+1))
                        .url(url)
                        .title(titleList.get(i))
                        .keyword_cnt(keyword_cnt)
                        .build();
                result.add(dto);
            }
            host_urlList.forEach(host_url -> {
                Optional<Host> optionalHost = hostRepository.findByNameAndKeywordId(host_url, savedKeyword);
                optionalHost.ifPresentOrElse(selectHost->{
                    selectHost.plus();
                    hostRepository.save(selectHost);
                },()->{
                    Host hostEntity = Host.builder()
                            .name(host_url)
                            .cnt(1)
                            .acc_cnt(1)
                            .keyword(savedKeyword)
                            .build();
                    hostRepository.save(hostEntity);
                });
            });
        });
        return result;
    }

    // 호스트 통계 결과
    @Transactional
    public List<HostResponseDto> getHostList(String keyword, String sort) {
        List<HostResponseDto> result = new ArrayList<>();
        AtomicInteger cnt = new AtomicInteger();
        cnt.set(0);
        List<Keyword> keywordList = keywordRepository.findByNameContaining(keyword);
        keywordList.forEach(keywordEntity -> {
            List<Host> hostList = keywordEntity.getHostList();
            hostList.forEach(host -> {
                HostResponseDto hostResponseDto = HostResponseDto.builder()
                        .id((long) cnt.incrementAndGet())
                        .host(host.getName())
                        .keyword(keywordEntity.getName())
                        .acc_cnt(host.getAcc_cnt())
                        .build();
                result.add(hostResponseDto);
            });
        });
        Collections.sort(result);
        if (sort.equals("desc")) {
            Collections.reverse(result);
        }
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setId((long) i+1);
        }
        return result;
    }
}
