package gr.illumine.jetlivesource;


import org.apache.log4j.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

class SAXParserHelper extends DefaultHandler {
	
	Logger logger =  Logger.getLogger(SAXParserHelper.class.getName());
	
	public Locator m_locator;

	public void setDocumentLocator(Locator locator) {
		this.m_locator = locator;
		logger.info("SYS ID: " + m_locator.getSystemId());
	}

	void syntax_error(String err_msg) throws SAXException {
		String msg = "XML (" + m_locator.getSystemId() + "). " + err_msg;
		logger.error("ERROR : " + msg);
		throw new SAXException(msg);
	}

	void syntax_error_for_element(String element, String attribute)
			throws SAXException {
		String msg = "XML (" + m_locator.getSystemId()
				+ "). You should specify attribute (" + attribute
				+ ") for element (" + element + ") At line : "
				+ m_locator.getLineNumber();

		logger.error("ERROR : " + msg);
		throw new SAXException(msg);
	}

	String get_attributes_value(Attributes attrs, String attr_key) {
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i); // Attr name
				String val = attrs.getValue(i); // Attr value

				if (aName.equals(""))
					aName = attrs.getQName(i);

				if (aName.equals(attr_key)) {
					if (val.equals(""))
						return null;
					else
						return val;
				}
			}
		}
		return null;
	}

}
