package com.atao.dftt.api.vo;

import com.atao.base.vo.BaseVo;

/**
 * 
 *
 * @author twang
 */
public class PddMallGoodsVo extends BaseVo {

    /**
    * 
    */
    private Long id;

    /**
     * 
     */
    private String mallId;
    /**
     * 
     */
    private String goodsId;
    /**
     * 
     */
    private String goodsName;
    /**
     * 
     */
    private String goodsLogo;
    /**
     * 
     */
    private String logoKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsLogo() {
        return goodsLogo;
    }

    public void setGoodsLogo(String goodsLogo) {
        this.goodsLogo = goodsLogo;
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
        public static String goodsId = "goodsId";  // 
        public static String goodsName = "goodsName";  // 
        public static String goodsLogo = "goodsLogo";  // 
        public static String logoKey = "logoKey";  // 

    }

}
