package xyz.sethy.website.pages.forums;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.user.CoreUserManager;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.Website;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.forum.Category;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForumsHomeGet extends Page implements Route
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

        List<Category> categories = WebsiteAPI.getForumManager().findAllCategories().stream().filter(category -> category.getParentCategory() == Integer.MAX_VALUE).collect(Collectors.toCollection(LinkedList::new));

        map.put("categories", categories);
        
        if(user != null)
        {
            String group = user.getProfile("permissions").getString("group");
            map.put("staff", Website.getInstance().getStaffGroups().contains(group));
        }
        else
            map.put("staff", false);

        return render(request, map, Path.Template.FORUMS_HOME);
    }
}
