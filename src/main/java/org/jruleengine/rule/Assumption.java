package org.jruleengine.rule;

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

public class Assumption implements Serializable {

  /** used as operator */
  public static final String EQUALS_TO = "=";

  /** used as operator */
  public static final String LESS_THAN = "<";

  /** used as operator */
  public static final String LESS_OR_EQUALS_TO = "<=";

  /** used as operator */
  public static final String GREATER_THAN = ">";

  /** used as operator */
  public static final String GREATER_OR_EQUALS_TO = ">=";

  /** used as operator */
  public static final String NOT_EQUALS_TO = "<>";

  /** used as operator */
  public static final String EXISTS = "exists";

  /** used as operator */
  public static final String CONTAINS = "contains";

  /** used as operator */
  public static final String NOT_CONTAINS = "notcontains";

  /** used as operator */
  public static final String CONTAINSATLEASTONE = "containsatleastone";

  /** used as operator */
  public static final String NOT_CONTAINSANYONE = "notcontainsanyone";

  /** left term of the assumption */
  private String leftTerm;

  /** operator of the assumption; possible values: =,<,>,<=,>=,<> */
  private String operator;

  /** right term of the assumption */
  private String rightTerm;


  /**
   * @param uniqueTerm unique term of the assumption
   * operator is set to "exists"
   * rightTerm is set to ""
   */
  public Assumption(String uniqueTerm) {
    this.leftTerm = uniqueTerm;
    this.operator = "exists";
    this.rightTerm = "";
  }


  /**
   * @param leftTerm left term of the assumption
   * @param operator operator of the assumption; possible values: =,<,>,<=,>=,<>
   * @param rightTerm right term of the assumption
   */
  public Assumption(String leftTerm,String operator,String rightTerm) {
    this.leftTerm = leftTerm;
    this.operator = operator;
    this.rightTerm = rightTerm;
  }


  /**
   * @return left term of the assumption
   */
  public final String getLeftTerm() {
    return leftTerm;
  }


  /**
   * @return operator of the assumption; possible values: =,<,>,<=,>=,<>
   */
  public final String getOperator() {
    return operator;
  }


  /**
   * @return right term of the assumption
   */
  public final String getRightTerm() {
    return rightTerm;
  }

}