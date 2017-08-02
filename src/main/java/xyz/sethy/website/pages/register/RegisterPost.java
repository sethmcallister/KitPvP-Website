package xyz.sethy.website.pages.register;

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
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.register.RegistrationLink;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterPost extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            for(User user1 : ((CoreUserManager)API.getUserManager()).getUserDataDriver().findAll())
            {
                if(user1.getName().equalsIgnoreCase(name))
                {
                    map.put("loggedIn", true);
                    map.put("loggedInAs", user1);
                }
            }
        }

        UUID uuid = UUID.fromString(request.params("uuid"));
        RegistrationLink link = WebsiteAPI.getRedisLinkDAO().find(uuid);
        if (link == null)
        {
            response.redirect("/");
            return render(request, map, Path.Template.REGISTER);
        }
        if (request.queryParams("password") != null)
        {
            String password = request.queryParams("password");

            System.out.println("active password = " + password);
            CoreUserManager userManager = (CoreUserManager) API.getUserManager();
            User user = userManager.getUserDataDriver().findById(link.getUserId());
            Profile profile = user.getProfile("website");
            String hashedPassword = BCrypt.hashpw(password, profile.getString("salt"));
            profile.set("password", hashedPassword);
            profile.set("registered", true);
            user.update();
            response.redirect("/login");
        }
        return render(request, map, Path.Template.REGISTER);
    }
}
