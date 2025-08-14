package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import pvt.example.mapper.BaseMapper;
import pvt.example.service.BaseService;

import javax.annotation.Resource;

/**
 * 信息：src/java/pvt/example/service/impl/BaseServiceImpl.java
 * <p>日期：2025/7/31
 * <p>描述：
 */
@Service
public class BaseServiceImpl implements BaseService {
    @Resource
    private BaseMapper baseMapper;


}
