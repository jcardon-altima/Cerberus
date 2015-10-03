/* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
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
package org.cerberus.crud.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.cerberus.crud.entity.DeployType;
import org.cerberus.util.answer.Answer;
import org.cerberus.util.answer.AnswerItem;
import org.cerberus.util.answer.AnswerList;

/**
 * @author bdumont
 */
public interface IDeployTypeDAO {

    /**
     *
     * @param deployType
     * @return
     */
    public AnswerItem readByKey(String deployType);

    /**
     *
     * @return
     */
    public AnswerList readAll();

    /**
     *
     * @param startPosition
     * @param length
     * @param columnName
     * @param sort
     * @param searchParameter
     * @param string
     * @return
     */
    public AnswerList readByCriteria(int startPosition, int length, String columnName, String sort, String searchParameter, String string);

    /**
     *
     * @param deployType
     * @return
     */
    public Answer create(DeployType deployType);

    /**
     *
     * @param deployType
     * @return
     */
    public Answer delete(DeployType deployType);

    /**
     *
     * @param deployType
     * @return
     */
    public Answer update(DeployType deployType);

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public DeployType loadFromResultSet(ResultSet rs) throws SQLException;
}
