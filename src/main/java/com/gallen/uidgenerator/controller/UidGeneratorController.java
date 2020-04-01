package com.gallen.uidgenerator.controller;

import com.baidu.fsg.uid.UidGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述
 *
 * @author LBG - 2019/5/7
 */
@RestController
public class UidGeneratorController {

    @Resource
    private UidGenerator uidGenerator;

    @RequestMapping(value = "/uidGenerator", method = RequestMethod.GET)
    public String getUid() {
        return String.valueOf(uidGenerator.getUID());
    }
}

