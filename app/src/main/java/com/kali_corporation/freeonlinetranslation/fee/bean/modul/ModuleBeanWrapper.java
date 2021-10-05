package com.kali_corporation.freeonlinetranslation.fee.bean.modul;

import java.io.Serializable;
import java.util.List;

public class ModuleBeanWrapper implements Serializable {


    private List<ModuleBean> modules;
    private List<ModuleBean> data;

    public List<ModuleBean> getModules() {
        return modules;
    }

    public void setModules(List<ModuleBean> modules) {
        this.modules = modules;
    }

    public List<ModuleBean> getData() {
        return data;
    }

    public void setData(List<ModuleBean> data) {
        this.data = data;
    }

}
