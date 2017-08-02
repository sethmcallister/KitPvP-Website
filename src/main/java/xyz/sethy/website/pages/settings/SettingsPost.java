package xyz.sethy.website.pages.settings;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import com.skygrind.core.framework.user.CoreUserManager;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;

import java.util.HashMap;
import java.util.Map;

public class SettingsPost extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        User user = null;
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            for(User user1 : ((CoreUserManager) API.getUserManager()).getUserDataDriver().findAll())
            {
                if(user1.getName().equalsIgnoreCase(name))
                {
                    user = user1;
                    map.put("loggedIn", true);
                    map.put("loggedInAs", user);
                }
            }
        }

        if(user == null)
        {
            response.redirect("/404");
            return render(request, map, Path.Template.SETTINGS);
        }

        map.put("foundUser", true);
        map.put("user", user);

        String newPassword = request.queryParams("new-password");
        if(newPassword == null)
            return render(request, map, Path.Template.SETTINGS);

        Profile profile = user.getProfile("website");
        String salt = BCrypt.gensalt(12);
        profile.set("salt", salt);
        profile.set("password", BCrypt.hashpw(newPassword, salt));
        user.update();
        return render(request, map, Path.Template.SETTINGS);
    }
}
