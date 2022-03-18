package com.kuang.ucenter.controller.inside;

import com.kuang.springcloud.utils.R;
import com.kuang.ucenter.service.UserCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inside/collection")
@Slf4j
public class InsideCollectionController {

    @Resource
    private UserCollectService collectService;

    //查询用户是否收藏了某个文章
    @GetMapping("findUserIsCollection")
    public R findUserIsCollection(String articleId , String userId){
        log.info("查询用户是否收藏了文章,文章id:" + articleId + ",用户id:" + userId);
        boolean isCollection = collectService.findUserIsCollection(articleId , userId);
        return R.ok().data("isCollection" , isCollection);
    }

}
