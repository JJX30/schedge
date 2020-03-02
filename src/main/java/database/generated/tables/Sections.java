/*
 * This file is generated by jOOQ.
 */
package database.generated.tables;


import database.generated.DefaultSchema;
import database.generated.Indexes;
import database.generated.Keys;
import database.generated.tables.records.SectionsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.13.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sections extends TableImpl<SectionsRecord> {

    private static final long serialVersionUID = 1561996147;

    /**
     * The reference instance of <code>sections</code>
     */
    public static final Sections SECTIONS = new Sections();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SectionsRecord> getRecordType() {
        return SectionsRecord.class;
    }

    /**
     * The column <code>sections.id</code>.
     */
    public final TableField<SectionsRecord, Integer> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sections.registration_number</code>.
     */
    public final TableField<SectionsRecord, Integer> REGISTRATION_NUMBER = createField(DSL.name("registration_number"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sections.course_id</code>.
     */
    public final TableField<SectionsRecord, Integer> COURSE_ID = createField(DSL.name("course_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sections.section_code</code>.
     */
    public final TableField<SectionsRecord, String> SECTION_CODE = createField(DSL.name("section_code"), org.jooq.impl.SQLDataType.VARCHAR(5).nullable(false), this, "");

    /**
     * The column <code>sections.instructor</code>.
     */
    public final TableField<SectionsRecord, String> INSTRUCTOR = createField(DSL.name("instructor"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>sections.section_type</code>.
     */
    public final TableField<SectionsRecord, Integer> SECTION_TYPE = createField(DSL.name("section_type"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sections.section_status</code>.
     */
    public final TableField<SectionsRecord, Integer> SECTION_STATUS = createField(DSL.name("section_status"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>sections.associated_with</code>.
     */
    public final TableField<SectionsRecord, Integer> ASSOCIATED_WITH = createField(DSL.name("associated_with"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>sections.waitlist_total</code>.
     */
    public final TableField<SectionsRecord, Integer> WAITLIST_TOTAL = createField(DSL.name("waitlist_total"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>sections.section_name</code>.
     */
    public final TableField<SectionsRecord, String> SECTION_NAME = createField(DSL.name("section_name"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>sections.min_units</code>.
     */
    public final TableField<SectionsRecord, Float> MIN_UNITS = createField(DSL.name("min_units"), org.jooq.impl.SQLDataType.REAL, this, "");

    /**
     * The column <code>sections.max_units</code>.
     */
    public final TableField<SectionsRecord, Float> MAX_UNITS = createField(DSL.name("max_units"), org.jooq.impl.SQLDataType.REAL, this, "");

    /**
     * The column <code>sections.campus</code>.
     */
    public final TableField<SectionsRecord, String> CAMPUS = createField(DSL.name("campus"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>sections.description</code>.
     */
    public final TableField<SectionsRecord, String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>sections.notes</code>.
     */
    public final TableField<SectionsRecord, String> NOTES = createField(DSL.name("notes"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>sections.instruction_mode</code>.
     */
    public final TableField<SectionsRecord, String> INSTRUCTION_MODE = createField(DSL.name("instruction_mode"), org.jooq.impl.SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>sections.grading</code>.
     */
    public final TableField<SectionsRecord, String> GRADING = createField(DSL.name("grading"), org.jooq.impl.SQLDataType.VARCHAR(48), this, "");

    /**
     * The column <code>sections.location</code>.
     */
    public final TableField<SectionsRecord, String> LOCATION = createField(DSL.name("location"), org.jooq.impl.SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>sections.prerequisites</code>.
     */
    public final TableField<SectionsRecord, String> PREREQUISITES = createField(DSL.name("prerequisites"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * Create a <code>sections</code> table reference
     */
    public Sections() {
        this(DSL.name("sections"), null);
    }

    /**
     * Create an aliased <code>sections</code> table reference
     */
    public Sections(String alias) {
        this(DSL.name(alias), SECTIONS);
    }

    /**
     * Create an aliased <code>sections</code> table reference
     */
    public Sections(Name alias) {
        this(alias, SECTIONS);
    }

    private Sections(Name alias, Table<SectionsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Sections(Name alias, Table<SectionsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Sections(Table<O> child, ForeignKey<O, SectionsRecord> key) {
        super(child, key, SECTIONS);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SECTIONS_ASSOCIATED_WITH);
    }

    @Override
    public UniqueKey<SectionsRecord> getPrimaryKey() {
        return Keys.PK_SECTIONS;
    }

    @Override
    public List<UniqueKey<SectionsRecord>> getKeys() {
        return Arrays.<UniqueKey<SectionsRecord>>asList(Keys.PK_SECTIONS);
    }

    @Override
    public List<ForeignKey<SectionsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SectionsRecord, ?>>asList(Keys.FK_SECTIONS_COURSES_1, Keys.FK_SECTIONS_SECTIONS_1);
    }

    public Courses courses() {
        return new Courses(this, Keys.FK_SECTIONS_COURSES_1);
    }

    public Sections sections() {
        return new Sections(this, Keys.FK_SECTIONS_SECTIONS_1);
    }

    @Override
    public Sections as(String alias) {
        return new Sections(DSL.name(alias), this);
    }

    @Override
    public Sections as(Name alias) {
        return new Sections(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Sections rename(String name) {
        return new Sections(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Sections rename(Name name) {
        return new Sections(name, null);
    }

    // -------------------------------------------------------------------------
    // Row19 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row19<Integer, Integer, Integer, String, String, Integer, Integer, Integer, Integer, String, Float, Float, String, String, String, String, String, String, String> fieldsRow() {
        return (Row19) super.fieldsRow();
    }
}
