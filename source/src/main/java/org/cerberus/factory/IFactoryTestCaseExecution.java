/*
 * Cerberus  Copyright (C) 2013  vertigo17
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.factory;

import java.util.List;
import java.util.Map;

import org.cerberus.entity.Country;
import org.cerberus.entity.Environment;
import org.cerberus.entity.MessageGeneral;
import org.cerberus.entity.Property;
import org.cerberus.entity.Step;
import org.cerberus.entity.TestCase;
import org.cerberus.entity.TestCaseExecution;

/**
 * @author bcivel
 */
public interface IFactoryTestCaseExecution {

    TestCaseExecution create(String environmentRun, String tag, String output, int verbose,
                             long runID, Country countryExecute, TestCase testCase, List<Property> properties,
                             List<Step> steps, MessageGeneral result, long start, long end, Environment environmentTest,
                             Map<String, String> devEnvironment);

}
