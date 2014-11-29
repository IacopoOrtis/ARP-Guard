import java.util.Comparator;

/**
 * This class implements a Binding class comparator
 * 
 * @author IacopoOrtis
 *
 */
class BindingComparator implements Comparator<Binding> {
	@Override
	public int compare(Binding b1, Binding b2) {
		return b1.getBinding().compareTo(b2.getBinding());
	}
}