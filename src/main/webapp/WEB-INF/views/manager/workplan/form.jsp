<%--
- form.jsp
-
- Copyright (c) 2012-2021 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<acme:form>
    <acme:form-textbox code="manager.workplan.label.title" path="title"/>
    <acme:form-textbox code="manager.workplan.label.description" path="description"/>
    <acme:form-checkbox code="manager.workplan.label.isPublic" path="isPublic"/>
    <jstl:if test="${command != 'create'}">
        <acme:form-textbox code="manager.workplan.label.WorkloadHours" path="workload" readonly="true"/>
    </jstl:if>

    <acme:form-moment code="manager.workplan.label.start-date-time" path="startDateTime"/>
    <acme:form-moment code="manager.workplan.label.finish-date-time" path="finishDateTime"/>

    <label for="tasks">
        <acme:message code="manager.workplan.label.task"/>
    </label>
    <div style="max-height: 40vmin; overflow-y: auto">
    <table class="table table-striped" >
        <tr>
            <th>
                <acme:message code="manager.workplan.label.title"/>
            </th>
            <th>
                <acme:message code="manager.workplan.label.start-date-time"/>
            </th>
            <th>
                <acme:message code="manager.workplan.label.finish-date-time"/>
            </th>
            <th>
                <acme:message code="manager.workplan.label.add"/>
            </th>
        </tr>
        <c:forEach items="${tasks}" var="task">
            <tr>
                <td>
                    <c:out value="${task.title}"/>
                </td>
                <td>
                    <c:out value="${task.executionPeriod.startDateTime}"/>
                </td>
                <td>
                    <c:out value="${task.executionPeriod.finishDateTime}"/>
                </td>
                <td>
                    <input type="checkbox" name="newTasksId" value="${task.id}" checked="true">
                </td>
            </tr>
        </c:forEach>

        <c:forEach items="${userTask}" var="task">
            <tr>
                <td>
                    <c:out value="${task.title}"/>
                </td>
                <td>
                    <c:out value="${task.executionPeriod.startDateTime}"/>
                </td>
                <td>
                    <c:out value="${task.executionPeriod.finishDateTime}"/>
                </td>
                <td>
                    <input type="checkbox" name="newTasksId" value="${task.id}">
                </td>
            </tr>
        </c:forEach>

    </table>
    </div>
    <br>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="manager.workplan.button.update"
                      action="/manager/work-plan/update"/>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="manager.workplan.button.delete"
                      action="/manager/work-plan/delete"/>
    <acme:form-submit test="${command == 'create'}" code="manager.workplan.button.create"
                      action="/manager/work-plan/create"/>
    <acme:form-submit test="${command == 'update'}" code="manager.workplan.button.update"
                      action="/manager/work-plan/update"/>
    <acme:form-submit test="${command == 'delete'||command == 'update'}" code="manager.workplan.button.delete"
                      action="/manager/work-plan/delete"/>
    <acme:form-return code="manager.workplan.button.return"/>
</acme:form>

<script src="webapp/META-INF/resources/workPlanCreateForm.js"></script>
