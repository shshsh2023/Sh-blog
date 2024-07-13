package com.sh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sh.config.PoetryResult;
import com.sh.entity.Comment;
import com.sh.vo.BaseRequestVO;
import com.sh.vo.CommentVO;


/**
 * <p>
 * 文章评论表 服务类
 * </p>
 *
 * @author sara
 * @since 2021-08-13
 */
public interface CommentService extends IService<Comment> {

    PoetryResult saveComment(CommentVO commentVO);

    PoetryResult deleteComment(Integer id);

    PoetryResult<BaseRequestVO> listComment(BaseRequestVO baseRequestVO);

    PoetryResult<Page> listAdminComment(BaseRequestVO baseRequestVO, Boolean isBoss);
}
