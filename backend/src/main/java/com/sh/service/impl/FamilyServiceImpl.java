package com.sh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sh.dao.FamilyMapper;
import com.sh.entity.Family;
import com.sh.service.FamilyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 家庭信息 服务实现类
 * </p>
 *
 * @author sara
 * @since 2023-01-03
 */
@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements FamilyService {

}
