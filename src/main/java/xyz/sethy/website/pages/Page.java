package xyz.sethy.website.pages;

import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.Request;
import spark.template.velocity.VelocityTemplateEngine;
import xyz.sethy.website.util.Path;

import java.util.Map;

/**
 * Created by seth on 06/07/17.
 */
public abstract class Page
{
    public String render(Request request, Map<String, Object> model, String templatePath)
    {
        model.put("currentUser", request.session().attribute("currentUser"));
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }


    private VelocityTemplateEngine strictVelocityEngine()
    {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}
