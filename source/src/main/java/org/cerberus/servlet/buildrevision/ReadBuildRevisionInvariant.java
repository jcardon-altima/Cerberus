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
package org.cerberus.servlet.buildrevision;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cerberus.crud.entity.BuildRevisionInvariant;

import org.cerberus.crud.entity.MessageEvent;
import org.cerberus.crud.service.IBuildRevisionInvariantService;
import org.cerberus.crud.service.impl.BuildRevisionInvariantService;
import org.cerberus.enums.MessageEventEnum;
import org.cerberus.exception.CerberusException;
import org.cerberus.util.ParameterParserUtil;
import org.cerberus.util.answer.AnswerItem;
import org.cerberus.util.answer.AnswerList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author vertigo
 */
@WebServlet(name = "ReadBuildRevisionInvariant", urlPatterns = {"/ReadBuildRevisionInvariant"})
public class ReadBuildRevisionInvariant extends HttpServlet {

    private IBuildRevisionInvariantService briService;
    private final String OBJECT_NAME = "ReadBuildRevisionInvariant";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.cerberus.exception.CerberusException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CerberusException {
        String echo = request.getParameter("sEcho");
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

        response.setContentType("application/json");

        // Default message to unexpected error.
        MessageEvent msg = new MessageEvent(MessageEventEnum.DATA_OPERATION_ERROR_UNEXPECTED);
        msg.setDescription(msg.getDescription().replace("%DESCRIPTION%", ""));

        /**
         * Parsing and securing all required parameters.
         */
        Integer lvlid = -1;
        boolean lvlid_error = true;
        try {
            if (request.getParameter("level") != null && !request.getParameter("level").equals("")) {
                lvlid = Integer.valueOf(policy.sanitize(request.getParameter("level")));
                lvlid_error = false;
            }
        } catch (Exception ex) {
            msg = new MessageEvent(MessageEventEnum.DATA_OPERATION_ERROR_EXPECTED);
            msg.setDescription(msg.getDescription().replace("%ITEM%", OBJECT_NAME));
            msg.setDescription(msg.getDescription().replace("%OPERATION%", "Read"));
            msg.setDescription(msg.getDescription().replace("%REASON%", "level must be an integer value."));
            lvlid_error = true;
        }
        Integer seqid = -1;
        boolean seqid_error = true;
        try {
            if (request.getParameter("seq") != null && !request.getParameter("seq").equals("")) {
                seqid = Integer.valueOf(policy.sanitize(request.getParameter("seq")));
                seqid_error = false;
            }
        } catch (Exception ex) {
            msg = new MessageEvent(MessageEventEnum.DATA_OPERATION_ERROR_EXPECTED);
            msg.setDescription(msg.getDescription().replace("%ITEM%", OBJECT_NAME));
            msg.setDescription(msg.getDescription().replace("%OPERATION%", "Read"));
            msg.setDescription(msg.getDescription().replace("%REASON%", "id must be an integer value."));
            seqid_error = true;
        }
        String system = policy.sanitize(request.getParameter("system"));

        // Init Answer with potencial error from Parsing parameter.
        AnswerItem answer = new AnswerItem(msg);

        try {
            JSONObject jsonResponse = new JSONObject();
            if ((request.getParameter("system") != null) && (request.getParameter("level") != null) && !(lvlid_error) && (request.getParameter("seq") != null) && !(seqid_error)) { // ID parameter is specified so we return the unique record of object.
                answer = findBuildRevisionInvariantByKey(system, lvlid, seqid, appContext);
                jsonResponse = (JSONObject) answer.getItem();
            } else { // Default behaviour, we return the list of objects.
                answer = findBuildRevisionInvariantList(system, lvlid, appContext, request, response);
                jsonResponse = (JSONObject) answer.getItem();
            }
            jsonResponse.put("messageType", answer.getResultMessage().getMessage().getCodeString());
            jsonResponse.put("message", answer.getResultMessage().getDescription());
            jsonResponse.put("sEcho", echo);

            response.getWriter().print(jsonResponse.toString());

        } catch (JSONException e) {
            org.apache.log4j.Logger.getLogger(ReadBuildRevisionInvariant.class.getName()).log(org.apache.log4j.Level.ERROR, null, e);
            //returns a default error message with the json format that is able to be parsed by the client-side
            response.setContentType("application/json");
            msg = new MessageEvent(MessageEventEnum.DATA_OPERATION_ERROR_UNEXPECTED);
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("{\"messageType\":\"").append(msg.getCode()).append("\",");
            errorMessage.append("\"message\":\"");
            errorMessage.append(msg.getDescription().replace("%DESCRIPTION%", "Unable to check the status of your request! Try later or open a bug."));
            errorMessage.append("\"}");
            response.getWriter().print(errorMessage.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (CerberusException ex) {
            Logger.getLogger(ReadBuildRevisionInvariant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (CerberusException ex) {
            Logger.getLogger(ReadBuildRevisionInvariant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private AnswerItem findBuildRevisionInvariantList(String system, Integer level, ApplicationContext appContext, HttpServletRequest request, HttpServletResponse response) throws JSONException {

        AnswerItem item = new AnswerItem();
        JSONObject jsonResponse = new JSONObject();
        briService = appContext.getBean(BuildRevisionInvariantService.class);

        int startPosition = Integer.valueOf(ParameterParserUtil.parseStringParam(request.getParameter("iDisplayStart"), "0"));
        int length = Integer.valueOf(ParameterParserUtil.parseStringParam(request.getParameter("iDisplayLength"), "0"));
        /*int sEcho  = Integer.valueOf(request.getParameter("sEcho"));*/

        String searchParameter = ParameterParserUtil.parseStringParam(request.getParameter("sSearch"), "");
        int columnToSortParameter = Integer.parseInt(ParameterParserUtil.parseStringParam(request.getParameter("iSortCol_0"), "1"));
        String sColumns = ParameterParserUtil.parseStringParam(request.getParameter("sColumns"), "system,level,seq,versionname");
        String columnToSort[] = sColumns.split(",");
        String columnName = columnToSort[columnToSortParameter];
        String sort = ParameterParserUtil.parseStringParam(request.getParameter("sSortDir_0"), "asc");
        AnswerList resp = briService.readBySystemByCriteria(system, level, startPosition, length, columnName, sort, searchParameter, "");

        JSONArray jsonArray = new JSONArray();
        boolean userHasPermissions = request.isUserInRole("IntegratorRO");
        if (resp.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {//the service was able to perform the query, then we should get all values
            for (BuildRevisionInvariant bri : (List<BuildRevisionInvariant>) resp.getDataList()) {
                jsonArray.put(convertBuildRevisionInvariantToJSONObject(bri));
            }
        }

        jsonResponse.put("hasPermissions", userHasPermissions);
        jsonResponse.put("contentTable", jsonArray);
        jsonResponse.put("iTotalRecords", resp.getTotalRows());
        jsonResponse.put("iTotalDisplayRecords", resp.getTotalRows());

        item.setItem(jsonResponse);
        item.setResultMessage(resp.getResultMessage());
        return item;
    }

    private AnswerItem findBuildRevisionInvariantByKey(String system, Integer level, Integer seq, ApplicationContext appContext) throws JSONException, CerberusException {
        AnswerItem item = new AnswerItem();
        JSONObject object = new JSONObject();

        IBuildRevisionInvariantService libService = appContext.getBean(IBuildRevisionInvariantService.class);

        //finds the project     
        AnswerItem answer = null;
        answer = libService.readByKey(system, level, seq);

        if (answer.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {
            //if the service returns an OK message then we can get the item and convert it to JSONformat
            BuildRevisionInvariant bri = (BuildRevisionInvariant) answer.getItem();
            JSONObject response = convertBuildRevisionInvariantToJSONObject(bri);
            object.put("contentTable", response);
        }

        item.setItem(object);
        item.setResultMessage(answer.getResultMessage());

        return item;
    }

    private JSONObject convertBuildRevisionInvariantToJSONObject(BuildRevisionInvariant brp) throws JSONException {

        Gson gson = new Gson();
        JSONObject result = new JSONObject(gson.toJson(brp));
        return result;
    }

}
