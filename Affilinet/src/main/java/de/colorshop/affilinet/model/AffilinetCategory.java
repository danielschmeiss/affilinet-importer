package de.colorshop.affilinet.model;

import java.io.Serializable;

public class AffilinetCategory implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String Id;

    private String IdPath;

    private String Title;

    private String TitlePath;

    private String ProductCount;

    private AffilinetShop shop;

    public AffilinetShop getShop()
    {
        return shop;
    }

    public void setShop(AffilinetShop shop)
    {
        this.shop = shop;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getIdPath()
    {
        return IdPath;
    }

    public void setIdPath(String idPath)
    {
        IdPath = idPath;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public String getTitlePath()
    {
        return TitlePath;
    }

    public void setTitlePath(String titlePath)
    {
        TitlePath = titlePath;
    }

    public String getProductCount()
    {
        return ProductCount;
    }

    public void setProductCount(String productCount)
    {
        ProductCount = productCount;
    }

    // public SimpleBooleanProperty isShouldImport()
    // {
    // return shouldImport;
    // }
    //
    // public void setShouldImport(SimpleBooleanProperty shouldImport)
    // {
    // this.shouldImport = shouldImport;
    // }

    @Override
    public String toString()
    {
        return "AffilinetCategory [" + (Id != null ? "Id=" + Id + ", " : "")
                + (IdPath != null ? "IdPath=" + IdPath + ", " : "") + (Title != null ? "Title=" + Title + ", " : "")
                + (TitlePath != null ? "TitlePath=" + TitlePath + ", " : "")
                + (ProductCount != null ? "ProductCount=" + ProductCount : "") + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        result = prime * result + ((IdPath == null) ? 0 : IdPath.hashCode());
        result = prime * result + ((ProductCount == null) ? 0 : ProductCount.hashCode());
        result = prime * result + ((Title == null) ? 0 : Title.hashCode());
        result = prime * result + ((TitlePath == null) ? 0 : TitlePath.hashCode());
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
        AffilinetCategory other = (AffilinetCategory) obj;
        if (Id == null)
        {
            if (other.Id != null)
                return false;
        }
        else if (!Id.equals(other.Id))
            return false;
        if (IdPath == null)
        {
            if (other.IdPath != null)
                return false;
        }
        else if (!IdPath.equals(other.IdPath))
            return false;
        if (ProductCount == null)
        {
            if (other.ProductCount != null)
                return false;
        }
        else if (!ProductCount.equals(other.ProductCount))
            return false;
        if (Title == null)
        {
            if (other.Title != null)
                return false;
        }
        else if (!Title.equals(other.Title))
            return false;
        if (TitlePath == null)
        {
            if (other.TitlePath != null)
                return false;
        }
        else if (!TitlePath.equals(other.TitlePath))
            return false;
        return true;
    }
}
