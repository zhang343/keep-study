package com.kuang.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.bbs.client.UcenterClient;
import com.kuang.bbs.client.VipClient;
import com.kuang.bbs.entity.ArticleRight;
import com.kuang.bbs.mapper.ArticleRightMapper;
import com.kuang.bbs.service.ArticleRightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.springcloud.entity.RightRedis;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.ResultCode;
import com.kuang.springcloud.utils.VipUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class ArticleRightServiceImpl extends ServiceImpl<ArticleRightMapper, ArticleRight> implements ArticleRightService {

    @Resource
    private VipClient vipClient;

    @Resource
    private UcenterClient ucenterClient;

    //修改用户当日文章权益
    @Transactional
    @Override
    public void updateArticleRight(String userId) {

        //取出用户权益
        RightRedis userRightRedis = VipUtils.getUserRightRedis(userId);
        if(userRightRedis == null){
            R rightRedisByUserId = vipClient.findRightRedisByUserId(userId);
            if(!rightRedisByUserId.getSuccess()){
                throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
            }
            userRightRedis = (RightRedis) rightRedisByUserId.getData().get("rightRedis");
        }


        //查询用户今日文章权益
        QueryWrapper<ArticleRight> wrapper = new QueryWrapper<>();
        wrapper.select("id" , "money" , "article_number" , "version");
        wrapper.eq("user_id" , userId);
        ArticleRight articleRight = baseMapper.selectOne(wrapper);
        if(articleRight.getArticleNumber() + 1 > userRightRedis.getArticleNumber()){
            throw new XiaoXiaException(ResultCode.ERROR , "今日文章发布数量已满");
        }
        articleRight.setArticleNumber(articleRight.getArticleNumber() + 1);

        if(articleRight.getMoney() + 10 > userRightRedis.getMoney()){
            articleRight.setMoney(null);
            int i = baseMapper.updateById(articleRight);
            if(i != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
            }
        }else {
            articleRight.setMoney(articleRight.getMoney() + 10);
            int i = baseMapper.updateById(articleRight);
            if(i != 1){
                throw new XiaoXiaException(ResultCode.ERROR , "发布文章失败");
            }
            //不强制一样
            ucenterClient.add(10 , userId);
        }
    }

    //插入文章权益，用户模块创建用户会用到
    @Override
    public void addArticleRight(String userId) {
        ArticleRight articleRight = new ArticleRight();
        articleRight.setUserId(userId);
        int insert = baseMapper.insert(articleRight);
        if(insert != 1){
            throw new XiaoXiaException(ResultCode.ERROR , "创建失败");
        }
    }
}
