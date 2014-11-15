package org.jruleengine.rule;

import java.util.*;
import javax.rules.admin.Rule;
import java.io.Serializable;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class implements a rule.</p>
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
public class RuleImpl implements Rule, Serializable {

  /** rule name */
  private String name;

  /** assumptions list */
  private ArrayList assumptions;

  /** actions list */
  private ArrayList actions;

  /** rule description */
  private String description;

  /** user defined or vendor defined properties */
  private Hashtable props = new Hashtable();

  private boolean enabled;

  /**
   * @param name rule name
   * @param description rule description
   * @param assumptions assumptions list
   * @param actions actions list
   */
  public RuleImpl(String name,String description,ArrayList assumptions,ArrayList actions,boolean enabled) {
    this.name = name;
    this.description = description;
    this.assumptions = assumptions;
    this.actions = actions;
    this.enabled = enabled;
  }


  /**
   * @return rule name
   */
  public final String getName() {
    return name;
  }


  /**
   * @return assumptions list
   */
  public final ArrayList getAssumptions() {
    return assumptions;
  }


  /**
   * @return actions list
   */
  public final ArrayList getActions() {
    return actions;
  }


  /**
   * @return rule description
   */
  public final String getDescription() {
    return description;
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

  public final boolean isEnabled() {
    return enabled;
  }
}