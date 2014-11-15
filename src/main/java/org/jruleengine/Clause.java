package org.jruleengine;

/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class represents a simple clause, composed of a name and a value.</p>
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
public class Clause {

  /** property name */
  private String name = "";

  /** property value */
  private Object value = "";


  public Clause() { }



  /**
   * @param name property name
   * @param value property value
   */
  public Clause(String name,Object value) {
    this.name = name;
    this.value = value;
  }


  /**
   * @param name property name
   * value property is set to name
   */
  public Clause(String name) {
    this.name = name;
    this.value = name;
  }


  /**
   * @param name property name
   * @param value property value
   */
  public final void setClause(String name,String value) {
    this.name = name;
    this.value = value;
  }


  /**
   * @param name property name
   * value property is set to name
   */
  public final void setClause(String name) {
    this.name = name;
    this.value = name;
  }


  /**
   * @return property name
   */
  public final String getName() {
    return name;
  }


  /**
   * @return property value
   */
  public final Object getValue() {
    return value;
  }


  /**
   * Set property name.
   * @param name property name
   */
  public final void setName(String name) {
    this.name = name;
  }


  /**
   * Set property value.
   * @param value property value
   */
  public final void setValue(Object value) {
    this.value = value;
  }


}