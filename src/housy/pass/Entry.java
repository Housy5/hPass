package housy.pass;

import java.util.Objects;

public class Entry {

    public String appName = "";
    public String password = "";

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.appName);
        hash = 47 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (obj instanceof Entry other) {
            return other.appName.equalsIgnoreCase(appName) && other.password.equals(password);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Entry{" + "appName=" + appName + ", passWord=" + password + '}';
    }

}
