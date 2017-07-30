package xyz.sethy.website.pages.home;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seth on 06/07/17.
 */
public class HomeGet extends Page implements Route
{
    @Override
    public Object handle(Request request, Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            User user = API.getUserManager().findByName(name);
            if(user != null)
            {
                map.put("loggedIn", true);
                map.put("loggedInAs", user);
            }
        }
        return render(request, map, Path.Template.HOME);
    }
}
