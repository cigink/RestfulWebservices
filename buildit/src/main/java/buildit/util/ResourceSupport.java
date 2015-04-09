package buildit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlTransient
public class ResourceSupport extends
		org.springframework.hateoas.ResourceSupport {
	@XmlElement(name = "_link", namespace = Link.ATOM_NAMESPACE)
	@JsonProperty("_links")
	private final List<ExtendedLink> _links;

	public ResourceSupport() {
		super();
		this._links = new ArrayList<>();
	}

	@Override
	public void add(final Link link) {
		if (link instanceof ExtendedLink) {
			this._links.add((ExtendedLink) link);
		} else {
			super.add(link);
		}
	}

	public List<ExtendedLink> getlinks() {
		return Collections.unmodifiableList(_links);
	}

	public void removelink() {
		_links.clear();
	}

	public Link getlink(final String rels) {

		for (final Link link : _links) {
			if (link.getRel().equals(rels)) {
				return link;
			}
		}

		return null;
	}
}
