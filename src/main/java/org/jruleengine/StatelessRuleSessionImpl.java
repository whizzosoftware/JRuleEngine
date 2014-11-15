package org.jruleengine;

import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import javax.rules.*;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: this class is a representation of a stateless rules engine session.
 * A stateless rules engine session exposes a stateless rule execution API to an underlying rules engine.
 * </p>
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
public class StatelessRuleSessionImpl  implements StatelessRuleSession {

  /** stateful session */
  private StatefulRuleSessionImpl session;

  /** rules */
  private RuleExecutionSetImpl ruleSet;


  public StatelessRuleSessionImpl(RuleExecutionSetImpl res, Map properties) {
    session = new StatefulRuleSessionImpl(res, properties);
    this.ruleSet = res;
  }


  /**
   * Executes the rules in the bound rule execution set using the supplied list of objects.
   */
  public final List executeRules(List objects) throws InvalidRuleSessionException {
    return executeRules(objects, ruleSet.resolveObjectFilter());
  }


  /**
   * Executes the rules in the bound rule execution set using the supplied list of objects.
   */
  public final List executeRules(List objects, ObjectFilter filter)
    throws InvalidRuleSessionException {
    session.reset();
    session.addObjects(objects);
    session.executeRules();
    return session.getObjects(filter);
  }


  public final RuleExecutionSetMetadata getRuleExecutionSetMetadata() throws InvalidRuleSessionException {
    return session.getRuleExecutionSetMetadata();
  }


  public final void release() throws InvalidRuleSessionException {
    session.release();
    session = null;
  }


  public final int getType() throws InvalidRuleSessionException {
    session.validateRuleSession();
    return RuleRuntime.STATELESS_SESSION_TYPE;
  }


  /**
   * @return method not included into JSR specifications: used to access to working memory
   */
  public final Hashtable getWorkingMemory() {
    return session.getWorkingMemory();
  }


}