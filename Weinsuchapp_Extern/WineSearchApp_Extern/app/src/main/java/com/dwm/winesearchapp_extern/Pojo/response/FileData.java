package com.dwm.winesearchapp_extern.Pojo.response;

import com.google.gson.annotations.SerializedName;

public class FileData {

    @SerializedName("FileName")
    public String fileName;

    @SerializedName("MimeType")
    public String mimeType;

    @SerializedName("FileData")
    public String fileData;

}
