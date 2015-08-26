package de.colorshop.affilinet.model;

public class GetCategoryListSummary extends CommonSummary
{
    public String ProgramId;

    public String ProgramTitle;

    public String ShopTitle;

    public String ShopId;

    @Override
    public String toString()
    {
        return "GetCategoryListSummary [" + (Records != null ? "Records=" + Records + ", " : "")
                + (TotalRecords != null ? "TotalRecords=" + TotalRecords + ", " : "")
                + (TotalPages != null ? "TotalPages=" + TotalPages + ", " : "")
                + (CurrentPage != null ? "CurrentPage=" + CurrentPage + ", " : "")
                + (ProgramId != null ? "ProgramId=" + ProgramId + ", " : "")
                + (ProgramTitle != null ? "ProgramTitle=" + ProgramTitle + ", " : "")
                + (ShopTitle != null ? "ShopTitle=" + ShopTitle + ", " : "")
                + (ShopId != null ? "ShopId=" + ShopId : "") + "]";
    }

}