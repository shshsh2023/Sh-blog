package com.sh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sh.dao.WebInfoMapper;
import com.sh.entity.WebInfo;
import com.sh.service.WebInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站信息表 服务实现类
 * </p>
 *
 * @author sara
 * @since 2021-09-14
 */
@Service
public class WebInfoServiceImpl extends ServiceImpl<WebInfoMapper, WebInfo> implements WebInfoService {

}
