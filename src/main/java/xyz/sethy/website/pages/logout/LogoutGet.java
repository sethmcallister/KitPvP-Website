package xyz.sethy.website.pages.logout;

import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;

import java.util.HashMap;
import java.util.Map;

public class LogoutGet extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        request.session().attribute("currentUser", null);
        response.redirect("/login");
        return render(request, map, Path.Template.HOME);
    }
}
