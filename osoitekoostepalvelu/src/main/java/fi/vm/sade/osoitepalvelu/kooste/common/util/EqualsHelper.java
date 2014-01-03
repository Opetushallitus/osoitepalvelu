package fi.vm.sade.osoitepalvelu.kooste.common.util;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class EqualsHelper {

    public static boolean equals(Object x, Object y) {
        return x == y || (x != null && y != null && x.equals(y));
    }

    private Object[] state;

    public EqualsHelper(Object ...state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EqualsHelper)) {
            return false;
        }
        EqualsHelper other = (EqualsHelper) obj;
        if (other.state.length != this.state.length) {
            return false;
        }
        for (int i = 0; i < this.state.length; ++i) {
            if (!equals(this.state[i], other.state[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        // http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
        // In practice: Use the same set of fields that you use to compute equals() to compute hashCode().
        HashCodeBuilder hashBuilder = new HashCodeBuilder();
        hashBuilder.append(this.state.length);
        for (int i = 0; i < this.state.length; ++i) {
            hashBuilder.append(this.state[i]);
        }
        return hashBuilder.toHashCode();
    }
}
