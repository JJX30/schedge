package database.courses;

import database.models.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;
import org.slf4j.*;
import types.*;
import utils.Utils;

public final class SearchRows {

  private static Logger logger =
      LoggerFactory.getLogger("database.courses.SearchCourses");

  public static Stream<Row> searchRows(Connection conn, Nyu.Term term,
                                       String subject, String school,
                                       String query, int titleWeight,
                                       int descriptionWeight, int notesWeight,
                                       int prereqsWeight) throws SQLException {
    if (titleWeight == 0 && descriptionWeight == 0 && notesWeight == 0 &&
        prereqsWeight == 0) {
      throw new IllegalArgumentException("all of the weights were zero");
    }

    ArrayList<String> fields = new ArrayList<>();
    ArrayList<String> rankings = new ArrayList<>();
    if (titleWeight != 0) {
      fields.add("courses.name_vec @@ q.query");
      fields.add("sections.name_vec @@ q.query");
      rankings.add(titleWeight + " * ts_rank_cd(courses.name_vec, q.query)");
      rankings.add(titleWeight + " * ts_rank_cd(sections.name_vec, q.query)");
    }
    if (descriptionWeight != 0) {
      fields.add("courses.description_vec @@ q.query");
      rankings.add(descriptionWeight +
                   " * ts_rank_cd(courses.description_vec, q.query)");
    }
    if (notesWeight != 0) {
      fields.add("sections.notes_vec @@ q.query");
      rankings.add(notesWeight + " * ts_rank_cd(sections.notes_vec, q.query)");
    }
    if (prereqsWeight != 0) {
      fields.add("sections.prereqs_vec @@ q.query");
      rankings.add(prereqsWeight +
                   " * ts_rank_cd(sections.prereqs_vec, q.query)");
    }

    // @TODO fix all this shit

    if (subject != null)
      subject = subject.toUpperCase();
    if (school != null)
      school = school.toUpperCase();

    String begin = "WITH q (query) AS (SELECT plainto_tsquery(?)) "
                   + "SELECT DISTINCT courses.id FROM q, "
                   + "courses JOIN sections ON courses.id = sections.course_id "
                   + "WHERE (" + String.join(" OR ", fields) + ") AND ";
    PreparedStatement idStmt;
    if (subject != null && school != null) {
      idStmt = conn.prepareStatement(
          begin + "term = ? AND courses.subject = ? AND courses.school = ?");
      Utils.setArray(idStmt, query, term.json(), subject, school);
    } else if (subject != null) {
      idStmt =
          conn.prepareStatement(begin + "term = ? AND courses.subject = ?");
      Utils.setArray(idStmt, query, term.json(), subject);
    } else if (school != null) {
      idStmt = conn.prepareStatement(begin + "term = ? AND courses.school = ?");
      Utils.setArray(idStmt, query, term.json(), school);
    } else {
      idStmt = conn.prepareStatement(begin + "term = ?");
      Utils.setArray(idStmt, query, term.json());
    }

    ArrayList<Integer> result = new ArrayList<>();
    ResultSet rs = idStmt.executeQuery();
    while (rs.next()) {
      result.add(rs.getInt(1));
    }
    rs.close();

    PreparedStatement rowStmt = conn.prepareStatement(
        "WITH q (query) AS (SELECT plainto_tsquery(?)) "
        + "SELECT courses.*, sections.id AS section_id, "
        + "sections.registration_number, sections.section_code, "
        + "sections.section_type, sections.section_status, "
        + "sections.associated_with, sections.waitlist_total, "
        + "sections.name AS section_name, sections.instruction_mode,"
        + "sections.min_units, sections.max_units, sections.location, "
        + "sections.instructors "
        + "FROM q, courses LEFT JOIN sections "
        + "ON courses.id = sections.course_id "
        + "WHERE courses.id = ANY (?) "
        + "ORDER BY " + String.join(" + ", rankings) + " DESC");
    Utils.setArray(rowStmt, query,
                   conn.createArrayOf("INTEGER", result.toArray()));
    Map<Integer, List<Nyu.Meeting>> meetingsList = SelectRows.selectMeetings(
        conn, " courses.id = ANY (?) ",
        conn.createArrayOf("integer", result.toArray()));

    ArrayList<Row> rows = new ArrayList<>();
    rs = rowStmt.executeQuery();
    while (rs.next()) {
      rows.add(new Row(rs, meetingsList.get(rs.getInt("section_id"))));
    }

    rs.close();
    return rows.stream();
  }

  public static Stream<FullRow>
  searchFullRows(Connection conn, Nyu.Term term, String subject, String school,
                 String query, int titleWeight, int descriptionWeight,
                 int notesWeight, int prereqsWeight) throws SQLException {
    if (titleWeight == 0 && descriptionWeight == 0 && notesWeight == 0 &&
        prereqsWeight == 0) {
      throw new IllegalArgumentException("all of the weights were zero");
    }

    ArrayList<String> fields = new ArrayList<>();
    ArrayList<String> rankings = new ArrayList<>();
    if (titleWeight != 0) {
      fields.add("courses.name_vec @@ q.query");
      fields.add("sections.name_vec @@ q.query");
      rankings.add(titleWeight + " * ts_rank_cd(courses.name_vec, q.query)");
      rankings.add(titleWeight + " * ts_rank_cd(sections.name_vec, q.query)");
    }
    if (descriptionWeight != 0) {
      fields.add("courses.description_vec @@ q.query");
      rankings.add(descriptionWeight +
                   " * ts_rank_cd(courses.description_vec, q.query)");
    }
    if (notesWeight != 0) {
      fields.add("sections.notes_vec @@ q.query");
      rankings.add(notesWeight + " * ts_rank_cd(sections.notes_vec, q.query)");
    }
    if (prereqsWeight != 0) {
      fields.add("sections.prereqs_vec @@ q.query");
      rankings.add(prereqsWeight +
                   " * ts_rank_cd(sections.prereqs_vec, q.query)");
    }

    if (subject != null)
      subject = subject.toUpperCase();
    if (school != null)
      school = school.toUpperCase();
    String begin = "WITH q (query) AS (SELECT plainto_tsquery(?)) "
                   + "SELECT DISTINCT courses.id FROM q, "
                   + "courses JOIN sections ON courses.id = sections.course_id "
                   + "WHERE (" + String.join(" OR ", fields) + ") AND ";
    PreparedStatement idStmt;
    if (subject != null && school != null) {
      idStmt = conn.prepareStatement(
          begin + "term = ? AND courses.subject = ? AND courses.school = ?");
      Utils.setArray(idStmt, query, term.json(), subject, school);
    } else if (subject != null) {
      idStmt =
          conn.prepareStatement(begin + "term = ? AND courses.subject = ?");
      Utils.setArray(idStmt, query, term.json(), subject);
    } else if (school != null) {
      idStmt = conn.prepareStatement(begin + "term = ? AND courses.school = ?");
      Utils.setArray(idStmt, query, term.json(), school);
    } else {
      idStmt = conn.prepareStatement(begin + "term = ?");
      Utils.setArray(idStmt, query, term.json());
    }

    ArrayList<Integer> result = new ArrayList<>();
    ResultSet rs = idStmt.executeQuery();
    while (rs.next()) {
      result.add(rs.getInt(1));
    }
    rs.close();

    PreparedStatement rowStmt = conn.prepareStatement(
        "WITH q (query) AS (SELECT plainto_tsquery(?)) "
        + "SELECT courses.*, sections.id AS section_id, "
        + "sections.registration_number, sections.section_code, "
        + "sections.section_type, sections.section_status, "
        + "sections.associated_with, sections.waitlist_total, "
        + "sections.name AS section_name, "
        + "sections.min_units, sections.max_units, sections.location, "
        + "sections.campus, sections.instruction_mode, "
        + "sections.grading, sections.notes, sections.prerequisites, "
        + "sections.instructors "
        + "FROM q, courses LEFT JOIN sections "
        + "ON courses.id = sections.course_id "
        + "WHERE courses.id = ANY (?) "
        + "ORDER BY " + String.join(" + ", rankings) + " DESC");
    Utils.setArray(rowStmt, query,
                   conn.createArrayOf("INTEGER", result.toArray()));
    Map<Integer, List<Nyu.Meeting>> meetingsList = SelectRows.selectMeetings(
        conn, " courses.id = ANY (?) ",
        conn.createArrayOf("integer", result.toArray()));

    ArrayList<FullRow> rows = new ArrayList<>();
    rs = rowStmt.executeQuery();
    while (rs.next()) {
      rows.add(new FullRow(rs, meetingsList.get(rs.getInt("section_id"))));
    }

    rs.close();
    return rows.stream();
  }
}
