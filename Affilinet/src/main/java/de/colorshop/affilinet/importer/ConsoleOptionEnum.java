package de.colorshop.affilinet.importer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ConsoleOptionEnum
{
    SHOPS(1, "Shows all shops out affilinet account is registered to."),

    CATEGORIES(2, "Shows the categories of a shop."),

    CATEGORIES_BY_PATH(3, "Same as 'SHOW_CATEGORIES' but sorted by path."),

    PRODUCTS(4, "Shows the products."),

    QUIT(5, "Quit.");

    private final String description;

    private final int id;

    private ConsoleOptionEnum(int id, String description)
    {
        this.id = id;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return this.description;
    }

    public static List<ConsoleOptionEnum> getSortedValues()
    {
        return Arrays.asList(values()).stream().sorted((c1, c2) -> Integer.compare(c1.id, c2.id))
                .collect(Collectors.toList());
    }

    public static ConsoleOptionEnum valueWithId(int id)
    {
        for (ConsoleOptionEnum o : values())
        {
            if (o.id == id)
                return o;
        }
        return null;
    }
}
