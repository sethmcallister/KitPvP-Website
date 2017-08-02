package xyz.sethy.website.pages.user;

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

public class UserGet extends Page implements Route
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
        String userSearched = request.params("name");

        boolean set = false;
        for (User user1 : ((CoreUserManager)API.getUserManager()).getUserDataDriver().findAll())
        {
            if (user1 != null)
            {
                if (user1.getName().equalsIgnoreCase(userSearched))
                {
                    set = true;
                    map.put("foundUser", true);
                    map.put("user", user1);
                    if(user != null && user.getUniqueId().equals(user1.getUniqueId()))
                        map.put("theirUser", true);
                    else
                        map.put("theirUser", false);
                }
            }
        }

        if (!set)
            response.redirect("/404");
        return render(request, map, Path.Template.USER);
    }
}
