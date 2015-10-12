package com.oilMap.server.bsfc;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by SungGeun on 2015-10-12.
 */
@Service
public class BsfcServiceImpl extends SqlSessionDaoSupport implements BsfcSerivce{

    @Override
    public Bsfc select(Bsfc bsfc) {
        return getSqlSession().selectOne("bsfc.select", bsfc);
    }

}
