package xyz.sethy.website.pages.forums;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.Website;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.forum.Category;

import java.util.HashMap;
import java.util.Map;

public class ForumsCategoryGet extends Page implements Route
{
    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        User user = null;
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            user = API.getUserManager().findByName(name);
            if(user != null)
            {
                map.put("loggedIn", true);
                map.put("loggedInAs", user);
            }
        }

        if(user != null)
        {
            String group = user.getProfile("permissions").getString("group");
            map.put("staff", Website.getInstance().getStaffGroups().contains(group));
        }
        else
            map.put("staff", false);

        Category currentCategory = WebsiteAPI.getForumManager().findCategoryById(Integer.valueOf(request.params("category")));
        if (currentCategory == null)
        {
            map.put("foundCategory", false);
            return render(request, map, Path.Template.FORUMS_CATEGORY);
        }

        map.put("categories", WebsiteAPI.getForumManager().findCategoriesByParent(currentCategory));
        map.put("threads", WebsiteAPI.getForumManager().findThreadsByParent(currentCategory));
        response.redirect("/forums");
        return render(request, map, Path.Template.FORUMS_CATEGORY);
    }
}
