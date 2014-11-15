package org.jruleengine;

import java.util.*;
import javax.rules.*;
import java.lang.reflect.*;
import java.awt.Component;

import org.jruleengine.rule.*;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class is a representation of a stateful rules engine session.
 * A stateful rules engine session exposes a stateful rule execution API to an underlying rules engine.
 * The session allows arbitrary objects to be added and removed to and from the rule session state.
 * Additionally, objects currently part of the rule session state may be updated.
 *
 * There are inherently side-effects to adding objects to the rule session state.
 * The execution of a RuleExecutionSet can add, remove and update objects in the rule session state.
 * The objects in the rule session state are therefore dependent on the rules within the RuleExecutionSet as well as the
 * rule engine vendor's specific rule engine behavior.
 *
 * Handle instances are used by the rule engine vendor to track Objects added to the rule session state.
 * This allows multiple instances of equivalent Objects to be added to the session state and identified, even after serialization.
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
public class StatefulRuleSessionImpl implements StatefulRuleSession {

  /** rules */
  private RuleExecutionSetImpl ruleSet;

  /** working memory */
  private Hashtable workingMemory = new Hashtable();


  public StatefulRuleSessionImpl(RuleExecutionSetImpl ruleset, Map properties) {
    this.ruleSet = ruleset;
  }


  /**
   * @return <code>true</code> if the given object is contained within rule session state of this rule session, <code>false</code> otherwise
   */
  public final boolean containsObject(Handle objectHandle)
      throws InvalidRuleSessionException, InvalidHandleException {
    try {
      validateRuleSession();
      Object obj = getObject(objectHandle);
      return workingMemory.contains(obj);
    }
    catch (Exception ex) {
      throw new InvalidRuleSessionException("Internal error", ex);
    }
  }


  /**
   * Adds a given object to the rule session state of this rule session.
   */
  public final Handle addObject(Object object) throws InvalidRuleSessionException {
    try {
      validateRuleSession();

      if (object instanceof Clause)
        workingMemory.put(((Clause)object).getName(),((Clause)object).getValue());
      else if (object instanceof Component && ((Component)object).getName()!=null)
        workingMemory.put(((Component)object).getName(),object);
      else {
        workingMemory.put(object.getClass().getName(),object);
        if (!object.getClass().getName().startsWith("java.")) {
          // add an entry for each super-class...
          Class c = object.getClass().getSuperclass();
          while(c!=null) {
            workingMemory.put(c.getName(),object);
            c = c.getSuperclass();
          }
          // add an entry for each implemented interface
          for (Class i : object.getClass().getInterfaces()) {
              workingMemory.put(i.getName(),object);
          }
        }
      }
      return new HandleImpl(object);
    }
    catch (Exception ex) {
      throw new InvalidRuleSessionException("Internal error", ex);
    }
  }


  /**
   * Adds a List of Objects to the rule session state of this rule session.
   */
  public final List addObjects(List objList) throws InvalidRuleSessionException {
    validateRuleSession();
    ArrayList al = new ArrayList();
    Iterator it = objList.iterator();
    while(it.hasNext())
      al.add(
          addObject(it.next())
      );
    return al;
  }


  /**
   * Notifies the rules engine that a given object in the rule session state has changed.
   */
  public final void updateObject(Handle objectHandle, Object newObject)
      throws InvalidRuleSessionException, InvalidHandleException {
    validateRuleSession();
    try {
      Object obj = getObject(objectHandle);

      workingMemory.remove(obj);

      HandleImpl newHandle = (HandleImpl)addObject(newObject);

      ((HandleImpl)objectHandle).setObject(newHandle.getObject());
    }
    catch(Exception je) {
      throw new InvalidHandleException("Internal error", je);
    }
  }


  /**
   * Removes a given object from the rule session state of this rule session.
   */
  public final void removeObject(Handle handleObject)
      throws InvalidHandleException, InvalidRuleSessionException {
    validateRuleSession();
    try {
      workingMemory.remove(getObject(handleObject));
    }
    catch(Exception je) {
      throw new InvalidRuleSessionException("Internal error", je);
    }
  }


  /**
   * @return List of all objects in the rule session state of this rule session
   */
  public final List getObjects() throws InvalidRuleSessionException {
    validateRuleSession();
    return getObjects(ruleSet.resolveObjectFilter());
  }


  /**
   * @return List over the objects in rule session state of this rule session
   */
  public final List getObjects(ObjectFilter filter) throws InvalidRuleSessionException {
    validateRuleSession();
    ArrayList al = new ArrayList();
    Iterator it = workingMemory.values().iterator();
    Object obj = null;
    while(it.hasNext())
      try {
        obj = it.next();
        if((obj = filter.filter(obj)) != null)
          al.add(obj);
      }
      catch(Exception ex) {
          throw new InvalidRuleSessionException("Internal error", ex);
      }

    return al;
  }


  /**
   * @return List of the Handles being used for object identity
   */
  public final List getHandles() throws InvalidRuleSessionException {
    validateRuleSession();
    ArrayList al = new ArrayList();
    Enumeration en = workingMemory.keys();
    Object obj = null;
    while(en.hasMoreElements())
      try {
        obj = en.nextElement();
        al.add(new HandleImpl(obj));
      }
      catch(Exception ex) {
          throw new InvalidRuleSessionException("Internal error", ex);
      }
    return al;
  }


  /**
   * Executes the rules in the bound rule execution set using the objects present in the rule session state.
   */
  public final void executeRules() throws InvalidRuleSessionException {
    validateRuleSession();
    try {
      forwardChaining(new HashSet());
    }
    catch(Exception ex) {
      throw new InvalidRuleSessionException("Internal error", ex);
    }
  }


  /**
   * Resets this rule session.
   */
  public final void reset() throws InvalidRuleSessionException {
    validateRuleSession();
    try {
      workingMemory.clear();
    }
    catch(Exception ex) {
        throw new InvalidRuleSessionException("Internal error", ex);
    }
  }


  /**
   * @return Object within the StatefulRuleSession associated with a Handle
   */
  public final Object getObject(Handle handle) throws InvalidHandleException, InvalidRuleSessionException {
    validateRuleSession();
    if(!(handle instanceof HandleImpl)) {
      throw new InvalidHandleException("Wrong driver");
    }
    else {
      HandleImpl hi = (HandleImpl)handle;
      return hi.getObject();
    }
  }


  /**
   * @return meta data for the rule execution set bound to this rule session
   */
  public final RuleExecutionSetMetadata getRuleExecutionSetMetadata() throws InvalidRuleSessionException {
    validateRuleSession();
    return new RuleExecutionSetMetadataImpl(ruleSet);
  }


  /**
   * Releases all resources used by this rule session.
   */
  public final void release() throws InvalidRuleSessionException {
    validateRuleSession();
    reset();
    ruleSet = null;
    workingMemory.clear();
  }


  /**
   * @return type identifier for this RuleSession
   */
  public final int getType() throws InvalidRuleSessionException {
    validateRuleSession();
    return RuleRuntime.STATEFUL_SESSION_TYPE;
  }


  /**
   * Verify that there exixts a rule execution set.
   */
  public final void validateRuleSession() throws InvalidRuleSessionException {
    if(ruleSet == null)
      throw new InvalidRuleSessionException("Null RuleExecutionSet");
  }


  /**
   * Execute the specified method on obj instance.
   * @param action method + argument value
   * @param obj instance containing the method to invoke
   */
  private void executeMethod(Action action,Object obj) throws InvalidRuleSessionException {
    Method[] methods = null;
    Object argValue = null;
    int i = -1;
    try {
      String method = action.getMethod();
      if (method.indexOf(".")==-1)
        throw new InvalidRuleSessionException("No method '"+action.getMethod()+"' found");
      else
        method = method.substring(method.lastIndexOf(".")+1);
      methods = obj.getClass().getMethods();
      for(i=0;i<methods.length;i++)
        if (methods[i].getName().equals(method)) {
          if (action.getValues().size()==methods[i].getParameterTypes().length) {
            // method found...
            List values = action.getValues(); // values to set (in String format)
            Object[] argValues = new Object[action.getValues().size()];
            String value = null;
            for(int j=0;j<values.size();j++) {
              value = values.get(j).toString();
              if (value.indexOf(".")!=-1) {
                // value must be decoded from the class.getMethod...
                Object aux = workingMemory.get(value.substring(0, value.lastIndexOf(".")));
                try {
                  if (aux != null)
                    value = aux.getClass().getMethod(value.substring(value.lastIndexOf(".")+1), new Class[0]).invoke(aux, new Object[0]).toString();
                }
                catch (NullPointerException ex) {
                  value = null;
                }
                catch (Throwable ex) {
                  throw new InvalidRuleSessionException("Method not found: '"+value.substring(0,value.lastIndexOf(".")+1)+"'");
                }
              }
              if (value!=null && value.endsWith(".0") && value.length()>2)
                value = value.substring(0,value.length()-2);

              // now value is a real value (in String format)...
              argValue = null;
              if (methods[i].getParameterTypes()[j]==double.class)
                argValue = Double.valueOf(value);
              else if (methods[i].getParameterTypes()[j]==float.class)
                argValue = Float.valueOf(value);
              else if (methods[i].getParameterTypes()[j]==int.class)
                argValue = Integer.valueOf(value);
              else if (methods[i].getParameterTypes()[j]==long.class)
                argValue = Long.valueOf(value);
              else if (methods[i].getParameterTypes()[j]==char.class)
                argValue = new Character(value.charAt(0));
              else if (methods[i].getParameterTypes()[j]==boolean.class)
                argValue = Boolean.valueOf(value);
              else if (methods[i].getParameterTypes()[j]==Object.class)
                argValue = value;
              else
                argValue = methods[i].getParameterTypes()[j].getConstructor(new Class[]{String.class}).newInstance(new Object[]{value});

              argValues[j] = argValue;
            }
            methods[i].invoke(obj,argValues);
            if (obj instanceof Clause)
              workingMemory.put(((Clause)obj).getName(),((Clause)obj).getValue());
            else if (obj instanceof Component && ((Component)obj).getName()!=null)
              workingMemory.put(((Component)obj).getName(),obj);
            else
              workingMemory.put(obj.getClass().getName(),obj);
            return;
          }
        }
      throw new InvalidRuleSessionException("No method '"+action.getMethod()+"' found in class '"+obj.getClass().getName()+"'");
    }
    catch (Throwable ex) {
      String msg = "";
      if (obj!=null)
        msg = "Class '"+obj.getClass().getName()+"' ";
      if (i!=-1 && methods!=null)
        msg += "Method '"+methods[i].getName()+"':\n";
      throw new InvalidRuleSessionException(msg+ex.getMessage());
    }
  }


  /**
   * Analyze all rules and fire those rules that are valid.
   * After rule firing all rules are re-analyzed.
   * @param rulesAlreadyFired rules already fired
   */
  private void forwardChaining(HashSet rulesAlreadyFired) throws InvalidRuleSessionException {
    RuleImpl rule = null;
    Assumption ass = null;
    Action action = null;
    String t1, t2;
    Object aux;
    String className = null;
    boolean ok = true;
    Hashtable variables = new Hashtable();
    for(int i=0;i<ruleSet.getRules().size();i++) {
      rule = (RuleImpl)ruleSet.getRules().get(i);
      if (!rule.isEnabled() || rulesAlreadyFired.contains(rule))
        continue;
      variables.clear();
      ok = true;
      for(int j=0;j<rule.getAssumptions().size();j++) {
        ass = (Assumption)rule.getAssumptions().get(j);

        // extract and parse left term...
        t1 = ass.getLeftTerm();
        if (t1.indexOf(".")!=-1) {
          aux = workingMemory.get(t1.substring(0, t1.lastIndexOf(".")));

          try {
            if (aux != null) {
              Class auxClass = aux.getClass();
              Method auxMethod = auxClass.getMethod(t1.substring(t1.lastIndexOf(".")+1), new Class[0]);
              Object auxResult = auxMethod.invoke(aux, new Object[0]);
              t1 = (auxResult == null) ? null : auxResult.toString();
            }
          }
//          try {
//            if (aux != null)
//              t1 = aux.getClass().getMethod(t1.substring(t1.lastIndexOf(".")+1), new Class[0]).invoke(aux, new Object[0]).toString();
//          }
          catch (NullPointerException ex) {
            t1 = null;
          }
          catch (NoSuchMethodError ex) {
            throw new InvalidRuleSessionException("Method not found: '"+t1.substring(t1.lastIndexOf(".")+1)+"'");
          }
          catch (NoSuchMethodException ex) {
            throw new InvalidRuleSessionException("Method not found: '"+t1.substring(t1.lastIndexOf(".")+1)+"'");
          }
          catch (Throwable ex) {
            throw new InvalidRuleSessionException("Method not found: '"+t1.substring(t1.lastIndexOf(".")+1)+"'\n"+ex.getMessage());
          }
        }
        else if (workingMemory.containsKey(t1))
          t1 = workingMemory.get(t1).toString();
        if (t1!=null && t1.endsWith(".0") && t1.length()>2)
          t1 = t1.substring(0,t1.length()-2);

        // extract and parse right term...
        t2 = ass.getRightTerm();
        if (t2.indexOf(".")!=-1) {
          aux = workingMemory.get(t2.substring(0, t2.lastIndexOf(".")));

          try {
            if (aux != null) {
              Class auxClass = aux.getClass();
              Method auxMethod = auxClass.getMethod(t2.substring(t2.lastIndexOf(".")+1), new Class[0]);
              Object auxResult = auxMethod.invoke(aux, new Object[0]);
              t2 = (auxResult == null) ? null : auxResult.toString();
            }
          }

//          try {
//            if (aux != null)
//              t2 = aux.getClass().getMethod(t2.substring(t2.lastIndexOf(".")+1), new Class[0]).invoke(aux, new Object[0]).toString();
//          }
          catch (NullPointerException ex) {
            t2 = null;
          }
          catch (Throwable ex) {
            throw new InvalidRuleSessionException("Method not found: '"+t2.substring(0,t2.lastIndexOf(".")+1)+"'");
          }
        }
        else if (workingMemory.containsKey(t2))
          t2 = workingMemory.get(t2).toString();
        if (t2!=null && t2.endsWith(".0") && t2.length()>2)
          t2 = t2.substring(0,t2.length()-2);


        // validate assumption...
        if (ass.getOperator().equals(Assumption.EXISTS)) {
          ok = t1!=null && workingMemory.containsKey(t1);
        }
        else if (ass.getOperator().equals(Assumption.EQUALS_TO)) {
          if (t1!=null && t1.startsWith(":") && t2!=null) { // t1 is a variable...
            Enumeration en = workingMemory.keys();
            Object varValue = null;
            Object value = null;
            ok = false;
            while(en.hasMoreElements()) {
              varValue = en.nextElement();
              value = workingMemory.get(varValue);
              if (value.equals(t2)) {
                variables.put(t1, varValue);
                ok = true;
                break;
              }
            }
          }
          else ok = t1==null && t2==null ||
                    t1!=null && t2!=null && t1.equals(t2);
        } // PART 0: 11.04.2008 - MAURIZIO FABBRI - Added management of Assumption.CONTAINSATLEASTONE
        else if (ass.getOperator().equals(Assumption.CONTAINSATLEASTONE)) {
                if(t1!=null && t2!=null && t2.startsWith("[") && t2.endsWith("]")){
                        t2 = t2.substring(1, t2.length() -1);
                        String[] t2List = t2.split(",");
                        boolean stop = false;
                        ok = false;
                        for(int t2i=0; t2i<t2List.length && !stop; t2i++){
                                //System.out.println(t1 + " contiene " + t2List[t2i] + " = " + t1.indexOf(t2List[t2i]));
                                if(t1.indexOf(t2List[t2i]) != -1){
                                        stop = true;
                                        ok = true;
                                }
                        }
                } else {
                          ok = t1!=null &&
                           t2!=null &&
                           t1.indexOf(t2)!=-1;
                }
        } // End PART 0
        else if (ass.getOperator().equals(Assumption.CONTAINS)) {
          if (t1!=null && t1.startsWith(":") && t2!=null) { // t1 is a variable...
            Enumeration en = workingMemory.keys();
            Object varValue = null;
            Object value = null;
            ok = false;
            while(en.hasMoreElements()) {
              varValue = en.nextElement();
              value = workingMemory.get(varValue);
              if (value.toString().indexOf(t2)!=-1) {
                variables.put(t1, varValue);
                ok = true;
                break;
              }
            }
          }
          else {
            // PART 1: 11.04.2008 - MAURIZIO FABBRI - Added management of the second term of type HashSet
            if(t1!=null && t2!=null && t2.startsWith("[") && t2.endsWith("]")){
                    t2 = t2.substring(1, t2.length() -1);
                    String[] t2List = t2.split(",");
                    boolean stop = false;
                    ok = true;
                    for(int t2i=0; t2i<t2List.length && !stop; t2i++){
                            if(t1.indexOf(t2List[t2i]) == -1){
                                    stop = true;
                                    ok = false;
                            }
                    }
             // End PART 1
            } else {
                    ok = t1!=null &&
                     t2!=null &&
                     t1.indexOf(t2)!=-1;
            }
          }
        }
        else if (ass.getOperator().equals(Assumption.NOT_CONTAINS)) {
          // PART 2: 11.04.2008 - MAURIZIO FABBRI - Added management of the second term of type HashSet
          if(t1!=null && t2!=null && t2.startsWith("[") && t2.endsWith("]")){
                  t2 = t2.substring(1, t2.length() -1);
                  String[] t2List = t2.split(",");
                  boolean stop = false;
                  ok = false;
                  for(int t2i=0; t2i<t2List.length && !stop; t2i++){
                          if(t1.indexOf(t2List[t2i]) == -1){
                                  stop = true;
                                  ok = true;
                          }
                  }
           // End PART 2
          } else {
            ok = t1!=null && t2==null ||
                 t1!=null && t2!=null && t1.indexOf(t2)==-1;
          }
        }
        else if (ass.getOperator().equals(Assumption.NOT_CONTAINSANYONE)) {
                  // PART 3: 11.04.2008 - MAURIZIO FABBRI - Added management of Assumption.NOT_CONTAINSANYONE
                  if(t1!=null && t2!=null && t2.startsWith("[") && t2.endsWith("]")){
                          t2 = t2.substring(1, t2.length() -1);
                          String[] t2List = t2.split(",");
                          boolean stop = false;
                          ok = true;
                          for(int t2i=0; t2i<t2List.length && !stop; t2i++){
                                  if(t1.indexOf(t2List[t2i]) != -1){
                                          stop = true;
                                          ok = false;
                                  }
                          }
                   // End PART 3
                  } else {
                    ok = t1!=null && t2==null ||
                         t1!=null && t2!=null && t1.indexOf(t2)==-1;
                  }
        }
        else if (ass.getOperator().equals(Assumption.NOT_EQUALS_TO)) {
          ok = t1==null && t2!=null ||
               t1!=null && t2==null ||
               t1!=null && t2!=null && !t1.equals(t2);
        }
        else if (t1==null && t2!=null ||
                 t1!=null && t2==null ||
                 t1==null && t2==null) {
          ok = false;
        }
        else if (ass.getOperator().equals(Assumption.LESS_THAN)) {
          try {
            ok = t1!=null &&
                 !"".equals(t1) &&
                 t2!=null &&
                 !"".equals(t2) &&
                 (new Double(t1)).doubleValue() < (new Double(t2)).doubleValue();
          }
          catch (NumberFormatException ex1) {
            throw new InvalidRuleSessionException("Numeric type expected: '"+t1+"','"+t2+"'");
          }
        }
        else if (ass.getOperator().equals(Assumption.LESS_OR_EQUALS_TO)) {
          try {
            ok =
               t1!=null &&
               !"".equals(t1) &&
               t2!=null &&
               !"".equals(t2) &&
               (new Double(t1)).doubleValue() <= (new Double(t2)).doubleValue();
          }
          catch (NumberFormatException ex2) {
            throw new InvalidRuleSessionException("Numeric type expected: '"+t1+"','"+t2+"'");
          }
        }
        else if (ass.getOperator().equals(Assumption.GREATER_THAN)) {
          try {
            ok =
                t1!=null &&
                !"".equals(t1) &&
                t2!=null &&
                !"".equals(t2) &&
                (new Double(t1)).doubleValue() > (new Double(t2)).doubleValue();
          }
          catch (NumberFormatException ex3) {
            throw new InvalidRuleSessionException("Numeric type expected: '"+t1+"','"+t2+"'");
          }
        }
        else if (ass.getOperator().equals(Assumption.GREATER_OR_EQUALS_TO)) {
          try {
            ok =
                t1!=null &&
                !"".equals(t1) &&
                t2!=null &&
                !"".equals(t2) &&
                (new Double(t1)).doubleValue() >= (new Double(t2)).doubleValue();
          }
          catch (NumberFormatException ex4) {
            throw new InvalidRuleSessionException("Numeric type expected: '"+t1+"','"+t2+"'");
          }
        }
        else throw new InvalidRuleSessionException("Operator not supported: '"+ass.getOperator()+"'");

        if (!ok)
          break;
      }

      if (!ok)
        // skip to the next rule...
        continue;

      // execute all actions associated to the current rule...
      for(int j=0;j<rule.getActions().size();j++) {
        action = (Action)rule.getActions().get(j);

        // set values into variables (if there are any vars...)
        for(int k=0;k<action.getValues().size();k++)
          if (action.getValues().get(k).toString().startsWith(":")) {
            // var found...
            if (variables.containsKey(action.getValues().get(k).toString()))
              action.getValues().set(k,variables.get(action.getValues().get(k)));
            else throw new InvalidRuleSessionException("Variable '"+action.getValues().get(k)+"' not bound");
          }

        if (action.getMethod().indexOf(".")!=-1) {
          className = action.getMethod().substring(0,action.getMethod().lastIndexOf("."));
          aux = workingMemory.get(className);
          try {
            if (aux == null) {
              aux = Class.forName(className).newInstance();
            }
          }
          catch (Throwable ex) {
            throw new InvalidRuleSessionException("Class not found '"+className+"'");
          }
        }
        else throw new InvalidRuleSessionException("Class not found '"+action.getMethod()+"'");
        executeMethod(action,aux);
      }
      rulesAlreadyFired.add(rule);
      forwardChaining(rulesAlreadyFired);
      break;
    }
  }


  /**
   * @return method not included into JSR specifications: used to access to working memory
   */
  public final Hashtable getWorkingMemory() {
    return workingMemory;
  }



}