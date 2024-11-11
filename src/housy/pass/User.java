package housy.pass;

import java.util.List;
import java.util.Objects;
import javax.crypto.SecretKey;

public class User {

    public String username;
    public byte[] hash;
    public byte[] salt;
    public List<Entry> entries;
    public SecretKey key;
    public byte[] iv;
    public long autoLogoutTime;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.username);
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }
}
