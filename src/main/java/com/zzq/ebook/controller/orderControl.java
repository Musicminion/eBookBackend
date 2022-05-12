package com.zzq.ebook.controller;

import com.zzq.ebook.utils.message.Msg;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class orderControl {

    @RequestMapping("/order/addToChart")
    public Msg addToChart(@RequestBody Map<String, String> params){
        return null;
    }

    @RequestMapping("/order/buyOneImmediately")
    public Msg buyOneImmediately(@RequestBody Map<String, String> params){

        return null;
    }
}
