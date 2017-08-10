package xyz.sethy.website.pages.forums;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.user.CoreUserManager;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.Website;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.forum.Category;
import xyz.sethy.websiteapi.framework.forum.Thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        if(user != null)
        {
            String group = user.getProfile("permissions").getString("group");
            map.put("staff", Website.getInstance().getStaffGroups().contains(group));
        }
        else
            map.put("staff", false);

        String category = request.params("category");
        if(!StringUtils.isNumeric(category))
        {
            response.redirect("/404");
            return render(request, map, Path.Template.FORUMS_CATEGORY);
        }

        Category currentCategory = WebsiteAPI.getForumManager().findCategoryById(Integer.valueOf(category));
        if (currentCategory == null)
        {
            response.redirect("/404");
            return render(request, map, Path.Template.FORUMS_CATEGORY);
        }

        map.put("currentCategory", currentCategory);
        map.put("foundCategory", true);
        map.put("categories", WebsiteAPI.getForumManager().findCategoriesByParent(currentCategory));
        List<Thread> threads = WebsiteAPI.getForumManager().findThreadsByParent(currentCategory).stream().filter(thread -> !thread.isDeleted()).collect(Collectors.toList());

        List<Thread> threadList = new ArrayList<>();
        for(Thread thread : threads)
        {
            if(thread == null || thread.isDeleted())
                continue;

            threadList.add(thread);
        }
        map.put("threads", threadList);
        return render(request, map, Path.Template.FORUMS_CATEGORY);
    }
}
