package com.sh.im.http.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 单聊记录
 * </p>
 *
 * @author sara
 * @since 2021-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_user_message")
public class ImChatUserMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送ID
     */
    @TableField("from_id")
    private Integer fromId;

    /**
     * 接收ID
     */
    @TableField("to_id")
    private Integer toId;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 是否已读[0:未读，1:已读]
     */
    @TableField("message_status")
    private Boolean messageStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


}
