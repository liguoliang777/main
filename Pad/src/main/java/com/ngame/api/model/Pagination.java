package com.ngame.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/12/5.
 */

public class Pagination {
    private String limit;
    private String offset;
    @SerializedName(value="rows_found")
    private String rowsFond;
    private String since;
    private String until;

    public Pagination(){super();}
    public String getLimit(){return limit;}
    public String getOffset(){return offset;}
    public String getRowsFond(){return rowsFond;}
    public String getSince(){return since;}
    public String getUntil(){return until;}
    public void setLimit(String s){limit = s;}
    public void setOffset(String s){offset = s;}
    public void setRowsFond(String s){rowsFond = s;}
    public void setSince(String s){since = s;}
    public void setUntil(String s){until = s;}
}
