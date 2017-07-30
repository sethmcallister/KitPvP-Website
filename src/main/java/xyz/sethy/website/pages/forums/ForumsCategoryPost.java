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
            response.redirect("/forums");
        }

        String ctitle = request.queryParams("c-title");
        String cDesc = request.queryParams("c-desc");
        if ((ctitle != null) && (cDesc != null))
        {
            Category forumCategory = new CoreCategory(WebsiteAPI.getForumManager().findAllCategories().size() + 1, null, ctitle);
            WebsiteAPI.getForumManager().findAllCategories().add(forumCategory);
        }
        response.redirect("/forums");
        return render(request, map, Path.Template.FORUMS_CATEGORY);
    }
}
