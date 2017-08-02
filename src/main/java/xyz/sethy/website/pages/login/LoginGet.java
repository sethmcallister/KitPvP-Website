package xyz.sethy.website.pages.login;


import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.user.CoreUserManager;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;

import java.util.HashMap;
import java.util.Map;

public class LoginGet extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        User user = null;
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            for(User user1 : ((CoreUserManager)API.getUserManager()).getUserDataDriver().findAll())
            {
                if(user1.getName().equalsIgnoreCase(name))
                {
                    user = user1;
                    map.put("loggedIn", true);
                    map.put("loggedInAs", user);
                }
            }
        }
        return render(request, map, Path.Template.LOGIN);
    }
}
