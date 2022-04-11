package com.kuang.bbs.controller;


import com.kuang.bbs.entity.Column;
import com.kuang.bbs.entity.vo.ColumnArticleVo;
import com.kuang.bbs.entity.vo.ColumnDetailVo;
import com.kuang.bbs.entity.vo.ColumnVo;
import com.kuang.bbs.entity.vo.UpdateColumnVo;
import com.kuang.bbs.service.ColumnService;
import com.kuang.bbs.service.ColunmArticleService;
import com.kuang.bbs.utils.ColumnUtils;
import com.kuang.springcloud.exceptionhandler.XiaoXiaException;
import com.kuang.springcloud.utils.JwtUtils;
import com.kuang.springcloud.utils.R;
import com.kuang.springcloud.utils.RedisUtils;
import com.kuang.springcloud.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/bbs/column")
@Slf4j
public class ColumnController {

    @Resource
    private ColumnService columnService;

    @Resource
    private ColunmArticleService colunmArticleService;

    //创建专栏
    @PostMapping("addUserColumn")
    public R addUserColumn(String title , String nickname , String avatar , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(title) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(avatar)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Column column = columnService.addUserColumn(userId , nickname , avatar , title);
        return R.ok().data("columnId" , column.getId());
    }

    //查询用户专栏
    @GetMapping("findUserColumn")
    public R findUserColumn(HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        List<ColumnVo> columnVoList = columnService.findUserColumn(userId);
        return R.ok().data("columnList" , columnVoList);
    }

    //查询他人专栏
    @GetMapping("findOtherUserColumn")
    public R findOtherUserColumn(String userId){
        //校验数据
        if(StringUtils.isEmpty(userId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        List<ColumnVo> columnVoList = columnService.findOtherUserColumn(userId);
        return R.ok().data("columnList" , columnVoList);
    }

    //查询专栏具体数据
    @GetMapping("findColumnDetail")
    public R findColumnDetail(HttpServletRequest request , String columnId){
        //取出用户id和校验专栏id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(columnId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }


        //校验是否可以访问
        Future<Boolean> userAbility = columnService.checkUserAbility(userId , columnId);
        //查出专栏数据
        ColumnDetailVo columnDetailVo = columnService.findColumnDetail(columnId);
        //设置专栏浏览量
        columnService.setColunmViews(columnId , request.getRemoteAddr());
        //查出专栏文章
        List<ColumnArticleVo> columnArticleVoList = colunmArticleService.findColumnArticle(columnId);

        boolean flag = false;
        try {
            flag = userAbility.get();
        }catch(Exception e){
            log.warn("校验用户是否可以访问失败");
        }
        if(!flag){
            throw new XiaoXiaException(ResultCode.ERROR , "没有权限访问该专栏");
        }

        long setSize = RedisUtils.getSetSize(columnDetailVo.getColumnId());
        columnDetailVo.setViews(setSize + columnDetailVo.getViews());
        return R.ok().data("columnDetail" , columnDetailVo).data("columnArticleList" , columnArticleVoList);
    }

    //删除专栏，删除专栏也会将下面的文章给删除
    @PostMapping("deleteColumn")
    public R deleteColumn(HttpServletRequest request , String columnId){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(columnId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        columnService.deleteColumn(userId , columnId);
        return R.ok();
    }

    //修改专栏数据
    @PostMapping("updateColumn")
    public R updateColumn(UpdateColumnVo updateColumnVo , String columnId , HttpServletRequest request){
        //校验数据
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == null || StringUtils.isEmpty(columnId)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }
        Long vsibility = updateColumnVo.getVsibility();
        //如果vsibility不为null，且不在数字限定范围内，则抛出异常
        if(vsibility != null && !ColumnUtils.vsibility.contains(vsibility)){
            throw new XiaoXiaException(ResultCode.ERROR , "请正确操作");
        }

        columnService.updateColumn(updateColumnVo , columnId , userId);
        return R.ok();
    }

}

