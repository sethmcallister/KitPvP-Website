package xyz.sethy.website.pages.forums;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.user.CoreUserManager;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.forum.Thread;
import xyz.sethy.websiteapi.impl.forums.CoreThread;

import java.util.HashMap;
import java.util.Map;

public class ForumsThreadDeleteGet extends Page implements Route
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

        Thread currentThread = WebsiteAPI.getForumManager().findById(Integer.valueOf(request.params("thread")));
        if (currentThread != null)
        {
            if (((user != null) && (currentThread.getAuthor().getUniqueId().equals(user.getUniqueId()))))
                map.put("author", true);
            else
                map.put("author", false);

            currentThread.setDeleted(true);
            WebsiteAPI.getRedisThreadDAO().update((CoreThread) currentThread);
            response.redirect("/forums");
            return render(request, map, Path.Template.FORUMS_THREAD);
        }
        response.redirect("/404");
        return render(request, map, Path.Template.FORUMS_THREAD);
    }
}
