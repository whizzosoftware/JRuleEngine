package org.jruleengine;

import java.rmi.RemoteException;
import java.util.*;
import javax.rules.admin.*;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Rule Administrator Implementation.</p>
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
public class RuleAdministratorImpl implements RuleAdministrator {

  /** rule execution sets */
  private static Map ruleExecutionSets = Collections.synchronizedMap(new HashMap());


  public RuleAdministratorImpl() { }


  /**
   * Creates a RuleExecutionSetProvider instance.
   * @param properties not supported
   * @return RuleExecutionSetProvider instance
   */
  public final RuleExecutionSetProvider getRuleExecutionSetProvider(Map properties) throws RemoteException {
      return new RuleExecutionSetProviderImpl();
  }


  /**
   * Creates a LocalRuleExecutionSetProvider instance.
   * @param properties not supported
   * @return LocalRuleExecutionSetProvider instance.
   */
  public final LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(Map properties)
      throws RemoteException, UnsupportedOperationException {
    if ("application/json".equals(properties.get("Content-Type"))) {
        return new LocalJsonRuleExecutionSetProvider();
    } else {
        return new LocalXmlRuleExecutionSetProvider();
    }
  }


  /**
   * Register a rule execution set.
   * @param bindUri rule execution set name
   * @param set rule execution set
   * @param properties not supported
   */
  public final void registerRuleExecutionSet(String bindUri, RuleExecutionSet set, Map properties)
      throws RuleExecutionSetRegisterException, RemoteException {
    if(!(set instanceof RuleExecutionSetImpl)) {
      throw new RuleExecutionSetRegisterException("Wrong driver");
    }
    else {
        ((RuleExecutionSetImpl)set).setUri(bindUri);
        ruleExecutionSets.put(bindUri, set);
        return;
    }
  }


  /**
   * De-register a rule execution set.
   * @param bindUri rule execution set name
   * @param properties not supported
   */
  public final void deregisterRuleExecutionSet(String bindUri, Map properties)
      throws RuleExecutionSetDeregistrationException, RemoteException {
    RuleExecutionSetImpl set = (RuleExecutionSetImpl)ruleExecutionSets.remove(bindUri);
    if(set != null)
      set.setUri(null);
  }


  /**
   * @return rule execution set names
   */
  static List getRegistrations() {
    return new ArrayList(ruleExecutionSets.keySet());
  }


  /**
   * @param uri rule execution set name
   * @return rule execution set
   */
  static RuleExecutionSetImpl lookup(String uri) {
    return (RuleExecutionSetImpl)ruleExecutionSets.get(uri);
  }


}
