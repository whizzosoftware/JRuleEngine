package org.jruleengine;

import java.io.*;
import java.util.*;
import javax.rules.admin.*;
import javax.rules.admin.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import org.jruleengine.rule.*;

/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Local Rule Execution Set Provider Implementation.</p>
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
public class LocalXmlRuleExecutionSetProvider extends AbstractLocalRuleExecutionSetProvider {

  public static final String NAME = "name";
  public static final String DESCRIPTION = "description";


  LocalXmlRuleExecutionSetProvider() { }


  /**
   * Creates a RuleExecutionSet implementation using a supplied input stream and additional vendor-specific properties.
   */
  public RuleExecutionSet createRuleExecutionSet(InputStream inputStream, Map props)
      throws RuleExecutionSetCreateException, IOException {
    Document doc = null;
    try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(new InputSource(inputStream));
    }
    catch(Exception e) {
        throw new RuleExecutionSetCreateException("Parse error", e);
    }
    return createRuleExecutionSet(doc.getDocumentElement(), props);
  }


  /**
   * Creates a RuleExecutionSet implementation using a supplied character stream Reader and additional vendor-specific properties.
   */
  public RuleExecutionSet createRuleExecutionSet(Reader reader, Map props)
      throws RuleExecutionSetCreateException, IOException {
    Document doc = null;
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.parse(new InputSource(reader));
    }
    catch(Exception e) {
      throw new RuleExecutionSetCreateException("Parse error", e);
    }
    return createRuleExecutionSet(doc.getDocumentElement(), props);
  }


  /**
   * Creates a RuleExecutionSet implementation from a vendor specific AST representation and vendor-specific properties.
   */
  public RuleExecutionSet createRuleExecutionSet(Object ast, Map properties)
      throws RuleExecutionSetCreateException {
    if (ast==null || !(ast instanceof Collection))
      throw new RuleExecutionSetCreateException("Invalid type argument: AST argument must be a collection of RuleImpl objects");
    if (properties == null) {
      properties = new HashMap();
    }
    try {
      return createRuleExecutionSetFromRuleList( (Collection) ast, properties);
    }
    catch (Exception ex) {
      throw new RuleExecutionSetCreateException(ex.getMessage());
    }
  }


  /**
   * Create the rule execution set.
   */
  private RuleExecutionSet createRuleExecutionSet(Element docElement, Map properties)
      throws RuleExecutionSetCreateException {
    try {
      NodeList contents = docElement.getElementsByTagName("name");
      if (contents.getLength() == 0) {
        throw new RuleExecutionSetCreateException("Name not specified");
      }
      String name = contents.item(0).getFirstChild().getNodeValue().trim();

      contents = docElement.getElementsByTagName("description");
      if (contents.getLength() == 0) {
        throw new RuleExecutionSetCreateException("Description not specified");
      }
      String description = contents.item(0).getFirstChild().getNodeValue().trim();

      if (properties == null) {
        properties = new HashMap();
      }
      properties.put("name", name);
      properties.put("description", description);

      Element el,node = null;
      ArrayList assumptions = null;
      ArrayList actions = null;
      NodeList nodes = null;

      // read synonyms...
      Hashtable synonymns = new Hashtable();
      nodes = docElement.getElementsByTagName("synonymn");
      for(int j=0;j<nodes.getLength();j++) {
        node = (Element)nodes.item(j);
        synonymns.put(
          node.getAttribute("name"),
          node.getAttribute("class")
        );
      }

      // read rules...
      contents = docElement.getElementsByTagName("rule");
      ArrayList rules = new ArrayList();
      for(int i=0;i<contents.getLength();i++) {
        el = (Element)contents.item(i);
        assumptions = new ArrayList();
        actions = new ArrayList();

        // read assumptions...
        nodes = el.getElementsByTagName("if");
        for(int j=0;j<nodes.getLength();j++) {
          node = (Element)nodes.item(j);

          if (node.getAttributeNode("op")!=null &&
              node.getAttributeNode("op").getValue()!=null &&
              node.getAttributeNode("rightTerm")!=null &&
              node.getAttributeNode("rightTerm").getValue()!=null)
            assumptions.add(new Assumption(
              parseClassName(node.getAttribute("leftTerm"),synonymns),
              node.getAttribute("op"),
              parseClassName(node.getAttribute("rightTerm"),synonymns)
            ));
          else
            assumptions.add(new Assumption(
              parseClassName(node.getAttribute("leftTerm"),synonymns)
            ));
        }

        // read actions...
        ArrayList args = null;
        int k;
        nodes = el.getElementsByTagName("then");
        for(int j=0;j<nodes.getLength();j++) {
          node = (Element)nodes.item(j);
          args = new ArrayList();
          k=1;
          while(node.getAttributeNode("arg"+k)!=null &&
                node.getAttributeNode("arg"+k).getValue()!=null) {
            args.add( parseClassName(node.getAttribute("arg"+k),synonymns) );
            k++;
          }
          actions.add(new Action(
            parseClassName(node.getAttribute("method"),synonymns),args
          ));
        }
        boolean enabled = true;
        if (el.hasAttribute("enabled")) {
          enabled = Boolean.parseBoolean(el.getAttribute("enabled"));
        }
        rules.add(new RuleImpl(
            el.getAttribute("name"),
            el.getAttribute("description"),
            assumptions,
            actions,
            enabled
        ));
      }

      return createRuleExecutionSetFromRuleList(rules, properties);
    }
    catch (Exception ex) {
      throw new RuleExecutionSetCreateException(ex.getMessage());
    }
  }
}
