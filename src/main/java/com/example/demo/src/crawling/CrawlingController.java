package com.example.demo.src.crawling;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.crawling.model.GetNewsArticleReq;
import com.example.demo.src.crawling.model.GetNewsArticleRes;
import com.example.demo.src.crawling.model.GetNewsListReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CrawlingProvider crawlingProvider;
    @Autowired
    private final CrawlingService crawlingService;
    @Autowired
    private final JwtService jwtService;


    public CrawlingController(CrawlingProvider crawlingProvider, CrawlingService crwalingService, JwtService jwtService) {
        this.crawlingProvider = crawlingProvider;
        this.crawlingService = crwalingService;
        this.jwtService = jwtService;

    }


    /**
     * 뉴스리스트API
     * [POST] /crawling/newsList
     * @return BaseResponse<ArrayList<ArrayList<String>>>
     */
    @ResponseBody
    @PostMapping("/newsList")
    public BaseResponse<ArrayList<ArrayList<String>>> createNewsList(@RequestBody GetNewsListReq getNewsListReq) {
        try {
            //jwt에서 idx 추출.
            //getUserIdx 타고 들가면 거기서 jwt 키 있는지 없는지 유효성 검사 실행함
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = getNewsListReq.getUserIdx();

            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 이거 나중에 수정
            if (getNewsListReq.getKeyword() == null) {

                Random random = new Random(); // 랜덤 객체 생성
                random.setSeed(System.currentTimeMillis());
                int ramdomNum = random.nextInt(28);

                ArrayList<String> KeywordList = new ArrayList<String>(List.of(
                        "국회행정지원 조사처 기본경비",
                        "국회행정지원 예산처 기본경비",
                        "국회행정지원 도서관 기본경비",
                        "국회행정지원 사무처 기본경비",
                        "국회행정지원 조사처 인건비",
                        "국회행정지원 예산처 인건비",
                        "국회행정지원 도서관 인건비",
                        "국회행정지원 사무처 인건비",
                        "의정활동지원 의정지원",
                        "의정활동지원 예비금",
                        "의정활동지원 국회활동관련단체지원",
                        "의정활동지원 의회외교",
                        "의정활동지원 위원회운영지원",
                        "입법조사처운영 입법조사처 정보화",
                        "입법조사처운영 입법및정책조사분석",
                        "입법조사처운영 기획관리및운영지원",
                        "예산정책처운영 예산정책처정보화",
                        "예산정책처운영 국가재정경제분석및의안비용추계",
                        "예산정책처운영 기획관리및정책총괄지원",
                        "국회도서관운영 전자도서관운영",
                        "국회도서관운영 도서관자료확충및보존",
                        "국회도서관운영 입법정보지원",
                        "국회도서관운영 도서관운영지원",
                        "국회사무처운영 국회방송운영",
                        "국회사무처운영 의회운영교육 수입대체경비",
                        "국회사무처운영 입법정보화",
                        "국회사무처운영 국회청사확보및시설관리",
                        "국회사무처운영 법제활동지원",
                        "국회사무처운영 의회운영지원"
                ));
                getNewsListReq.setKeyword(KeywordList.get(ramdomNum));

            }
            if (getNewsListReq.getPage() == 0) {
                return new BaseResponse<>(BaseResponseStatus.POST_CRAWLING_EMPTY_PAGE_INFO);
            }

            ArrayList<ArrayList<String>> getNewsList10Res = crawlingProvider.retrieveNewsList(getNewsListReq.getKeyword(), getNewsListReq.getPage());

            return new BaseResponse<ArrayList<ArrayList<String>>>(getNewsList10Res);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 뉴스기사API
     * [POST] /crawling/article
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/article")
    public BaseResponse<GetNewsArticleRes> createArticle(@RequestBody GetNewsArticleReq getNewsArticleReq) {
        try {
            //jwt에서 idx 추출.
            //getUserIdx 타고 들가면 거기서 jwt 키 있는지 없는지 유효성 검사 실행함
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = getNewsArticleReq.getUserIdx();

            // 실제 Idx와 jwt로 추출한 Idx가 맞는지 유효성검사
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // 이거 나중에 수정
            if (getNewsArticleReq.getUrl().length() < 1) {
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }

            //DB에 해당 키워드 Stack 1씩 쌓기
            crawlingService.stackKeyword(getNewsArticleReq);

            //url에 접근하여 타이틀,날짜,기사내용 파싱
            GetNewsArticleRes getNewsArticleRes = crawlingProvider.getArticleByUrl(getNewsArticleReq.getUrl());

            return new BaseResponse<>(getNewsArticleRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
