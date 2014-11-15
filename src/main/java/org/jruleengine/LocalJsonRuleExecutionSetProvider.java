package org.jruleengine;

import org.jruleengine.rule.Action;
import org.jruleengine.rule.Assumption;
import org.jruleengine.rule.RuleImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Local Rule Execution Set Provider Implementation for JSON.</p>
 * <p>Copyright: Copyright (C) 2014 Mauro Carniel</p>
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
 * @author Dan Noguerol
 * @version 1.0
 */
public class LocalJsonRuleExecutionSetProvider extends AbstractLocalRuleExecutionSetProvider {
    public static final String ACTIONS = "actions";
    public static final String ARG = "arg";
    public static final String ASSUMPTIONS = "assumptions";
    public static final String CLASS = "class";
    public static final String DESCRIPTION = "description";
    public static final String LEFT_TERM = "leftTerm";
    public static final String METHOD = "method";
    public static final String NAME = "name";
    public static final String OP = "op";
    public static final String RIGHT_TERM = "rightTerm";
    public static final String RULES = "rules";
    public static final String SYNONYMS = "synonyms";
    public static final String ENABLED = "enabled";

    public LocalJsonRuleExecutionSetProvider() {}

    @Override
    public RuleExecutionSet createRuleExecutionSet(InputStream inputStream, Map map) throws RuleExecutionSetCreateException, IOException {
        try {
            return createRuleExecutionSet(new JSONObject(new JSONTokener(new InputStreamReader(inputStream))), map);
        } catch (JSONException e) {
            throw new RuleExecutionSetCreateException(e.getMessage());
        }
    }

    @Override
    public RuleExecutionSet createRuleExecutionSet(Reader reader, Map map) throws RuleExecutionSetCreateException, IOException {
        try {
            return createRuleExecutionSet(new JSONObject(new JSONTokener(reader)), map);
        } catch (JSONException e) {
            throw new RuleExecutionSetCreateException(e.getMessage());
        }
    }

    @Override
    public RuleExecutionSet createRuleExecutionSet(Object ast, Map properties) throws RuleExecutionSetCreateException {
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

    protected RuleExecutionSet createRuleExecutionSet(JSONObject obj, Map properties) throws RuleExecutionSetCreateException {
        try {
            String name = obj.get(NAME).toString().trim();
            String description = obj.get(DESCRIPTION).toString().trim();

            if (properties == null) {
                properties = new HashMap();
            }
            properties.put(NAME, name);
            properties.put(DESCRIPTION, description);

            JSONObject el, node = null;
            ArrayList assumptions = null;
            ArrayList actions = null;
            JSONArray nodes = null;

            // read synonyms...
            Hashtable synonymns = new Hashtable();
            if (obj.has(SYNONYMS)) {
                nodes = obj.getJSONArray(SYNONYMS);
                for (int j = 0; j < nodes.length(); j++) {
                    node = nodes.getJSONObject(j);
                    synonymns.put(node.getString(NAME), node.getString(CLASS));
                }
            }

            // read rules...
            List rules = new ArrayList();
            if (obj.has(RULES)) {
                JSONArray contents = obj.getJSONArray(RULES);
                for (int i = 0; i < contents.length(); i++) {
                    el = contents.getJSONObject(i);
                    assumptions = new ArrayList();
                    actions = new ArrayList();

                    // read assumptions...
                    nodes = el.getJSONArray(ASSUMPTIONS);
                    for (int j = 0; j < nodes.length(); j++) {
                        node = nodes.getJSONObject(j);

                        if (node.has(OP) && node.has(RIGHT_TERM))
                            assumptions.add(new Assumption(
                                    parseClassName(node.getString(LEFT_TERM), synonymns),
                                    node.getString(OP),
                                    parseClassName(node.getString(RIGHT_TERM), synonymns)
                            ));
                        else
                            assumptions.add(new Assumption(
                                    parseClassName(node.getString(LEFT_TERM), synonymns)
                            ));
                    }

                    // read actions...
                    ArrayList args = null;
                    int k;
                    nodes = el.getJSONArray(ACTIONS);
                    for (int j = 0; j < nodes.length(); j++) {
                        node = nodes.getJSONObject(j);
                        args = new ArrayList();
                        k = 1;
                        while (node.has(ARG + k)) {
                            args.add(parseClassName(node.getString(ARG + k), synonymns));
                            k++;
                        }
                        actions.add(new Action(
                                parseClassName(node.getString(METHOD), synonymns), args
                        ));
                    }

                    boolean enabled = true;
                    if (el.has(ENABLED)) {
                      enabled = el.getBoolean(ENABLED);
                    }

                    rules.add(new RuleImpl(
                            el.getString(NAME),
                            el.getString(DESCRIPTION),
                            assumptions,
                            actions,
                            enabled
                    ));
                }
            }

            return createRuleExecutionSetFromRuleList(rules, properties);
        } catch (Exception ex) {
            throw new RuleExecutionSetCreateException(ex.getMessage());
        }
    }
}
