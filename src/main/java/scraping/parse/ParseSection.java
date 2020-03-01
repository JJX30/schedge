package scraping.parse;

import java.util.*;
import java.util.regex.Pattern;
import nyu.SectionStatus;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scraping.models.SectionAttribute;
import utils.Utils;

/**
 * Parses a section string.
 *
 * @author Albert Liu
 */
public class ParseSection {
  private static Logger logger =
      LoggerFactory.getLogger("scraping.parse.ParseSection");

  public static SectionAttribute parse(@NotNull String rawData) {
    logger.debug("parsing raw catalog section data into SectionAttribute...");

    rawData = rawData.trim();

    if (rawData.equals("")) {
      logger.warn("Got bad data: empty string");
      return null; // the course doesn't exist
    }

    Document doc = Jsoup.parse(rawData);
    Element failed = doc.selectFirst("div.alert.alert-info");
    if (failed != null) {
      logger.warn("Got bad data: " + failed.text());
      return null; // the course doesn't exist
    }

    Elements elements = doc.select("a");
    String link = null;
    for (Element element : elements) {
      String el = element.attr("href");
      if (el.contains("mapBuilding")) {
        link = el;
      }
    }

    doc.select("a").unwrap();
    doc.select("i").unwrap();
    doc.select("b").unwrap();
    Element outerDataSection = doc.selectFirst("body > section.main");
    Element header = outerDataSection.selectFirst("> header.page-header");
    Element innerDataSection = outerDataSection.selectFirst("> section");
    Element courseNameDiv = innerDataSection.selectFirst("> div.primary-head");
    String courseName = courseNameDiv.text();
    Elements dataDivs =
        innerDataSection.select("> div.section-content.clearfix");
    Map<String, String> secData = parseSectionAttributes(dataDivs);

    return parsingElements(secData, courseName, link);
  }

  static @NotNull Map<String, String>
  parseSectionAttributes(@NotNull Elements attributeData) {
    Map<String, String> map = new HashMap<>();
    for (Element e : attributeData) {
      if (e.child(0).text().equals("Topic") &&
          e.child(1).text().contains("Room")) {
        continue;
      }
      map.put(e.child(0).text(), e.child(1).text());
    }
    return map;
  }

  public static @NotNull SectionAttribute
  parsingElements(Map<String, String> secData, String courseName, String link) {
    String units = secData.get("Units");
    float minUnits = 0, maxUnits;
    if (units.contains("-")) {
      minUnits = Float.parseFloat(units.split(" - ")[0]);
      maxUnits = Float.parseFloat(units.split(" - ")[1].split(" ")[0]);
    } else {
      maxUnits = Float.parseFloat(units.split(" ")[0]);
    }

    courseName +=
        secData.containsKey("Topic") ? " " + secData.get("Topic") : "";

    String location = secData.get("Room");
    if (location.contains("Loc:")) {
      location = location.split("Loc:")[0];
      secData.put("Room", location);
    }

    // parseBuilding(secData, link);

    return new SectionAttribute(
        courseName.equals("") ? null : courseName,
        Integer.parseInt(secData.get("Class Number")),
        SectionStatus.parseStatus(secData.get("Status")),
        secData.get("Location"), secData.get("Description"),
        secData.get("Instruction Mode"), secData.get("Instructor(s)"), minUnits,
        maxUnits, secData.get("Grading"),
        secData.getOrDefault("Notes", "See Description. None otherwise"),
        secData.get("Room"));
  }

<<<<<<< HEAD:src/main/java/scraping/parse/ParseSection.java
  public static void parseBuilding(Map<String, String> secData, String link) {
=======
  public static Map<String, String> getBuilding() {
>>>>>>> update:src/main/java/services/ParseSection.java
    List<String> lines = Utils.asResourceLines("/building.txt");
    Map<String, String> buildings = new HashMap<>();
    lines.stream().map(str -> str.split(",", 2)).forEach(strings -> {
      buildings.put(strings[0], strings[1]);
    });
    return buildings;
  }

  public static boolean checkDigit(String location) {
    return Pattern.compile("[0-9]").matcher(location).find();
  }

  public static void parseBuilding(Map<String, String> secData, String link) {
    Map<String, String> buildings = getBuilding();

    String location = secData.get("Room");
    String room = "";
    String building = null;

    if (location.contains("Loc") || location.contains("Loc:")) {
      location = location.split("Loc")[0];
      location = location.strip();
      if (checkDigit(location)) {
        if (location.contains("Rm:")) {
          String[] arrs = location.split("Rm:");
          if (arrs.length == 2) {
            room = location.split("Rm:")[1];
          }
        } else if (location.contains("Rm")) {
          String[] arrs = location.split("Rm");
          if (arrs.length == 2) {
            room = location.split("Rm")[1];
          }
        } else if (location.contains("Room:")) {
          String[] arrs = location.split("Room:");
          if (arrs.length == 2) {
            room = location.split("Room:")[1];
          }
        } else if (location.contains("Room")) {
          String[] arrs = location.split("Room");
          if (arrs.length == 2) {
            room = location.split("Room")[1];
          }
        }
      }

      if (link != null) {
        link = link.substring(link.lastIndexOf("/") + 1);
        if (buildings.containsKey(link)) {
          building = buildings.get(link);
        }
      }

      if (!room.equals("") && building != null) {
        secData.put("Room", building + " - Room:" + room);
      } else {
        secData.put("Room", location);
      }
    }
  }
}
