package de.colorshop.affilinet.model;

import java.util.Date;

public class AffilinetProduct
{
    private String category;

    private String pageUrl;

    private String title;

    private Long affilinetId;

    private String shop;

    private String productData;

    private Date lastUpdated;

    public String getPageUrl()
    {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl)
    {
        this.pageUrl = pageUrl;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public Long getAffilinetId()
    {
        return affilinetId;
    }

    public void setAffilinetId(Long affilinetId)
    {
        this.affilinetId = affilinetId;
    }

    public String getShop()
    {
        return shop;
    }

    public void setShop(String shop)
    {
        this.shop = shop;
    }

    public String getProductData()
    {
        return productData;
    }

    public void setProductData(String productData)
    {
        this.productData = productData;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

}
