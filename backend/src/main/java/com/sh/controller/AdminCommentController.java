package com.sh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sh.aop.LoginCheck;
import com.sh.config.PoetryResult;
import com.sh.entity.Article;
import com.sh.entity.Comment;
import com.sh.enums.CommentTypeEnum;
import com.sh.service.ArticleService;
import com.sh.service.CommentService;
import com.sh.utils.PoetryUtil;
import com.sh.vo.BaseRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 后台评论 前端控制器
 * </p>
 *
 * @author sara
 * @since 2021-08-13
 */
@RestController
@RequestMapping("/admin")
public class AdminCommentController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    /**
     * 作者删除评论
     */
    @GetMapping("/comment/user/deleteComment")
    @LoginCheck(1)
    public PoetryResult userDeleteComment(@RequestParam("id") Integer id) {
        Comment comment = commentService.lambdaQuery().select(Comment::getSource, Comment::getType).eq(Comment::getId, id).one();
        if (comment == null) {
            return PoetryResult.success();
        }
        if (!CommentTypeEnum.COMMENT_TYPE_ARTICLE.getCode().equals(comment.getType())) {
            return PoetryResult.fail("权限不足！");
        }
        Article one = articleService.lambdaQuery().eq(Article::getId, comment.getSource()).select(Article::getUserId).one();
        if (one == null || (PoetryUtil.getUserId().intValue() != one.getUserId().intValue())) {
            return PoetryResult.fail("权限不足！");
        }
        commentService.removeById(id);
        return PoetryResult.success();
    }

    /**
     * Boss删除评论
     */
    @GetMapping("/comment/boss/deleteComment")
    @LoginCheck(0)
    public PoetryResult bossDeleteComment(@RequestParam("id") Integer id) {
        commentService.removeById(id);
        return PoetryResult.success();
    }

    /**
     * 用户查询评论
     */
    @PostMapping("/comment/user/list")
    @LoginCheck(1)
    public PoetryResult<Page> listUserComment(@RequestBody BaseRequestVO baseRequestVO) {
        return commentService.listAdminComment(baseRequestVO, false);
    }

    /**
     * Boss查询评论
     */
    @PostMapping("/comment/boss/list")
    @LoginCheck(0)
    public PoetryResult<Page> listBossComment(@RequestBody BaseRequestVO baseRequestVO) {
        return commentService.listAdminComment(baseRequestVO, true);
    }
}
