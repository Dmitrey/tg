package service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import entity.Stop;
import entity.Transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyRequest {

    private static Optional<DomElement> fullName;
    private static Optional<DomElement> direction;
    private static Optional<DomElement> estimatedTime;
    private static String name;
    private static String type;

    public static List<Transport> getTransports(Stop stop) {
        List<Transport> transportList = new ArrayList<>();

        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        String url = String.format("https://yandex.by/maps/157/minsk/stops/%s/",stop.getId());
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert page != null;
        DomNodeList<DomElement> li = page.getElementsByTagName("li");
        for (int i = 1; i <= li.size(); i++) {
            fullName = Optional.ofNullable(page.getFirstByXPath("//li["+i+"]//a[@class=\"masstransit-vehicle-snippet-view__name\"]"));
            direction = Optional.ofNullable(page.getFirstByXPath("//li["+i+"]//a[@class=\"masstransit-vehicle-snippet-view__essential-stop\"]"));
            estimatedTime = Optional.ofNullable(page.getFirstByXPath("//li["+i+"]//span[@class=\"masstransit-prognoses-view__title-text\"]"));
            if (fullName.isPresent()){

                name = fullName.get().getAttribute("title").split(" ")[1];
                type = fullName.get().getAttribute("title").split(" ")[0];
            }
            System.out.println( "name: " + (fullName.isPresent() ? fullName.get().getAttribute("title") : "null") +
                    " direction: " + (direction.isPresent() ? direction.get().getTextContent() : "null") +
                    " estimated time: " + (estimatedTime.isPresent() ? estimatedTime.get().getTextContent() : "null"));
            transportList.add(new Transport((fullName.isPresent() ? fullName.get().getAttribute("title") : "null"),
                    name,
                    (direction.isPresent() ? direction.get().getTextContent() : "null"),
                    type,
                    (estimatedTime.isPresent() ? estimatedTime.get().getTextContent() : "null")));
        }
        return transportList;
    }
}
