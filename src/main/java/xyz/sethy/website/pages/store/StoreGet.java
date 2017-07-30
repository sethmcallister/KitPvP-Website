package xyz.sethy.website.pages.store;

import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;

import java.util.HashMap;
import java.util.Map;

public class StoreGet extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        return render(request, map, Path.Template.STORE);
    }
}
