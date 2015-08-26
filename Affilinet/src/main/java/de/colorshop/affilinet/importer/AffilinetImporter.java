package de.colorshop.affilinet.importer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.colorshop.affilinet.model.AffilinetCategory;
import de.colorshop.affilinet.model.AffilinetProduct;
import de.colorshop.affilinet.model.AffilinetShop;
import de.colorshop.affilinet.model.CommonSummary;
import de.colorshop.affilinet.model.GetCategoryListSummary;

/**
 * Imports TODO...
 * 
 * @author Dani
 * 
 */
public class AffilinetImporter
{

    @Inject
    private Logger logger;

    @Inject
    private AffilinetWebtargetService webtargetService;

    @Inject
    private AffilinetConsole console;

    private static final int MAX_PAGE_LIMIT = 500;

    private static Map<String, String> SHOP_NAMES = new HashMap<>();

    private static Gson gson;
    static
    {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
        {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException
            {
                return getDate(json);
            }
        });

        gson = builder.create();
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        LogManager logManager = LogManager.getLogManager();
        Logger rootLogger = logManager.getLogger("");
        rootLogger.setLevel(Level.INFO);
        
        new AffilinetImporter().start();
    }

    public AffilinetImporter()
    {
        Injector injector = Guice.createInjector();
        injector.injectMembers(this);
    }

    public void start() throws IOException, InterruptedException
    {
        console.start();
        ConsoleOptionEnum options;
        while (true)
        {
            options = console.getOptions();
            switch (options)
            {
                case SHOPS:
                    console.printLine("Loading all available shops...");
                    List<AffilinetShop> shops = getAffilinetShops();
                    shops.stream().sorted((s1, s2) -> s1.getShopTitle().compareTo(s2.getShopTitle()))
                            .forEach(line -> console.printLine("\t" + line));
                    break;
                case CATEGORIES:
                {
                    String shopId = console.readLine("Please enter the shop ID");
                    List<AffilinetCategory> categoriesOfShop = getCategoriesOfShop(shopId);
                    console.printLine("Found " + categoriesOfShop.size() + " categories:");
                    categoriesOfShop
                            .stream()
                            .sorted((c1, c2) -> c1.getTitle().compareTo(c2.getTitle()))
                            .forEach(
                                    category -> console.printLine("\t" + category.getTitle() + " - ID='"
                                            + category.getId() + "', Path='" + category.getTitlePath() + "'"));
                    break;
                }
                case CATEGORIES_BY_PATH:
                {
                    String shopId = console.readLine("Please enter the shop ID");
                    List<AffilinetCategory> categoriesOfShop = getCategoriesOfShop(shopId);
                    console.printLine("Found " + categoriesOfShop.size() + " categories:");
                    categoriesOfShop
                            .stream()
                            .sorted((c1, c2) -> c1.getTitlePath().compareTo(c2.getTitlePath()))
                            .forEach(
                                    category -> console.printLine("\t" + category.getTitlePath() + ", Title="
                                            + category.getTitle() + " - ID='" + category.getId() + "'"));
                    break;
                }
                case PRODUCTS:
                {
                    String shopId = console.readLine("Please enter the shop ID: ");
                    String categoryIds = console.readLine("Please enter the category IDs (comma-separated): ");
                    List<AffilinetProduct> products = getProductsOfShop(shopId, Arrays.asList(categoryIds.split(",")));
                    console.printLine("Found " + products.size() + " products:");
                    products.sort((c1, c2) -> c1.getTitle().compareTo(c1.getTitle()));
                    products.stream().forEach(a -> console.printLine("\t" + a.getTitle()));
                    break;
                }
                case QUIT:
                    console.printLine("Good Bye and have a nice day...");
                    Thread.sleep(100);
                    return;
            }
        }
    }

    public List<AffilinetCategory> getCategoriesOfShop(String shopId)
    {
        JsonObject categoriesJson = webtargetService.requestCategories(shopId, 1);

        GetCategoryListSummary summary =
                gson.fromJson(categoriesJson.get("GetCategoryListSummary"), GetCategoryListSummary.class);

        logger.info("Received list of categories: " + summary);

        List<AffilinetCategory> extractedCategories = new ArrayList<>();
        extractedCategories.addAll(extractCategories(categoriesJson));

        for (int i = 2; i <= summary.TotalPages; i++)
        {
            JsonObject nextPageCategoriesJson = webtargetService.requestCategories(shopId, i);
            extractedCategories.addAll(extractCategories(nextPageCategoriesJson));
        }
        return extractedCategories;
    }

    private List<AffilinetCategory> extractCategories(JsonObject categoriesJson)
    {
        List<AffilinetCategory> extractedCategories = new ArrayList<>();
        logger.info("Extracting categories from '" + categoriesJson.toString() + "'.");
        JsonArray categoriesArray = categoriesJson.get("Categories").getAsJsonArray();
        for (JsonElement categoriesElement : categoriesArray)
        {
            AffilinetCategory category = gson.fromJson(categoriesElement, AffilinetCategory.class);
            extractedCategories.add(category);
        }
        return extractedCategories;
    }

    public List<AffilinetShop> getAffilinetShops()
    {
        logger.info("Querying shops");
        JsonObject shopsJson = webtargetService.requestShops(1);

        CommonSummary shopListSummary = gson.fromJson(shopsJson.get("GetShopListSummary"), CommonSummary.class);

        logger.info("Received list of shops: " + shopListSummary);

        List<AffilinetShop> extractedShops = new ArrayList<>();

        extractedShops.addAll(extractShops(shopsJson));

        for (int i = 2; i <= shopListSummary.TotalPages; i++)
        {
            JsonObject nextPageShopsJson = webtargetService.requestShops(i);
            extractedShops.addAll(extractShops(nextPageShopsJson));
        }

        return extractedShops;
    }

    private List<AffilinetShop> extractShops(JsonObject shopsJson)
    {
        List<AffilinetShop> extractedShops = new ArrayList<>();
        logger.info("Extracting shops from '" + shopsJson.toString() + "'.");
        JsonArray shopsArray = shopsJson.get("Shops").getAsJsonArray();
        for (JsonElement shopElement : shopsArray)
        {
            extractedShops.add(gson.fromJson(shopElement, AffilinetShop.class));
        }
        return extractedShops;
    }

    public CommonSummary getProductsOfShopSummary(String shopId, List<String> categoryIds)
    {
        JsonObject productsJsonObject = webtargetService.requestProducts(shopId, 1, categoryIds);
        return gson.fromJson(productsJsonObject.get("ProductsSummary"), CommonSummary.class);
    }

    public List<AffilinetProduct> getProductsOfShop(String shopId, List<String> givenCategoryIds)
    {
        List<AffilinetProduct> result = new ArrayList<>();
        // The API does not allow to query more than 100 categories at once.
        if (givenCategoryIds.size() > 100)
        {
            List<String> categoryIds = givenCategoryIds.subList(0, 100);
            result.addAll(getProductsOfShopWithoutLimitCheck(shopId, categoryIds));
            List<String> nextCategoryIds = givenCategoryIds.subList(100, givenCategoryIds.size());
            result.addAll(getProductsOfShopWithoutLimitCheck(shopId, nextCategoryIds));
        }
        else
        {
            result.addAll(getProductsOfShopWithoutLimitCheck(shopId, givenCategoryIds));
        }

        return result;

    }

    private List<AffilinetProduct> getProductsOfShopWithoutLimitCheck(String shopId, List<String> categoryIds)
    {
        String shopName = SHOP_NAMES.get(shopId);

        logger.info("Loading products (shop='" + shopId + "-" + shopName + "') for category Ids: "
                + categoryIds.stream().collect(Collectors.joining(",")));

        JsonObject productsJsonObject = webtargetService.requestProducts(shopId, 1, categoryIds);

        CommonSummary summary = getProductsOfShopSummary(shopId, categoryIds);

        logger.info("Received list of products: " + summary);

        List<AffilinetProduct> extractedProducts = new ArrayList<>();
        extractedProducts.addAll(getProducts(productsJsonObject.get("Products"), shopName));

        for (int i = 2; i <= Math.min(summary.TotalPages, MAX_PAGE_LIMIT); i++)
        {
            JsonObject nextPageProductsJson = webtargetService.requestProducts(shopId, i, categoryIds);
            extractedProducts.addAll(getProducts(nextPageProductsJson.get("Products"), shopName));
        }

        logger.info("Got " + extractedProducts.size() + " products.");

        return extractedProducts;
    }

    public List<AffilinetProduct> getProductsOfShop(String shopId, List<String> categoryIds, int page)
    {
        String shopName = SHOP_NAMES.get(shopId);
        logger.info("Loading products (page ='" + page + "', shop='" + shopId + "-" + shopName
                + "') for category Ids: " + categoryIds.stream().collect(Collectors.joining(",")));

        JsonObject nextPageProductsJson = webtargetService.requestProducts(shopId, page, categoryIds);
        return getProducts(nextPageProductsJson.get("Products"), shopName);
    }

    private List<AffilinetProduct> getProducts(JsonElement productsJson, String shopName)
    {
        List<AffilinetProduct> imagesToImport = new ArrayList<AffilinetProduct>();
        JsonArray asJsonArray = productsJson.getAsJsonArray();
        Iterator<JsonElement> productsJsonIterator = asJsonArray.iterator();
        while (productsJsonIterator.hasNext())
        {
            JsonElement next = productsJsonIterator.next();
            AffilinetProduct importBo = new AffilinetProduct();
            JsonObject productJsonObject = next.getAsJsonObject();
            importBo.setCategory((productJsonObject.get("ShopCategoryPath").getAsString().substring(productJsonObject
                    .get("ShopCategoryPath").getAsString().lastIndexOf(">") + 1)).trim());

            JsonArray properties = productJsonObject.get("Properties").getAsJsonArray();
            JsonObject o = new JsonObject();
            Iterator<JsonElement> it = properties.iterator();
            while (it.hasNext())
            {
                JsonObject next2 = it.next().getAsJsonObject();
                switch (next2.get("PropertyName").getAsString())
                {
                // case "img_url1":
                // case "img_url2":
                // case "img_url3":
                // case "img_url4":
                // case "img_url5":
                // case "img_url6":
                // case "img_url7":
                // case "img_url8":
                // case "img_url9":
                // String imgUrl = next2.get("PropertyValue").getAsString();
                // if (imgUrl != null && !imgUrl.isEmpty())
                // {
                // importBo.addOtherImageUrl(imgUrl);
                // }
                // break;
                    case "Style":
                        o.add("Style", next2.get("PropertyValue"));
                        break;
                    case "COLOR":
                    case "Color":
                        o.add("Color", next2.get("PropertyValue"));
                        break;
                }
            }

            JsonArray imagesArray = productJsonObject.get("Images").getAsJsonArray().get(0).getAsJsonArray();
            // FIXME
            Iterator<JsonElement> imageIterator = imagesArray.iterator();
            // while (imageIterator.hasNext())
            // {
            // String imageUrl = imageIterator.next().getAsJsonObject().get("URL").getAsString();
            // importBo.addOtherImageUrl(imageUrl);
            // }

            importBo.setPageUrl(productJsonObject.get("Deeplink1").getAsString());
            importBo.setTitle(productJsonObject.get("ProductName").getAsString());
            o.add("ProductId", productJsonObject.get("ProductId"));
            o.add("DescriptionShort", productJsonObject.get("DescriptionShort"));
            o.add("LastShopUpdate", productJsonObject.get("LastShopUpdate"));
            o.add("LastProductChange", productJsonObject.get("LastProductChange"));

            JsonObject priceInformation = productJsonObject.get("PriceInformation").getAsJsonObject();
            o.add("DisplayPrice", priceInformation.get("DisplayPrice"));

            o.add("Properties", productJsonObject.get("Properties"));

            importBo.setShop(shopName);
            importBo.setProductData(new Gson().toJson(o));
            importBo.setLastUpdated(getDate(productJsonObject.get("LastProductChange")));
            importBo.setAffilinetId(productJsonObject.get("ProductId").getAsLong());

            imagesToImport.add(importBo);
        }
        logger.info("Found " + imagesToImport.size() + " images to import.");
        return imagesToImport;
    }

    private static Date getDate(JsonElement json)
    {
        // Parse date value, example: "Date(1412252040000+0200)"
        String value = json.getAsJsonPrimitive().getAsString();
        return new Date(Long.parseLong(value.substring(value.indexOf("(") + 1, value.indexOf("+"))));
    }
}
