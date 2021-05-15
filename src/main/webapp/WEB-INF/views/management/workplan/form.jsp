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
    <acme:form-textbox code="management.workplan.label.title" path="title"/>
    <acme:form-textbox code="management.workplan.label.description" path="description"/>
    <acme:form-checkbox code="management.workplan.label.isPublic" path="isPublic"/>
    <jstl:if test="${command != 'create'}">
        <acme:form-textbox code="management.workplan.label.WorkloadHours" path="workload" readonly="true"/>
    </jstl:if>

    <acme:form-moment code="management.workplan.label.start-date-time" path="startDateTime"/>
    <acme:form-moment code="management.workplan.label.finish-date-time" path="finishDateTime"/>

    <label for="tasks">
        <acme:message code="management.workplan.label.task"/>
    </label>
    <div style="max-height: 40vmin; overflow-y: auto">
    <table class="table table-striped" >
        <tr>
            <th>
                <acme:message code="management.workplan.label.title"/>
            </th>
            <th>
                <acme:message code="management.workplan.label.start-date-time"/>
            </th>
            <th>
                <acme:message code="management.workplan.label.finish-date-time"/>
            </th>
            <th>
                <acme:message code="management.workplan.label.task.isPublic"/>
            </th>
            <th>
                <acme:message code="management.workplan.label.add"/>
            </th>
        </tr>
        <c:forEach items="${tasks}" var="task">
            <tr>
                <td>
                    <c:out value="${task.title}"/>
                </td>
                <td>
                    <acme:print value="${task.executionPeriod.startDateTime}"/>
                </td>
                <td>
                    <acme:print value="${task.executionPeriod.finishDateTime}"/>
                </td>
                <td>
                    <c:out value="${task.isPublic}"/>
                </td>
                <td>
                    <input type="checkbox" id="${task.title}" name="newTasksId" value="${task.id}" checked="true">
                </td>
            </tr>
        </c:forEach>
    <jstl:if test="${command == 'create' || isFinished == 'false'}">
        <c:forEach items="${userTask}" var="task">
            <tr>
                <td>
                    <c:out value="${task.title}"/>
                </td>
                <td id="startDateTime${task.id}" date="${task.executionPeriod.startDateTime}">
                    <acme:print value="${task.executionPeriod.startDateTime}"/>
                </td>
                <td id="finishDateTime${task.id}" date="${task.executionPeriod.finishDateTime}">
                    <acme:print value="${task.executionPeriod.finishDateTime}"/>
                </td>
                <td>
                    <c:out value="${task.isPublic}"/>
                </td>
                <td>
                    <input type="checkbox" id="${task.title}" name="newTasksId" value="${task.id}">
                </td>
            </tr>
        </c:forEach>
    </jstl:if>
    </table>
    </div>
    <jstl:if test="${isFinished == 'true'}">
            <acme:message code="management.workplan.label.FinishedWorkplanMessage"/>
    </jstl:if>
    <br>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="management.workplan.button.update"
                      action="/management/work-plan/update"/>
    <acme:form-submit test="${command == 'show' && isFinished == 'false'}" code="management.workplan.button.delete"
                      action="/management/work-plan/delete"/>
    <acme:form-submit test="${command == 'create'}" code="management.workplan.button.create"
                      action="/management/work-plan/create"/>
    <acme:form-submit test="${command == 'update'}" code="management.workplan.button.update"
                      action="/management/work-plan/update"/>
    <acme:form-submit test="${command == 'delete'||command == 'update'}" code="management.workplan.button.delete"
                      action="/management/work-plan/delete"/>
    <acme:form-return code="management.workplan.button.return"/>
</acme:form>

<c:if test="${command == 'create'}">
<script type="text/javascript" src="workPlanCreateForm.js"></script>
</c:if>


