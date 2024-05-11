package at.gammastrahlung.monopoly_server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Controller
@RequestMapping("/")
public class Homepage {

    @Autowired
    private BuildProperties buildProperties;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String index(TimeZone timezone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .RFC_1123_DATE_TIME
                .withZone(timezone.toZoneId()); // Display in caller timezone

        return """
                <html>
                    <head>
                        <title>GammaStrahlung Monopoly Server - %s</title>
                    </head>
                    <body>
                        <h1>GammaStrahlung Monopoly Server</h1>
                        <a href="https://github.com/gammaStrahlung/monopoly_server"><h4>Visit the GitHub repository</h4></a>
                        <div>
                            Built: %s <br>
                            %s.%s - <a href="https://github.com/gammaStrahlung/monopoly_server/releases/tag/%s">%s</a>
                        </div>
                    </body>
                </html>
                """
                .formatted(buildProperties.getVersion(), // Title
                        dateTimeFormatter.format(buildProperties.getTime()), // Build time
                        buildProperties.getGroup(), buildProperties.getArtifact(), // at.gammastrahlung.monopoly_server
                        buildProperties.getVersion(), // vX.X.X for github release link
                        buildProperties.getVersion()); // vX.X.X displayed in page
    }
}
