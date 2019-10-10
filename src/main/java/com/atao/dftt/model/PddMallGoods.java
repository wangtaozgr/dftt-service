package com.atao.dftt.model;

import com.atao.base.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 
 *
 * @author twang
 */
public class PddMallGoods extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 
     */
    @Column(name = "mall_id")
    private String mallId;
    /**
     * 
     */
    @Column(name = "goods_id")
    private String goodsId;
    /**
     * 
     */
    @Column(name = "goods_logo")
    private String goodsLogo;
    /**
     * 
     */
    @Column(name = "logo_key")
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

        public static String TABLE_NAME = "PDD_MALL_GOODS";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String mallId = "mall_id";  // 
        public static String goodsId = "goods_id";  // 
        public static String goodsLogo = "goods_logo";  // 
        public static String logoKey = "logo_key";  // 

    }
}
