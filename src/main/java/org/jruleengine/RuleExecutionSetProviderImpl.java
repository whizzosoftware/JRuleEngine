package org.jruleengine;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.Map;
import javax.rules.admin.*;
import org.w3c.dom.Element;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Rule Execution Set Provider Implementation.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JRuleEngine project.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class RuleExecutionSetProviderImpl implements RuleExecutionSetProvider {


  public RuleExecutionSetProviderImpl() {}


  /**
   * Creates a RuleExecutionSet implementation from an XML Document and additional vendor-specific properties.
   */
  public RuleExecutionSet createRuleExecutionSet(Element docElement, Map properties)
      throws RuleExecutionSetCreateException, RemoteException {
    return (new LocalXmlRuleExecutionSetProvider()).createRuleExecutionSet(docElement, properties);
  }


  /**
   * Creates a RuleExecutionSet implementation from a vendor specific AST representation and vendor-specific properties.
   */
  public RuleExecutionSet createRuleExecutionSet(Serializable ast, Map properties)
      throws RuleExecutionSetCreateException, RemoteException {
    throw new RuleExecutionSetCreateException("Operation not supported");
  }


  /**
   * Creates a RuleExecutionSet implementation from a URI.
   */
  public RuleExecutionSet createRuleExecutionSet(String uri, Map properties)
      throws RuleExecutionSetCreateException, IOException, RemoteException {
    URLConnection urlc = (new URL(uri)).openConnection();
    java.io.InputStream is = urlc.getInputStream();
    return (new LocalXmlRuleExecutionSetProvider()).createRuleExecutionSet(is, properties);
  }

}