package com.example.demo.src.crawling;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.crawling.model.GetNewsArticleReq;
import com.example.demo.src.crawling.model.GetNewsArticleRes;
import com.example.demo.src.crawling.model.GetNewsListReq;
import com.example.demo.src.post.PostService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CrawlingProvider crawlingProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;


    public CrawlingController(CrawlingProvider crawlingProvider, PostService postService, JwtService jwtService) {
        this.crawlingProvider = crawlingProvider;
        this.postService = postService;
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
            // 이거 나중에 수정
            if (getNewsListReq.getKeyword().length() <1) {
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
            if (getNewsListReq.getPage() == 0) {
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_EMPTY_IMGURL);
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

            // 이거 나중에 수정
            if (getNewsArticleReq.getUrl().length() <1) {
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }

//            System.out.println(getNewsArticleReq.getUrl());

            GetNewsArticleRes getNewsArticleRes = crawlingProvider.getArticleByUrl(getNewsArticleReq.getUrl());

            return new BaseResponse<>(getNewsArticleRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
