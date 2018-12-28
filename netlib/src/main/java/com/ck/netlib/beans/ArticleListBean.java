package com.ck.netlib.beans;


import java.io.Serializable;

public class ArticleListBean implements Serializable {

    /**
     * id : 54504913
     * abstracts :
     * articleid : ae263b7d717bce464f62155f
     * countrycode : RU
     * subtime : 1525936891314
     * title : Обновление Windows 10 April 2018 Update: новые функции и как обновить
     * titlepic : http://cdn.img.coolook.org/2018-05-10/34d0517be2eac6b04c7ca5dacf597d34.png!240|http://cdn.img.coolook.org/2018-05-10/e80585b5c49b559e13d5080024a20c65.png!240|http://cdn.img.coolook.org/2018-05-10/f0bdf619fc5e33b47fe2cbcdae2b3896.png!240
     * topicsid : 69
     * langid : 2
     * linkurl : http://www.coolooknews.com/news/54504913.html
     * attr : 1
     * author : null
     * channelid : 6
     * createtime : 1525936891314
     * createuid : 0
     * ctype : 0
     * imgcount : 0
     * keywords :
     * latitude : 0
     * longitude : 0
     * medialink : null
     * publicationid : 10004
     * rank : 0
     * source : null
     * sourceurl : https://windowsten.ru/windows-10-april-2018-update-chto-novogo-i-kak-ustanovit/
     * status : 0
     * subuid : 0
     * writer : null
     * sourcetitilepic : null
     * pushnum : 0
     * pushtime : null
     */

    private int id;
    private String articleid;
    private String countrycode;
    private long subtime;
    private String title;
    private String titlepic;
    private int topicsid;
    private String langid;
    private String linkurl;
    //    private Object author;
    private int channelid;
    private int imgcount;
    private String rank;
    private String source;
    private String sourceurl;
    private int status;
    private int subuid;
//    private Object writer;
    private int ctype;

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public long getSubtime() {
        return subtime;
    }

    public void setSubtime(long subtime) {
        this.subtime = subtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlepic() {
        return titlepic;
    }

    public void setTitlepic(String titlepic) {
        this.titlepic = titlepic;
    }

    public int getTopicsid() {
        return topicsid;
    }

    public void setTopicsid(int topicsid) {
        this.topicsid = topicsid;
    }

    public String getLangid() {
        return langid;
    }

    public void setLangid(String langid) {
        this.langid = langid;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public int getChannelid() {
        return channelid;
    }

    public void setChannelid(int channelid) {
        this.channelid = channelid;
    }

    public int getImgcount() {
        return imgcount;
    }

    public void setImgcount(int imgcount) {
        this.imgcount = imgcount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubuid() {
        return subuid;
    }

    public void setSubuid(int subuid) {
        this.subuid = subuid;
    }



    private int itemType;
    private String[] imgs;

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }
}
