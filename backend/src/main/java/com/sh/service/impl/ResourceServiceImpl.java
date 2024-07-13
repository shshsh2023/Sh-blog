package com.sh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sh.dao.ResourceMapper;
import com.sh.entity.Resource;
import com.sh.service.ResourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源信息 服务实现类
 * </p>
 *
 * @author sara
 * @since 2022-03-06
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

}
