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
package org.cerberus.crud.service;

import java.util.List;
import org.cerberus.crud.entity.BuildRevisionBatch;
import org.cerberus.exception.CerberusException;
import org.cerberus.util.answer.Answer;
import org.cerberus.util.answer.AnswerItem;
import org.cerberus.util.answer.AnswerList;

/**
 *
 * @author bcivel
 */
public interface IBuildRevisionBatchService {

    /**
     *
     * @param id
     * @return
     */
    AnswerItem readByKey(Long id);

    /**
     *
     * @return
     */
    AnswerList readAll();

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @param startPosition
     * @param length
     * @param columnName
     * @param sort
     * @param searchParameter
     * @param string
     * @return
     */
    AnswerList readByVariousByCriteria(String system, String country, String environment, int startPosition, int length, String columnName, String sort, String searchParameter, String string);

    /**
     *
     * @param countryEnvParamLog
     * @return true if project exist. false if not.
     */
    boolean exist(Long countryEnvParamLog);

    /**
     *
     * @param buildRevisionBatch
     * @return
     */
    Answer create(BuildRevisionBatch buildRevisionBatch);

    /**
     *
     * @param buildRevisionBatch
     * @return
     */
    Answer delete(BuildRevisionBatch buildRevisionBatch);

    /**
     *
     * @param buildRevisionBatch
     * @return
     */
    Answer update(BuildRevisionBatch buildRevisionBatch);

    /**
     *
     * @param system
     * @param country
     * @param environment
     * @param build
     * @param revision
     * @param batch
     * @return
     */
    Answer create(String system, String country, String environment, String build, String revision, String batch);
    
    /**
     *
     * @param answerItem
     * @return
     * @throws CerberusException
     */
    BuildRevisionBatch convert(AnswerItem answerItem) throws CerberusException;

    /**
     *
     * @param answerList
     * @return
     * @throws CerberusException
     */
    List<BuildRevisionBatch> convert(AnswerList answerList) throws CerberusException;

    /**
     *
     * @param answer
     * @throws CerberusException
     */
    void convert(Answer answer) throws CerberusException;
}
