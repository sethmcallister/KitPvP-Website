package xyz.sethy.website.util;

/**
 * Created by seth on 06/07/17.
 */
public class Path
{
    public static final class Web
    {
        public static final String HOME = "/";
        public static final String USER = "/user/:name";
        public static final String STORE = "/store";
        public static final String LEADERBOARDS = "/leaderboards/:server/:page";
        public static String LOGIN = "/login";
        public static String LOGOUT = "/logout";
        public static String FORUMS_HOME = "/forums";
        public static String FORUMS_CATEGORY = "/forums/category/:category";
        public static String FORUMS_THREAD = "/forums/thread/:thread";
        public static String FORUMS_DELETE_THREAD = "/forums/thread/:thread/delete";
        public static String FORUMS_LOCK_THREAD = "/forums/thread/:thread/lock";
        public static String REGISTER = "/register/:uuid";
        public static String SETTINGS = "/settings";
        public static String RULES = "/rules";
    }

    public static class Template
    {
        public static final String HOME = "/web/home/index.vm";
        public static final String USER = "/web/user/user.vm";
        public static final String NOT_FOUND = "/web/404.vm";
        public static final String STORE = "/web/store/store.vm";
        public static final String LEADERBOARDS = "/web/leaderboard/leaderboard.vm";
        public static final String FORUMS_HOME = "/web/forums/home.vm";
        public static final String LOGIN = "/web/login/login.vm";
        public static final String FORUMS_CATEGORY = "/web/forums/category.vm";
        public static final String FORUMS_THREAD = "/web/forums/thread.vm";
        public static final String REGISTER = "/web/register/register.vm";
        public static final String SETTINGS = "/web/settings/settings.vm";
        public static final String RULES = "/web/rules/rules.vm";
    }
}
