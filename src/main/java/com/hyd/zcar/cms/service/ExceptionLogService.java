package com.hyd.zcar.cms.service;

import com.hyd.zcar.cms.Dao.ExceptionLogDaoJpaRepository;
import com.hyd.zcar.cms.entity.ExceptionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExceptionLogService {
    @Autowired
    private ExceptionLogDaoJpaRepository exceptionLogDaoJpaRepository;



    public void insert(ExceptionLog excepLog) {
        exceptionLogDaoJpaRepository.save(excepLog);
    }
}
