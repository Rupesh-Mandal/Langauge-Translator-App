package com.kali_corporation.freeonlinetranslation.fee.bean.modul;

import java.io.Serializable;

public class ModuleBean implements Serializable {
    /**
     * moduleName : 系统
     * name : 系统
     * code : 000
     * showType : 0
     */

    private String moduleName;
    private String name;
    private String code;
    private int showType;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }
}
