package xyz.sethy.website.pages.leaderboards;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.Page;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seth on 06/07/17.
 */
public class LeaderboardsGet extends Page implements Route
{
    @Override
    public Object handle(Request request, Response response) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        if(request.session().attribute("currentUser") != null)
        {
            String name = request.session().attribute("currentUser");
            User user = API.getUserManager().findByName(name);
            if(user != null)
            {
                map.put("loggedIn", true);
                map.put("loggedInAs", user);
            }
        }
        String server = request.params("server");
        String pageStr = request.params("page");

        if(!StringUtils.isNumeric(pageStr))
        {
            map.put("pageNotNumber", true);
            return render(request, map, Path.Template.LEADERBOARDS);
        }

        Integer page = Integer.valueOf(pageStr);

        switch (server.toLowerCase())
        {
            case "kitpvp":
            {
                map.put("entries_1", WebsiteAPI.getLeaderboardManager().getLeaderboardEntries("kitpvp_kills", page));
                map.put("entries_2", WebsiteAPI.getLeaderboardManager().getLeaderboardEntries("kitpvp_deaths", page));
            }
        }
        return render(request, map, Path.Template.LEADERBOARDS);
    }
}
