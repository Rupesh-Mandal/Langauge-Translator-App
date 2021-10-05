package com.kali_corporation.freeonlinetranslation.nt.api;

import com.hjq.http.config.IRequestApi;

public class TrsApi implements IRequestApi {
    @Override
    public String getApi() {
        return "get/text";
    }
//    private String keyword;
//    public TrsApi setKeyword(String keyword) {
//        this.keyword = keyword;
//        return this;
//    }
}
