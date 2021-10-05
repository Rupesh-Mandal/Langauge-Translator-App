package com.kali_corporation.freeonlinetranslation.nt.server;

import com.hjq.http.config.IRequestServer;

public class MyServer implements IRequestServer {

    @Override
    public String getHost() {
        return "https://amor.depe.website/";
    }

    @Override
    public String getPath() {
        return "server_api/";
    }


}