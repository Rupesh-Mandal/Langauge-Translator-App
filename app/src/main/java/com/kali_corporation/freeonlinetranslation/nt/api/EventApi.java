package com.kali_corporation.freeonlinetranslation.nt.api;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class EventApi implements IRequestApi,IRequestType {
    @Override
    public String getApi() {
        return "submit/data";
    }
    @Override
    public BodyType getType() {
        // 上传文件需要使用表单的形式提交
        return BodyType.JSON;
    }
    private String amoracc,amorpd,amorack;
    public EventApi setAmoracc(String amoracc) {
        this.amoracc = amoracc;
        return this;
    }
    public EventApi setAmorpd(String amorpd) {
        this.amorpd = amorpd;
        return this;
    }
    public EventApi setAmorack(String amorack) {
        this.amorack = amorack;
        return this;
    }


}

