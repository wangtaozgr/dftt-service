package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;

/**
 * 
 *
 * @author twang
 */
public class PddMallVo extends BaseVo {

    /**
    * 
    */
    private String id;

    /**
     * 
     */
    private String mallId;
    /**
     * 
     */
    private String mallName;
    /**
     * 
     */
    private String mallLogo;
    /**
     * 
     */
    private String logoKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }
    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }
    public String getMallLogo() {
        return mallLogo;
    }

    public void setMallLogo(String mallLogo) {
        this.mallLogo = mallLogo;
    }
    public String getLogoKey() {
        return logoKey;
    }

    public void setLogoKey(String logoKey) {
        this.logoKey = logoKey;
    }

    public static class TF {
        public static String id = "id";  // 
        public static String mallId = "mallId";  // 
        public static String mallName = "mallName";  // 
        public static String mallLogo = "mallLogo";  // 
        public static String logoKey = "logoKey";  // 

    }

}
