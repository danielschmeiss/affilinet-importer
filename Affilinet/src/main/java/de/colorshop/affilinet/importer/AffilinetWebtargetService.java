package de.colorshop.affilinet.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;

public class AffilinetWebtargetService
{
    @Inject
    private Logger logger;

    @Inject
    private JsonParser parser;

    private static final String SHOP_ID_QUERY_PARAM = "shopId";

    private static final String CATEGORIES_URL =
            "http://product-api.affili.net/V3/productservice.svc/JSON/GetCategoryList";

    private static final String SHOP_LIST_URL = "https://product-api.affili.net/V3/productservice.svc/JSON/GetShopList";

    private static final String PRODUCTS_URL =
            "http://product-api.affili.net/V3/productservice.svc/JSON//SearchProducts";

    private String PASSWORD;

    private String PUBLISHER_ID;

    private static final Client client = ClientBuilder.newClient();

    private static final String IMAGE_SCALE_QUERY_PARAM = "ImageScales";

    private static final Object ORIGINAL_IMAGE = "OriginalImage";

    private static final String CATEGORY_IDS_QUERY_PARAM = "categoryIds";

    public AffilinetWebtargetService()
    {
        Properties prop = new Properties();
        try
        {
            prop.load(AffilinetWebtargetService.class.getResourceAsStream("account.properties"));
        }
        catch (IOException e)
        {
            throw new AffilinetImporterException("Could not load credentials from properties file.", e);
        }

        PASSWORD = prop.getProperty("password");
        PUBLISHER_ID = prop.getProperty("publisherId");
    }

    public JsonObject requestCategories(String shopId, int page)
    {
        WebTarget categoriesWebTarget = getAffilinetTarget(CATEGORIES_URL);
        categoriesWebTarget = categoriesWebTarget.queryParam(SHOP_ID_QUERY_PARAM, shopId);
        return request(categoriesWebTarget, page);
    }

    public WebTarget getAffilinetTarget(String url)
    {
        WebTarget webTarget = client.target(url);
        webTarget = webTarget.queryParam("publisherId", PUBLISHER_ID);
        webTarget = webTarget.queryParam("Password", PASSWORD);
        return webTarget;
    }

    private JsonObject request(WebTarget webTarget)
    {
        logger.info("Querying " + webTarget.getUri());
        String jsonString = webTarget.request(MediaType.APPLICATION_JSON).get().readEntity(String.class);
        return parser.parse(jsonString).getAsJsonObject();
    }

    private JsonObject request(WebTarget webTarget, int page)
    {
        logger.info("Requesting page " + page);
        webTarget = webTarget.queryParam("CurrentPage", page);
        return request(webTarget);
    }

    public JsonObject requestShops(int page)
    {
        WebTarget shopsWebTarget = getAffilinetTarget(SHOP_LIST_URL);
        return request(shopsWebTarget, page);
    }

    public JsonObject requestProducts(String shopId, int page, Collection<String> categoryIds)
    {
        WebTarget productsWebTarget = getAffilinetTarget(PRODUCTS_URL);
        productsWebTarget = productsWebTarget.queryParam(SHOP_ID_QUERY_PARAM, shopId);
        productsWebTarget = productsWebTarget.queryParam(IMAGE_SCALE_QUERY_PARAM, ORIGINAL_IMAGE);
        productsWebTarget =
                productsWebTarget.queryParam(CATEGORY_IDS_QUERY_PARAM,
                        categoryIds.stream().collect(Collectors.joining(",")));
        return request(productsWebTarget, page);
    }
}
