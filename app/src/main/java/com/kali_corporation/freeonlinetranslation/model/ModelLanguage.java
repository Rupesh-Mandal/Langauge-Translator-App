package com.kali_corporation.freeonlinetranslation.model;

import java.io.Serializable;

public class ModelLanguage implements Serializable {
    String lang_code;
    String language;

    public ModelLanguage(String language2, String lang_code2) {
        this.language = language2;
        this.lang_code = lang_code2;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getLang_code() {
        return this.lang_code;
    }
}
