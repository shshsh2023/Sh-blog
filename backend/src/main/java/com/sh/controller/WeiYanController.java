package com.sh.controller;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sh.aop.LoginCheck;
import com.sh.aop.SaveCheck;
import com.sh.config.PoetryResult;
import com.sh.constants.CommonConst;
import com.sh.dao.ArticleMapper;
import com.sh.entity.Article;
import com.sh.entity.WeiYan;
import com.sh.enums.PoetryEnum;
import com.sh.service.WeiYanService;
import com.sh.utils.PoetryUtil;
import com.sh.utils.StringUtil;
import com.sh.vo.BaseRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 微言表 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-10-26
 */
@RestController
@RequestMapping("/weiYan")
public class WeiYanController {

    @Autowired
    private WeiYanService weiYanService;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 保存
     */
    @PostMapping("/saveWeiYan")
    @LoginCheck
    @SaveCheck
    public PoetryResult saveWeiYan(@RequestBody WeiYan weiYanVO) {
        if (!StringUtils.hasText(weiYanVO.getContent())) {
            return PoetryResult.fail("微言不能为空！");
        }

        String content = StringUtil.removeHtml(weiYanVO.getContent());
        if (!StringUtils.hasText(content)) {
            return PoetryResult.fail("微言内容不合法！");
        }
        weiYanVO.setContent(content);

        WeiYan weiYan = new WeiYan();
        weiYan.setUserId(PoetryUtil.getUserId());
        weiYan.setContent(weiYanVO.getContent());
        weiYan.setIsPublic(weiYanVO.getIsPublic());
        weiYan.setType(CommonConst.WEIYAN_TYPE_FRIEND);
        weiYanService.save(weiYan);
        return PoetryResult.success();
    }


    /**
     * 保存
     */
    @PostMapping("/saveNews")
    @LoginCheck
    public PoetryResult saveNews(@RequestBody WeiYan weiYanVO) {
        if (!StringUtils.hasText(weiYanVO.getContent()) || weiYanVO.getSource() == null) {
            return PoetryResult.fail("信息不全！");
        }

        if (weiYanVO.getCreateTime() == null) {
            weiYanVO.setCreateTime(LocalDateTime.now());
        }

        Integer userId = PoetryUtil.getUserId();

        LambdaQueryChainWrapper<Article> wrapper = new LambdaQueryChainWrapper<>(articleMapper);
        Integer count = wrapper.eq(Article::getId, weiYanVO.getSource()).eq(Article::getUserId, userId).count().intValue();

        if (count == null || count < 1) {
            return PoetryResult.fail("来源不存在！");
        }

        WeiYan weiYan = new WeiYan();
        weiYan.setUserId(userId);
        weiYan.setContent(weiYanVO.getContent());
        weiYan.setIsPublic(Boolean.TRUE);
        weiYan.setSource(weiYanVO.getSource());
        weiYan.setCreateTime(weiYanVO.getCreateTime());
        weiYan.setType(CommonConst.WEIYAN_TYPE_NEWS);
        weiYanService.save(weiYan);
        return PoetryResult.success();
    }

    /**
     * 查询List
     */
    @PostMapping("/listNews")
    public PoetryResult<BaseRequestVO> listNews(@RequestBody BaseRequestVO baseRequestVO) {
        if (baseRequestVO.getSource() == null) {
            return PoetryResult.fail("来源不能为空！");
        }
        LambdaQueryChainWrapper<WeiYan> lambdaQuery = weiYanService.lambdaQuery();
        lambdaQuery.eq(WeiYan::getType, CommonConst.WEIYAN_TYPE_NEWS);
        lambdaQuery.eq(WeiYan::getSource, baseRequestVO.getSource());
        lambdaQuery.eq(WeiYan::getIsPublic, PoetryEnum.PUBLIC.getCode());

        lambdaQuery.orderByDesc(WeiYan::getCreateTime).page(baseRequestVO);
        return PoetryResult.success(baseRequestVO);
    }

    /**
     * 删除
     */
    @GetMapping("/deleteWeiYan")
    @LoginCheck
    public PoetryResult deleteWeiYan(@RequestParam("id") Integer id) {
        Integer userId = PoetryUtil.getUserId();
        weiYanService.lambdaUpdate().eq(WeiYan::getId, id)
                .eq(WeiYan::getUserId, userId)
                .remove();
        return PoetryResult.success();
    }


    /**
     * 查询List
     */
    @PostMapping("/listWeiYan")
    public PoetryResult<BaseRequestVO> listWeiYan(@RequestBody BaseRequestVO baseRequestVO) {
        LambdaQueryChainWrapper<WeiYan> lambdaQuery = weiYanService.lambdaQuery();
        lambdaQuery.eq(WeiYan::getType, CommonConst.WEIYAN_TYPE_FRIEND);
        if (baseRequestVO.getUserId() == null) {
            if (PoetryUtil.getUserId() != null) {
                lambdaQuery.eq(WeiYan::getUserId, PoetryUtil.getUserId());
            } else {
                lambdaQuery.eq(WeiYan::getIsPublic, PoetryEnum.PUBLIC.getCode());
                lambdaQuery.eq(WeiYan::getUserId, PoetryUtil.getAdminUser().getId());
            }
        } else {
            if (!baseRequestVO.getUserId().equals(PoetryUtil.getUserId())) {
                lambdaQuery.eq(WeiYan::getIsPublic, PoetryEnum.PUBLIC.getCode());
            }
            lambdaQuery.eq(WeiYan::getUserId, baseRequestVO.getUserId());
        }

        lambdaQuery.orderByDesc(WeiYan::getCreateTime).page(baseRequestVO);
        return PoetryResult.success(baseRequestVO);
    }
}
