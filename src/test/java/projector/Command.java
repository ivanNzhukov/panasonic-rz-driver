package projector;

public class Command {

    private String function;
    private String parametr;
    private String subparametr;
    private String control;
    private String query;
    private String callback;
    private String humanReadeable;

    public Command() {
    }

    public Command(String function, String parametr, String subparametr, String control, String query, String callback, String humanReadeable) {
        this.function = function;
        this.parametr = parametr;
        this.subparametr = subparametr;
        this.control = control;
        this.query = query;
        this.callback = callback;
        this.humanReadeable = humanReadeable;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getParametr() {
        return parametr;
    }

    public void setParametr(String parametr) {
        this.parametr = parametr;
    }

    public String getSubparametr() {
        return subparametr;
    }

    public void setSubparametr(String subparametr) {
        this.subparametr = subparametr;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getHumanReadeable() {
        return humanReadeable;
    }

    public void setHumanReadeable(String humanReadeable) {
        this.humanReadeable = humanReadeable;
    }

    public enum Errors {
        ERR1("Management command not defined"),
        ERR2("Parameter range exceeded"),
        ERR3("Employment Status or invalid period"),
        ERR4("Waiting time or invalid period"),
        ERR5("Invalid data length"),
        ERRA("Password mismatch");

        private String description;

        Errors(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }


}
