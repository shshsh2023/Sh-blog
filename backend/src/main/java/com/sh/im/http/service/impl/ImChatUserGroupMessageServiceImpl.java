package com.sh.im.http.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sh.im.http.dao.ImChatUserGroupMessageMapper;
import com.sh.im.http.entity.ImChatUserGroupMessage;
import com.sh.im.http.service.ImChatUserGroupMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊记录 服务实现类
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Service
public class ImChatUserGroupMessageServiceImpl extends ServiceImpl<ImChatUserGroupMessageMapper, ImChatUserGroupMessage> implements ImChatUserGroupMessageService {

}
