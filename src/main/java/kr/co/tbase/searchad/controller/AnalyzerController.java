package kr.co.tbase.searchad.controller;

import kr.co.tbase.searchad.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AnalyzerController {

    private final AnalyzeService analyzeService;

    // 실시간 검색 결과 페이지로 이동
    @GetMapping("/search")
    public String searchPage(Model model, @RequestParam(defaultValue = "") String keyword) {
        if(!keyword.equals("")){
            model.addAttribute("searchList", analyzeService.search(keyword));
        }
        return "search";
    }

    // 호스트 통계 페이지로 이동
    @GetMapping("/host")
    public String hostPage(Model model, @RequestParam(defaultValue = "") String keyword) {
        if(!keyword.equals("")){
            model.addAttribute("hostList", analyzeService.getHostList(keyword));
        }
        return "host";
    }

    // 단어통계 페이지로 이동
    @GetMapping("/relatedwords")
    public String relatedWordListPage() {
        return "relatedWordList";
    }

}
