package xyz.sethy.website;

import com.skygrind.api.API;
import com.skygrind.core.framework.CoreFramework;
import xyz.sethy.website.pages.forums.ForumsCategoryGet;
import xyz.sethy.website.pages.forums.ForumsCategoryPost;
import xyz.sethy.website.pages.forums.ForumsHomeGet;
import xyz.sethy.website.pages.forums.ForumsHomePost;
import xyz.sethy.website.pages.home.HomeGet;
import xyz.sethy.website.pages.home.HomePost;
import xyz.sethy.website.pages.leaderboards.LeaderboardsGet;
import xyz.sethy.website.pages.store.StoreGet;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;

import java.util.LinkedList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by seth on 06/07/17.
 */
public class Website
{
    private static Website instance;
    private final List<String> staffGroups;

    public Website()
    {
        instance = this;
        this.staffGroups = new LinkedList<>();
        this.staffGroups.add("FOUNDER");
        this.staffGroups.add("DEVELOPER");
        this.staffGroups.add("MANAGER");
        this.staffGroups.add("ADMIN");
        this.staffGroups.add("MODERATOR");

        WebsiteAPI.setFramework(new xyz.sethy.websiteapi.impl.CoreFramework());
        API.setFramework(new CoreFramework());

        spark.debug.DebugScreen.enableDebugScreen();

        get(Path.Web.HOME, new HomeGet());
        post(Path.Web.HOME, new HomePost());

        get(Path.Web.LEADERBOARDS, new LeaderboardsGet());

        get(Path.Web.STORE, new StoreGet());

        get(Path.Web.FORUMS_HOME, new ForumsHomeGet());
        post(Path.Web.FORUMS_HOME, new ForumsHomePost());

        get(Path.Web.FORUMS_CATEGORY, new ForumsCategoryGet());
        post(Path.Web.FORUMS_CATEGORY, new ForumsCategoryPost());

    }

    public List<String> getStaffGroups()
    {
        return staffGroups;
    }

    public static Website getInstance()
    {
        return instance;
    }
}
