package de.colorshop.affilinet.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AffilinetShop implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String ShopId;

    private String ProgramId;

    private String ShopTitle;

    private Date LastUpdate;

    private String ShopLink;

    private int ProductCount;

    private List<AffilinetCategory> categories;

    public List<AffilinetCategory> getCategories()
    {
        return categories;
    }

    public void setCategories(List<AffilinetCategory> categories)
    {
        this.categories = categories;
    }

    public String getShopId()
    {
        return ShopId;
    }

    public void setShopId(String shopId)
    {
        ShopId = shopId;
    }

    public String getProgramId()
    {
        return ProgramId;
    }

    public void setProgramId(String programId)
    {
        ProgramId = programId;
    }

    public String getShopTitle()
    {
        return ShopTitle;
    }

    public void setShopTitle(String shopTitle)
    {
        ShopTitle = shopTitle;
    }

    public Date getLastUpdate()
    {
        return LastUpdate;
    }

    public void setLastUpdate(Date lastUpdate)
    {
        LastUpdate = lastUpdate;
    }

    public String getShopLink()
    {
        return ShopLink;
    }

    public void setShopLink(String shopLink)
    {
        ShopLink = shopLink;
    }

    public int getProductCount()
    {
        return ProductCount;
    }

    public void setProductCount(int productCount)
    {
        ProductCount = productCount;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((LastUpdate == null) ? 0 : LastUpdate.hashCode());
        result = prime * result + ProductCount;
        result = prime * result + ((ProgramId == null) ? 0 : ProgramId.hashCode());
        result = prime * result + ((ShopId == null) ? 0 : ShopId.hashCode());
        result = prime * result + ((ShopLink == null) ? 0 : ShopLink.hashCode());
        result = prime * result + ((ShopTitle == null) ? 0 : ShopTitle.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AffilinetShop other = (AffilinetShop) obj;
        if (LastUpdate == null)
        {
            if (other.LastUpdate != null)
                return false;
        }
        else if (!LastUpdate.equals(other.LastUpdate))
            return false;
        if (ProductCount != other.ProductCount)
            return false;
        if (ProgramId == null)
        {
            if (other.ProgramId != null)
                return false;
        }
        else if (!ProgramId.equals(other.ProgramId))
            return false;
        if (ShopId == null)
        {
            if (other.ShopId != null)
                return false;
        }
        else if (!ShopId.equals(other.ShopId))
            return false;
        if (ShopLink == null)
        {
            if (other.ShopLink != null)
                return false;
        }
        else if (!ShopLink.equals(other.ShopLink))
            return false;
        if (ShopTitle == null)
        {
            if (other.ShopTitle != null)
                return false;
        }
        else if (!ShopTitle.equals(other.ShopTitle))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "AffilinetShop [" + (ShopId != null ? "ShopId=" + ShopId + ", " : "")
                + (ProgramId != null ? "ProgramId=" + ProgramId + ", " : "")
                + (ShopTitle != null ? "ShopTitle=" + ShopTitle + ", " : "")
                + (LastUpdate != null ? "LastUpdate=" + LastUpdate + ", " : "")
                + (ShopLink != null ? "ShopLink=" + ShopLink + ", " : "") + "ProductCount=" + ProductCount + "]";
    }

}
