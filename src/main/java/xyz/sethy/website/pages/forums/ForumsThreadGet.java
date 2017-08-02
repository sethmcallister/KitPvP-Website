package xyz.sethy.website.pages.forums;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.user.CoreUserManager;
import org.apache.commons.lang3.StringUtils;
import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.Website;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;
import xyz.sethy.websiteapi.framework.forum.Thread;

import java.util.HashMap;
import java.util.Map;

public class ForumsThreadGet extends Page implements Route
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

        String thread = request.params("thread");
        if(!StringUtils.isNumeric(thread))
        {
            response.redirect("/404");
            return render(request, map, Path.Template.FORUMS_THREAD);
        }

        Thread currentThread = WebsiteAPI.getForumManager().findById(Integer.valueOf(thread));
        if (currentThread != null)
        {
            if(user == null)
                map.put("author", false);
            else if (currentThread.getAuthor().getUniqueId().equals(user.getUniqueId()))
                map.put("author", true);
            else if(Website.getInstance().getStaffGroups().contains(user.getProfile("permissions").getString("group")))
                map.put("author", true);
            else
                map.put("author", false);

            Integer views = currentThread.getViews();
            currentThread.setViews(views + 1);
            map.put("foundThread", true);
            map.put("thread", currentThread);
            TextProcessor processor = BBProcessorFactory.getInstance().createFromResource("org/kefirsf/bb/markdown.xml");
            String text = currentThread.getBody();
            map.put("post", processor.process(text));
            return render(request, map, Path.Template.FORUMS_THREAD);
        }
        response.redirect("/404");
        return render(request, map, Path.Template.FORUMS_THREAD);
    }
}
