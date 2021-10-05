package com.kali_corporation.freeonlinetranslation.nt.api;

import com.hjq.http.config.IRequestApi;

public class PostApi implements IRequestApi {
    @Override
    public String getApi() {
        return "get/data";
    }
    private String keyword;
    public PostApi setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
