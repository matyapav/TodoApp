package cz.matyapav.todoapp.todo.util.enums;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */

public enum SupportedLanguages {

    ENGLISH("English", "en"),
    CZECH("Čestina", "cs");

    String langName;
    String langAbbreviation;

    SupportedLanguages(String langName, String langAbbreviation) {
        this.langName = langName;
        this.langAbbreviation = langAbbreviation;
    }

    public String getLangName() {
        return langName;
    }

    public String getLangAbbreviation() {
        return langAbbreviation;
    }
}
