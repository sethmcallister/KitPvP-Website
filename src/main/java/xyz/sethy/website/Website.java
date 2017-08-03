package xyz.sethy.website;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.core.framework.CoreFramework;
import com.skygrind.core.framework.user.CoreUserManager;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import xyz.sethy.website.pages.forums.*;
import xyz.sethy.website.pages.home.HomeGet;
import xyz.sethy.website.pages.home.HomePost;
import xyz.sethy.website.pages.leaderboards.LeaderboardsGet;
import xyz.sethy.website.pages.login.LoginGet;
import xyz.sethy.website.pages.login.LoginPost;
import xyz.sethy.website.pages.logout.LogoutGet;
import xyz.sethy.website.pages.notfound.NotFoundGet;
import xyz.sethy.website.pages.register.RegisterGet;
import xyz.sethy.website.pages.register.RegisterPost;
import xyz.sethy.website.pages.settings.SettingsGet;
import xyz.sethy.website.pages.settings.SettingsPost;
import xyz.sethy.website.pages.store.StoreGet;
import xyz.sethy.website.pages.user.UserGet;
import xyz.sethy.website.util.Path;
import xyz.sethy.websiteapi.WebsiteAPI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static spark.Spark.*;

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
        API.setFramework(new CoreFramework(true));

        spark.debug.DebugScreen.enableDebugScreen();

//        secure("./KeyStore.jks", "password", null, null);

        before("*", (request, response) -> {
            if (request.pathInfo().length() > 1 && request.pathInfo().endsWith("/")) {
                response.redirect(request.pathInfo().substring(0, request.pathInfo().length() - 1));
            }
        });

        after("*", (request, response) -> response.header("Content-Encoding", "gzip"));

        get(Path.Web.HOME, new HomeGet());
        post(Path.Web.HOME, new HomePost());

        get(Path.Web.LEADERBOARDS, new LeaderboardsGet());

        get(Path.Web.STORE, new StoreGet());

        get(Path.Web.FORUMS_HOME, new ForumsHomeGet());
        post(Path.Web.FORUMS_HOME, new ForumsHomePost());

        get(Path.Web.FORUMS_CATEGORY, new ForumsCategoryGet());
        post(Path.Web.FORUMS_CATEGORY, new ForumsCategoryPost());

        get(Path.Web.FORUMS_THREAD, new ForumsThreadGet());
        get(Path.Web.FORUMS_DELETE_THREAD, new ForumsThreadDeleteGet());
        get(Path.Web.FORUMS_LOCK_THREAD, new ForumsThreadLockGet());

        get(Path.Web.LOGIN, new LoginGet());
        post(Path.Web.LOGIN, new LoginPost());

        get(Path.Web.LOGOUT, new LogoutGet());

        get(Path.Web.REGISTER, new RegisterGet());
        post(Path.Web.REGISTER, new RegisterPost());

        get(Path.Web.USER, new UserGet());

        get(Path.Web.SETTINGS, new SettingsGet());
        post(Path.Web.SETTINGS, new SettingsPost());
        get("*", new NotFoundGet());
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
