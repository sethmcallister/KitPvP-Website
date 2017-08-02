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
import xyz.sethy.websiteapi.framework.forum.Thread;
import xyz.sethy.websiteapi.impl.forums.CoreCategory;
import xyz.sethy.websiteapi.impl.forums.CoreThread;

import java.util.*;

public class ForumsCategoryPost extends Page implements Route
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

        String title = request.queryParams("title");
        String post = request.queryParams("post");

        Category currentCategory = WebsiteAPI.getForumManager().findCategoryById(Integer.valueOf(request.params("category")));
        if ((title != null) && (post != null))
        {
            if (currentCategory == null)
            {
                map.put("foundCategory", false);
                return render(request, map, Path.Template.FORUMS_CATEGORY);
            }
            Thread thread = new CoreThread(WebsiteAPI.getForumManager().findAllThreads().size() + 1, title, post, user.getUniqueId());
            WebsiteAPI.getForumManager().findAllThreads().add(thread);
            currentCategory.getThreads().add(thread.getId());

            WebsiteAPI.getRedisThreadDAO().insert((CoreThread) thread);
            WebsiteAPI.getRedisCategoryDAO().insert((CoreCategory) currentCategory);
            response.redirect("/forums/thread/" + thread.getId());
            return render(request, map, Path.Template.FORUMS_CATEGORY);
        }

        String ctitle = request.queryParams("c-title");
        String cDesc = request.queryParams("c-desc");
        if ((ctitle != null) && (cDesc != null))
        {
            Category forumCategory = new CoreCategory(WebsiteAPI.getForumManager().findAllCategories().size() + 1, Integer.MAX_VALUE, ctitle, cDesc);
            WebsiteAPI.getForumManager().findAllCategories().add(forumCategory);
            WebsiteAPI.getRedisCategoryDAO().update((CoreCategory) forumCategory);
        }
        return render(request, map, Path.Template.FORUMS_CATEGORY);
    }
}
