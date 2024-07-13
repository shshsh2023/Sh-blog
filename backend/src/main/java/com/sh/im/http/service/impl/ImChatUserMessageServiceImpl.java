package com.sh.im.http.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sh.im.http.dao.ImChatUserMessageMapper;
import com.sh.im.http.entity.ImChatUserMessage;
import com.sh.im.http.service.ImChatUserMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单聊记录 服务实现类
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Service
public class ImChatUserMessageServiceImpl extends ServiceImpl<ImChatUserMessageMapper, ImChatUserMessage> implements ImChatUserMessageService {

}
