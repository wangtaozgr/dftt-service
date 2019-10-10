package com.atao.dftt.model;

import com.atao.base.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 
 *
 * @author twang
 */
public class PddMall extends BaseEntity {

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 
     */
    @Column(name = "mall_id")
    private String mallId;
    /**
     * 
     */
    @Column(name = "mall_name")
    private String mallName;
    /**
     * 
     */
    @Column(name = "mall_logo")
    private String mallLogo;
    /**
     * 
     */
    @Column(name = "logo_key")
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

        public static String TABLE_NAME = "PDD_MALL";   // 表名

        //public static String TABLE_SCHEMA = ConfigUtils.getValue("");   // 库名

        public static String id = "id";  // 
        public static String mallId = "mall_id";  // 
        public static String mallName = "mall_name";  // 
        public static String mallLogo = "mall_logo";  // 
        public static String logoKey = "logo_key";  // 

    }
}
