package org.jruleengine.rule;

import java.util.List;
import java.io.Serializable;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class implements an assumption inside a rule.</p>
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
public class Action implements Serializable {

  /** method to execute */
  private String method;

  /** argument values of the method to execute */
  private List values;

  /**
   * @param method one argument method to execute
   * @param values argument values of the method to execute
   */
  public Action(String method,List values) {
    this.method = method;
    this.values = values;
  }


  /**
   * @return left term of the assumption
   */
  public final String getMethod() {
    return method;
  }


  /**
   * @return argument value of the method to execute
   */
  public final List getValues() {
    return values;
  }


}
