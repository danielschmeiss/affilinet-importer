package de.colorshop.affilinet.model;

public class CommonSummary
{
    public Integer Records;

    public Integer TotalRecords;

    public Integer TotalPages;

    public Integer CurrentPage;

    @Override
    public String toString()
    {
        return "GetShopListSummary [" + (Records != null ? "Records=" + Records + ", " : "")
                + (TotalRecords != null ? "TotalRecords=" + TotalRecords + ", " : "")
                + (TotalPages != null ? "TotalPages=" + TotalPages + ", " : "")
                + (CurrentPage != null ? "CurrentPage=" + CurrentPage : "") + "]";
    }
}
