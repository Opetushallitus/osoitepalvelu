package fi.vm.sade.osoitepalvelu.kooste.common.util;

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
        if( !(obj instanceof EqualsHelper) ) {
            return false;
        }
        EqualsHelper other = (EqualsHelper) obj;
        if( other.state.length != this.state.length )
            return false;
        for( int i = 0; i < this.state.length; ++i ) {
            if(!equals( this.state[i], other.state[i] ) )
                return false;
        }
        return true;
    }
}
