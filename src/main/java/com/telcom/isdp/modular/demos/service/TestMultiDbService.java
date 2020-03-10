package com.telcom.isdp.modular.demos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestMultiDbService {

    @Autowired
    private IsdpDbService isdpDbService;

    @Autowired
    private OtherDbService otherDbService;

    @Transactional(rollbackFor = Exception.class)
    public void beginTest() {

        isdpDbService.isdpdb();

        otherDbService.otherdb();
    }
}
