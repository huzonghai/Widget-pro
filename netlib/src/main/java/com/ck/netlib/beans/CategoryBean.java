package com.ck.netlib.beans;

import java.util.List;

/**
 * Created by yt on 2016/12/14.
 */

public class CategoryBean {


    /**
     * topics : [{"id":2749,"countryid":36,"topicid":114,"topicicon":"/2017-09-06/3937f459b2ad56d26dabc301948ee170.png","topicname":"推荐","topictype":1,"keywords":null,"hubiiid":"","createuid":null,"createtime":1504685684000,"rank":-1},{"id":2755,"countryid":36,"topicid":7,"topicicon":"/2017-09-06/99db99e48e905ae02f168bc6f357eb63.jpg","topicname":"旅游","topictype":0,"keywords":"","hubiiid":"522504d18588881182161cd7","createuid":2,"createtime":1508321929000,"rank":7},{"id":2756,"countryid":36,"topicid":8,"topicicon":"/2017-09-06/e4f49c8af62486622b0822a9b9d3d74d.jpg","topicname":"汽车","topictype":0,"keywords":"","hubiiid":"522504d18588881182161cd8","createuid":2,"createtime":1508321929000,"rank":8},{"id":2759,"countryid":36,"topicid":23,"topicicon":"/2017-09-06/f0d6ccc1bc7f1f91116267d9a93a8862.jpg","topicname":"游戏","topictype":0,"keywords":"","hubiiid":"","createuid":2,"createtime":1508321929000,"rank":11}]
     * status : 0
     * msg : Success
     * runtime : 0
     */

    private int status;
    private String msg;
    private List<TopicsBean> topics;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public static class TopicsBean {
        /**
         * id : 2749
         * countryid : 36
         * topicid : 114
         * topicicon : /2017-09-06/3937f459b2ad56d26dabc301948ee170.png
         * topicname : 推荐
         * topictype : 1
         * keywords : null
         * hubiiid :
         * createuid : null
         * createtime : 1504685684000
         * rank : -1
         */

        private int topicid;
        private String topicname;

        public int getTopicid() {
            return topicid;
        }

        public void setTopicid(int topicid) {
            this.topicid = topicid;
        }

        public String getTopicname() {
            return topicname;
        }

        public void setTopicname(String topicname) {
            this.topicname = topicname;
        }

        @Override
        public String toString() {
            return "TopicsBean{" +
                    "topicid=" + topicid +
                    ", topicname='" + topicname + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", topics=" + topics +
                '}';
    }
}
