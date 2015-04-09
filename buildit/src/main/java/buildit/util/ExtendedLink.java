package buildit.util;

import javax.xml.bind.annotation.XmlType;

import org.springframework.hateoas.Link;

@XmlType(name = "_link", namespace = Link.ATOM_NAMESPACE)
public class ExtendedLink extends Link {
	private static final long serialVersionUID = -9037755944661782122L;
	private String elMethod;

	protected ExtendedLink() {
	}

	public ExtendedLink(final String href, final String rels, final String subMethod) {
		super(href, rels);
		this.elMethod = subMethod;
	}

	public String getMethod() {
		return elMethod;
	}

	public void setMethod(final String method) {
		this.elMethod = method;
	}
}
