package org.jruleengine;

import java.util.*;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Rule Execution Set Implementation.</p>
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
public class RuleExecutionSetImpl extends Hashtable implements RuleExecutionSet {

  /** rule execution set name */
  private String name;

  /** rule execution set description */
  private String description;

  /** rule execution set URI */
  private String uri;

  /** rule filter */
  private String filter;

  /** rules */
  private ArrayList rules = new ArrayList();

  /** user defined or vendor defined properties */
  private Hashtable props = new Hashtable();


  /**
   * Create the rule execution set.
   * @param name rule execution set name
   * @param description rule execution set description
   * @param uri rule execution set URI
   */
  RuleExecutionSetImpl(String name, String description, String uri) {
    this.name = name;
    this.description = description;
    this.uri = uri;
  }


  /**
   * @return rule execution set name
   */
  public final String getName() {
    return name;
  }


  /**
   * @return rule execution set description
   */
  public final String getDescription() {
    return description;
  }


  /**
   * @return rule execution set URI
   */
  public final String getUri() {
    return uri;
  }


  /**
   * Set the rule execution set URI.
   * @param uri rule execution set URI
   */
  public final void setUri(String uri) {
    this.uri = uri;
  }


  /**
   * Set the filter.
   * @param objectFilterClassname filter class name
   */
  public final void setDefaultObjectFilter(String objectFilterClassname) {
    filter = objectFilterClassname;
  }


  /**
   * @return filter class name
   */
  public final String getDefaultObjectFilter() {
    return filter;
  }


  /**
   * @return filter
   * @throws InvalidRuleSessionException
   */
  public final ObjectFilter resolveObjectFilter() throws InvalidRuleSessionException {
    try {
      if (filter == null) {
        return new ObjectFilterImpl();
      }
      Class c = Class.forName(filter);
      return (ObjectFilter)c.newInstance();
    }
    catch (Exception ex) {
      throw new InvalidRuleSessionException("Bad object filter", ex);
    }
  }


  /**
   * @return rules of the rule execution set
   */
  public final List getRules() {
    return rules;
  }


  /**
   * @param propName a user defined or vendor defined property name
   * @return a user defined or vendor defined property
   */
  public final Object getProperty(Object propName) {
    return props.get(propName);
  }


  /**
   * Set a user defined or vendor defined property.
   * @param propName a user defined or vendor defined property name
   * @param propValue a user defined or vendor defined property value
   */
  public final void setProperty(Object propName, Object propValue) {
    props.put(propName,propValue);
  }

}