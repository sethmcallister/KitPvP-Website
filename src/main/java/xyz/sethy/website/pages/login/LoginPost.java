package xyz.sethy.website.pages.login;

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

public class LoginPost extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        String name = request.queryParams("username");
        String password = request.queryParams("password");

        User user = null;
        for(User user1 : ((CoreUserManager)API.getUserManager()).getUserDataDriver().findAll())
            if(user1.getName().equalsIgnoreCase(name))
                user = user1;

        if(user == null)
        {
            map.put("success", false);
            map.put("loggedIn", false);
            return render(request, map, Path.Template.LOGIN);
        }

        Profile profile = user.getProfile("website");
        String userPassword = profile.getString("password");
        if(userPassword == null)
        {
            response.redirect("/");
            return render(request, map, Path.Template.LOGIN);
        }
        if ((password != null) && (BCrypt.checkpw(password, userPassword)))
        {
            request.session().attribute("currentUser", user.getName());
            response.redirect("/user/" + user.getName());
            return render(request, map, "/web/login/login.vm");
        }
        map.put("success", false);
        map.put("loggedIn", false);
        return render(request, map, Path.Template.LOGIN);
    }
}
